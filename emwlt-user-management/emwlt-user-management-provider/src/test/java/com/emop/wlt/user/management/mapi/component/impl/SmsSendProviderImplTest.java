package com.emop.wlt.user.management.mapi.component.impl;

import com.emop.wlt.captcha.provider.CaptchaOperator;
import com.emop.wlt.common.model.mapp.RequestModel;
import com.emop.wlt.common.model.mapp.ResponseModel;
import com.emop.wlt.user.management.exception.UserManageException;
import com.emop.wlt.user.management.helper.MockFlowContext;
import com.emop.wlt.user.management.helper.MockMobileRpcHolder;
import com.emop.wlt.user.management.helper.RequestModelHelper;
import com.emop.wlt.user.management.model.request.Mapp02100101Req;
import com.emop.wlt.user.management.model.response.Mapp02100101Resp;
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
public class SmsSendProviderImplTest {

    @InjectMocks
    private SmsSendProviderImpl smsSendProvider;
    @Mock
    private CaptchaOperator captchaOperator;

    @BeforeClass
    public static void beforeClass() {
        MockFlowContext.buildFlowContextMock();
        MockMobileRpcHolder.buildMock();
    }

    @Before
    public void before() {
        String configParam = ""
            + "{\n"
            + "    \"svcType\": \"ST02\",\n"
            + "    \"getCtxMobileNumber\": true\n"
            + "}";
        MockFlowContext.mockFlowContext(configParam);
        MockMobileRpcHolder.mockMobileRpcHolder();
    }

    @AfterClass
    public static void afterClass() {
        MockFlowContext.closeFlowContextMock();
        MockMobileRpcHolder.closeMobileRpcHolderMock();
    }

    @Test
    public void testSendSms1() {
        when(captchaOperator.sendVerifyCode(any())).thenReturn("666888");

        RequestModel<Mapp02100101Req> request = RequestModelHelper.buildRequestModel(new Mapp02100101Req());
        ResponseModel<Mapp02100101Resp> result = smsSendProvider.sendSms(request);
        Assert.assertNotNull(result);
    }

    @Test
    public void testSendSms2() {
        //异常-configParam为null
        MockFlowContext.mockFlowContext(null);

        Mapp02100101Req mapp02100101Req = new Mapp02100101Req();
        mapp02100101Req.setMobileNumber("+853-60001234");
        RequestModel<Mapp02100101Req> request = RequestModelHelper.buildRequestModel(mapp02100101Req);
        Assert.assertThrows("短信参数配置错误", UserManageException.class,
            ()->smsSendProvider.sendSms(request));
    }

    @Test
    public void testSendSms3() {
        //异常-svcType为null
        String configParam = ""
            + "{\n"
            + "    \"svcType\": \"\",\n"
            + "    \"getCtxMobileNumber\": true\n"
            + "}";
        MockFlowContext.mockFlowContext(configParam);

        RequestModel<Mapp02100101Req> request = RequestModelHelper.buildRequestModel(new Mapp02100101Req());
        Assert.assertThrows("短信参数配置错误", UserManageException.class,
            ()->smsSendProvider.sendSms(request));
    }
}
