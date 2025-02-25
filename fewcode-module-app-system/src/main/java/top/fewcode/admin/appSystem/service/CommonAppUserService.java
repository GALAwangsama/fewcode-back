package top.fewcode.admin.appSystem.service;

import cn.crane4j.annotation.ContainerMethod;
import cn.crane4j.annotation.MappingType;

public interface CommonAppUserService {
    @ContainerMethod(
            namespace = "AppUserNickname",
            type = MappingType.ORDER_OF_KEYS
    )
    String getNicknameById(Long id);
}

