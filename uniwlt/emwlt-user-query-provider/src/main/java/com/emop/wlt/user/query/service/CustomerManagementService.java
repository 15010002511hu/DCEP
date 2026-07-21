package com.emop.wlt.user.query.service;

import com.emop.wlt.user.entity.Customer;

public interface CustomerManagementService {

    /**
     * 根据cid查询customer
     */
    Customer selectByCid(String cid);
}
