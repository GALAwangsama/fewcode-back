-- liquibase formatted sql

-- changeset galawang:1
-- comment 初始化知识库插件
-- 初始化表结构
create table if not exists sys_knowledge_manage
(
    id          int auto_increment comment '主键'
        primary key,
    type        varchar(50)                          not null comment '文章类型',
    title       varchar(255)                         not null comment '文章题目',
    content     text                                 not null comment '内容',
    create_time datetime   default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime   default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '修改时间',
    is_delete   tinyint(1) default 0                 not null comment '是否删除',
    create_user bigint                               not null comment '创建用户',
    update_user bigint     default 0                 not null comment '更新用户'
)comment '知识库管理表';



-- 知识库管理管理菜单
INSERT INTO `sys_menu`
(`title`, `parent_id`, `type`, `path`, `name`, `component`, `redirect`, `icon`, `is_external`, `is_cache`, `is_hidden`, `permission`, `sort`, `status`, `create_user`, `create_time`, `update_user`, `update_time`)
SELECT
    '知识库管理管理', 7000, 2, '/knowledge/knowledgeManage', 'KnowledgeManage', 'knowledge/knowledgeManage/index', NULL, NULL, b'0', b'0', b'0', NULL, 1, 1, 1, NOW(), NULL, NULL
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM `sys_menu` WHERE `title` = '知识库管理管理' AND `parent_id` = 7000
);

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








