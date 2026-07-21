package com.emop.wlt.user.management.manager;

import com.emop.wlt.user.entity.User;
import com.emop.wlt.user.management.service.MappingIndexManagementService;
import com.emop.wlt.user.management.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserManagerTest {

    @InjectMocks
    private UserManager userManager;
    @Mock
    private MappingIndexManagementService mappingIndexManagementService;
    @Mock
    private UserService userService;

    @Test
    public void testQueryUserByPhone1() {
        when(mappingIndexManagementService.selectByPhone(anyString())).thenReturn("");
        User result = userManager.queryUserByPhone("phone");
        Assert.assertNull(result);
    }

    @Test
    public void testQueryUserByPhone2() {
        when(mappingIndexManagementService.selectByPhone(anyString())).thenReturn("123456");
        when(userService.selectByPrimaryKey(anyString())).thenReturn(null);
        User result = userManager.queryUserByPhone("phone");
        Assert.assertNull(result);
    }

    @Test
    public void testQueryUserByPhone3() {
        when(mappingIndexManagementService.selectByPhone(anyString())).thenReturn("123456");
        when(userService.selectByPrimaryKey(anyString())).thenReturn(new User());
        User result = userManager.queryUserByPhone("phone");
        Assert.assertNotNull(result);
    }
}
