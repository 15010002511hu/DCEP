package com.emop.wlt.user.management.mapi.component.impl;

import com.emop.wlt.captcha.provider.CaptchaOperator;
import com.emop.wlt.common.exception.EmwltBuzException;
import com.emop.wlt.common.model.mapp.RequestModel;
import com.emop.wlt.common.model.mapp.ResponseModel;
import com.emop.wlt.ifaa.manager.IFAAServiceLdcManager;
import com.emop.wlt.ifaa.vo.CheckIfaaResultDTO;
import com.emop.wlt.user.management.helper.MockFlowContext;
import com.emop.wlt.user.management.helper.RequestModelHelper;
import com.emop.wlt.user.management.model.request.Mapp02200101Req;
import com.emop.wlt.user.management.model.response.Mapp02200101Resp;
import com.emop.wlt.user.management.model.vo.BioAuthReqInfoVO;
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
public class SmsCheckProviderImplTest {

    @InjectMocks
    private SmsCheckProviderImpl smsCheckProvider;
    @Mock
    private CaptchaOperator captchaOperator;
    @Mock
    private IFAAServiceLdcManager IFAAServiceLdcManager;

    @BeforeClass
    public static void beforeClass() {
        MockFlowContext.buildFlowContextMock();
    }

    @Before
    public void before() {
        String configParam = ""
            + "{\n"
            + "    \"svcType\": \"ST02\",\n"
            + "    \"supportIfaa\": true\n"
            + "}";
        MockFlowContext.mockFlowContext(configParam);
    }

    @AfterClass
    public static void afterClass() {
        MockFlowContext.closeFlowContextMock();
    }

    @Test
    public void testCheckSms1() {
        //支持IFAA
        doNothing().when(captchaOperator).checkVerifyCode(any());
        CheckIfaaResultDTO checkIfaaResultDTO = new CheckIfaaResultDTO();
        checkIfaaResultDTO.setIfaaResult("supportIFAA");
        when(IFAAServiceLdcManager.sendIFAADubboRequest(any(), anyString())).thenReturn(checkIfaaResultDTO);

        Mapp02200101Req mapp02200101Req = new Mapp02200101Req();
        mapp02200101Req.setVerifyCode("666888");
        BioAuthReqInfoVO bioAuthReqInfoVO = new BioAuthReqInfoVO();
        bioAuthReqInfoVO.setAuthDeviceId("authDeviceId");
        bioAuthReqInfoVO.setAuthActionType("AU00");
        bioAuthReqInfoVO.setAuthReqMessage("{\"action\":\"request/register\"}");
        mapp02200101Req.setBioAuthReqInfo(bioAuthReqInfoVO);
        RequestModel<Mapp02200101Req> request = RequestModelHelper.buildRequestModel(mapp02200101Req);
        ResponseModel<Mapp02200101Resp> result = smsCheckProvider.checkSms(request);
        Assert.assertEquals("supportIFAA", result.getMessageBody().getAuthRespMessage());
    }

    @Test
    public void testCheckSms2() {
        //不支持IFAA
        doNothing().when(captchaOperator).checkVerifyCode(any());

        Mapp02200101Req mapp02200101Req = new Mapp02200101Req();
        mapp02200101Req.setVerifyCode("666888");
        RequestModel<Mapp02200101Req> request = RequestModelHelper.buildRequestModel(mapp02200101Req);
        ResponseModel<Mapp02200101Resp> result = smsCheckProvider.checkSms(request);
        Assert.assertNull(result.getMessageBody().getAuthRespMessage());
    }

    @Test
    public void testCheckSms3() {
        //更新IFAA失败 authActionType与authMsgReq中的业务类型不一致
        doNothing().when(captchaOperator).checkVerifyCode(any());

        Mapp02200101Req mapp02200101Req = new Mapp02200101Req();
        mapp02200101Req.setVerifyCode("666888");
        BioAuthReqInfoVO bioAuthReqInfoVO = new BioAuthReqInfoVO();
        bioAuthReqInfoVO.setAuthDeviceId("authDeviceId");
        bioAuthReqInfoVO.setAuthActionType("AU00");
        bioAuthReqInfoVO.setAuthReqMessage("{\"action\":\"request/auth\"}");
        mapp02200101Req.setBioAuthReqInfo(bioAuthReqInfoVO);
        RequestModel<Mapp02200101Req> request = RequestModelHelper.buildRequestModel(mapp02200101Req);
        ResponseModel<Mapp02200101Resp> result = smsCheckProvider.checkSms(request);
        Assert.assertNull(result.getMessageBody().getAuthRespMessage());
    }

