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
package top.fewcode.admin.knowledge.model.entity;

import java.io.Serial;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import com.baomidou.mybatisplus.annotation.TableName;

import top.continew.starter.extension.crud.model.entity.BaseDO;

/**
 * 知识库管理实体
 *
 * @author admin
 * @since 2025/01/30 10:36
 */
@Data
@TableName("sys_knowledge_manage")
public class KnowledgeManageDO extends BaseDO {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 声明id为自增类型
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 文章类型
     */
    private String type;

    /**
     * 文章题目
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 是否删除
     */
    private Boolean isDelete;
}