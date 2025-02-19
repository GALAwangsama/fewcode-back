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
package top.fewcode.admin.system.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 角色和部门实体
 *
 * @author Charles7c
 * @since 2023/2/18 21:57
 */
@Data
@NoArgsConstructor
@TableName("sys_role_dept")
public class RoleDeptDO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 角色 ID
     */
    private Long roleId;

    /**
     * 部门 ID
     */
    private Long deptId;

    public RoleDeptDO(Long roleId, Long deptId) {
        this.roleId = roleId;
        this.deptId = deptId;
    }
}
