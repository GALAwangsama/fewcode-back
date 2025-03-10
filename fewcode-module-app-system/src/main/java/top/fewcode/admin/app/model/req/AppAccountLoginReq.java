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
package top.fewcode.admin.app.model.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 账号登录参数
 *
 * @author GALAwang
 * @since 2022/12/21 20:43
 */
@Data
@Schema(description = "账号登录参数")
public class AppAccountLoginReq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;


    /**
     * 微信用户唯一标识
     */
    @Schema(description = "微信用户唯一标识", example = "o6_x0OXJ-lxJ98Kl-8X2j5X2j5X2j5X2")
    @NotBlank(message = "微信用户唯一标识不能为空")
    private String openId;

    /**
     * 小程序用户名
     */
    @Schema(description = "小程序用户名", example = "zhangsan")
    @NotBlank(message = "小程序用户名不能为空")
    private String username;

    /**
     * 密码（加密）
     */
    @Schema(description = "密码（加密）", example = "HHwZoiBwCfh0xLdWOAd0bHOkEZlIMMOQKJyeFUw9T3ArrhL57od2i42s1o0sSXKkeHPJXvQsninhPFH2lArDDQ==")
    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 验证码
     */
    @Schema(description = "验证码", example = "ABCD")
    private String captcha;

    /**
     * 验证码标识
     */
    @Schema(description = "验证码标识", example = "090b9a2c-1691-4fca-99db-e4ed0cff362f")
    private String uuid;
}
