package com.emop.wlt.user.management.mapi.business.impl;

import com.emop.wlt.common.model.mapp.RequestModel;
import com.emop.wlt.common.model.mapp.ResponseModel;
import com.emop.wlt.user.entity.User;
import com.emop.wlt.user.info.api.UserInfoProvider;
import com.emop.wlt.user.management.exception.UserManageBuzException;
import com.emop.wlt.user.management.exception.UserManageException;
import com.emop.wlt.user.management.helper.MockFlowContext;
import com.emop.wlt.user.management.helper.MockMobileRpcHolder;
import com.emop.wlt.user.management.helper.RequestModelHelper;
import com.emop.wlt.user.management.manager.MessagePushManager;
import com.emop.wlt.user.management.manager.ResetLoginPwdManager;
import com.emop.wlt.user.management.manager.UserPasswordManager;
import com.emop.wlt.user.management.model.UserWalletInfoDTO;
import com.emop.wlt.user.management.model.request.Mapp11100101Req;
import com.emop.wlt.user.management.model.request.Mapp11200101Req;
import com.emop.wlt.user.management.model.response.Mapp11100101Resp;
import com.emop.wlt.user.management.model.response.Mapp11200101Resp;
import com.emop.wlt.user.management.provider.CFCAManagerProviderImpl;
import com.emop.wlt.user.management.service.MappingIndexManagementService;
import com.emop.wlt.user.management.service.UserService;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ResetLoginPwdProviderImplTest {

    @InjectMocks
    private ResetLoginPwdProviderImpl resetLoginPwdProvider;
    @Mock
    private MappingIndexManagementService mappingIndexManagementService;
    @Mock
    private ResetLoginPwdManager resetLoginPwdManager;
    @Mock
    private CFCAManagerProviderImpl cfcaManagerProvider;
    @Mock
    private UserPasswordManager userPasswordManager;
    @Mock
    private UserService userService;
    @Mock
    private MessagePushManager messagePushManager;
    @Mock
    private UserInfoProvider userInfoProvider;

    @BeforeClass
    public static void beforeClass() {
        MockFlowContext.buildFlowContextMock();
        MockMobileRpcHolder.buildMock();
    }

    @Before
    public void before() {
        MockFlowContext.mockFlowContext(null);
        MockMobileRpcHolder.mockMobileRpcHolder();
    }

    @AfterClass
    public static void afterClass() {
        MockFlowContext.closeFlowContextMock();
        MockMobileRpcHolder.closeMobileRpcHolderMock();
    }

    @Test
    public void testResetLoginPwdValidation1() {
        //正常
        User user = new User();
        user.setPwd("password");
        when(userService.selectByPrimaryKey(anyString())).thenReturn(user);
        when(mappingIndexManagementService.selectByPhone(anyString())).thenReturn("123456");
        when(resetLoginPwdManager.getUserHighestWalletInfo(anyString())).thenReturn(
            UserWalletInfoDTO.builder().walletId("1122334455667788").idType("IT01").walletTotalStatus("2").build());

        Mapp11100101Req mapp11100101Req = new Mapp11100101Req();
        mapp11100101Req.setMobileNumber("+853-60001234");
        RequestModel<Mapp11100101Req> request = RequestModelHelper.buildRequestModel(mapp11100101Req);
        ResponseModel<Mapp11100101Resp> result = resetLoginPwdProvider.resetLoginPwdValidation(request);
        Assert.assertEquals("MAPP111_03", result.getMessageBody().getFlowRespInfo().getRouteCondition());
    }

    @Test
    public void testResetLoginPwdValidation2() {
        //账号未注册
        when(mappingIndexManagementService.selectByPhone(anyString())).thenReturn("");

        Mapp11100101Req mapp11100101Req = new Mapp11100101Req();
        mapp11100101Req.setMobileNumber("+853-60001234");
        RequestModel<Mapp11100101Req> request = RequestModelHelper.buildRequestModel(mapp11100101Req);
        Assert.assertThrows("账号未注册", UserManageException.class,
            ()-> resetLoginPwdProvider.resetLoginPwdValidation(request));
    }

    @Test
    public void testResetLoginPwdValidation3() {
        //账号未设置登录密码
        User user = new User();
        user.setPwd("");
        when(userService.selectByPrimaryKey(anyString())).thenReturn(user);
        when(mappingIndexManagementService.selectByPhone(anyString())).thenReturn("123456");

        Mapp11100101Req mapp11100101Req = new Mapp11100101Req();
        mapp11100101Req.setMobileNumber("+853-60001234");
        RequestModel<Mapp11100101Req> request = RequestModelHelper.buildRequestModel(mapp11100101Req);
        Assert.assertThrows("账号未设置登录密码", UserManageBuzException.class,
            ()-> resetLoginPwdProvider.resetLoginPwdValidation(request));
    }

    @Test
    public void testResetLoginPwd() {
        when(cfcaManagerProvider.decryptPassWord(anyString())).thenReturn("passwordPlain");
        doNothing().when(userPasswordManager).updateUserPwd(anyString(), anyString());
        when(userInfoProvider.forceKickOutFromOtherDevice(anyString(), anyString(), anyString(),
            anyString())).thenReturn("token");
        doNothing().when(messagePushManager).sendResetLoginPwdLogoutMessage(anyString(), anyString(),
            anyString(), anyString());
        doNothing().when(messagePushManager).sendResetLoginPwdMessage(anyString(), anyString(), anyString());

        Mapp11200101Req mapp11200101Req = new Mapp11200101Req();
        mapp11200101Req.setPwdEnc("passwordCipher");
        mapp11200101Req.setDeviceName("Samsung-5G");
        RequestModel<Mapp11200101Req> request = RequestModelHelper.buildRequestModel(mapp11200101Req);
        ResponseModel<Mapp11200101Resp> result = resetLoginPwdProvider.resetLoginPwd(request);
        Assert.assertNotNull(result);
    }
}
