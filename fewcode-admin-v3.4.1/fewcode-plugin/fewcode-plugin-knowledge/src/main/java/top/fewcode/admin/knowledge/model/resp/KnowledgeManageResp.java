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
package top.fewcode.admin.knowledge.model.resp;

import java.io.Serial;
import java.time.*;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

import top.continew.starter.extension.crud.model.resp.BaseResp;

/**
 * 知识库管理信息
 *
 * @author admin
 * @since 2025/01/30 10:36
 */
@Data
@Schema(description = "知识库管理信息")
public class KnowledgeManageResp extends BaseResp {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 文章类型
     */
    @Schema(description = "文章类型")
    private String type;

    /**
     * 文章题目
     */
    @Schema(description = "文章题目")
    private String title;

    /**
     * 内容
     */
    @Schema(description = "内容")
    private String content;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}