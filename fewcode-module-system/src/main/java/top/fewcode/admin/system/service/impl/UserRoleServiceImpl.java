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
package top.fewcode.admin.system.service.impl;

import cn.crane4j.annotation.ContainerMethod;
import cn.crane4j.annotation.MappingType;
import cn.hutool.core.collection.CollUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.fewcode.admin.common.constant.ContainerConstants;
import top.fewcode.admin.common.constant.SysConstants;
import top.fewcode.admin.system.mapper.UserRoleMapper;
import top.fewcode.admin.system.model.entity.UserRoleDO;
import top.fewcode.admin.system.service.UserRoleService;
import top.continew.starter.core.validation.CheckUtils;

import java.util.List;

/**
 * 用户和角色业务实现
 *
 * @author Charles7c
 * @since 2023/2/20 21:30
 */
@Service
@RequiredArgsConstructor
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleMapper baseMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignRolesToUser(List<Long> roleIds, Long userId) {
        // 检查是否有变更
        List<Long> oldRoleIdList = baseMapper.lambdaQuery()
            .select(UserRoleDO::getRoleId)
            .eq(UserRoleDO::getUserId, userId)
            .list()
            .stream()
            .map(UserRoleDO::getRoleId)
            .toList();
        if (CollUtil.isEmpty(CollUtil.disjunction(roleIds, oldRoleIdList))) {
            return false;
        }
        CheckUtils.throwIf(SysConstants.SUPER_USER_ID.equals(userId) && !roleIds
            .contains(SysConstants.SUPER_ROLE_ID), "不允许变更超管用户角色");
        // 删除原有关联
        baseMapper.lambdaUpdate().eq(UserRoleDO::getUserId, userId).remove();
        // 保存最新关联
        List<UserRoleDO> userRoleList = roleIds.stream().map(roleId -> new UserRoleDO(userId, roleId)).toList();
        return baseMapper.insertBatch(userRoleList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignRoleToUsers(Long roleId, List<Long> userIds) {
        // 检查是否有变更
        List<Long> oldUserIdList = baseMapper.lambdaQuery()
            .select(UserRoleDO::getUserId)
            .eq(UserRoleDO::getRoleId, roleId)
            .list()
            .stream()
            .map(UserRoleDO::getUserId)
            .toList();
        if (CollUtil.isEmpty(CollUtil.disjunction(userIds, oldUserIdList))) {
            return false;
        }
        CheckUtils.throwIf(SysConstants.SUPER_ROLE_ID.equals(roleId) && !userIds
            .contains(SysConstants.SUPER_USER_ID), "不允许变更超管用户角色");
        // 删除原有关联
        baseMapper.lambdaUpdate().eq(UserRoleDO::getRoleId, roleId).remove();
        // 保存最新关联
        List<UserRoleDO> userRoleList = userIds.stream().map(userId -> new UserRoleDO(userId, roleId)).toList();
        return baseMapper.insertBatch(userRoleList);
    }

    @Override
    public void deleteByUserIds(List<Long> userIds) {
        baseMapper.lambdaUpdate().in(UserRoleDO::getUserId, userIds).remove();
    }

    @Override
    public void saveBatch(List<UserRoleDO> list) {
        baseMapper.insert(list);
    }

    @Override
    @ContainerMethod(namespace = ContainerConstants.USER_ROLE_ID_LIST, type = MappingType.ORDER_OF_KEYS)
    public List<Long> listRoleIdByUserId(Long userId) {
        return baseMapper.lambdaQuery()
            .select(UserRoleDO::getRoleId)
            .eq(UserRoleDO::getUserId, userId)
            .list()
            .stream()
            .map(UserRoleDO::getRoleId)
            .toList();
    }

    @Override
    public List<Long> listUserIdByRoleId(Long roleId) {
        return baseMapper.lambdaQuery()
            .select(UserRoleDO::getUserId)
            .eq(UserRoleDO::getRoleId, roleId)
            .list()
            .stream()
            .map(UserRoleDO::getUserId)
            .toList();
    }

    @Override
    public boolean isRoleIdExists(List<Long> roleIds) {
        return baseMapper.lambdaQuery().in(UserRoleDO::getRoleId, roleIds).exists();
    }
}
