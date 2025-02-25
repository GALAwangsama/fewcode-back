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
package top.fewcode.admin.controller.app;

import com.xkcoding.justauth.AuthRequestFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.continew.starter.cache.redisson.util.RedisUtils;
import top.continew.starter.core.util.ExceptionUtils;
import top.continew.starter.core.validation.ValidationUtils;
import top.fewcode.admin.appSystem.model.req.user.AppUserBasicInfoUpdateReq;
import top.fewcode.admin.appSystem.model.req.user.AppUserEmailUpdateRequest;
import top.fewcode.admin.appSystem.model.req.user.AppUserPasswordUpdateReq;
import top.fewcode.admin.appSystem.model.req.user.AppUserPhoneUpdateReq;
import top.fewcode.admin.appSystem.service.AppUserService;
import top.fewcode.admin.common.constant.CacheConstants;
import top.fewcode.admin.common.context.UserContextHolder;
import top.fewcode.admin.common.util.SecureUtils;
import top.fewcode.admin.system.model.resp.AvatarResp;

import java.io.IOException;

/**
 * 小程序个人中心 API
 *
 * @author GALAwang
 * @since 2023/1/2 11:41
 */
@Tag(name = "小程序个人中心 API")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/app/user")
public class AppUserCenterController {

    private static final String DECRYPT_FAILED = "当前密码解密失败";
    private static final String CAPTCHA_EXPIRED = "验证码已失效";
    private final AppUserService userService;
    private final AuthRequestFactory authRequestFactory;

    @Operation(summary = "修改头像", description = "用户修改个人头像")
    @PostMapping("/avatar")
    public AvatarResp updateAvatar(@NotNull(message = "头像不能为空") MultipartFile avatarFile) throws IOException {
        ValidationUtils.throwIf(avatarFile::isEmpty, "头像不能为空");
        String newAvatar = userService.updateAvatar(avatarFile, UserContextHolder.getUserId());
        return AvatarResp.builder().avatar(newAvatar).build();
    }

    @Operation(summary = "修改基础信息", description = "修改用户基础信息")
    @PatchMapping("/basic/info")
    public void updateBasicInfo(@Validated @RequestBody AppUserBasicInfoUpdateReq req) {
        userService.updateBasicInfo(req, UserContextHolder.getUserId());
    }

    @Operation(summary = "修改密码", description = "修改用户登录密码")
    @PatchMapping("/password")
    public void updatePassword(@Validated @RequestBody AppUserPasswordUpdateReq updateReq) {
        String rawOldPassword = ExceptionUtils.exToNull(() -> SecureUtils.decryptByRsaPrivateKey(updateReq
                .getOldPassword()));
        ValidationUtils.throwIfNull(rawOldPassword, DECRYPT_FAILED);
        String rawNewPassword = ExceptionUtils.exToNull(() -> SecureUtils.decryptByRsaPrivateKey(updateReq
                .getNewPassword()));
        ValidationUtils.throwIfNull(rawNewPassword, "新密码解密失败");
        userService.updatePassword(rawOldPassword, rawNewPassword, UserContextHolder.getUserId());
    }

    @Operation(summary = "修改手机号", description = "修改手机号")
    @PatchMapping("/phone")
    public void updatePhone(@Validated @RequestBody AppUserPhoneUpdateReq updateReq) {
        String rawOldPassword = ExceptionUtils.exToNull(() -> SecureUtils.decryptByRsaPrivateKey(updateReq
                .getOldPassword()));
        ValidationUtils.throwIfBlank(rawOldPassword, DECRYPT_FAILED);
        String captchaKey = CacheConstants.CAPTCHA_KEY_PREFIX + updateReq.getPhone();
        String captcha = RedisUtils.get(captchaKey);
        ValidationUtils.throwIfBlank(captcha, CAPTCHA_EXPIRED);
        ValidationUtils.throwIfNotEqualIgnoreCase(updateReq.getCaptcha(), captcha, "验证码错误");
        RedisUtils.delete(captchaKey);
        userService.updatePhone(updateReq.getPhone(), rawOldPassword, UserContextHolder.getUserId());
    }

    @Operation(summary = "修改邮箱", description = "修改用户邮箱")
    @PatchMapping("/email")
    public void updateEmail(@Validated @RequestBody AppUserEmailUpdateRequest updateReq) {
        String rawOldPassword = ExceptionUtils.exToNull(() -> SecureUtils.decryptByRsaPrivateKey(updateReq
                .getOldPassword()));
        ValidationUtils.throwIfBlank(rawOldPassword, DECRYPT_FAILED);
        String captchaKey = CacheConstants.CAPTCHA_KEY_PREFIX + updateReq.getEmail();
        String captcha = RedisUtils.get(captchaKey);
        ValidationUtils.throwIfBlank(captcha, CAPTCHA_EXPIRED);
        ValidationUtils.throwIfNotEqualIgnoreCase(updateReq.getCaptcha(), captcha, "验证码错误");
        RedisUtils.delete(captchaKey);
        userService.updateEmail(updateReq.getEmail(), rawOldPassword, UserContextHolder.getUserId());
    }

}
