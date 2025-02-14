package top.continew.admin.knowledge.service.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import top.continew.starter.extension.crud.service.BaseServiceImpl;
import top.continew.admin.knowledge.mapper.KnowledgeManageMapper;
import top.continew.admin.knowledge.model.entity.KnowledgeManageDO;
import top.continew.admin.knowledge.model.query.KnowledgeManageQuery;
import top.continew.admin.knowledge.model.req.KnowledgeManageReq;
import top.continew.admin.knowledge.model.resp.KnowledgeManageDetailResp;
import top.continew.admin.knowledge.model.resp.KnowledgeManageResp;
import top.continew.admin.knowledge.service.KnowledgeManageService;

/**
 * 知识库管理业务实现
 *
 * @author admin
 * @since 2025/01/30 10:36
 */
@Service
@RequiredArgsConstructor
public class KnowledgeManageServiceImpl extends BaseServiceImpl<KnowledgeManageMapper, KnowledgeManageDO, KnowledgeManageResp, KnowledgeManageDetailResp, KnowledgeManageQuery, KnowledgeManageReq> implements KnowledgeManageService {}