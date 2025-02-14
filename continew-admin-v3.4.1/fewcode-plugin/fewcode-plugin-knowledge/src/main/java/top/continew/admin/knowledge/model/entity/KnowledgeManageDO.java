package top.continew.admin.knowledge.model.entity;

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