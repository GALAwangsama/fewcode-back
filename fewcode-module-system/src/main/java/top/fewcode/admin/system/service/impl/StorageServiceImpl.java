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

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.dromara.x.file.storage.core.FileStorageProperties;
import org.dromara.x.file.storage.core.FileStorageService;
import org.dromara.x.file.storage.core.FileStorageServiceBuilder;
import org.dromara.x.file.storage.core.platform.FileStorage;
import org.springframework.stereotype.Service;
import top.fewcode.admin.common.enums.DisEnableStatusEnum;
import top.fewcode.admin.common.util.SecureUtils;
import top.fewcode.admin.system.enums.StorageTypeEnum;
import top.fewcode.admin.system.mapper.StorageMapper;
import top.fewcode.admin.system.model.entity.StorageDO;
import top.fewcode.admin.system.model.query.StorageQuery;
import top.fewcode.admin.system.model.req.StorageReq;
import top.fewcode.admin.system.model.resp.StorageResp;
import top.fewcode.admin.system.service.FileService;
import top.fewcode.admin.system.service.StorageService;
import top.fewcode.admin.system.validation.ValidationGroup;
import top.continew.starter.core.constant.StringConstants;
import top.continew.starter.core.util.ExceptionUtils;
import top.continew.starter.core.util.URLUtils;
import top.continew.starter.core.validation.CheckUtils;
import top.continew.starter.core.validation.ValidationUtils;
import top.continew.starter.extension.crud.service.BaseServiceImpl;
import top.continew.starter.web.util.SpringWebUtils;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 存储业务实现
 *
 * @author Charles7c
 * @since 2023/12/26 22:09
 */
@Service
@RequiredArgsConstructor
public class StorageServiceImpl extends BaseServiceImpl<StorageMapper, StorageDO, StorageResp, StorageResp, StorageQuery, StorageReq> implements StorageService {

    private final FileStorageService fileStorageService;
    @Resource
    private FileService fileService;

    @Override
    public void beforeAdd(StorageReq req) {
        this.decodeSecretKey(req, null);
        CheckUtils.throwIf(Boolean.TRUE.equals(req.getIsDefault()) && this.isDefaultExists(null), "请先取消原有默认存储");
        String code = req.getCode();
        CheckUtils.throwIf(this.isCodeExists(code, null), "新增失败，[{}] 已存在", code);
        this.load(req);
    }

    @Override
    public void beforeUpdate(StorageReq req, Long id) {
        StorageDO oldStorage = super.getById(id);
        CheckUtils.throwIfNotEqual(req.getCode(), oldStorage.getCode(), "不允许修改存储编码");
        CheckUtils.throwIfNotEqual(req.getType(), oldStorage.getType(), "不允许修改存储类型");
        DisEnableStatusEnum newStatus = req.getStatus();
        CheckUtils.throwIf(Boolean.TRUE.equals(oldStorage.getIsDefault()) && DisEnableStatusEnum.DISABLE
            .equals(newStatus), "[{}] 是默认存储，不允许禁用", oldStorage.getName());
        this.decodeSecretKey(req, oldStorage);
        DisEnableStatusEnum oldStatus = oldStorage.getStatus();
        if (Boolean.TRUE.equals(req.getIsDefault())) {
            CheckUtils.throwIf(this.isDefaultExists(id), "请先取消原有默认存储");
            CheckUtils.throwIf(!DisEnableStatusEnum.ENABLE.equals(oldStatus) && !DisEnableStatusEnum.ENABLE
                .equals(newStatus), "请先启用该存储");
        }
        // 先卸载
        if (DisEnableStatusEnum.ENABLE.equals(oldStatus)) {
            this.unload(BeanUtil.copyProperties(oldStorage, StorageReq.class));
        }
        // 再加载
        if (DisEnableStatusEnum.ENABLE.equals(newStatus)) {
            this.load(req);
        }
    }

    @Override
    public void beforeDelete(List<Long> ids) {
        CheckUtils.throwIf(fileService.countByStorageIds(ids) > 0, "所选存储存在文件关联，请删除文件后重试");
        List<StorageDO> storageList = baseMapper.lambdaQuery().in(StorageDO::getId, ids).list();
        storageList.forEach(s -> {
            CheckUtils.throwIfEqual(Boolean.TRUE, s.getIsDefault(), "[{}] 是默认存储，不允许禁用", s.getName());
            // 卸载启用状态的存储
            if (DisEnableStatusEnum.ENABLE.equals(s.getStatus())) {
                this.unload(BeanUtil.copyProperties(s, StorageReq.class));
            }
        });
    }

