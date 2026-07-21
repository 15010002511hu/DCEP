package com.emop.wlt.user.query.service.impl;


import com.dubbo.dds.DDS;
import com.dubbo.dds.rule.SingleRule;
import com.emop.wlt.user.pojo.SystemMessage;
import com.emop.wlt.user.query.constant.DatabaseConstant;
import com.emop.wlt.user.query.service.SystemMessageQueryServie;
import com.emop.wlt.user.service.SystemMessageService;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author fanjianyu
 * @title
 * @description SystemMessageService
 * @date 2022/3/31
 */
@Service
public class SystemMessageQueryServiceImpl implements SystemMessageQueryServie {

    @Autowired
    private SystemMessageService systemMessageService;

    @Override
    @DDS(value = DatabaseConstant.CONVENTION_QUERY_DATABASE, rule = SingleRule.class)
    public List<SystemMessage> selectAll() {
        return systemMessageService.selectAll();
    }

    @Override
    @DDS(value = DatabaseConstant.CONVENTION_QUERY_DATABASE, rule = SingleRule.class)
    public List<SystemMessage> selectByInstAndGtTimeStampAndGeValidTime(String inst, Timestamp timeStamp,
        LocalDateTime validTime) {
        return systemMessageService.selectByInstAndGtTimeStampAndGeValidTime(inst, timeStamp, validTime);
    }
}
