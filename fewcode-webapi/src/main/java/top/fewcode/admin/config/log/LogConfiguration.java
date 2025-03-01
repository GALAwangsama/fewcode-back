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
package top.fewcode.admin.config.log;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.continew.starter.log.core.dao.LogDao;
import top.continew.starter.log.interceptor.autoconfigure.ConditionalOnEnabledLog;
import top.continew.starter.web.autoconfigure.trace.TraceProperties;
import top.fewcode.admin.system.mapper.LogMapper;
import top.fewcode.admin.system.service.UserService;

/**
 * 日志配置
 *
 * @author GALAwang
 * @since 2022/12/24 23:15
 */
@Configuration
@ConditionalOnEnabledLog
public class LogConfiguration {

    /**
     * 日志持久层接口本地实现类
     */
    @Bean
    public LogDao logDao(UserService userService, LogMapper logMapper, TraceProperties traceProperties) {
        return new LogDaoLocalImpl(userService, logMapper, traceProperties);
    }
}
