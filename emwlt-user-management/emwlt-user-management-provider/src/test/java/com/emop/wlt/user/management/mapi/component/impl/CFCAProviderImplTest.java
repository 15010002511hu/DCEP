package com.emop.wlt.user.management.mapi.component.impl;

import com.emop.wlt.common.model.mapp.RequestModel;
import com.emop.wlt.common.model.mapp.ResponseModel;
import com.emop.wlt.encrypt.operator.SecurityOperator;
import com.emop.wlt.user.management.config.CFCAConfig;
import com.emop.wlt.user.management.manager.CFCARedisManager;
import com.emop.wlt.user.management.model.response.Mapp03100101Resp;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CFCAProviderImplTest {

    @InjectMocks
    private CFCAProviderImpl cfcaProvider;
    @Mock
    private SecurityOperator securityOperator;
    @Mock
    private CFCARedisManager cfcaRedisManager;
    @Mock
    private CFCAConfig cfcaConfig;

    @Test
    public void testCreateCFCARs() {
        when(securityOperator.generateRandom(anyInt())).thenReturn(new byte[]{(byte) 0});
        when(cfcaRedisManager.saveServerRandom(any())).thenReturn("serverRandom");
        when(cfcaConfig.getDefaultPubKey()).thenReturn("defaultPubKey");

        ResponseModel<Mapp03100101Resp> result = cfcaProvider.createCFCARs(new RequestModel());
        Assert.assertEquals("defaultPubKey", result.getMessageBody().getPwdPublicKey());
        Assert.assertEquals("serverRandom", result.getMessageBody().getServerRandom());
    }
}
