package com.emop.wlt.user.management.provider;

import com.emop.wlt.user.service.impl.WalletUserIndexDBService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class WalletUserIndexManagementProviderImplTest {

    @InjectMocks
    private WalletUserIndexManagementProviderImpl walletUserIndexManagementProvider;
    @Mock
    private WalletUserIndexDBService walletUserIndexDBService;

    @Test
    public void testInsert() {
        when(walletUserIndexDBService.insert(any())).thenReturn(1);
        int result = walletUserIndexManagementProvider.insert("walletId", "userId");
        Assert.assertEquals(1, result);
    }

    @Test
    public void testRemove() {
        when(walletUserIndexDBService.removeByWalletIdAndUserId(anyString(), anyString())).thenReturn(1);
        int result = walletUserIndexManagementProvider.remove("walletId", "userId");
        Assert.assertEquals(1, result);
    }
}
