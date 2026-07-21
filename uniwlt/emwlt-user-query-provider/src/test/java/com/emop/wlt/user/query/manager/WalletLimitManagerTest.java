package com.emop.wlt.user.query.manager;


import com.emop.infocache.WalletParamCache;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WalletLimitManagerTest {

    @InjectMocks
    private WalletLimitManager walletLimitManager;

    @Mock
    private WalletParamCache walletParamCache;

    @Before
    public void setUp() {
        String fiLimitJson = "[{\"instNo\":\"002000\",\"walletLevel\":\"WL04\",\"singleLimit\":\"1000.00\",\"dailyLimit\":\"10000.00\",\"annualLimit\":\"100000.00\"},{\"instNo\":\"002000\",\"walletLevel\":\"WL03\",\"singleLimit\":\"2000.00\",\"dailyLimit\":\"20000.00\",\"annualLimit\":\"200000.00\"}]";
        Mockito.when(walletParamCache.getFiLimitJson()).thenReturn(fiLimitJson);
    }

    @Test
    public void queryWalletLimit_1() {
        String actualResp = walletLimitManager.queryWalletLimit("002000", "WL04");
        String expectedResp = "[{\"annualLimit\":\"100000.00\",\"singleLimit\":\"1000.00\",\"instNo\":\"002000\",\"dailyLimit\":\"10000.00\",\"walletLevel\":\"WL04\"}]";
        Assert.assertEquals(expectedResp, actualResp);
    }

    @Test
    public void queryWalletLimit_2() {
        String actualResp = walletLimitManager.queryWalletLimit("002000", "WL03");
        String expectedResp = "[{\"annualLimit\":\"200000.00\",\"singleLimit\":\"2000.00\",\"instNo\":\"002000\",\"dailyLimit\":\"20000.00\",\"walletLevel\":\"WL03\"}]";
        Assert.assertEquals(expectedResp, actualResp);
    }
}