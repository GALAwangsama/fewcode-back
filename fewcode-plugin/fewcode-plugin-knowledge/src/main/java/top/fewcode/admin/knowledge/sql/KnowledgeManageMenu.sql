-- 知识库管理管理菜单
INSERT INTO `sys_menu`
(`title`, `parent_id`, `type`, `path`, `name`, `component`, `redirect`, `icon`, `is_external`, `is_cache`, `is_hidden`, `permission`, `sort`, `status`, `create_user`, `create_time`, `update_user`, `update_time`)
VALUES
('知识库管理管理', 1000, 2, '/knowledge/knowledgeManage', 'KnowledgeManage', 'knowledge/knowledgeManage/index', NULL, NULL, b'0', b'0', b'0', NULL, 1, 1, 1, NOW(), NULL, NULL);

SET @parentId = LAST_INSERT_ID();

-- 知识库管理管理按钮
INSERT INTO `sys_menu`
(`title`, `parent_id`, `type`, `path`, `name`, `component`, `redirect`, `icon`, `is_external`, `is_cache`, `is_hidden`, `permission`, `sort`, `status`, `create_user`, `create_time`, `update_user`, `update_time`)
VALUES
('列表', @parentId, 3, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'knowledge:knowledgeManage:list', 1, 1, 1, NOW(), NULL, NULL),
('详情', @parentId, 3, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'knowledge:knowledgeManage:detail', 2, 1, 1, NOW(), NULL, NULL),
('新增', @parentId, 3, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'knowledge:knowledgeManage:add', 3, 1, 1, NOW(), NULL, NULL),
('修改', @parentId, 3, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'knowledge:knowledgeManage:update', 4, 1, 1, NOW(), NULL, NULL),
('删除', @parentId, 3, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'knowledge:knowledgeManage:delete', 5, 1, 1, NOW(), NULL, NULL),
('导出', @parentId, 3, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'knowledge:knowledgeManage:export', 6, 1, 1, NOW(), NULL, NULL);

