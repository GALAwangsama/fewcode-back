package top.continew.admin.knowledge.model.query;

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