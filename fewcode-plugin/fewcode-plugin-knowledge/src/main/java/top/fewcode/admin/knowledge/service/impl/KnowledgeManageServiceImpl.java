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
package top.fewcode.admin.knowledge.service.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import top.continew.starter.extension.crud.service.BaseServiceImpl;
import top.fewcode.admin.knowledge.mapper.KnowledgeManageMapper;
import top.fewcode.admin.knowledge.model.entity.KnowledgeManageDO;
import top.fewcode.admin.knowledge.model.query.KnowledgeManageQuery;
import top.fewcode.admin.knowledge.model.req.KnowledgeManageReq;
import top.fewcode.admin.knowledge.model.resp.KnowledgeManageDetailResp;
import top.fewcode.admin.knowledge.model.resp.KnowledgeManageResp;
import top.fewcode.admin.knowledge.service.KnowledgeManageService;

/**
 * 知识库管理业务实现
 *
 * @author admin
 * @since 2025/01/30 10:36
 */
@Service
@RequiredArgsConstructor
public class KnowledgeManageServiceImpl extends BaseServiceImpl<KnowledgeManageMapper, KnowledgeManageDO, KnowledgeManageResp, KnowledgeManageDetailResp, KnowledgeManageQuery, KnowledgeManageReq> implements KnowledgeManageService {}