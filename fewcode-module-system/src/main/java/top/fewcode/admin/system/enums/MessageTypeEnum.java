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
package top.fewcode.admin.system.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import top.continew.starter.core.enums.BaseEnum;
import top.fewcode.admin.common.constant.UiConstants;

/**
 * 消息类型枚举
 *
 * @author GALAwang
 * @since 2023/11/2 20:08
 */
@Getter
@RequiredArgsConstructor
public enum MessageTypeEnum implements BaseEnum<Integer> {

    /**
     * 安全消息
     */
    SECURITY(1, "安全消息", UiConstants.COLOR_PRIMARY),;

    private final Integer value;
    private final String description;
    private final String color;
}
