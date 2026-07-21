package com.emop.wlt.user.query.service.impl;

import com.dubbo.dds.DDS;
import com.dubbo.dds.rule.SingleRule;
import com.emop.wlt.user.entity.CidIndex;
import com.emop.wlt.user.query.constant.DatabaseConstant;
import com.emop.wlt.user.query.service.CidIndexManagementService;
import com.emop.wlt.user.service.CidIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CidIndexManagementServiceImpl implements CidIndexManagementService {

    @Autowired
    private CidIndexService cidIndexService;

    @Override
    @DDS(value = DatabaseConstant.CUSTOMER_QUERY_FLAG, rule = SingleRule.class)
    public CidIndex getCidByIdentityInfo(String identityType, String identityNumber) {
        CidIndex cidIndex = new CidIndex();
        cidIndex.setIdentityType(identityType);
        cidIndex.setIdentityNumber(identityNumber);
        return cidIndexService.selectOne(cidIndex);
    }
}
