package com.emop.wlt.user.management.mapi.component.impl;

import com.emop.wlt.common.model.flow.FlowReqInfo;
import com.emop.wlt.common.model.mapp.RequestModel;
import com.emop.wlt.common.model.mapp.ResponseModel;
import com.emop.wlt.user.management.helper.MockFlowContext;
import com.emop.wlt.user.management.helper.RequestModelHelper;
import com.emop.wlt.user.management.model.request.Mapp02900101Req;
import com.emop.wlt.user.management.model.response.Mapp02900101Resp;
import com.emop.wlt.user.wallet.api.PaymentPwdProvider;
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
public class PaymentPwdVerifyProviderImplTest {

    @InjectMocks
    private PaymentPwdVerifyProviderImpl paymentPwdVerifyProvider;
    @Mock
    private PaymentPwdProvider paymentPwdProvider;

    @BeforeClass
    public static void beforeClass() {
        MockFlowContext.buildFlowContextMock();
    }

    @Before
    public void before() {
        String configParam = "{\"getCtxWltId\":true}";
        MockFlowContext.mockFlowContext(configParam);
    }

    @AfterClass
    public static void afterClass() {
        MockFlowContext.closeFlowContextMock();
    }

    @Test
    public void testPaymentPasswordVerify() {
        when(paymentPwdProvider.paymentPwdInstVerify(any())).thenReturn("instContextNo");

        FlowReqInfo flowReqInfo = new FlowReqInfo();
        flowReqInfo.setVerifySceneType("VST");
        Mapp02900101Req mapp02900101Req = new Mapp02900101Req();
        mapp02900101Req.setPwdEnc("paymentPassword");
        mapp02900101Req.setFlowReqInfo(flowReqInfo);
        RequestModel<Mapp02900101Req> request = RequestModelHelper.buildRequestModel(mapp02900101Req);
        ResponseModel<Mapp02900101Resp> result = paymentPwdVerifyProvider.paymentPasswordVerify(request);
        Assert.assertNotNull(result);
    }
}
