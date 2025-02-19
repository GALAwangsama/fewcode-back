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

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.fewcode.admin.common.context.UserContextHolder;
import top.fewcode.admin.system.mapper.NoticeMapper;
import top.fewcode.admin.system.model.entity.NoticeDO;
import top.fewcode.admin.system.model.query.NoticeQuery;
import top.fewcode.admin.system.model.req.NoticeReq;
import top.fewcode.admin.system.model.resp.dashboard.DashboardNoticeResp;
import top.fewcode.admin.system.model.resp.NoticeDetailResp;
import top.fewcode.admin.system.model.resp.NoticeResp;
import top.fewcode.admin.system.service.NoticeService;
import top.continew.starter.extension.crud.service.BaseServiceImpl;

import java.util.List;

/**
 * 公告业务实现
 *
 * @author Charles7c
 * @since 2023/8/20 10:55
 */
@Service
@RequiredArgsConstructor
public class NoticeServiceImpl extends BaseServiceImpl<NoticeMapper, NoticeDO, NoticeResp, NoticeDetailResp, NoticeQuery, NoticeReq> implements NoticeService {

    @Override
    public List<DashboardNoticeResp> listDashboard() {
        Long userId = UserContextHolder.isAdmin() ? null : UserContextHolder.getUserId();
        return baseMapper.selectDashboardList(userId);
    }
}