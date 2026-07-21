package com.emop.wlt.user.management.mapi.component.impl;

import com.emop.wlt.common.model.flow.FlowReqInfo;
import com.emop.wlt.common.model.mapp.RequestModel;
import com.emop.wlt.common.model.mapp.ResponseModel;
import com.emop.wlt.model.emap.dto.ChineseNameInfo;
import com.emop.wlt.model.emap.dto.EnglishNameInfo;
import com.emop.wlt.user.management.helper.MockFlowContext;
import com.emop.wlt.user.management.helper.RequestModelHelper;
import com.emop.wlt.user.management.model.request.Mapp02600101Req;
import com.emop.wlt.user.management.model.response.Mapp02600101Resp;
import com.emop.wlt.user.wallet.api.RealNameInfoProvider;
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
public class RealNameInfoValidateProviderImplTest {

    @InjectMocks
    private RealNameInfoValidateProviderImpl realNameInfoValidateProvider;
    @Mock
    private RealNameInfoProvider realNameInfoProvider;

    @BeforeClass
    public static void beforeClass() {
        MockFlowContext.buildFlowContextMock();
    }

    @Before
    public void before() {
        MockFlowContext.mockFlowContext(null);
    }

    @AfterClass
    public static void afterClass() {
        MockFlowContext.closeFlowContextMock();
    }

    @Test
    public void testRealNameInfoValidate() {
        when(realNameInfoProvider.realNameInfoVerify(any())).thenReturn("instContextNo");

        FlowReqInfo flowReqInfo = new FlowReqInfo();
        flowReqInfo.setVerifySceneType("VST");
        Mapp02600101Req mapp02600101Req = new Mapp02600101Req();
        mapp02600101Req.setIdType("idType");
        mapp02600101Req.setIdNumber("idNumber");
        mapp02600101Req.setChineseNameInfo(new ChineseNameInfo());
        mapp02600101Req.setEnglishNameInfo(new EnglishNameInfo());
        mapp02600101Req.setFlowReqInfo(flowReqInfo);
        RequestModel<Mapp02600101Req> request = RequestModelHelper.buildRequestModel(mapp02600101Req);
        ResponseModel<Mapp02600101Resp> result = realNameInfoValidateProvider.realNameInfoValidate(request);
        Assert.assertNotNull(result);
    }
}
