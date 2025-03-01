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
package top.fewcode.admin.system.mapper;

import org.apache.ibatis.annotations.Param;
import top.fewcode.admin.system.model.entity.MessageUserDO;
import top.continew.starter.data.mp.base.BaseMapper;

/**
 * 消息和用户 Mapper
 *
 * @author Bull-BCLS
 * @since 2023/10/15 20:25
 */
public interface MessageUserMapper extends BaseMapper<MessageUserDO> {

    /**
     * 根据用户 ID 和消息类型查询未读消息数量
     *
     * @param userId 用户 ID
     * @param type   消息类型
     * @return 未读消息信息
     */
    Long selectUnreadCountByUserIdAndType(@Param("userId") Long userId, @Param("type") Integer type);
}