package com.emop.wlt.user.query.service.impl;

import com.dubbo.dds.DDS;
import com.dubbo.dds.rule.SingleRule;
import com.emop.wlt.user.entity.Customer;
import com.emop.wlt.user.query.constant.DatabaseConstant;
import com.emop.wlt.user.query.service.CustomerManagementService;
import com.emop.wlt.user.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerManagementServiceImpl implements CustomerManagementService {

    @Autowired
    private CustomerService customerService;

    @Override
    @DDS(value = DatabaseConstant.CUSTOMER_QUERY_FLAG, rule = SingleRule.class)
    public Customer selectByCid(String cid) {
        return customerService.selectOneByCid(cid);
    }
}
