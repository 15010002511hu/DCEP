package com.emop.wlt.user.management.mapi.component.impl;

import com.emop.wlt.common.model.flow.FlowReqInfo;
import com.emop.wlt.common.model.mapp.RequestModel;
import com.emop.wlt.common.model.mapp.ResponseModel;
import com.emop.wlt.user.management.helper.MockFlowContext;
import com.emop.wlt.user.management.helper.RequestModelHelper;
import com.emop.wlt.user.management.model.request.Mapp02300101Req;
import com.emop.wlt.user.management.model.response.Mapp02300101Resp;
import com.emop.wlt.user.wallet.api.SmsProvider;
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
public class WalletSmsSendProviderImplTest {

    @InjectMocks
    private WalletSmsSendProviderImpl walletSmsSendProvider;
    @Mock
    private SmsProvider smsProvider;

    @BeforeClass
    public static void beforeClass() {
        MockFlowContext.buildFlowContextMock();
    }

    @Before
    public void before() {
        String configParam = ""
            + "{\n"
            + "    \"getCtxWltId\": true,\n"
            + "    \"getCtxMobileNumber\": true,\n"
            + "}";
        MockFlowContext.mockFlowContext(configParam);
    }

    @AfterClass
    public static void afterClass() {
        MockFlowContext.closeFlowContextMock();
    }

    @Test
    public void testWalletSendSms() {
        when(smsProvider.getWalletVerifyCode(any())).thenReturn("instContextNo");

        Mapp02300101Req mapp02300101Req = new Mapp02300101Req();
        FlowReqInfo flowReqInfo = new FlowReqInfo();
        flowReqInfo.setVerifySceneType("VST37");
        flowReqInfo.setFlowVersion("VST37_01");
        mapp02300101Req.setFlowReqInfo(flowReqInfo);
        RequestModel<Mapp02300101Req> request = RequestModelHelper.buildRequestModel(mapp02300101Req);
        ResponseModel<Mapp02300101Resp> result = walletSmsSendProvider.walletSendSms(request);
        Assert.assertNotNull(result);
    }
}
