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
package top.fewcode.admin.knowledge.model.query;

import java.io.Serial;
import java.io.Serializable;
import java.time.*;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

import top.continew.starter.data.core.annotation.Query;
import top.continew.starter.data.core.enums.QueryType;

/**
 * 知识库管理查询条件
 *
 * @author admin
 * @since 2025/01/30 10:36
 */
@Data
@Schema(description = "知识库管理查询条件")
public class KnowledgeManageQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 文章类型
     */
    @Schema(description = "文章类型")
    @Query(type = QueryType.EQ)
    private String type;

    /**
     * 文章题目
     */
    @Schema(description = "文章题目")
    @Query(type = QueryType.LIKE)
    private String title;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @Query(type = QueryType.EQ)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    @Query(type = QueryType.EQ)
    private LocalDateTime updateTime;

    /**
     * 创建用户
     */
    @Schema(description = "创建用户")
    @Query(type = QueryType.EQ)
    private Long createUser;

    /**
     * 更新用户
     */
    @Schema(description = "更新用户")
    @Query(type = QueryType.EQ)
    private Long updateUser;
}