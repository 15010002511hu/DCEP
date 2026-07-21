package com.emop.wlt.user.management.service.impl;


import com.dubbo.dds.DDS;
import com.dubbo.dds.rule.SingleRule;
import com.emop.wlt.user.management.constant.DatabaseConstant;
import com.emop.wlt.user.management.service.SystemMessageManageService;
import com.emop.wlt.user.pojo.SystemMessage;
import com.emop.wlt.user.service.SystemMessageService;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author fanjianyu
 * @title
 * @description SystemMessageManageServiceImpl
 * @date 2022/3/31
 */
@Service
public class SystemMessageManageServiceImpl implements SystemMessageManageService {

    @Autowired
    private SystemMessageService systemMessageService;

    @Override
    @DDS(value = DatabaseConstant.CONVENTION_MANAGEMENT_DB_KEY, rule = SingleRule.class)
    public boolean save(SystemMessage systemMessage) {
        return systemMessageService.insert(systemMessage) > NumberUtils.INTEGER_ZERO;
    }
}
