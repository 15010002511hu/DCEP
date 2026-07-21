package com.emop.wlt.user.query.provider;

import com.emop.wlt.user.query.service.WalletUserIndexQueryService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WalletUserIndexQueryProviderImplTest {

    @Mock
    private WalletUserIndexQueryService walletUserIndexQueryService;
    @InjectMocks
    private WalletUserIndexQueryProviderImpl walletUserIndexQueryProviderImpl;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testQueryUserIdByWalletId() {
        when(walletUserIndexQueryService.selectByWalletId(anyString())).thenReturn("selectByWalletIdResponse");

        walletUserIndexQueryProviderImpl.queryUserIdByWalletId("walletId");
    }

}
