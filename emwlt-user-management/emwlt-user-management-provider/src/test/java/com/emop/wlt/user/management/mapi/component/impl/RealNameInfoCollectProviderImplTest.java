package com.emop.wlt.user.management.mapi.component.impl;

import com.emop.wlt.common.model.flow.FlowReqInfo;
import com.emop.wlt.common.model.mapp.RequestModel;
import com.emop.wlt.common.model.mapp.ResponseModel;
import com.emop.wlt.model.emap.dto.ChineseNameInfo;
import com.emop.wlt.model.emap.dto.EnglishNameInfo;
import com.emop.wlt.user.management.helper.MockFlowContext;
import com.emop.wlt.user.management.helper.RequestModelHelper;
import com.emop.wlt.user.management.model.request.Mapp02500101Req;
import com.emop.wlt.user.management.model.response.Mapp02500101Resp;
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
public class RealNameInfoCollectProviderImplTest {

    @InjectMocks
    private RealNameInfoCollectProviderImpl realNameInfoCollectProvider;
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
    public void testRealNameInfoCollect() {
        when(realNameInfoProvider.realNameInfoVerify(any())).thenReturn("instContextNo");

        FlowReqInfo flowReqInfo = new FlowReqInfo();
        flowReqInfo.setVerifySceneType("VST");
        Mapp02500101Req mapp02500101Req = new Mapp02500101Req();
        mapp02500101Req.setIdType("idType");
        mapp02500101Req.setIdNumber("idNumber");
        mapp02500101Req.setChineseNameInfo(new ChineseNameInfo());
        mapp02500101Req.setEnglishNameInfo(new EnglishNameInfo());
        mapp02500101Req.setBirthDate("1990-08-15");
        mapp02500101Req.setIdIssueDate("2010-01-01");
        mapp02500101Req.setIdExpireDate("2030-12-31");
        mapp02500101Req.setFlowReqInfo(flowReqInfo);
        RequestModel<Mapp02500101Req> request = RequestModelHelper.buildRequestModel(mapp02500101Req);
        ResponseModel<Mapp02500101Resp> result = realNameInfoCollectProvider.realNameInfoCollect(request);
        Assert.assertNotNull(result);
    }
}
