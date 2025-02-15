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
package top.fewcode.admin.knowledge.model.req;

import java.io.Serial;
import java.time.*;

import jakarta.validation.constraints.*;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

import org.hibernate.validator.constraints.Length;

import top.continew.starter.extension.crud.model.req.BaseReq;

/**
 * 创建或修改知识库管理参数
 *
 * @author admin
 * @since 2025/01/30 10:36
 */
@Data
@Schema(description = "创建或修改知识库管理参数")
public class KnowledgeManageReq extends BaseReq {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 文章类型
     */
    @Schema(description = "文章类型")
    @NotBlank(message = "文章类型不能为空")
    @Length(max = 50, message = "文章类型长度不能超过 {max} 个字符")
    private String type;

    /**
     * 文章题目
     */
    @Schema(description = "文章题目")
    @NotBlank(message = "文章题目不能为空")
    @Length(max = 255, message = "文章题目长度不能超过 {max} 个字符")
    private String title;

    /**
     * 内容
     */
    @Schema(description = "内容")
    @NotBlank(message = "内容不能为空")
    @Length(max = 65535, message = "内容长度不能超过 {max} 个字符")
    private String content;
}