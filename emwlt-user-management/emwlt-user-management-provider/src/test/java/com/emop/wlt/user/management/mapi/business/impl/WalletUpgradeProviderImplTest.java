package com.emop.wlt.user.management.mapi.business.impl;

import com.emop.gateway.dto.emap.EmapDTO;
import com.emop.wlt.common.model.mapp.RequestModel;
import com.emop.wlt.common.model.mapp.ResponseModel;
import com.emop.wlt.gateway.manager.EmapGatewayManager;
import com.emop.wlt.user.management.helper.MockFlowContext;
import com.emop.wlt.user.management.helper.RequestModelHelper;
import com.emop.wlt.user.management.model.request.Mapp31900101Req;
import com.emop.wlt.user.management.model.response.Mapp31900101Resp;
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
public class WalletUpgradeProviderImplTest {

    @InjectMocks
    private WalletUpgradeProviderImpl walletUpgradeProvider;
    @Mock
    private EmapGatewayManager emapGatewayManager;

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
    public void testWalletUpgradePreCheck() {
        when(emapGatewayManager.send(any())).thenReturn(new com.emop.gateway.dto.emap.ResponseModel<EmapDTO>());

        Mapp31900101Req mapp31900101Req = new Mapp31900101Req();
        mapp31900101Req.setTargetLevel("WL04");
        RequestModel<Mapp31900101Req> request = RequestModelHelper.buildRequestModel(mapp31900101Req);
        ResponseModel<Mapp31900101Resp> result = walletUpgradeProvider.walletUpgradePreCheck(request);
        Assert.assertNotNull(result);
    }
}