    @Override
    public StorageDO getDefaultStorage() {
        return baseMapper.lambdaQuery().eq(StorageDO::getIsDefault, true).one();
    }

    @Override
    public StorageDO getByCode(String code) {
        return baseMapper.lambdaQuery().eq(StorageDO::getCode, code).one();
    }

    @Override
    public void load(StorageReq req) {
        CopyOnWriteArrayList<FileStorage> fileStorageList = fileStorageService.getFileStorageList();
        String domain = req.getDomain();
        ValidationUtils.throwIf(!URLUtils.isHttpUrl(domain), "域名格式错误");
        String bucketName = req.getBucketName();
        StorageTypeEnum type = req.getType();
        if (StorageTypeEnum.LOCAL.equals(type)) {
            ValidationUtils.validate(req, ValidationGroup.Storage.Local.class);
            req.setBucketName(StrUtil.appendIfMissing(bucketName
                .replace(StringConstants.BACKSLASH, StringConstants.SLASH), StringConstants.SLASH));
            FileStorageProperties.LocalPlusConfig config = new FileStorageProperties.LocalPlusConfig();
            config.setPlatform(req.getCode());
            config.setStoragePath(bucketName);
            fileStorageList.addAll(FileStorageServiceBuilder.buildLocalPlusFileStorage(Collections
                .singletonList(config)));
            SpringWebUtils.registerResourceHandler(MapUtil.of(URLUtil.url(req.getDomain()).getPath(), bucketName));
        } else if (StorageTypeEnum.S3.equals(type)) {
            ValidationUtils.validate(req, ValidationGroup.Storage.S3.class);
            FileStorageProperties.AmazonS3Config config = new FileStorageProperties.AmazonS3Config();
            config.setPlatform(req.getCode());
            config.setAccessKey(req.getAccessKey());
            config.setSecretKey(req.getSecretKey());
            config.setEndPoint(req.getEndpoint());
            config.setBucketName(bucketName);
            config.setDomain(domain);
            fileStorageList.addAll(FileStorageServiceBuilder.buildAmazonS3FileStorage(Collections
                .singletonList(config), null));
        }
    }

    @Override
    public void unload(StorageReq req) {
        CopyOnWriteArrayList<FileStorage> fileStorageList = fileStorageService.getFileStorageList();
        FileStorage fileStorage = fileStorageService.getFileStorage(req.getCode());
        fileStorageList.remove(fileStorage);
        fileStorage.close();
        SpringWebUtils.deRegisterResourceHandler(MapUtil.of(URLUtil.url(req.getDomain()).getPath(), req
            .getBucketName()));
    }

    /**
     * 解密 SecretKey
     *
     * @param req     请求参数
     * @param storage 存储信息
     */
    private void decodeSecretKey(StorageReq req, StorageDO storage) {
        if (!StorageTypeEnum.S3.equals(req.getType())) {
            return;
        }
        // 修改时，如果 SecretKey 不修改，需要手动修正
        String newSecretKey = req.getSecretKey();
        boolean isSecretKeyNotUpdate = StrUtil.isBlank(newSecretKey) || newSecretKey.contains(StringConstants.ASTERISK);
        if (null != storage && isSecretKeyNotUpdate) {
            req.setSecretKey(storage.getSecretKey());
            return;
        }
        // 新增时或修改了 SecretKey
        String secretKey = ExceptionUtils.exToNull(() -> SecureUtils.decryptByRsaPrivateKey(newSecretKey));
        ValidationUtils.throwIfNull(secretKey, "私有密钥解密失败");
        ValidationUtils.throwIf(secretKey.length() > 255, "私有密钥长度不能超过 255 个字符");
        req.setSecretKey(secretKey);
    }

    /**
     * 默认存储是否存在
     *
     * @param id ID
     * @return 是否存在
     */
    private boolean isDefaultExists(Long id) {
        return baseMapper.lambdaQuery().eq(StorageDO::getIsDefault, true).ne(null != id, StorageDO::getId, id).exists();
    }

    /**
     * 编码是否存在
     *
     * @param code 编码
     * @param id   ID
     * @return 是否存在
     */
    private boolean isCodeExists(String code, Long id) {
        return baseMapper.lambdaQuery().eq(StorageDO::getCode, code).ne(null != id, StorageDO::getId, id).exists();
    }
}