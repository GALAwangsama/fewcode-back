package top.continew.admin.controller.knowledge;

import top.continew.starter.extension.crud.enums.Api;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;

import top.continew.starter.extension.crud.annotation.CrudRequestMapping;
import top.continew.admin.common.base.BaseController;
import top.continew.admin.knowledge.model.query.KnowledgeManageQuery;
import top.continew.admin.knowledge.model.req.KnowledgeManageReq;
import top.continew.admin.knowledge.model.resp.KnowledgeManageDetailResp;
import top.continew.admin.knowledge.model.resp.KnowledgeManageResp;
import top.continew.admin.knowledge.service.KnowledgeManageService;

/**
 * 知识库管理管理 API
 *
 * @author admin
 * @since 2025/01/30 10:36
 */
@Tag(name = "知识库管理管理 API")
@RestController
@CrudRequestMapping(value = "/knowledge/knowledgeManage", api = {Api.PAGE, Api.DETAIL, Api.ADD, Api.UPDATE, Api.DELETE, Api.EXPORT})
public class KnowledgeManageController extends BaseController<KnowledgeManageService, KnowledgeManageResp, KnowledgeManageDetailResp, KnowledgeManageQuery, KnowledgeManageReq> {}