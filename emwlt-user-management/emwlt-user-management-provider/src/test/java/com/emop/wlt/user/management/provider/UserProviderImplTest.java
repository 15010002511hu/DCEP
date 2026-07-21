package com.emop.wlt.user.management.provider;

import com.emop.wlt.user.entity.User;
import com.emop.wlt.user.info.api.UserInfoProvider;
import com.emop.wlt.user.management.dto.UserVO;
import com.emop.wlt.user.management.manager.MessagePushManager;
import com.emop.wlt.user.management.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserProviderImplTest {
    @InjectMocks
    private UserProviderImpl userProvider;
    @Mock
    private UserService userService;
    @Mock
    private UserInfoProvider userInfoProvider;
    @Mock
    private MessagePushManager messagePushManager;

    @Test
    public void testSelectByPrimaryKey1() {
        when(userService.selectByPrimaryKey(anyString())).thenReturn(new User());
        UserVO result = userProvider.selectByPrimaryKey("userId");
        Assert.assertNotNull(result);
    }

    @Test
    public void testSelectByPrimaryKey2() {
        when(userService.selectByPrimaryKey(anyString())).thenReturn(null);
        UserVO result = userProvider.selectByPrimaryKey("userId");
        Assert.assertNull(result);
    }

    @Test
    public void testDeactivateUser() {
        userProvider.deactivateUser("phone", "userId");
    }

    @Test
    public void testReportAsLost() {
        User user = new User();
        user.setPhone("+853-60001234");
        when(userService.selectByPrimaryKey(anyString())).thenReturn(user);
        when(userService.updateStatusByUserId(any())).thenReturn(true);
        when(userInfoProvider.forceKickOutUser(anyString(), anyString())).thenReturn("token");
        Mockito.doNothing().when(messagePushManager).sendForceLoginOutMsg(anyString(), anyString(), anyString());
        userProvider.reportAsLost("userId");
    }

    @Test
    public void testCancelLoss() {
        when(userService.updateStatusByUserId(any())).thenReturn(true);
        userProvider.cancelLoss("userId");
    }
}
