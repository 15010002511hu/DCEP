package com.emop.wlt.user.management.manager;

import com.emop.wlt.user.entity.User;
import com.emop.wlt.user.info.api.UserInfoProvider;
import com.emop.wlt.user.info.dto.UserInfoLoginRespDTO;
import com.emop.wlt.user.management.exception.UserManageException;
import com.emop.wlt.user.management.model.AccountInfoDTO;
import com.emop.wlt.user.management.model.LoginRequestDTO;
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
public class LoginManagerTest {
    @InjectMocks
    private LoginManager loginManager;
    @Mock
    private MappingIndexManagementService mappingIndexManagementService;
    @Mock
    private UserService userService;
    @Mock
    private UserInfoProvider userInfoProvider;

    @Test
    public void testLoginPreCheck1() {
        //正常
        User user = new User();
        user.setUserId("446120241029100193613376");
        user.setStatus("US03");
        user.setPwd("password");
        when(userService.selectByPrimaryKey(anyString())).thenReturn(user);
        when(mappingIndexManagementService.selectByPhone(anyString())).thenReturn("446120241029100193613376");

        AccountInfoDTO expectedResp = new AccountInfoDTO();
        expectedResp.setUserId("446120241029100193613376");
        expectedResp.setIsSetLoginPwd("true");
        AccountInfoDTO result = loginManager.loginPreCheck("+853-60001234");
        Assert.assertEquals(expectedResp, result);
    }

    @Test
    public void testLoginPreCheck2() {
        //账户下钱包为挂失状态
        User user = new User();
        user.setUserId("446120241029100193613376");
        user.setStatus("US06");
        user.setPwd("password");
        when(userService.selectByPrimaryKey(anyString())).thenReturn(user);
        when(mappingIndexManagementService.selectByPhone(anyString())).thenReturn("446120241029100193613376");
        Assert.assertThrows("账户下钱包为挂失状态", UserManageException.class,
            ()-> loginManager.loginPreCheck("+853-60001234"));
    }

    @Test
    public void testLoginPreCheck3() {
        //账户未设置密码
        User user = new User();
        user.setUserId("446120241029100193613376");
        user.setStatus("US03");
        user.setPwd("");
        when(userService.selectByPrimaryKey(anyString())).thenReturn(user);
        when(mappingIndexManagementService.selectByPhone(anyString())).thenReturn("446120241029100193613376");

        AccountInfoDTO expectedResp = new AccountInfoDTO();
        expectedResp.setUserId("446120241029100193613376");
        expectedResp.setIsSetLoginPwd("false");
        AccountInfoDTO result = loginManager.loginPreCheck("+853-60001234");
        Assert.assertEquals(expectedResp, result);
    }

    @Test
    public void testLoginPreCheck4() {
        //账户未注册
        when(mappingIndexManagementService.selectByPhone(anyString())).thenReturn("");
        Assert.assertThrows("账号未注册", UserManageException.class,
            ()-> loginManager.loginPreCheck("+853-60001234"));
    }

    @Test
    public void testLoginPreCheck5() {
        //user为null
        when(userService.selectByPrimaryKey(anyString())).thenReturn(null);
        when(mappingIndexManagementService.selectByPhone(anyString())).thenReturn("446120241029100193613376");

        AccountInfoDTO expectedResp = new AccountInfoDTO();
        expectedResp.setUserId("446120241029100193613376");
        expectedResp.setIsSetLoginPwd("false");
        AccountInfoDTO result = loginManager.loginPreCheck("+853-60001234");
        Assert.assertEquals(expectedResp, result);
    }

    @Test
    public void testLoginLogicV2_1() {
        when(mappingIndexManagementService.selectByPhone(anyString())).thenReturn("446120241029100193613376");
        UserInfoLoginRespDTO expectedResp = new UserInfoLoginRespDTO();
        expectedResp.setUserId("446120241029100193613376");
        expectedResp.setToken("token");
        expectedResp.setExpireSeconds(1800L);
        expectedResp.setRefreshIntervalSeconds(1800L);
        when(userInfoProvider.loginV2(any())).thenReturn(expectedResp);

        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setMobileNumber("+853-60001234");
        loginRequestDTO.setDeviceId("deviceId");
        loginRequestDTO.setDeviceName("deviceName");
        loginRequestDTO.setDeviceType("deviceType");
        UserInfoLoginRespDTO result = loginManager.loginLogicV2(loginRequestDTO);
        Assert.assertEquals(expectedResp, result);
    }

    @Test
    public void testLoginLogicV2_2() {
        Assert.assertThrows("用户未注册", UserManageException.class,
            ()-> loginManager.loginLogicV2(new LoginRequestDTO()));
    }
}
