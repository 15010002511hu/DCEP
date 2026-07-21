package com.emop.wlt.user.management.mapi.component.impl;

import com.emop.wlt.common.model.flow.FlowReqInfo;
import com.emop.wlt.common.model.mapp.RequestModel;
import com.emop.wlt.common.model.mapp.ResponseModel;
import com.emop.wlt.user.management.helper.MockFlowContext;
import com.emop.wlt.user.management.helper.RequestModelHelper;
import com.emop.wlt.user.management.model.request.Mapp02800101Req;
import com.emop.wlt.user.management.model.response.Mapp02800101Resp;
import com.emop.wlt.user.wallet.api.RealNameInfoProvider;
import com.emop.wlt.user.wallet.api.SmsProvider;
import com.emop.wlt.user.wallet.dto.Emap304RespParam;
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
public class BankAccountSmsCheckProviderImplTest {

    @InjectMocks
    private BankAccountSmsCheckProviderImpl bankAccountSmsCheckProvider;
    @Mock
    private SmsProvider smsProvider;
    @Mock
    private RealNameInfoProvider realNameInfoProvider;

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
            + "    \"saveRealNameInfo\": true\n"
            + "}";
        MockFlowContext.mockFlowContext(configParam);
    }

    @AfterClass
    public static void afterClass() {
        MockFlowContext.closeFlowContextMock();
    }

    @Test
    public void testBankAccountCheckSms1() {
        //正常
        when(smsProvider.checkBankAccountVerifyCode(any())).thenReturn(
            Emap304RespParam.builder().bankCode("bankCode").signingNumber("signingNumber").build());
        doNothing().when(realNameInfoProvider).saveRealNameInfo(anyString(), anyString());

        Mapp02800101Req mapp02800101Req = new Mapp02800101Req();
        mapp02800101Req.setVerifyCode("666888");
        FlowReqInfo flowReqInfo = new FlowReqInfo();
        flowReqInfo.setVerifySceneType("VST37");
        flowReqInfo.setFlowVersion("VST37_01");
        mapp02800101Req.setFlowReqInfo(flowReqInfo);
        RequestModel<Mapp02800101Req> request = RequestModelHelper.buildRequestModel(mapp02800101Req);
        ResponseModel<Mapp02800101Resp> result = bankAccountSmsCheckProvider.bankAccountCheckSms(request);
        Assert.assertEquals("bankCode", result.getMessageBody().getBankCode());
        Assert.assertEquals("signingNumber", result.getMessageBody().getSigningNumber());
        Assert.assertEquals("VST37", result.getMessageBody().getFlowRespInfo().getVerifySceneType());
        Assert.assertEquals("VST37_01", result.getMessageBody().getFlowRespInfo().getFlowVersion());
        verify(realNameInfoProvider, times(1)).saveRealNameInfo(anyString(), anyString());
    }

    @Test
    public void testBankAccountCheckSms2() {
        //升级成功后保存实名信息异常
        when(smsProvider.checkBankAccountVerifyCode(any())).thenReturn(
            Emap304RespParam.builder().bankCode("bankCode").signingNumber("signingNumber").build());
        doThrow(new RuntimeException()).when(realNameInfoProvider).saveRealNameInfo(anyString(), anyString());

        Mapp02800101Req mapp02800101Req = new Mapp02800101Req();
        mapp02800101Req.setVerifyCode("666888");
        FlowReqInfo flowReqInfo = new FlowReqInfo();
        flowReqInfo.setVerifySceneType("VST37");
        flowReqInfo.setFlowVersion("VST37_01");
        mapp02800101Req.setFlowReqInfo(flowReqInfo);
        RequestModel<Mapp02800101Req> request = RequestModelHelper.buildRequestModel(mapp02800101Req);
        ResponseModel<Mapp02800101Resp> result = bankAccountSmsCheckProvider.bankAccountCheckSms(request);
        Assert.assertEquals("bankCode", result.getMessageBody().getBankCode());
        Assert.assertEquals("signingNumber", result.getMessageBody().getSigningNumber());
        Assert.assertEquals("VST37", result.getMessageBody().getFlowRespInfo().getVerifySceneType());
        Assert.assertEquals("VST37_01", result.getMessageBody().getFlowRespInfo().getFlowVersion());
        verify(realNameInfoProvider, times(1)).saveRealNameInfo(anyString(), anyString());
    }
}