    @Test
    public void testCheckSms4() {
        //异常-configParam为null
        MockFlowContext.mockFlowContext(null);

        Mapp02200101Req mapp02200101Req = new Mapp02200101Req();
        mapp02200101Req.setVerifyCode("666888");
        BioAuthReqInfoVO bioAuthReqInfoVO = new BioAuthReqInfoVO();
        bioAuthReqInfoVO.setAuthDeviceId("authDeviceId");
        bioAuthReqInfoVO.setAuthActionType("AU00");
        bioAuthReqInfoVO.setAuthReqMessage("{\"action\":\"request/register\"}");
        mapp02200101Req.setBioAuthReqInfo(bioAuthReqInfoVO);
        RequestModel<Mapp02200101Req> request = RequestModelHelper.buildRequestModel(mapp02200101Req);
        Assert.assertThrows("短信参数配置错误", EmwltBuzException.class,
            ()->smsCheckProvider.checkSms(request));
    }

    @Test
    public void testCheckSms5() {
        //异常-svcType为null
        String configParam = ""
            + "{\n"
            + "    \"svcType\": \"\",\n"
            + "    \"supportIfaa\": true\n"
            + "}";
        MockFlowContext.mockFlowContext(configParam);

        Mapp02200101Req mapp02200101Req = new Mapp02200101Req();
        mapp02200101Req.setVerifyCode("666888");
        BioAuthReqInfoVO bioAuthReqInfoVO = new BioAuthReqInfoVO();
        bioAuthReqInfoVO.setAuthDeviceId("authDeviceId");
        bioAuthReqInfoVO.setAuthActionType("AU00");
        bioAuthReqInfoVO.setAuthReqMessage("{\"action\":\"request/register\"}");
        mapp02200101Req.setBioAuthReqInfo(bioAuthReqInfoVO);
        RequestModel<Mapp02200101Req> request = RequestModelHelper.buildRequestModel(mapp02200101Req);
        Assert.assertThrows("短信参数配置错误", EmwltBuzException.class,
            ()->smsCheckProvider.checkSms(request));
    }

    @Test
    public void testCheckSms6() {
        //异常-supportIfaa为false
        String configParam = ""
            + "{\n"
            + "    \"svcType\": \"ST02\",\n"
            + "    \"supportIfaa\": false\n"
            + "}";
        MockFlowContext.mockFlowContext(configParam);

        doNothing().when(captchaOperator).checkVerifyCode(any());

        Mapp02200101Req mapp02200101Req = new Mapp02200101Req();
        mapp02200101Req.setVerifyCode("666888");
        BioAuthReqInfoVO bioAuthReqInfoVO = new BioAuthReqInfoVO();
        bioAuthReqInfoVO.setAuthDeviceId("authDeviceId");
        bioAuthReqInfoVO.setAuthActionType("AU00");
        bioAuthReqInfoVO.setAuthReqMessage("{\"action\":\"request/register\"}");
        mapp02200101Req.setBioAuthReqInfo(bioAuthReqInfoVO);
        RequestModel<Mapp02200101Req> request = RequestModelHelper.buildRequestModel(mapp02200101Req);
        ResponseModel<Mapp02200101Resp> result = smsCheckProvider.checkSms(request);
        Assert.assertNull(result.getMessageBody().getAuthRespMessage());
    }

    @Test
    public void testCheckSms7() {
        //异常-authReqMessage为null
        doNothing().when(captchaOperator).checkVerifyCode(any());

        Mapp02200101Req mapp02200101Req = new Mapp02200101Req();
        mapp02200101Req.setVerifyCode("666888");
        BioAuthReqInfoVO bioAuthReqInfoVO = new BioAuthReqInfoVO();
        bioAuthReqInfoVO.setAuthDeviceId("authDeviceId");
        bioAuthReqInfoVO.setAuthActionType("AU00");
        bioAuthReqInfoVO.setAuthReqMessage(null);
        mapp02200101Req.setBioAuthReqInfo(bioAuthReqInfoVO);
        RequestModel<Mapp02200101Req> request = RequestModelHelper.buildRequestModel(mapp02200101Req);
        ResponseModel<Mapp02200101Resp> result = smsCheckProvider.checkSms(request);
        Assert.assertNull(result.getMessageBody().getAuthRespMessage());
    }
}
