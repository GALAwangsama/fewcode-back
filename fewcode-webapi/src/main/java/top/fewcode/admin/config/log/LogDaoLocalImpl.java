/*
 * Copyright (c) 2025-present IPBD Organization. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package top.fewcode.admin.config.log;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Async;
import top.continew.starter.core.constant.StringConstants;
import top.continew.starter.core.util.ExceptionUtils;
import top.continew.starter.core.util.StrUtils;
import top.continew.starter.log.core.dao.LogDao;
import top.continew.starter.log.core.model.LogRecord;
import top.continew.starter.log.core.model.LogRequest;
import top.continew.starter.log.core.model.LogResponse;
import top.continew.starter.web.autoconfigure.trace.TraceProperties;
import top.continew.starter.web.model.R;
import top.fewcode.admin.auth.model.req.AccountLoginReq;
import top.fewcode.admin.common.constant.SysConstants;
import top.fewcode.admin.system.enums.LogStatusEnum;
import top.fewcode.admin.system.mapper.LogMapper;
import top.fewcode.admin.system.model.entity.LogDO;
import top.fewcode.admin.system.service.UserService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.Set;

/**
 * 日志持久层接口本地实现类
 *
 * @author GALAwang
 * @since 2023/12/16 23:55
 */
@RequiredArgsConstructor
public class LogDaoLocalImpl implements LogDao {

    private final UserService userService;
    private final LogMapper logMapper;
    private final TraceProperties traceProperties;

    @Async
    @Override
    public void add(LogRecord logRecord) {
        LogDO logDO = new LogDO();
        // 设置请求信息
        LogRequest logRequest = logRecord.getRequest();
        this.setRequest(logDO, logRequest);
        // 设置响应信息
        LogResponse logResponse = logRecord.getResponse();
        this.setResponse(logDO, logResponse);
        // 设置基本信息
        logDO.setDescription(logRecord.getDescription());
        logDO.setModule(StrUtils.blankToDefault(logRecord.getModule(), null, m -> m
            .replace("API", StringConstants.EMPTY)
            .trim()));
        logDO.setTimeTaken(logRecord.getTimeTaken().toMillis());
        logDO.setCreateTime(LocalDateTime.ofInstant(logRecord.getTimestamp(), ZoneId.systemDefault()));
        // 设置操作人
        this.setCreateUser(logDO, logRequest, logResponse);
        logMapper.insert(logDO);
    }

    /**
     * 设置请求信息
     *
     * @param logDO      日志信息
     * @param logRequest 请求信息
     */
    private void setRequest(LogDO logDO, LogRequest logRequest) {
        logDO.setRequestMethod(logRequest.getMethod());
        logDO.setRequestUrl(logRequest.getUrl().toString());
        logDO.setRequestHeaders(JSONUtil.toJsonStr(logRequest.getHeaders()));
        logDO.setRequestBody(logRequest.getBody());
        logDO.setIp(logRequest.getIp());
        logDO.setAddress(logRequest.getAddress());
        logDO.setBrowser(logRequest.getBrowser());
        logDO.setOs(StrUtil.subBefore(logRequest.getOs(), " or", false));
    }

    /**
     * 设置响应信息
     *
     * @param logDO       日志信息
     * @param logResponse 响应信息
     */
    private void setResponse(LogDO logDO, LogResponse logResponse) {
        Map<String, String> responseHeaders = logResponse.getHeaders();
        logDO.setResponseHeaders(JSONUtil.toJsonStr(responseHeaders));
        logDO.setTraceId(responseHeaders.get(traceProperties.getTraceIdName()));
        String responseBody = logResponse.getBody();
        logDO.setResponseBody(responseBody);
        // 状态
        Integer statusCode = logResponse.getStatus();
        logDO.setStatusCode(statusCode);
        logDO.setStatus(statusCode >= HttpStatus.HTTP_BAD_REQUEST ? LogStatusEnum.FAILURE : LogStatusEnum.SUCCESS);
        if (StrUtil.isNotBlank(responseBody)) {
            R result = JSONUtil.toBean(responseBody, R.class);
            if (!result.isSuccess()) {
                logDO.setStatus(LogStatusEnum.FAILURE);
                logDO.setErrorMsg(result.getMsg());
            }
        }
    }

    /**
     * 设置操作人
     *
     * @param logDO       日志信息
     * @param logRequest  请求信息
     * @param logResponse 响应信息
     */
    private void setCreateUser(LogDO logDO, LogRequest logRequest, LogResponse logResponse) {
        String requestUri = URLUtil.getPath(logDO.getRequestUrl());
        // 解析退出接口信息
        String responseBody = logResponse.getBody();
        if (requestUri.startsWith(SysConstants.LOGOUT_URI) && StrUtil.isNotBlank(responseBody)) {
            R result = JSONUtil.toBean(responseBody, R.class);
            logDO.setCreateUser(Convert.toLong(result.getData(), null));
            return;
        }
        // 解析登录接口信息
        if (requestUri.startsWith(SysConstants.LOGIN_URI) && LogStatusEnum.SUCCESS.equals(logDO.getStatus())) {
            String requestBody = logRequest.getBody();
            AccountLoginReq loginReq = JSONUtil.toBean(requestBody, AccountLoginReq.class);
            logDO.setCreateUser(ExceptionUtils.exToNull(() -> userService.getByUsername(loginReq.getUsername())
                .getId()));
            return;
        }
        // 解析 Token 信息
        Map<String, String> requestHeaders = logRequest.getHeaders();
        String headerName = HttpHeaders.AUTHORIZATION;
        boolean isContainsAuthHeader = CollUtil.containsAny(requestHeaders.keySet(), Set.of(headerName, headerName
            .toLowerCase()));
        if (MapUtil.isNotEmpty(requestHeaders) && isContainsAuthHeader) {
            String authorization = requestHeaders.getOrDefault(headerName, requestHeaders.get(headerName
                .toLowerCase()));
            String token = authorization.replace(SaManager.getConfig()
                .getTokenPrefix() + StringConstants.SPACE, StringConstants.EMPTY);
            logDO.setCreateUser(Convert.toLong(StpUtil.getLoginIdByToken(token)));
        }
    }
}
