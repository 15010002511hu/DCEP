package com.emop.wlt.user.management.mapi.component.impl;

import com.emop.wlt.common.model.flow.FlowReqInfo;
import com.emop.wlt.common.model.mapp.RequestModel;
import com.emop.wlt.common.model.mapp.ResponseModel;
import com.emop.wlt.user.management.helper.MockFlowContext;
import com.emop.wlt.user.management.helper.RequestModelHelper;
import com.emop.wlt.user.management.model.request.Mapp02700101Req;
import com.emop.wlt.user.management.model.response.Mapp02700101Resp;
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
public class BankAccountSmsSendProviderImplTest {

    @InjectMocks
    private BankAccountSmsSendProviderImpl bankAccountSmsSendProvider;
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
            + "    \"getCtxInstContext\": true,\n"
            + "}";
        MockFlowContext.mockFlowContext(configParam);
    }

    @AfterClass
    public static void afterClass() {
        MockFlowContext.closeFlowContextMock();
    }

    @Test
    public void testBankAccountSendSms() {
        doNothing().when(smsProvider).getBankAccountVerifyCode(any());

        Mapp02700101Req mapp02700101Req = new Mapp02700101Req();
        mapp02700101Req.setMobileNumber("+853-60001234");
        mapp02700101Req.setBankCode("bankCode");
        mapp02700101Req.setAccountNumber("accountNumber");
        mapp02700101Req.setAccountType("accountType");
        FlowReqInfo flowReqInfo = new FlowReqInfo();
        flowReqInfo.setVerifySceneType("VST37");
        flowReqInfo.setFlowVersion("VST37_01");
        mapp02700101Req.setFlowReqInfo(flowReqInfo);
        RequestModel<Mapp02700101Req> request = RequestModelHelper.buildRequestModel(mapp02700101Req);
        ResponseModel<Mapp02700101Resp> result = bankAccountSmsSendProvider.bankAccountSendSms(request);
        Assert.assertNotNull(result);
    }
}
