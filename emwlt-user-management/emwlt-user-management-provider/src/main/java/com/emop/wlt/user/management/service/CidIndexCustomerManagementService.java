package com.emop.wlt.user.management.service;

import com.emop.wlt.user.entity.CidIndex;
import com.emop.wlt.user.entity.Customer;

public interface CidIndexCustomerManagementService {

    /**
     * 查询身份证与cid关系
     *
     * @param cidIndex
     * @return
     */
    CidIndex selectCidIndex(CidIndex cidIndex);

    void saveCidIndexCustom(CidIndex cidIndex, Customer customer);

    /**
     * 检查指定cid对应记录是否存在
     */
    boolean checkExistByCid(Customer customer);

    /**
     * 更新客户信息
     */
    boolean updateByPrimaryKeyCustomer(Customer customer);

    /**
     * 插入客户信息
     */
    boolean insertCustomer(Customer customer);

}
