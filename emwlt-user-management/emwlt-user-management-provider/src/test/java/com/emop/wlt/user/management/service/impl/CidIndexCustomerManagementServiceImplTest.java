package com.emop.wlt.user.management.service.impl;

import com.emop.wlt.user.entity.CidIndex;
import com.emop.wlt.user.entity.Customer;
import com.emop.wlt.user.service.CidIndexService;
import com.emop.wlt.user.service.CustomerService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CidIndexCustomerManagementServiceImplTest {

    @InjectMocks
    private CidIndexCustomerManagementServiceImpl cidIndexCustomerManagementService;
    @Mock
    private CidIndexService cidIndexService;
    @Mock
    private CustomerService customerService;

    @Test
    public void testSelectCidIndex() {
        when(cidIndexService.selectOne(any())).thenReturn(new CidIndex());
        CidIndex result = cidIndexCustomerManagementService.selectCidIndex(new CidIndex());
        Assert.assertEquals(new CidIndex(), result);
    }

    @Test
    public void testSaveCidIndexCustom() {
        when(cidIndexService.insert(any())).thenReturn(0);
        when(customerService.insert(any())).thenReturn(0);
        cidIndexCustomerManagementService.saveCidIndexCustom(new CidIndex(), new Customer());
    }

    @Test
    public void testCheckExistByCid() {
        when(customerService.getCountByCid(any())).thenReturn(1L);
        boolean result = cidIndexCustomerManagementService.checkExistByCid(new Customer());
        Assert.assertTrue(result);
    }

    @Test
    public void testUpdateByPrimaryKeyCustomer() {
        when(customerService.updateByPrimaryKey(any())).thenReturn(true);
        boolean result = cidIndexCustomerManagementService.updateByPrimaryKeyCustomer(new Customer());
        Assert.assertTrue(result);
    }

    @Test
    public void testInsertCustomer() {
        when(customerService.insert(any())).thenReturn(1);
        boolean result = cidIndexCustomerManagementService.insertCustomer(new Customer());
        Assert.assertTrue(result);
    }
}
