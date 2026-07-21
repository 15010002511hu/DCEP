package com.emop.wlt.user.management.provider;

import com.emop.wlt.user.management.dto.CustomerSaveDTO;
import com.emop.wlt.user.management.manager.CustomerSaveManager;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CustomerSaveProviderImplTest {

    @InjectMocks
    private CustomerSaveProviderImpl customerSaveProviderImpl;
    @Mock
    private CustomerSaveManager customerSaveManager;

    @Test
    public void testSaveCustomer() {
        when(customerSaveManager.saveCustomer(any())).thenReturn("saveCustomerResponse");

        String result = customerSaveProviderImpl.saveCustomer(new CustomerSaveDTO());
        Assert.assertEquals("saveCustomerResponse", result);
    }
}