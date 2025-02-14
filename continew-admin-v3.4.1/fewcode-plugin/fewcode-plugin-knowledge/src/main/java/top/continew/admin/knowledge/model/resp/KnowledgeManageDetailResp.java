package top.continew.admin.knowledge.model.resp;

import java.io.Serial;
import java.time.*;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;

import top.continew.starter.extension.crud.model.resp.BaseDetailResp;

/**
 * 知识库管理详情信息
 *
 * @author admin
 * @since 2025/01/30 10:36
 */
@Data
@ExcelIgnoreUnannotated
@Schema(description = "知识库管理详情信息")
public class KnowledgeManageDetailResp extends BaseDetailResp {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 文章类型
     */
    @Schema(description = "文章类型")
    @ExcelProperty(value = "文章类型")
    private String type;

    /**
     * 文章题目
     */
    @Schema(description = "文章题目")
    @ExcelProperty(value = "文章题目")
    private String title;

    /**
     * 内容
     */
    @Schema(description = "内容")
    @ExcelProperty(value = "内容")
    private String content;

    /**
     * 是否删除
     */
    @Schema(description = "是否删除")
    @ExcelProperty(value = "是否删除")
    private Boolean isDelete;
}