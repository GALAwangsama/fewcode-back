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
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.continew.starter.extension.crud.annotation.CrudRequestMapping;
import top.continew.starter.extension.crud.enums.Api;
import top.continew.starter.log.core.annotation.Log;
import top.fewcode.admin.common.base.BaseController;
import top.fewcode.admin.system.model.query.FileQuery;
import top.fewcode.admin.system.model.req.FileReq;
import top.fewcode.admin.system.model.resp.FileResp;
import top.fewcode.admin.system.model.resp.FileStatisticsResp;
import top.fewcode.admin.system.service.FileService;

/**
 * 文件管理 API
 *
 * @author GALAwang
 * @since 2023/12/23 10:38
 */
@Tag(name = "文件管理 API")
@RestController
@RequiredArgsConstructor
@CrudRequestMapping(value = "/system/file", api = {Api.PAGE, Api.UPDATE, Api.DELETE})
public class FileController extends BaseController<FileService, FileResp, FileResp, FileQuery, FileReq> {

    @Log(ignore = true)
    @Operation(summary = "查询文件资源统计", description = "查询文件资源统计")
    @SaCheckPermission("system:file:list")
    @GetMapping("/statistics")
    public FileStatisticsResp statistics() {
        return baseService.statistics();
    }
}