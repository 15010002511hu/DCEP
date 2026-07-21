package com.emop.wlt.user.management.mapi.business.impl;

import com.emop.wlt.common.model.flow.FlowRespInfo;
import com.emop.wlt.common.model.mapp.RequestModel;
import com.emop.wlt.common.model.mapp.ResponseModel;
import com.emop.wlt.user.info.dto.UserInfoLoginRespDTO;
import com.emop.wlt.user.management.helper.MockFlowContext;
import com.emop.wlt.user.management.helper.MockMobileRpcHolder;
import com.emop.wlt.user.management.helper.RequestModelHelper;
import com.emop.wlt.user.management.manager.LoginManager;
import com.emop.wlt.user.management.model.AccountInfoDTO;
import com.emop.wlt.user.management.model.request.Mapp10200101Req;
import com.emop.wlt.user.management.model.request.Mapp10300101Req;
import com.emop.wlt.user.management.model.response.Mapp10200101Resp;
import com.emop.wlt.user.management.model.response.Mapp10300101Resp;
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
public class LoginProviderImplTest {

    @InjectMocks
    private LoginProviderImpl loginProvider;
    @Mock
    private LoginManager loginManager;

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
    public void testLoginValidation() {
        AccountInfoDTO accountInfoDTO = new AccountInfoDTO();
        accountInfoDTO.setIsSetLoginPwd("true");
        accountInfoDTO.setUserId("123456");
        when(loginManager.loginPreCheck(anyString())).thenReturn(accountInfoDTO);

        FlowRespInfo flowRespInfo = new FlowRespInfo();
        flowRespInfo.setRouteCondition("MAPP102_02");

        Mapp10200101Req mapp10200101Req = new Mapp10200101Req();
        mapp10200101Req.setMobileNumber("+853-60001234");
        mapp10200101Req.setIfaaOpenFlag("false");
        RequestModel<Mapp10200101Req> request = RequestModelHelper.buildRequestModel(mapp10200101Req);
        ResponseModel<Mapp10200101Resp> result = loginProvider.loginValidation(request);
        Assert.assertEquals("true", result.getMessageBody().getSetPasswordFlag());
        Assert.assertEquals("MAPP102_02", result.getMessageBody().getFlowRespInfo().getRouteCondition());
    }

    @Test
    public void testLogin() {
        UserInfoLoginRespDTO userInfoLoginRespDTO = new UserInfoLoginRespDTO();
        userInfoLoginRespDTO.setToken("token");
        userInfoLoginRespDTO.setUserId("123456");
        userInfoLoginRespDTO.setRefreshIntervalSeconds(1800L);
        userInfoLoginRespDTO.setExpireSeconds(1800L);
        when(loginManager.loginLogicV2(any())).thenReturn(userInfoLoginRespDTO);

        Mapp10300101Req mapp10300101Req = new Mapp10300101Req();
        mapp10300101Req.setDeviceName("Samsung-5G");
        RequestModel<Mapp10300101Req> request = RequestModelHelper.buildRequestModel(mapp10300101Req);
        ResponseModel<Mapp10300101Resp> result = loginProvider.login(request);
        Assert.assertEquals("123456", result.getMessageBody().getUserId());
        Assert.assertEquals("+853-60001234", result.getMessageBody().getMobileNumber());
        Assert.assertEquals("token", result.getMessageBody().getToken());
        Assert.assertEquals("1800", result.getMessageBody().getRefreshIntervalSeconds());
        Assert.assertEquals("1800", result.getMessageBody().getExpireSeconds());
    }
}
