package com.emop.wlt.user.query.provider;

import com.emop.common.enums.errorcode.BaseErrorEnum;
import com.emop.wlt.user.entity.Customer;
import com.emop.wlt.user.query.api.CustomerQueryProvider;
import com.emop.wlt.user.query.dto.CustomerOutDTO;
import com.emop.wlt.user.query.exception.ExceptionCast;
import com.emop.wlt.user.query.service.CustomerManagementService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService
@Slf4j
public class CustomerQueryProviderImpl implements CustomerQueryProvider {

    @Autowired
    private CustomerManagementService customerManagementService;

    @Override
    public CustomerOutDTO queryCustomerInfo(String cid) {
        if (StringUtils.isEmpty(cid)) {
            ExceptionCast.cast(BaseErrorEnum.S02021);
        }
        Customer customer = customerManagementService.selectByCid(cid);
        if (customer == null) {
            ExceptionCast.cast(BaseErrorEnum.S05102);
        }
        CustomerOutDTO customerOutDTO = new CustomerOutDTO();
        customerOutDTO.setIdType(customer.getIdentityType());
        customerOutDTO.setIdNumber(customer.getIdentityNumber());
        return customerOutDTO;
    }
}
