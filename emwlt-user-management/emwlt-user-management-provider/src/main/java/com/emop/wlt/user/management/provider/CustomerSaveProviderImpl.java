package com.emop.wlt.user.management.provider;

import com.emop.wlt.user.management.api.CustomerSaveProvider;
import com.emop.wlt.user.management.dto.CustomerSaveDTO;
import com.emop.wlt.user.management.manager.CustomerSaveManager;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService
public class CustomerSaveProviderImpl implements CustomerSaveProvider {

    @Autowired
    private CustomerSaveManager customerSaveManager;

    @Override
    public String saveCustomer(CustomerSaveDTO in) {
        return customerSaveManager.saveCustomer(in);
    }
}
