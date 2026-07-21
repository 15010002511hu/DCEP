package com.emop.wlt.user.management.provider;

import com.emop.wlt.user.management.exception.UserManageBuzException;
import com.emop.wlt.user.management.manager.UserPasswordManager;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserPasswordProviderImplTest {

    @InjectMocks
    private UserPasswordProviderImpl userPasswordProviderImpl;
    @Mock
    private UserPasswordManager userPasswordManager;
    @Mock
    private CFCAManagerProviderImpl cfcaManagerProvider;

    @Test
    public void testCheckUserPwd() {
        when(userPasswordManager.equalsUserPwd(anyString(), anyString())).thenReturn(true);
        Assert.assertThrows("新旧账号密码相同", UserManageBuzException.class,
            ()-> userPasswordProviderImpl.checkUserPwd("userId", "userPwd"));
    }

    @Test
    public void testUpdateUserPwd() {
        doNothing().when(userPasswordManager).updateUserPwd(anyString(), anyString());
        userPasswordProviderImpl.updateUserPwd("userId", "userPwd");
    }

    @Test
    public void testDecryptAndUpdateUserPwd() {
        when(cfcaManagerProvider.decryptPassWord(anyString())).thenReturn("decryptUserPwd");
        userPasswordProviderImpl.decryptAndUpdateUserPwd("userId", "encUserPwd");
    }
}
