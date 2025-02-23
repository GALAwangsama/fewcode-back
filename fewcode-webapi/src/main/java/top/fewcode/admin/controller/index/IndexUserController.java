package top.fewcode.admin.controller.index;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.util.ReUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.continew.starter.core.util.ExceptionUtils;
import top.continew.starter.core.validation.ValidationUtils;
import top.continew.starter.extension.crud.annotation.CrudRequestMapping;
import top.continew.starter.extension.crud.enums.Api;
import top.continew.starter.extension.crud.model.resp.BaseIdResp;
import top.continew.starter.extension.crud.validation.CrudValidationGroup;
import top.fewcode.admin.common.base.BaseController;
import top.fewcode.admin.common.constant.RegexConstants;
import top.fewcode.admin.common.util.SecureUtils;
import top.fewcode.admin.indexSystem.model.query.IndexUserQuery;
import top.fewcode.admin.indexSystem.model.req.user.IndexUserImportReq;
import top.fewcode.admin.indexSystem.model.req.user.IndexUserPasswordResetReq;
import top.fewcode.admin.indexSystem.model.req.user.IndexUserReq;
import top.fewcode.admin.indexSystem.model.resp.user.IndexUserDetailResp;
import top.fewcode.admin.indexSystem.model.resp.user.IndexUserImportParseResp;
import top.fewcode.admin.indexSystem.model.resp.user.IndexUserImportResp;
import top.fewcode.admin.indexSystem.model.resp.user.IndexUserResp;
import top.fewcode.admin.indexSystem.service.IndexUserService;

import java.io.IOException;

/**
 * 前台用户管理 API
 *
 * @author GALAwang
 * @since 2023/2/20 21:00
 */
@Tag(name = "前台用户管理 API")
@Validated
@RestController
@RequiredArgsConstructor
@CrudRequestMapping(value = "/index/user", api = {Api.PAGE, Api.LIST, Api.DETAIL, Api.ADD, Api.UPDATE, Api.DELETE,
        Api.EXPORT})
public class IndexUserController extends BaseController<IndexUserService, IndexUserResp, IndexUserDetailResp, IndexUserQuery, IndexUserReq> {

    private final IndexUserService userService;

    @Override
    public BaseIdResp<Long> add(@Validated(CrudValidationGroup.Add.class) @RequestBody IndexUserReq req) {
        String rawPassword = ExceptionUtils.exToNull(() -> SecureUtils.decryptByRsaPrivateKey(req.getPassword()));
        ValidationUtils.throwIfNull(rawPassword, "密码解密失败");
        ValidationUtils.throwIf(!ReUtil
                .isMatch(RegexConstants.PASSWORD, rawPassword), "密码长度为 8-32 个字符，支持大小写字母、数字、特殊字符，至少包含字母和数字");
        req.setPassword(rawPassword);
        return super.add(req);
    }

    @Operation(summary = "下载导入模板", description = "下载导入模板")
    @SaCheckPermission("index:user:import")
    @GetMapping(value = "/import/template", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void downloadImportTemplate(HttpServletResponse response) throws IOException {
        userService.downloadImportTemplate(response);
    }

    @Operation(summary = "解析导入数据", description = "解析导入数据")
    @SaCheckPermission("index:user:import")
    @PostMapping("/import/parse")
    public IndexUserImportParseResp parseImport(@NotNull(message = "文件不能为空") MultipartFile file) {
        ValidationUtils.throwIf(file::isEmpty, "文件不能为空");
        return userService.parseImport(file);
    }

    @Operation(summary = "导入数据", description = "导入数据")
    @SaCheckPermission("index:user:import")
    @PostMapping(value = "/import")
    public IndexUserImportResp importUser(@Validated @RequestBody IndexUserImportReq req) {
        return userService.importUser(req);
    }

    @Operation(summary = "重置密码", description = "重置用户登录密码")
    @Parameter(name = "id", description = "ID", example = "1", in = ParameterIn.PATH)
    @SaCheckPermission("index:user:resetPwd")
    @PatchMapping("/{id}/password")
    public void resetPassword(@Validated @RequestBody IndexUserPasswordResetReq req, @PathVariable Long id) {
        String rawNewPassword = ExceptionUtils.exToNull(() -> SecureUtils.decryptByRsaPrivateKey(req.getNewPassword()));
        ValidationUtils.throwIfNull(rawNewPassword, "新密码解密失败");
        ValidationUtils.throwIf(!ReUtil
                .isMatch(RegexConstants.PASSWORD, rawNewPassword), "密码长度为 8-32 个字符，支持大小写字母、数字、特殊字符，至少包含字母和数字");
        req.setNewPassword(rawNewPassword);
        baseService.resetPassword(req, id);
    }

}
