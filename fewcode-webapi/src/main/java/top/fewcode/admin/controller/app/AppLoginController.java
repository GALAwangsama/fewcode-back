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
package top.fewcode.admin.controller.app;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.continew.starter.cache.redisson.util.RedisUtils;
import top.continew.starter.core.exception.BusinessException;
import top.continew.starter.core.util.ExceptionUtils;
import top.continew.starter.core.validation.ValidationUtils;
import top.continew.starter.log.core.annotation.Log;
import top.continew.starter.web.model.R;
import top.fewcode.admin.app.model.req.AppAccountLoginReq;
import top.fewcode.admin.app.model.req.AppEmailLoginReq;
import top.fewcode.admin.app.model.req.AppPhoneLoginReq;
import top.fewcode.admin.app.model.resp.AppLoginResp;
import top.fewcode.admin.app.model.resp.AppUserInfoResp;
import top.fewcode.admin.app.service.AppLoginService;
import top.fewcode.admin.appSystem.model.req.user.AppUserRegisterReq;
import top.fewcode.admin.appSystem.model.resp.user.AppUserDetailResp;
import top.fewcode.admin.appSystem.service.AppUserService;
import top.fewcode.admin.auth.model.resp.RouteResp;
import top.fewcode.admin.common.constant.CacheConstants;
import top.fewcode.admin.common.constant.SysConstants;
import top.fewcode.admin.common.context.UserContext;
import top.fewcode.admin.common.context.UserContextHolder;
import top.fewcode.admin.common.util.SecureUtils;
import top.fewcode.admin.system.service.OptionService;

import java.util.List;

/**
 * 小程序认证 API
 *
 * @author GALAwang
 * @since 2022/12/21 20:37
 */
@Log(module = "小程序登录")
@Tag(name = "小程序认证 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/app")
public class AppLoginController {

    private static final String CAPTCHA_EXPIRED = "验证码已失效";
    private static final String CAPTCHA_ERROR = "验证码错误";
    private final OptionService optionService;
    private final AppLoginService loginService;
    private final AppUserService indexUserService;

    @SaIgnore
    @Operation(summary = "小程序账号登录", description = "根据账号和密码进行登录认证")
    @PostMapping("/login")
    public AppLoginResp accountLogin(@Validated @RequestBody AppAccountLoginReq loginReq, HttpServletRequest request) {
        // 校验验证码
        int loginCaptchaEnabled = optionService.getValueByCode2Int("LOGIN_CAPTCHA_ENABLED");
        if (SysConstants.YES.equals(loginCaptchaEnabled)) {
            ValidationUtils.throwIfBlank(loginReq.getCaptcha(), "验证码不能为空");
            ValidationUtils.throwIfBlank(loginReq.getUuid(), "验证码标识不能为空");
            String captchaKey = CacheConstants.CAPTCHA_KEY_PREFIX + loginReq.getUuid();
            String captcha = RedisUtils.get(captchaKey);
            ValidationUtils.throwIfBlank(captcha, CAPTCHA_EXPIRED);
            RedisUtils.delete(captchaKey);
            ValidationUtils.throwIfNotEqualIgnoreCase(loginReq.getCaptcha(), captcha, CAPTCHA_ERROR);
        }

        // 用户登录
        String rawPassword = ExceptionUtils.exToNull(() -> SecureUtils.decryptByRsaPrivateKey(loginReq.getPassword()));
        ValidationUtils.throwIfBlank(rawPassword, "密码解密失败");
        String token = loginService.accountLogin(loginReq.getUsername(), rawPassword, request);
        return AppLoginResp.builder().token(token).build();
    }

    @SaIgnore
    @Operation(summary = "手机号登录", description = "根据手机号和验证码进行登录认证")
    @PostMapping("/phone")
    public AppLoginResp phoneLogin(@Validated @RequestBody AppPhoneLoginReq loginReq) {
        String phone = loginReq.getPhone();
        String captchaKey = CacheConstants.CAPTCHA_KEY_PREFIX + phone;
        String captcha = RedisUtils.get(captchaKey);
        ValidationUtils.throwIfBlank(captcha, CAPTCHA_EXPIRED);
        ValidationUtils.throwIfNotEqualIgnoreCase(loginReq.getCaptcha(), captcha, CAPTCHA_ERROR);
        RedisUtils.delete(captchaKey);
        String token = loginService.phoneLogin(phone);
        return AppLoginResp.builder().token(token).build();
    }

