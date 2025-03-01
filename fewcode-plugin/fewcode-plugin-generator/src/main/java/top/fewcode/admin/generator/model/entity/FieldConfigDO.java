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
package top.fewcode.admin.generator.model.entity;

import cn.hutool.core.util.StrUtil;
import cn.hutool.db.meta.Column;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import top.continew.starter.core.constant.StringConstants;
import top.fewcode.admin.generator.enums.FormTypeEnum;
import top.fewcode.admin.generator.enums.QueryTypeEnum;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 字段配置实体
 *
 * @author GALAwang
 * @since 2023/4/12 20:21
 */
@Data
@NoArgsConstructor
@TableName("gen_field_config")
@Schema(description = "字段配置信息")
public class FieldConfigDO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId
    private Long id;

    /**
     * 表名称
     */
    @Schema(description = "表名称", example = "sys_user")
    @NotBlank(message = "表名称不能为空")
    private String tableName;

    /**
     * 列名称
     */
    @Schema(description = "列名称", example = "nickname")
    @NotBlank(message = "列名称不能为空")
    private String columnName;

    /**
     * 列类型
     */
    @Schema(description = "列类型", example = "varchar")
    @NotBlank(message = "列类型不能为空")
    private String columnType;

    /**
     * 列大小
     */
    @Schema(description = "列大小", example = "255")
    private Long columnSize;

    /**
     * 字段名称
     */
    @Schema(description = "字段名称", example = "nickname")
    @NotBlank(message = "字段名称不能为空")
    private String fieldName;

    /**
     * 字段类型
     */
    @Schema(description = "字段类型", example = "String")
    @NotBlank(message = "字段类型不能为空")
    private String fieldType;

    /**
     * 字段排序
     */
    @Schema(description = "字段排序", example = "字段排序")
    @NotNull(message = "字段排序不能为空")
    private Integer fieldSort;

    /**
     * 注释
     */
    @Schema(description = "注释", example = "昵称")
    private String comment;

    /**
     * 是否必填
     */
    @Schema(description = "是否必填", example = "true")
    private Boolean isRequired;

    /**
     * 是否在列表中显示
     */
    @Schema(description = "是否在列表中显示", example = "true")
    private Boolean showInList;

    /**
     * 是否在表单中显示
     */
    @Schema(description = "是否在表单中显示", example = "true")
    private Boolean showInForm;

    /**
     * 是否在查询中显示
     */
    @Schema(description = "是否在查询中显示", example = "true")
    private Boolean showInQuery;

    /**
     * 表单类型
     */
    @Schema(description = "表单类型", example = "1")
    private FormTypeEnum formType;

    /**
     * 查询方式
     */
    @Schema(description = "查询方式", example = "1")
    private QueryTypeEnum queryType;

    /**
     * 字典编码
     */
    @Schema(description = "字典编码", example = "notice_type")
    private String dictCode;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间", example = "2023-08-08 08:08:08", type = "string")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    public FieldConfigDO(@NonNull Column column) {
        this.setTableName(column.getTableName());
        this.setColumnName(column.getName());
        this.setColumnType(column.getTypeName());
        this.setColumnSize(column.getSize());
        this.setComment(column.getComment());
        this.setIsRequired(!column.isPk() && !column.isNullable());
        this.setShowInList(true);
        this.setShowInForm(this.getIsRequired());
        this.setShowInQuery(this.getIsRequired());
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
        this.fieldName = StrUtil.toCamelCase(this.columnName);
    }

    public void setColumnType(String columnType) {
        String[] arr = StrUtil.splitToArray(columnType, StringConstants.SPACE);
        this.columnType = arr.length > 1 ? arr[0].toLowerCase() : columnType.toLowerCase();
    }

    public void setComment(String comment) {
        this.comment = StrUtil.nullToDefault(comment, StringConstants.EMPTY);
    }
}
