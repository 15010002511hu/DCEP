package com.emop.wlt.user.management.service;

import com.emop.wlt.user.entity.MappingIndex;
import com.emop.wlt.user.entity.User;

public interface MappingIndexManagementService {

    String selectByPhone(String phone);

    /**
     * 保存索引和用户 有事物
     */
    void saveUserLogic(MappingIndex mappingIndex, User user);

}
