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
package top.fewcode.admin.indexSystem.model.resp.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户导入结果
 *
 * @author kils
 * @since 2024-06-18 14:37
 */
@Data
@Schema(description = "前台用户导入结果")
@AllArgsConstructor
@NoArgsConstructor
public class IndexUserImportResp {

    private static final long serialVersionUID = 1L;

    /**
     * 总计行数
     */
    @Schema(description = "总计行数", example = "100")
    private Integer totalRows;

    /**
     * 新增行数
     */
    @Schema(description = "新增行数", example = "100")
    private Integer insertRows;

    /**
     * 修改行数
     */
    @Schema(description = "修改行数", example = "100")
    private Integer updateRows;
}
