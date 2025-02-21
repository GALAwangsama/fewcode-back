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
package top.fewcode.admin.indexSystem.service;

import com.alicp.jetcache.anno.CacheUpdate;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import top.continew.starter.data.mp.service.IService;
import top.continew.starter.extension.crud.service.BaseService;
import top.fewcode.admin.common.constant.CacheConstants;
import top.fewcode.admin.indexSystem.model.entity.IndexUserDO;
import top.fewcode.admin.indexSystem.model.query.IndexUserQuery;
import top.fewcode.admin.indexSystem.model.req.user.IndexUserBasicInfoUpdateReq;
import top.fewcode.admin.indexSystem.model.req.user.IndexUserImportReq;
import top.fewcode.admin.indexSystem.model.req.user.IndexUserPasswordResetReq;
import top.fewcode.admin.indexSystem.model.req.user.IndexUserReq;
import top.fewcode.admin.indexSystem.model.resp.user.IndexUserDetailResp;
import top.fewcode.admin.indexSystem.model.resp.user.IndexUserImportParseResp;
import top.fewcode.admin.indexSystem.model.resp.user.IndexUserImportResp;
import top.fewcode.admin.indexSystem.model.resp.user.IndexUserResp;

import java.io.IOException;

/**
 * 用户业务接口
 *
 * @author Charles7c
 * @since 2022/12/21 21:48
 */
public interface IndexUserService extends BaseService<IndexUserResp, IndexUserDetailResp, IndexUserQuery, IndexUserReq>, IService<IndexUserDO> {

    @Transactional(rollbackFor = Exception.class)
    @CacheUpdate(key = "#id", value = "#req.nickname", name = CacheConstants.USER_KEY_PREFIX)
    void update(IndexUserReq req, Long id);

    /**
     * 下载导入模板
     *
     * @param response 响应对象
     * @throws IOException /
     */
    void downloadImportTemplate(HttpServletResponse response) throws IOException;

    /**
     * 解析导入数据
     *
     * @param file 导入文件
     * @return 解析结果
     */
    IndexUserImportParseResp parseImport(MultipartFile file);

    /**
     * 导入数据
     *
     * @param req 导入信息
     * @return 导入结果
     */
    IndexUserImportResp importUser(IndexUserImportReq req);

    /**
     * 重置密码
     *
     * @param req 重置信息
     * @param id  ID
     */
    void resetPassword(IndexUserPasswordResetReq req, Long id);


    /**
     * 上传头像
     *
     * @param avatar 头像文件
     * @param id     ID
     * @return 新头像路径
     * @throws IOException /
     */
    String updateAvatar(MultipartFile avatar, Long id) throws IOException;

    /**
     * 修改基础信息
     *
     * @param req 修改信息
     * @param id  ID
     */
    @CacheUpdate(key = "#id", value = "#req.nickname", name = CacheConstants.USER_KEY_PREFIX)
    void updateBasicInfo(IndexUserBasicInfoUpdateReq req, Long id);

    /**
     * 修改密码
     *
     * @param oldPassword 当前密码
     * @param newPassword 新密码
     * @param id          ID
     */
    void updatePassword(String oldPassword, String newPassword, Long id);

    /**
     * 修改手机号
     *
     * @param newPhone    新手机号
     * @param oldPassword 当前密码
     * @param id          ID
     */
    void updatePhone(String newPhone, String oldPassword, Long id);

    /**
     * 修改邮箱
     *
     * @param newEmail    新邮箱
     * @param oldPassword 当前密码
     * @param id          ID
     */
    void updateEmail(String newEmail, String oldPassword, Long id);

    /**
     * 新增
     *
     * @param user 用户信息
     * @return ID
     */
    Long add(IndexUserDO user);

    /**
     * 根据用户名查询
     *
     * @param username 用户名
     * @return 用户信息
     */
    IndexUserDO  getByUsername(String username);

    /**
     * 根据手机号查询
     *
     * @param phone 手机号
     * @return 用户信息
     */
    IndexUserDO  getByPhone(String phone);

    /**
     * 根据邮箱查询
     *
     * @param email 邮箱
     * @return 用户信息
     */
    IndexUserDO  getByEmail(String email);

}
