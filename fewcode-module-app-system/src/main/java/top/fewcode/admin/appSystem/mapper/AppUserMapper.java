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
package top.fewcode.admin.appSystem.mapper;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import top.fewcode.admin.appSystem.model.entity.AppUserDO;
import top.fewcode.admin.appSystem.model.resp.user.AppUserDetailResp;
import top.fewcode.admin.common.config.mybatis.DataPermissionMapper;
import top.continew.starter.extension.datapermission.annotation.DataPermission;
import top.continew.starter.security.crypto.annotation.FieldEncrypt;
import java.util.List;

/**
 * 用户 Mapper
 *
 * @author Charles7c
 * @since 2022/12/22 21:47
 */
public interface AppUserMapper extends DataPermissionMapper<AppUserDO> {

    /**
     * 分页查询列表
     *
     * @param page         分页条件
     * @param queryWrapper 查询条件
     * @return 分页列表信息
     */
    @DataPermission(tableAlias = "t1")
    IPage<AppUserDetailResp> selectIndexUserPage(@Param("page") IPage<AppUserDO> page,
                                                 @Param(Constants.WRAPPER) QueryWrapper<AppUserDO> queryWrapper);

    /**
     * 查询列表
     *
     * @param queryWrapper 查询条件
     * @return 列表信息
     */
    @DataPermission(tableAlias = "t1")
    List<AppUserDetailResp> selectIndexUserList(@Param(Constants.WRAPPER) QueryWrapper<AppUserDO> queryWrapper);

    /**
     * 根据用户名查询
     *
     * @param username 用户名
     * @return 用户信息
     */
    @Select("SELECT * FROM sys_index_user WHERE username = #{username}")
    AppUserDO selectByUsername(@Param("username") String username);

    /**
     * 根据手机号查询
     *
     * @param phone 手机号
     * @return 用户信息
     */
    @Select("SELECT * FROM sys_index_user WHERE phone = #{phone}")
    AppUserDO selectByPhone(@FieldEncrypt @Param("phone") String phone);

    /**
     * 根据邮箱查询
     *
     * @param email 邮箱
     * @return 用户信息
     */
    @Select("SELECT * FROM sys_index_user WHERE email = #{email}")
    AppUserDO selectByEmail(@FieldEncrypt @Param("email") String email);

    /**
     * 根据 ID 查询昵称
     *
     * @param id ID
     * @return 昵称
     */
    @Select("SELECT nickname FROM sys_index_user WHERE id = #{id}")
    String selectNicknameById(@Param("id") Long id);

    /**
     * 根据邮箱查询数量
     *
     * @param email 邮箱
     * @param id    ID
     * @return 用户数量
     */
    Long selectCountByEmail(@FieldEncrypt @Param("email") String email, @Param("id") Long id);

    /**
     * 根据手机号查询数量
     *
     * @param phone 手机号
     * @param id    ID
     * @return 用户数量
     */
    Long selectCountByPhone(@FieldEncrypt @Param("phone") String phone, @Param("id") Long id);
}
