package com.emop.wlt.user.query.mapi.impl;

import com.emop.gateway.dto.emap.EmapDTO;
import com.emop.gateway.dto.emap.ResponseModel;
import com.emop.infocache.UniCodeCache;
import com.emop.wlt.common.model.mapp.MessageHeader;
import com.emop.wlt.common.model.mapp.RequestModel;
import com.emop.wlt.gateway.manager.EmapGatewayManager;
import com.emop.wlt.model.emap.emap214.Emap21400101RespDTO;
import com.emop.wlt.model.emap.emap214.QRCodeInfoDTO;
import com.emop.wlt.redis.service.RedisOperator;
import com.emop.wlt.user.query.manager.CloudControlUserManager;
import com.emop.wlt.user.query.model.request.Mapp09500101Req;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PayProviderImplTest {

    @Mock
    private UniCodeCache uniCodeCache;
    @Mock
    private EmapGatewayManager emapGatewayManager;
    @Mock
    private CloudControlUserManager cloudControlUserManager;
    @Mock
    private RedisOperator redisOperator;
    @InjectMocks
    private PayProviderImpl payProviderImpl;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testQrCodeQuery() {
        Emap21400101RespDTO emap21400101RespDTO = new Emap21400101RespDTO();
        emap21400101RespDTO.setQrCodeInfo(new QRCodeInfoDTO());
        ResponseModel<EmapDTO> responseModel = new ResponseModel();
        responseModel.setMessageBody(emap21400101RespDTO);
        when(emapGatewayManager.send(any())).thenReturn(responseModel);

        Mapp09500101Req mapp09500101Req = new Mapp09500101Req();
        mapp09500101Req.setQrCode("000201qrCode");
        RequestModel<Mapp09500101Req> requestModel = new RequestModel<>();
        requestModel.setMessageBody(mapp09500101Req);
        requestModel.setMessageHeader(new MessageHeader());
        payProviderImpl.qrCodeQuery(requestModel);
    }

}
