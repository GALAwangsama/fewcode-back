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
package top.fewcode.admin.app.service.impl;

import cn.dev33.satoken.stp.SaLoginConfig;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import top.continew.starter.cache.redisson.util.RedisUtils;
import top.continew.starter.core.autoconfigure.project.ProjectProperties;
import top.continew.starter.core.validation.CheckUtils;
import top.continew.starter.extension.crud.annotation.TreeField;
import top.continew.starter.extension.crud.autoconfigure.CrudProperties;
import top.continew.starter.messaging.websocket.util.WebSocketUtils;
import top.continew.starter.web.util.SpringWebUtils;
import top.fewcode.admin.app.service.AppLoginService;
import top.fewcode.admin.appSystem.model.entity.AppUserDO;
import top.fewcode.admin.appSystem.service.AppUserService;
import top.fewcode.admin.auth.model.resp.RouteResp;
import top.fewcode.admin.common.constant.CacheConstants;
import top.fewcode.admin.common.constant.SysConstants;
import top.fewcode.admin.common.context.UserContext;
import top.fewcode.admin.common.context.UserContextHolder;
import top.fewcode.admin.common.context.UserExtraContext;
import top.fewcode.admin.common.enums.DisEnableStatusEnum;
import top.fewcode.admin.system.enums.MenuTypeEnum;
import top.fewcode.admin.system.enums.MessageTemplateEnum;
import top.fewcode.admin.system.enums.MessageTypeEnum;
import top.fewcode.admin.system.enums.PasswordPolicyEnum;
import top.fewcode.admin.system.model.req.MessageReq;
import top.fewcode.admin.system.model.resp.MenuResp;
import top.fewcode.admin.system.service.MenuService;
import top.fewcode.admin.system.service.MessageService;
import top.fewcode.admin.system.service.OptionService;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * 登录业务实现
 *
 * @author GALAwang
 * @since 2022/12/21 21:49
 */
@Service
@RequiredArgsConstructor
public class AppLoginServiceImpl implements AppLoginService {

    private final ProjectProperties projectProperties;
    private final PasswordEncoder passwordEncoder;
    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;
    private final AppUserService appUserService;
    private final OptionService optionService;
    private final MessageService messageService;
    private final MenuService menuService;
    private final CrudProperties crudProperties;

    @Override
    public String accountLogin(String username, String password, HttpServletRequest request) {
        AppUserDO user = appUserService.getByUsername(username);
        boolean isError = ObjectUtil.isNull(user) || !passwordEncoder.matches(password, user.getPassword());
        this.checkUserLocked(username, request, isError);
        CheckUtils.throwIf(isError, "用户名或密码错误");
        this.checkUserStatus(user);
        return this.login(user);
    }

    @Override
    public String phoneLogin(String phone) {
        AppUserDO user = appUserService.getByPhone(phone);
        CheckUtils.throwIfNull(user, "此手机号未绑定本系统账号");
        this.checkUserStatus(user);
        return this.login(user);
    }

    @Override
    public String emailLogin(String email) {
        AppUserDO user = appUserService.getByEmail(email);
        CheckUtils.throwIfNull(user, "此邮箱未绑定本系统账号");
        this.checkUserStatus(user);
        return this.login(user);
    }

    /**
     * 登录并缓存用户信息
     *
     * @param user 用户信息
     * @return 令牌
     */
    private String login(AppUserDO user) {
        CompletableFuture<Integer> passwordExpirationDaysFuture = CompletableFuture.supplyAsync(() -> optionService
                .getValueByCode2Int(PasswordPolicyEnum.PASSWORD_EXPIRATION_DAYS.name()));
        CompletableFuture.allOf(passwordExpirationDaysFuture);
        UserContext userContext = new UserContext(passwordExpirationDaysFuture.join());
        BeanUtil.copyProperties(user, userContext);
        // 登录并缓存用户信息
        StpUtil.login(userContext.getId(), SaLoginConfig.setExtraData(BeanUtil
                .beanToMap(new UserExtraContext(SpringWebUtils.getRequest()))));
        UserContextHolder.setContext(userContext);
        return StpUtil.getTokenValue();
    }

