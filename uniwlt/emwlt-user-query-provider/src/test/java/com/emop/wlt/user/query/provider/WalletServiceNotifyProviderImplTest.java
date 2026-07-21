package com.emop.wlt.user.query.provider;

import com.emop.gateway.dto.emap.EmapDTO;
import com.emop.gateway.dto.emap.RequestModel;
import com.emop.wlt.model.emap.emap602.Emap60200101ReqDTO;
import com.emop.wlt.user.query.service.WalletUserIndexQueryService;
import com.emop.wlt.user.wallet.api.UserWalletManagerProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WalletServiceNotifyProviderImplTest {

    @Mock
    private WalletUserIndexQueryService walletUserIndexQueryService;
    @Mock
    private UserWalletManagerProvider userWalletManagerProvider;
    @InjectMocks
    private WalletServiceNotifyProviderImpl walletServiceNotifyProviderImpl;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testExecute() {
        when(walletUserIndexQueryService.selectByWalletId("walletId")).thenReturn("userId");

        Emap60200101ReqDTO emap60200101ReqDTO = new Emap60200101ReqDTO();
        emap60200101ReqDTO.setWalletServiceNotifyScene("WSNSO1");
        emap60200101ReqDTO.setWalletId("walletId");
        RequestModel<EmapDTO> requestModel = new RequestModel<>();
        requestModel.setMessageBody(emap60200101ReqDTO);
        walletServiceNotifyProviderImpl.execute(requestModel);
    }

}
