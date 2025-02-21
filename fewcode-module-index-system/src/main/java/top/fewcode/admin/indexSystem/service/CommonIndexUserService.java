package top.fewcode.admin.indexSystem.service;

import cn.crane4j.annotation.ContainerMethod;
import cn.crane4j.annotation.MappingType;

public interface CommonIndexUserService {
    @ContainerMethod(
            namespace = "IndexUserNickname",
            type = MappingType.ORDER_OF_KEYS
    )
    String getNicknameById(Long id);
}

