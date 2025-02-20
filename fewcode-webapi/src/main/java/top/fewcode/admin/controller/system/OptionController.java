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
package top.fewcode.admin.controller.system;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.fewcode.admin.system.service.OptionService;
import top.fewcode.admin.system.model.query.OptionQuery;
import top.fewcode.admin.system.model.req.OptionReq;
import top.fewcode.admin.system.model.req.OptionResetValueReq;
import top.fewcode.admin.system.model.resp.OptionResp;

import java.util.List;

/**
 * 参数管理 API
 *
 * @author Bull-BCLS
 * @since 2023/8/26 19:38
 */
@Tag(name = "参数管理 API")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/option")
public class OptionController {

    private final OptionService baseService;

    @Operation(summary = "查询参数列表", description = "查询参数列表")
    @SaCheckPermission("system:config:list")
    @GetMapping
    public List<OptionResp> list(@Validated OptionQuery query) {
        return baseService.list(query);
    }

    @Operation(summary = "修改参数", description = "修改参数")
    @SaCheckPermission("system:config:update")
    @PutMapping
    public void update(@Valid @RequestBody List<OptionReq> options) {
        baseService.update(options);
    }

    @Operation(summary = "重置参数", description = "重置参数")
    @SaCheckPermission("system:config:reset")
    @PatchMapping("/value")
    public void resetValue(@Validated @RequestBody OptionResetValueReq req) {
        baseService.resetValue(req);
    }
}