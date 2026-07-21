package com.emop.wlt.user.query.provider;

import com.emop.wlt.user.entity.User;
import com.emop.wlt.user.query.service.MappingIndexManagementService;
import com.emop.wlt.user.query.service.UserService;
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
public class UserProviderImplTest {
    @Mock
    private UserService userService;
    @Mock
    private MappingIndexManagementService mappingIndexManagementService;
    @InjectMocks
    private UserProviderImpl userProviderImpl;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSelectByPrimaryKey() {
        when(userService.selectByPrimaryKey(anyString())).thenReturn(new User());

        userProviderImpl.selectByPrimaryKey("userId");
    }

    @Test
    public void testSelectByLandAls() {
        when(userService.selectByPrimaryKey(anyString())).thenReturn(new User());
        when(mappingIndexManagementService.selectByPhone(anyString())).thenReturn("selectByPhoneResponse");

        userProviderImpl.selectByLandAls("landAls");
    }

    @Test
    public void testSelectUserIdByPhone() {
        when(mappingIndexManagementService.selectByPhone(anyString())).thenReturn("selectByPhoneResponse");

        userProviderImpl.selectUserIdByPhone("phone");
    }

}