    @SaIgnore
    @Operation(summary = "邮箱登录", description = "根据邮箱和验证码进行登录认证")
    @PostMapping("/email")
    public AppLoginResp emailLogin(@Validated @RequestBody AppEmailLoginReq loginReq) {
        String email = loginReq.getEmail();
        String captchaKey = CacheConstants.CAPTCHA_KEY_PREFIX + email;
        String captcha = RedisUtils.get(captchaKey);
        ValidationUtils.throwIfBlank(captcha, CAPTCHA_EXPIRED);
        ValidationUtils.throwIfNotEqualIgnoreCase(loginReq.getCaptcha(), captcha, CAPTCHA_ERROR);
        RedisUtils.delete(captchaKey);
        String token = loginService.emailLogin(email);
        return AppLoginResp.builder().token(token).build();
    }

    @Operation(summary = "小程序用户退出", description = "注销用户的当前登录")
    @Parameter(name = "Authorization", description = "令牌", required = true, example = "Bearer xxxx-xxxx-xxxx-xxxx", in = ParameterIn.HEADER)
    @PostMapping("/logout")
    public Object logout() {
        Object loginId = StpUtil.getLoginId(-1L);
        StpUtil.logout();
        return loginId;
    }

    @Log(ignore = true)
    @Operation(summary = "获取用户信息", description = "获取登录用户信息")
    @GetMapping("/user/info")
    public AppUserInfoResp getUserInfo() {
        UserContext userContext = UserContextHolder.getContext();
        AppUserDetailResp userDetailResp = indexUserService.get(userContext.getId());
        AppUserInfoResp userInfoResp = BeanUtil.copyProperties(userDetailResp, AppUserInfoResp.class);
        userInfoResp.setPwdExpired(userContext.isPasswordExpired());
        return userInfoResp;
    }

    @SaIgnore
    @Operation(summary = "小程序用户注册", description = "根据用户信息进行注册")
    @PostMapping("/register")
    public R<String> register(@Validated @RequestBody AppUserRegisterReq registerReq) {
        // 校验验证码
        int loginCaptchaEnabled = optionService.getValueByCode2Int("LOGIN_CAPTCHA_ENABLED");
        if (SysConstants.YES.equals(loginCaptchaEnabled)) {
            ValidationUtils.throwIfBlank(registerReq.getCaptcha(), "验证码不能为空");
            ValidationUtils.throwIfBlank(registerReq.getUuid(), "验证码标识不能为空");
            String captchaKey = CacheConstants.CAPTCHA_KEY_PREFIX + registerReq.getUuid();
            String captcha = RedisUtils.get(captchaKey);
            ValidationUtils.throwIfBlank(captcha, CAPTCHA_EXPIRED);
            RedisUtils.delete(captchaKey);
            ValidationUtils.throwIfNotEqualIgnoreCase(registerReq.getCaptcha(), captcha, CAPTCHA_ERROR);
        }

        // 密码加密
        String rawPassword = ExceptionUtils.exToNull(() -> SecureUtils.decryptByRsaPrivateKey(registerReq.getPassword()));
        ValidationUtils.throwIfBlank(rawPassword, "密码解密失败");
        Boolean result = indexUserService.register(registerReq, rawPassword);

        if (result) {
            return R.ok("用户注册成功");
        } else {
            throw new BusinessException("用户注册失败");
        }
    }

    //TODO 路由权限认证，不需要可删
    @Log(ignore = true)
    @Operation(summary = "获取路由信息", description = "获取登录用户的路由信息")
    @GetMapping("/user/route")
    public List<RouteResp> listRoute() {
        return loginService.buildRouteTree(UserContextHolder.getUserId());
    }

}