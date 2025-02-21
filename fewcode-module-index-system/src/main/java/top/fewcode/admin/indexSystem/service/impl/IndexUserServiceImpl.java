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
package top.fewcode.admin.indexSystem.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.validation.ValidationUtil;
import cn.hutool.http.ContentType;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.alicp.jetcache.anno.CacheInvalidate;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CacheUpdate;
import com.alicp.jetcache.anno.Cached;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ahoo.cosid.IdGenerator;
import me.ahoo.cosid.provider.DefaultIdGeneratorProvider;
import net.dreamlu.mica.core.result.R;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import top.continew.starter.cache.redisson.util.RedisUtils;
import top.continew.starter.core.constant.StringConstants;
import top.continew.starter.core.exception.BusinessException;
import top.continew.starter.core.validation.CheckUtils;
import top.continew.starter.extension.crud.model.query.PageQuery;
import top.continew.starter.extension.crud.model.query.SortQuery;
import top.continew.starter.extension.crud.model.resp.PageResp;
import top.continew.starter.extension.crud.service.BaseServiceImpl;
import top.continew.starter.web.util.FileUploadUtils;
import top.fewcode.admin.auth.service.OnlineUserService;
import top.fewcode.admin.common.constant.CacheConstants;
import top.fewcode.admin.common.context.IndexUserContext;
import top.fewcode.admin.common.context.IndexUserContextHolder;
import top.fewcode.admin.common.enums.DisEnableStatusEnum;
import top.fewcode.admin.common.enums.GenderEnum;
import top.fewcode.admin.common.util.SecureUtils;
import top.fewcode.admin.indexSystem.mapper.IndexUserMapper;
import top.fewcode.admin.indexSystem.model.entity.IndexUserDO;
import top.fewcode.admin.indexSystem.model.query.IndexUserQuery;
import top.fewcode.admin.indexSystem.model.req.user.*;
import top.fewcode.admin.indexSystem.model.resp.user.IndexUserDetailResp;
import top.fewcode.admin.indexSystem.model.resp.user.IndexUserImportParseResp;
import top.fewcode.admin.indexSystem.model.resp.user.IndexUserImportResp;
import top.fewcode.admin.indexSystem.model.resp.user.IndexUserResp;
import top.fewcode.admin.indexSystem.service.CommonIndexUserService;
import top.fewcode.admin.indexSystem.service.IndexUserService;
import top.fewcode.admin.system.enums.ImportPolicyEnum;
import top.fewcode.admin.system.enums.OptionCategoryEnum;
import top.fewcode.admin.system.enums.PasswordPolicyEnum;
import top.fewcode.admin.system.model.entity.UserDO;
import top.fewcode.admin.system.model.req.user.UserImportRowReq;
import top.fewcode.admin.system.service.OptionService;
import top.fewcode.admin.system.service.UserPasswordHistoryService;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 用户业务实现
 *
 * @author Charles7c
 * @since 2022/12/21 21:49
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class IndexUserServiceImpl extends BaseServiceImpl<IndexUserMapper, IndexUserDO, IndexUserResp, IndexUserDetailResp, IndexUserQuery, IndexUserReq> implements IndexUserService, CommonIndexUserService {

    private final PasswordEncoder passwordEncoder;
    private final UserPasswordHistoryService userPasswordHistoryService;
    private final OptionService optionService;
    private final OnlineUserService onlineUserService;

    @Value("${avatar.support-suffix}")
    private String[] avatarSupportSuffix;

    @Override
    public PageResp<IndexUserResp> page(IndexUserQuery query, PageQuery pageQuery) {
        QueryWrapper<IndexUserDO> queryWrapper = this.buildQueryWrapper(query);
        super.sort(queryWrapper, pageQuery);
        IPage<IndexUserDetailResp> page = baseMapper.selectIndexUserPage(new Page<>(pageQuery.getPage(), pageQuery
            .getSize()), queryWrapper);
        PageResp<IndexUserResp> pageResp = PageResp.build(page, super.getListClass());
        pageResp.getList().forEach(this::fill);
        return pageResp;
    }

    @Override
    public void beforeAdd(IndexUserReq req) {
        final String errorMsgTemplate = "新增失败，[{}] 已存在";
        String username = req.getUsername();
        CheckUtils.throwIf(this.isNameExists(username, null), errorMsgTemplate, username);
        String email = req.getEmail();
        CheckUtils.throwIf(StrUtil.isNotBlank(email) && this.isEmailExists(email, null), errorMsgTemplate, email);
        String phone = req.getPhone();
        CheckUtils.throwIf(StrUtil.isNotBlank(phone) && this.isPhoneExists(phone, null), errorMsgTemplate, phone);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheUpdate(key = "#id", value = "#req.nickname", name = CacheConstants.USER_KEY_PREFIX)
    public void update(IndexUserReq req, Long id) {
        final String errorMsgTemplate = "修改失败，[{}] 已存在";
        String username = req.getUsername();
        CheckUtils.throwIf(this.isNameExists(username, id), errorMsgTemplate, username);
        String email = req.getEmail();
        CheckUtils.throwIf(StrUtil.isNotBlank(email) && this.isEmailExists(email, id), errorMsgTemplate, email);
        String phone = req.getPhone();
        CheckUtils.throwIf(StrUtil.isNotBlank(phone) && this.isPhoneExists(phone, id), errorMsgTemplate, phone);
        DisEnableStatusEnum newStatus = req.getStatus();
        CheckUtils.throwIf(DisEnableStatusEnum.DISABLE.equals(newStatus) && ObjectUtil.equal(id, IndexUserContextHolder
            .getUserId()), "不允许禁用当前用户");

        // 更新信息
        IndexUserDO newUser = BeanUtil.toBean(req, IndexUserDO.class);
        newUser.setId(id);
        baseMapper.updateById(newUser);

        // 如果禁用用户，则踢出在线用户
        if (DisEnableStatusEnum.DISABLE.equals(newStatus)) {
            onlineUserService.kickOut(id);
            return;
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheInvalidate(key = "#ids", name = CacheConstants.USER_KEY_PREFIX, multi = true)
    public void delete(List<Long> ids) {
        CheckUtils.throwIf(CollUtil.contains(ids, IndexUserContextHolder.getUserId()), "不允许删除当前用户");

        // 删除历史密码
        userPasswordHistoryService.deleteByUserIds(ids);
        // 删除用户
        super.delete(ids);
        // 踢出在线用户
        ids.forEach(onlineUserService::kickOut);
    }

    @Override
    public void downloadImportTemplate(HttpServletResponse response) throws IOException {
        try {
            FileUploadUtils.download(response, ResourceUtil.getStream("templates/import/user.xlsx"), "用户导入模板.xlsx");
        } catch (Exception e) {
            log.error("下载用户导入模板失败：{}", e.getMessage(), e);
            response.setCharacterEncoding(CharsetUtil.UTF_8);
            response.setContentType(ContentType.JSON.toString());
            response.getWriter().write(JSONUtil.toJsonStr(R.fail("下载用户导入模板失败")));
        }
    }

    @Override
    public IndexUserImportParseResp parseImport(MultipartFile file) {
        IndexUserImportParseResp userImportResp = new IndexUserImportParseResp();
        List<IndexUserImportRowReq> importRowList;
        // 读取表格数据
        try {
            importRowList = EasyExcel.read(file.getInputStream())
                .head(UserImportRowReq.class)
                .sheet()
                .headRowNumber(1)
                .doReadSync();
        } catch (Exception e) {
            log.error("用户导入数据文件解析异常：{}", e.getMessage(), e);
            throw new BusinessException("数据文件解析异常");
        }
        // 总计行数
        userImportResp.setTotalRows(importRowList.size());
        CheckUtils.throwIfEmpty(importRowList, "数据文件格式错误");
        // 有效行数：过滤无效数据
        List<IndexUserImportRowReq> validRowList = this.filterImportData(importRowList);
        userImportResp.setValidRows(validRowList.size());
        CheckUtils.throwIfEmpty(validRowList, "数据文件格式错误");

        // 检测表格内数据是否合法
        Set<String> seenEmails = new HashSet<>();
        boolean hasDuplicateEmail = validRowList.stream()
            .map(IndexUserImportRowReq::getEmail)
            .anyMatch(email -> email != null && !seenEmails.add(email));
        CheckUtils.throwIf(hasDuplicateEmail, "存在重复邮箱，请检测数据");
        Set<String> seenPhones = new HashSet<>();
        boolean hasDuplicatePhone = validRowList.stream()
            .map(IndexUserImportRowReq::getPhone)
            .anyMatch(phone -> phone != null && !seenPhones.add(phone));
        CheckUtils.throwIf(hasDuplicatePhone, "存在重复手机，请检测数据");

        // 查询重复用户
        userImportResp
            .setDuplicateUserRows(countExistByField(validRowList, IndexUserImportRowReq::getUsername, IndexUserDO::getUsername, false));
        // 查询重复邮箱
        userImportResp
            .setDuplicateEmailRows(countExistByField(validRowList, IndexUserImportRowReq::getEmail, IndexUserDO::getEmail, true));
        // 查询重复手机
        userImportResp
            .setDuplicatePhoneRows(countExistByField(validRowList, IndexUserImportRowReq::getPhone, IndexUserDO::getPhone, true));

        // 设置导入会话并缓存数据，有效期10分钟
        String importKey = UUID.fastUUID().toString(true);
        RedisUtils.set(CacheConstants.DATA_IMPORT_KEY + importKey, JSONUtil.toJsonStr(validRowList), Duration
            .ofMinutes(10));
        userImportResp.setImportKey(importKey);
        return userImportResp;
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public IndexUserImportResp importUser(IndexUserImportReq req) {
        // 校验导入会话是否过期
        List<IndexUserImportRowReq> importUserList;
        try {
            String data = RedisUtils.get(CacheConstants.DATA_IMPORT_KEY + req.getImportKey());
            importUserList = JSONUtil.toList(data, IndexUserImportRowReq.class);
            CheckUtils.throwIf(CollUtil.isEmpty(importUserList), "导入已过期，请重新上传");
        } catch (Exception e) {
            log.error("导入异常:", e);
            throw new BusinessException("导入已过期，请重新上传");
        }
        // 已存在数据查询
        List<String> existEmails = listExistByField(importUserList, IndexUserImportRowReq::getEmail, IndexUserDO::getEmail);
        List<String> existPhones = listExistByField(importUserList, IndexUserImportRowReq::getPhone, IndexUserDO::getPhone);
        List<IndexUserDO> existUserList = listByUsernames(importUserList.stream()
            .map(IndexUserImportRowReq::getUsername)
            .filter(Objects::nonNull)
            .toList());
        List<String> existUsernames = existUserList.stream().map(IndexUserDO::getUsername).toList();
        CheckUtils
            .throwIf(isExitImportUser(req, importUserList, existUsernames, existEmails, existPhones), "数据不符合导入策略，已退出导入");

        // 基础数据准备
        Map<String, Long> userMap = existUserList.stream()
            .collect(Collectors.toMap(IndexUserDO::getUsername, IndexUserDO::getId));

        // 批量操作数据库集合
        List<IndexUserDO> insertList = new ArrayList<>();
        List<IndexUserDO> updateList = new ArrayList<>();

        // ID生成器
        IdGenerator idGenerator = DefaultIdGeneratorProvider.INSTANCE.getShare();
        for (IndexUserImportRowReq row : importUserList) {
            if (isSkipUserImport(req, row, existUsernames, existPhones, existEmails)) {
                // 按规则跳过该行
                continue;
            }
            IndexUserDO userDO = BeanUtil.toBeanIgnoreError(row, IndexUserDO.class);
            userDO.setStatus(req.getDefaultStatus());
            userDO.setPwdResetTime(LocalDateTime.now());
            userDO.setGender(EnumUtil.getBy(GenderEnum::getDescription, row.getGender(), GenderEnum.UNKNOWN));
            // 修改 or 新增
            if (ImportPolicyEnum.UPDATE.validate(req.getDuplicateUser(), row.getUsername(), existUsernames)) {
                userDO.setId(userMap.get(row.getUsername()));
                updateList.add(userDO);
            } else {
                userDO.setId(idGenerator.generate());
                insertList.add(userDO);
            }
        }
        doImportUser(insertList, updateList);
        RedisUtils.delete(CacheConstants.DATA_IMPORT_KEY + req.getImportKey());
        return new IndexUserImportResp(insertList.size() + updateList.size(), insertList.size(), updateList.size());
    }

    @Override
    public void resetPassword(IndexUserPasswordResetReq req, Long id) {
        super.getById(id);
        baseMapper.lambdaUpdate()
            .set(IndexUserDO::getPassword, req.getNewPassword())
            .set(IndexUserDO::getPwdResetTime, LocalDateTime.now())
            .eq(IndexUserDO::getId, id)
            .update();
    }


    @Override
    public String updateAvatar(MultipartFile avatarFile, Long id) throws IOException {
        String avatarImageType = FileNameUtil.extName(avatarFile.getOriginalFilename());
        CheckUtils.throwIf(!StrUtil.equalsAnyIgnoreCase(avatarImageType, avatarSupportSuffix), "头像仅支持 {} 格式的图片", String
            .join(StringConstants.CHINESE_COMMA, avatarSupportSuffix));
        // 更新用户头像
        String base64 = ImgUtil.toBase64DataUri(ImgUtil.scale(ImgUtil.toImage(avatarFile
            .getBytes()), 100, 100, null), avatarImageType);
        baseMapper.lambdaUpdate().set(IndexUserDO::getAvatar, base64).eq(IndexUserDO::getId, id).update();
        return base64;
    }

    @Override
    @CacheUpdate(key = "#id", value = "#req.nickname", name = CacheConstants.USER_KEY_PREFIX)
    public void updateBasicInfo(IndexUserBasicInfoUpdateReq req, Long id) {
        super.getById(id);
        baseMapper.lambdaUpdate()
            .set(IndexUserDO::getNickname, req.getNickname())
            .set(IndexUserDO::getGender, req.getGender())
            .eq(IndexUserDO::getId, id)
            .update();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(String oldPassword, String newPassword, Long id) {
        CheckUtils.throwIfEqual(newPassword, oldPassword, "新密码不能与当前密码相同");
        IndexUserDO user = super.getById(id);
        String password = user.getPassword();
        if (StrUtil.isNotBlank(password)) {
            CheckUtils.throwIf(!passwordEncoder.matches(oldPassword, password), "当前密码错误");
        }
        // 校验密码合法性
        UserDO newUser = new UserDO();
        BeanUtils.copyProperties(newUser,user);
        int passwordRepetitionTimes = this.checkPassword(newPassword,newUser);
        // 更新密码和密码重置时间
        baseMapper.lambdaUpdate()
            .set(IndexUserDO::getPassword, newPassword)
            .set(IndexUserDO::getPwdResetTime, LocalDateTime.now())
            .eq(IndexUserDO::getId, id)
            .update();
        // 保存历史密码
        userPasswordHistoryService.add(id, password, passwordRepetitionTimes);
        // 修改后登出
        StpUtil.logout();
    }

    @Override
    public void updatePhone(String newPhone, String oldPassword, Long id) {
        IndexUserDO user = super.getById(id);
        CheckUtils.throwIf(!passwordEncoder.matches(oldPassword, user.getPassword()), "当前密码错误");
        CheckUtils.throwIf(this.isPhoneExists(newPhone, id), "手机号已绑定其他账号，请更换其他手机号");
        CheckUtils.throwIfEqual(newPhone, user.getPhone(), "新手机号不能与当前手机号相同");
        // 更新手机号
        baseMapper.lambdaUpdate().set(IndexUserDO::getPhone, newPhone).eq(IndexUserDO::getId, id).update();
    }

    @Override
    public void updateEmail(String newEmail, String oldPassword, Long id) {
        IndexUserDO user = super.getById(id);
        CheckUtils.throwIf(!passwordEncoder.matches(oldPassword, user.getPassword()), "当前密码错误");
        CheckUtils.throwIf(this.isEmailExists(newEmail, id), "邮箱已绑定其他账号，请更换其他邮箱");
        CheckUtils.throwIfEqual(newEmail, user.getEmail(), "新邮箱不能与当前邮箱相同");
        // 更新邮箱
        baseMapper.lambdaUpdate().set(IndexUserDO::getEmail, newEmail).eq(IndexUserDO::getId, id).update();
    }

    @Override
    public Long add(IndexUserDO user) {
        user.setStatus(DisEnableStatusEnum.ENABLE);
        baseMapper.insert(user);
        return user.getId();
    }

    @Override
    public IndexUserDO getByUsername(String username) {
        return baseMapper.selectByUsername(username);
    }

    @Override
    public IndexUserDO getByPhone(String phone) {
        return baseMapper.selectByPhone(phone);
    }

    @Override
    public IndexUserDO getByEmail(String email) {
        return baseMapper.selectByEmail(email);
    }


    @Override
    @Cached(key = "#id", name = CacheConstants.USER_KEY_PREFIX, cacheType = CacheType.BOTH, syncLocal = true)
    public String getNicknameById(Long id) {
        return baseMapper.selectNicknameById(id);
    }

    @Override
    protected <E> List<E> list(IndexUserQuery query, SortQuery sortQuery, Class<E> targetClass) {
        QueryWrapper<IndexUserDO> queryWrapper = this.buildQueryWrapper(query);
        // 设置排序
        super.sort(queryWrapper, sortQuery);
        List<IndexUserDetailResp> entityList = baseMapper.selectIndexUserList(queryWrapper);
        if (this.getEntityClass() == targetClass) {
            return (List<E>)entityList;
        }
        return BeanUtil.copyToList(entityList, targetClass);
    }

    @Override
    public QueryWrapper<IndexUserDO> buildQueryWrapper(IndexUserQuery query) {
        String description = query.getDescription();
        DisEnableStatusEnum status = query.getStatus();
        List<Date> createTimeList = query.getCreateTime();
        List<Long> userIdList = query.getUserIds();
        return new QueryWrapper<IndexUserDO>().and(StrUtil.isNotBlank(description), q -> q.like("t1.username", description)
            .or()
            .like("t1.nickname", description)
            .or()
            .like("t1.description", description))
            .eq(null != status, "t1.status", status)
            .between(CollUtil.isNotEmpty(createTimeList), "t1.create_time", CollUtil.getFirst(createTimeList), CollUtil
                .getLast(createTimeList))
            .in(CollUtil.isNotEmpty(userIdList), "t1.id", userIdList);
    }

    /**
     * 导入用户
     *
     * @param insertList     新增用户
     * @param updateList     修改用户
     */
    private void doImportUser(List<IndexUserDO> insertList, List<IndexUserDO> updateList) {
        if (CollUtil.isNotEmpty(insertList)) {
            baseMapper.insert(insertList);
        }
        if (CollUtil.isNotEmpty(updateList)) {
            this.updateBatchById(updateList);
        }

    }

    /**
     * 判断是否跳过导入
     *
     * @param req            导入参数
     * @param row            导入数据
     * @param existUsernames 导入数据中已存在的用户名
     * @param existEmails    导入数据中已存在的邮箱
     * @param existPhones    导入数据中已存在的手机号
     * @return 是否跳过
     */
    private boolean isSkipUserImport(IndexUserImportReq req,
                                     IndexUserImportRowReq row,
                                     List<String> existUsernames,
                                     List<String> existPhones,
                                     List<String> existEmails) {
        return ImportPolicyEnum.SKIP.validate(req.getDuplicateUser(), row.getUsername(), existUsernames) || ImportPolicyEnum.SKIP.validate(req
            .getDuplicateEmail(), row.getEmail(), existEmails) || ImportPolicyEnum.SKIP.validate(req.getDuplicatePhone(), row
                .getPhone(), existPhones);
    }

    /**
     * 判断是否退出导入
     *
     * @param req            导入参数
     * @param list           导入数据
     * @param existUsernames 导入数据中已存在的用户名
     * @param existEmails    导入数据中已存在的邮箱
     * @param existPhones    导入数据中已存在的手机号
     * @return 是否退出
     */
    private boolean isExitImportUser(IndexUserImportReq req,
                                     List<IndexUserImportRowReq> list,
                                     List<String> existUsernames,
                                     List<String> existEmails,
                                     List<String> existPhones) {
        return list.stream()
            .anyMatch(row -> ImportPolicyEnum.EXIT.validate(req.getDuplicateUser(), row.getUsername(), existUsernames) || ImportPolicyEnum.EXIT
                .validate(req.getDuplicateEmail(), row.getEmail(), existEmails) || ImportPolicyEnum.EXIT.validate(req
                    .getDuplicatePhone(), row.getPhone(), existPhones));
    }

    /**
     * 按指定数据集获取数据库已存在的数量
     *
     * @param userRowList 导入的数据源
     * @param rowField    导入数据的字段
     * @param dbField     对比数据库的字段
     * @return 存在的数量
     */
    private int countExistByField(List<IndexUserImportRowReq> userRowList,
                                  Function<IndexUserImportRowReq, String> rowField,
                                  SFunction<IndexUserDO, ?> dbField,
                                  boolean fieldEncrypt) {
        List<String> fieldValues = userRowList.stream().map(rowField).filter(Objects::nonNull).toList();
        if (fieldValues.isEmpty()) {
            return 0;
        }
        return (int)this.count(Wrappers.<IndexUserDO>lambdaQuery()
            .in(dbField, fieldEncrypt ? SecureUtils.encryptFieldByAes(fieldValues) : fieldValues));
    }

    /**
     * 按指定数据集获取数据库已存在内容
     *
     * @param userRowList 导入的数据源
     * @param rowField    导入数据的字段
     * @param dbField     对比数据库的字段
     * @return 存在的内容
     */
    private List<String> listExistByField(List<IndexUserImportRowReq> userRowList,
                                          Function<IndexUserImportRowReq, String> rowField,
                                          SFunction<IndexUserDO, String> dbField) {
        List<String> fieldValues = userRowList.stream().map(rowField).filter(Objects::nonNull).toList();
        if (fieldValues.isEmpty()) {
            return Collections.emptyList();
        }
        List<IndexUserDO> userDOList = baseMapper.selectList(Wrappers.<IndexUserDO>lambdaQuery()
            .in(dbField, SecureUtils.encryptFieldByAes(fieldValues))
            .select(dbField));
        return userDOList.stream().map(dbField).filter(Objects::nonNull).toList();
    }

    /**
     * 过滤无效的导入用户数据（批量导入不严格校验数据）
     *
     * @param importRowList 导入数据
     */
    private List<IndexUserImportRowReq> filterImportData(List<IndexUserImportRowReq> importRowList) {
        // 校验过滤
        List<IndexUserImportRowReq> list = importRowList.stream()
            .filter(row -> ValidationUtil.validate(row).isEmpty())
            .toList();
        // 用户名去重
        return list.stream()
            .collect(Collectors.toMap(IndexUserImportRowReq::getUsername, user -> user, (existing, replacement) -> existing))
            .values()
            .stream()
            .toList();
    }

    /**
     * 检测密码合法性
     *
     * @param password 密码
     * @param user     用户信息
     * @return 密码允许重复使用次数
     */
    private int checkPassword(String password, UserDO user) {
        Map<String, String> passwordPolicy = optionService.getByCategory(OptionCategoryEnum.PASSWORD);
        // 密码最小长度
        PasswordPolicyEnum.PASSWORD_MIN_LENGTH.validate(password, MapUtil.getInt(passwordPolicy, PasswordPolicyEnum.PASSWORD_MIN_LENGTH.name()), user);
        // 密码是否必须包含特殊字符
        PasswordPolicyEnum.PASSWORD_REQUIRE_SYMBOLS.validate(password, MapUtil.getInt(passwordPolicy, PasswordPolicyEnum.PASSWORD_REQUIRE_SYMBOLS
            .name()), user);
        // 密码是否允许包含正反序账号名
        PasswordPolicyEnum.PASSWORD_ALLOW_CONTAIN_USERNAME.validate(password, MapUtil
            .getInt(passwordPolicy, PasswordPolicyEnum.PASSWORD_ALLOW_CONTAIN_USERNAME.name()), user);
        // 密码重复使用次数
        int passwordRepetitionTimes = MapUtil.getInt(passwordPolicy, PasswordPolicyEnum.PASSWORD_REPETITION_TIMES.name());
        PasswordPolicyEnum.PASSWORD_REPETITION_TIMES.validate(password, passwordRepetitionTimes, user);
        return passwordRepetitionTimes;
    }

    /**
     * 名称是否存在
     *
     * @param name 名称
     * @param id   ID
     * @return 是否存在
     */
    private boolean isNameExists(String name, Long id) {
        return baseMapper.lambdaQuery().eq(IndexUserDO::getUsername, name).ne(null != id, IndexUserDO::getId, id).exists();
    }

    /**
     * 邮箱是否存在
     *
     * @param email 邮箱
     * @param id    ID
     * @return 是否存在
     */
    private boolean isEmailExists(String email, Long id) {
        Long count = baseMapper.selectCountByEmail(email, id);
        return null != count && count > 0;
    }

    /**
     * 手机号码是否存在
     *
     * @param phone 手机号码
     * @param id    ID
     * @return 是否存在
     */
    private boolean isPhoneExists(String phone, Long id) {
        Long count = baseMapper.selectCountByPhone(phone, id);
        return null != count && count > 0;
    }

    /**
     * 根据用户名获取用户列表
     *
     * @param usernames 用户名列表
     * @return 用户列表
     */
    private List<IndexUserDO> listByUsernames(List<String> usernames) {
        return this.list(Wrappers.<IndexUserDO>lambdaQuery()
            .in(IndexUserDO::getUsername, usernames)
            .select(IndexUserDO::getId, IndexUserDO::getUsername));
    }

    /**
     * 更新用户上下文信息
     *
     * @param id ID
     */
    private void updateContext(Long id) {
        IndexUserContext userContext = IndexUserContextHolder.getContext(id);
        if (null != userContext) {
//            userContext.setRoles(roleService.listByUserId(id));
//            userContext.setPermissions(roleService.listPermissionByUserId(id));
            IndexUserContextHolder.setContext(userContext);
        }
    }
}
