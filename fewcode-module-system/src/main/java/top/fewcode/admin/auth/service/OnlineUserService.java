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
package top.fewcode.admin.auth.service;

import top.continew.starter.extension.crud.model.query.PageQuery;
import top.continew.starter.extension.crud.model.resp.PageResp;
import top.fewcode.admin.auth.model.query.OnlineUserQuery;
import top.fewcode.admin.auth.model.resp.OnlineUserResp;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 在线用户业务接口
 *
 * @author GALAwang
 * @since 2023/3/25 22:48
 */
public interface OnlineUserService {

    /**
     * 分页查询列表
     *
     * @param query     查询条件
     * @param pageQuery 分页查询条件
     * @return 分页列表信息
     */
    PageResp<OnlineUserResp> page(OnlineUserQuery query, PageQuery pageQuery);

    /**
     * 查询列表
     *
     * @param query 查询条件
     * @return 列表信息
     */
    List<OnlineUserResp> list(OnlineUserQuery query);

    /**
     * 查询 Token 最后活跃时间
     *
     * @param token Token
     * @return 最后活跃时间
     */
    LocalDateTime getLastActiveTime(String token);

    /**
     * 踢出用户
     *
     * @param userId 用户 ID
     */
    void kickOut(Long userId);
}