    /**
     * 检查用户状态
     *
     * @param user 用户信息
     */
    private void checkUserStatus(AppUserDO user) {
        CheckUtils.throwIfEqual(DisEnableStatusEnum.DISABLE, user.getStatus(), "此账号已被禁用，如有疑问，请联系管理员");
    }

    /**
     * 检测用户是否已被锁定
     *
     * @param username 用户名
     * @param request  请求对象
     * @param isError  是否登录错误
     */
    private void checkUserLocked(String username, HttpServletRequest request, boolean isError) {
        // 不锁定
        int maxErrorCount = optionService.getValueByCode2Int(PasswordPolicyEnum.PASSWORD_ERROR_LOCK_COUNT.name());
        if (maxErrorCount <= SysConstants.NO) {
            return;
        }
        // 检测是否已被锁定
        String key = CacheConstants.USER_PASSWORD_ERROR_KEY_PREFIX + RedisUtils.formatKey(username, JakartaServletUtil
                .getClientIP(request));
        int lockMinutes = optionService.getValueByCode2Int(PasswordPolicyEnum.PASSWORD_ERROR_LOCK_MINUTES.name());
        Integer currentErrorCount = ObjectUtil.defaultIfNull(RedisUtils.get(key), 0);
        CheckUtils.throwIf(currentErrorCount >= maxErrorCount, "账号锁定 {} 分钟，请稍后再试", lockMinutes);
        // 登录成功清除计数
        if (!isError) {
            RedisUtils.delete(key);
            return;
        }
        // 登录失败递增计数
        currentErrorCount++;
        RedisUtils.set(key, currentErrorCount, Duration.ofMinutes(lockMinutes));
        CheckUtils.throwIf(currentErrorCount >= maxErrorCount, "密码错误已达 {} 次，账号锁定 {} 分钟", maxErrorCount, lockMinutes);
    }

    /**
     * 发送安全消息
     *
     * @param user 用户信息
     */
    private void sendSecurityMsg(AppUserDO user) {
        MessageReq req = new MessageReq();
        MessageTemplateEnum socialRegister = MessageTemplateEnum.SOCIAL_REGISTER;
        req.setTitle(socialRegister.getTitle().formatted(projectProperties.getName()));
        req.setContent(socialRegister.getContent().formatted(user.getNickname()));
        req.setType(MessageTypeEnum.SECURITY);
        messageService.add(req, CollUtil.toList(user.getId()));
        List<String> tokenList = StpUtil.getTokenValueListByLoginId(user.getId());
        for (String token : tokenList) {
            WebSocketUtils.sendMessage(token, "1");
        }
    }

    //TODO 路由树构建（进行诱骗前端，正式不需要权限控制则可以酌情删除）
    @Override
    public List<RouteResp> buildRouteTree(Long userId) {
        // 查询所有菜单数据
        List<MenuResp> menuList = menuService.listAll();

        // 过滤掉按钮类型的菜单，只保留页面或目录
        menuList = menuList.stream()
                .filter(m -> !MenuTypeEnum.BUTTON.equals(m.getType()))
                .collect(Collectors.toList());

        // 构建路由树
        TreeField treeField = MenuResp.class.getDeclaredAnnotation(TreeField.class);
        TreeNodeConfig treeNodeConfig = crudProperties.getTree().genTreeNodeConfig(treeField);
        List<Tree<Long>> treeList = TreeUtil.build(menuList, treeField.rootId(), treeNodeConfig, (m, tree) -> {
            tree.setId(m.getId());
            tree.setParentId(m.getParentId());
            tree.setName(m.getTitle());
            tree.setWeight(m.getSort());
            tree.putExtra("type", m.getType().getValue());
            tree.putExtra("path", m.getPath());
            tree.putExtra("name", m.getName());
            tree.putExtra("component", m.getComponent());
            tree.putExtra("redirect", m.getRedirect());
            tree.putExtra("icon", m.getIcon());
            tree.putExtra("isExternal", m.getIsExternal());
            tree.putExtra("isCache", m.getIsCache());
            tree.putExtra("isHidden", m.getIsHidden());
            tree.putExtra("permission", m.getPermission());
        });

        return BeanUtil.copyToList(treeList, RouteResp.class);
    }
}
