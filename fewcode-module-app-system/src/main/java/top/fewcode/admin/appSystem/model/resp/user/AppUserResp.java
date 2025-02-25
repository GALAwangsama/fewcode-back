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
package top.fewcode.admin.appSystem.model.resp.user;

import cn.crane4j.annotation.Assemble;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import top.continew.starter.extension.crud.model.resp.BaseDetailResp;
import top.continew.starter.security.mask.annotation.JsonMask;
import top.continew.starter.security.mask.enums.MaskType;
import top.fewcode.admin.common.constant.ContainerConstants;
import top.fewcode.admin.common.context.UserContextHolder;
import top.fewcode.admin.common.enums.DisEnableStatusEnum;
import top.fewcode.admin.common.enums.GenderEnum;

import java.io.Serial;
import java.util.Objects;

/**
 * 用户信息
 *
 * @author Charles7c
 * @since 2023/2/20 21:08
 */
@Data
@Schema(description = "前台用户信息")
@Assemble(key = "id", prop = ":roleIds", sort = 0, container = ContainerConstants.USER_ROLE_ID_LIST)
public class AppUserResp extends BaseDetailResp {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户名
     */
    @Schema(description = "前台用户名", example = "zhangsan")
    private String username;

    /**
     * 昵称
     */
    @Schema(description = "昵称", example = "张三")
    private String nickname;

    /**
     * 性别
     */
    @Schema(description = "性别", example = "1")
    private GenderEnum gender;

    /**
     * 头像地址
     */
    @Schema(description = "头像地址", example = "https://himg.bdimg.com/sys/portrait/item/public.1.81ac9a9e.rf1ix17UfughLQjNo7XQ_w.jpg")
    private String avatar;

    /**
     * 邮箱
     */
    @Schema(description = "邮箱", example = "c*******@126.com")
    @JsonMask(MaskType.EMAIL)
    private String email;

    /**
     * 手机号码
     */
    @Schema(description = "手机号码", example = "188****8888")
    @JsonMask(MaskType.MOBILE_PHONE)
    private String phone;

    /**
     * 状态
     */
    @Schema(description = "状态", example = "1")
    private DisEnableStatusEnum status;

    /**
     * 描述
     */
    @Schema(description = "描述", example = "张三描述信息")
    private String description;


    @Override
    public Boolean getDisabled() {
        return Objects.equals(this.getId(), UserContextHolder.getUserId());
    }
}
