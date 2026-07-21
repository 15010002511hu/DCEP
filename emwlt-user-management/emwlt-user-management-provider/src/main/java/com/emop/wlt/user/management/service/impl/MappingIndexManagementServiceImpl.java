package com.emop.wlt.user.management.service.impl;

import com.dubbo.dds.DDS;
import com.dubbo.dds.rule.SingleRule;
import com.emop.wlt.user.entity.MappingIndex;
import com.emop.wlt.user.entity.User;
import com.emop.wlt.user.management.constant.DatabaseConstant;
import com.emop.wlt.user.management.service.MappingIndexManagementService;
import com.emop.wlt.user.management.service.UserService;
import com.emop.wlt.user.service.MappingIndexService;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class MappingIndexManagementServiceImpl implements MappingIndexManagementService {

    @Autowired
    private MappingIndexService mappingIndexService;

    @Autowired
    private UserService userService;

    @Override
    @DDS(value = DatabaseConstant.USER_MANAGEMENT_DB_KEY, rule = SingleRule.class)
    public String selectByPhone(String phone) {
        if (Strings.isBlank(phone)) {
            return null;
        }
        MappingIndex mappingIndex = new MappingIndex();
        mappingIndex.setMappingKey(phone);
        MappingIndex result = mappingIndexService.selectByPhone(mappingIndex);
        if (Objects.isNull(result)) {
            return null;
        }

        return result.getMappingValue();
    }

    /**
     * 同时保存索引刚和用户信息表 添加事物
     *
     * @param mappingIndex
     * @param user
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @DDS(value = DatabaseConstant.USER_MANAGEMENT_DB_KEY, rule = SingleRule.class, force = true)
    public void saveUserLogic(MappingIndex mappingIndex, User user) {
        log.info("mappingIndex:{}",mappingIndex);
        log.info("user:{}",user);
        if (Objects.nonNull(mappingIndex)) {
            mappingIndexService.saveLandals(mappingIndex);
        }
        userService.saveUserInfo(user);
    }


}
