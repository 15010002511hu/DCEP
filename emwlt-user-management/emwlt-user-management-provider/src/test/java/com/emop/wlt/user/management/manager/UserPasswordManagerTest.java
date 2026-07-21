package com.emop.wlt.user.management.manager;

import com.emop.wlt.user.entity.User;
import com.emop.wlt.user.management.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserPasswordManagerTest {

    @InjectMocks
    private UserPasswordManager userPasswordManager;
    @Mock
    private UserService userService;

    @Test
    public void testUpdateUserPwd() {
        when(userService.updateUserPwdByUserId(anyString(), anyString(), anyString())).thenReturn(true);
        userPasswordManager.updateUserPwd("userId", "userPwd");
    }

    @Test
    public void testEqualsUserPwd1() {
        //DB无密码和盐
        User user = new User();
        user.setPwd("");
        user.setPwdSalt("");
        when(userService.selectByPrimaryKey(anyString())).thenReturn(user);
        boolean result = userPasswordManager.equalsUserPwd("userId", "newPwd");
        Assert.assertFalse(result);
    }

    @Test
    public void testEqualsUserPwd2() {
        //正常
        User user = new User();
        user.setPwd("e974b457cc9790eead63a178dc5e3dd861d2a36dad1a9dafbe828f3abafa5beb");
        user.setPwdSalt("salt");
        when(userService.selectByPrimaryKey(anyString())).thenReturn(user);
        boolean result = userPasswordManager.equalsUserPwd("userId", "password");
        Assert.assertTrue(result);
    }

    @Test
    public void testEqualsUserPwd3() {
        //新旧密码不一致
        User user = new User();
        user.setPwd("oldPassword");
        user.setPwdSalt("salt");
        when(userService.selectByPrimaryKey(anyString())).thenReturn(user);
        boolean result = userPasswordManager.equalsUserPwd("userId", "newPassword");
        Assert.assertFalse(result);
    }
}
