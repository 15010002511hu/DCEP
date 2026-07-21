package com.emop.wlt.user.management.manager;

import com.emop.wlt.id.db.service.EmwltDBIdGenerator;
import com.emop.wlt.user.entity.CidIndex;
import com.emop.wlt.user.management.dto.CustomerSaveDTO;
import com.emop.wlt.user.management.exception.UserManageException;
import com.emop.wlt.user.management.service.impl.CidIndexCustomerManagementServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CustomerSaveManagerTest {
    @InjectMocks
    private CustomerSaveManager customerSaveManager;
    @Mock
    private EmwltDBIdGenerator emwltDBIdGenerator;
    @Mock
    private CidIndexCustomerManagementServiceImpl cidIndexCustomerManagementService;

    @Test
    public void testSaveCustomer1() {
        //新生成CID
        when(emwltDBIdGenerator.getCustomerId()).thenReturn("12345678905");
        when(cidIndexCustomerManagementService.selectCidIndex(any())).thenReturn(null);

        CustomerSaveDTO customerSaveDTO = new CustomerSaveDTO();
        customerSaveDTO.setCustomerType("01");
        customerSaveDTO.setCountryAndRegionCode("MO");
        customerSaveDTO.setIdType("IT02");
        customerSaveDTO.setIdNumber("112233445566");
        customerSaveDTO.setWalletId("0021012345678911");
        String result = customerSaveManager.saveCustomer(customerSaveDTO);
        Assert.assertEquals("MO0112345678909157", result);
    }

    @Test
    public void testSaveCustomer2() {
        //已经有CID-更新
        CidIndex cidIndex = new CidIndex();
        cidIndex.setIdentityNumber("665544332211");
        cidIndex.setIdentityType("IT02");
        cidIndex.setCid("MO0109876543219157");
        when(cidIndexCustomerManagementService.selectCidIndex(any())).thenReturn(cidIndex);
        when(cidIndexCustomerManagementService.checkExistByCid(any())).thenReturn(true);
        when(cidIndexCustomerManagementService.updateByPrimaryKeyCustomer(any())).thenReturn(true);

        CustomerSaveDTO customerSaveDTO = new CustomerSaveDTO();
        customerSaveDTO.setCustomerType("01");
        customerSaveDTO.setCountryAndRegionCode("MO");
        customerSaveDTO.setIdType("IT02");
        customerSaveDTO.setIdNumber("665544332211");
        customerSaveDTO.setWalletId("0021012345678911");
        String result = customerSaveManager.saveCustomer(customerSaveDTO);
        Assert.assertEquals("MO0109876543219157", result);
    }

    @Test
    public void testSaveCustomer3() {
        //已经有CID-插入
        CidIndex cidIndex = new CidIndex();
        cidIndex.setIdentityNumber("665544332211");
        cidIndex.setIdentityType("IT02");
        cidIndex.setCid("MO0109876543219157");
        when(cidIndexCustomerManagementService.selectCidIndex(any())).thenReturn(cidIndex);
        when(cidIndexCustomerManagementService.checkExistByCid(any())).thenReturn(false);
        when(cidIndexCustomerManagementService.insertCustomer(any())).thenReturn(true);

        CustomerSaveDTO customerSaveDTO = new CustomerSaveDTO();
        customerSaveDTO.setCustomerType("01");
        customerSaveDTO.setCountryAndRegionCode("MO");
        customerSaveDTO.setIdType("IT02");
        customerSaveDTO.setIdNumber("665544332211");
        customerSaveDTO.setWalletId("0021012345678911");
        String result = customerSaveManager.saveCustomer(customerSaveDTO);
        Assert.assertEquals("MO0109876543219157", result);
    }

    @Test
    public void testSaveCustomer4() {
        //异常-国家标识传入长度有误
        CustomerSaveDTO customerSaveDTO = new CustomerSaveDTO();
        customerSaveDTO.setCustomerType("01");
        customerSaveDTO.setCountryAndRegionCode("MACAO");
        customerSaveDTO.setIdType("IT02");
        customerSaveDTO.setIdNumber("112233445566");
        customerSaveDTO.setWalletId("0021012345678911");
        Assert.assertThrows("国家标识传入长度有误", UserManageException.class,
            ()->customerSaveManager.saveCustomer(customerSaveDTO));
    }

    @Test
    public void testSaveCustomer5() {
        //新生成CID - 证件类型为空
        when(emwltDBIdGenerator.getCustomerId()).thenReturn("12345678905");
        when(cidIndexCustomerManagementService.selectCidIndex(any())).thenReturn(null);

        CustomerSaveDTO customerSaveDTO = new CustomerSaveDTO();
        customerSaveDTO.setCustomerType("01");
        customerSaveDTO.setCountryAndRegionCode("MO");
        customerSaveDTO.setIdNumber("112233445566");
        customerSaveDTO.setWalletId("0021012345678911");
        String result = customerSaveManager.saveCustomer(customerSaveDTO);
        Assert.assertEquals("MO0112345678909157", result);
    }
}
