package com.emop.wlt.user.query.provider;

import com.emop.wlt.user.entity.Customer;
import com.emop.wlt.user.query.service.CustomerManagementService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CustomerQueryProviderImplTest {
    @Mock
    private CustomerManagementService customerManagementService;
    @InjectMocks
    private CustomerQueryProviderImpl customerQueryProviderImpl;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testQueryCustomerInfo() {
        when(customerManagementService.selectByCid(anyString())).thenReturn(new Customer());

        customerQueryProviderImpl.queryCustomerInfo("cid");
    }

}
