package top.continew.admin.knowledge.service;

import top.continew.starter.extension.crud.service.BaseService;
import top.continew.admin.knowledge.model.query.KnowledgeManageQuery;
import top.continew.admin.knowledge.model.req.KnowledgeManageReq;
import top.continew.admin.knowledge.model.resp.KnowledgeManageDetailResp;
import top.continew.admin.knowledge.model.resp.KnowledgeManageResp;

/**
 * 知识库管理业务接口
 *
 * @author admin
 * @since 2025/01/30 10:36
 */
public interface KnowledgeManageService extends BaseService<KnowledgeManageResp, KnowledgeManageDetailResp, KnowledgeManageQuery, KnowledgeManageReq> {}