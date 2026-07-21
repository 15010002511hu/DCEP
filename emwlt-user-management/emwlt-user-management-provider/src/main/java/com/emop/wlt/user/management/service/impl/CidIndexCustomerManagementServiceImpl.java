package com.emop.wlt.user.management.service.impl;

import com.dubbo.dds.DDS;
import com.dubbo.dds.rule.SingleRule;
import com.emop.wlt.user.entity.CidIndex;
import com.emop.wlt.user.entity.Customer;
import com.emop.wlt.user.management.constant.DatabaseConstant;
import com.emop.wlt.user.management.service.CidIndexCustomerManagementService;
import com.emop.wlt.user.service.CidIndexService;
import com.emop.wlt.user.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class CidIndexCustomerManagementServiceImpl implements CidIndexCustomerManagementService {

    @Autowired
    private CidIndexService cidIndexService;

    @Autowired
    private CustomerService customerService;

    @Override
    @DDS(value = DatabaseConstant.CUSTOMER_MANAGEMENT_DB_KEY, rule = SingleRule.class)
    public CidIndex selectCidIndex(CidIndex cidIndex) {
        return cidIndexService.selectOne(cidIndex);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    @Override
    @DDS(value = DatabaseConstant.CUSTOMER_MANAGEMENT_DB_KEY, rule = SingleRule.class)
    public void saveCidIndexCustom(CidIndex cidIndex, Customer customer) {
        log.info("记录证件号-客户id关联关系");
        cidIndexService.insert(cidIndex);

        log.info("记录客户信息");
        customerService.insert(customer);
    }

    @Override
    @DDS(value = DatabaseConstant.CUSTOMER_MANAGEMENT_DB_KEY, rule = SingleRule.class)
    public boolean checkExistByCid(Customer customer) {
        return customerService.getCountByCid(customer) > DatabaseConstant.ZERO;
    }

    @Override
    @DDS(value = DatabaseConstant.CUSTOMER_MANAGEMENT_DB_KEY, rule = SingleRule.class)
    public boolean updateByPrimaryKeyCustomer(Customer customer) {
        return customerService.updateByPrimaryKey(customer);
    }

    @Override
    @DDS(value = DatabaseConstant.CUSTOMER_MANAGEMENT_DB_KEY, rule = SingleRule.class)
    public boolean insertCustomer(Customer customer) {
        return customerService.insert(customer) > NumberUtils.INTEGER_ZERO;
    }
}
