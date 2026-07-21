package com.emop.wlt.user.management.manager;

import com.emop.wlt.user.management.model.UserWalletInfoDTO;
import com.emop.wlt.user.wallet.api.UserWalletQueryProvider;
import com.emop.wlt.user.wallet.dto.CustomerIdInfoDTO;
import com.emop.wlt.user.wallet.dto.WalletBasicInfoDTO;
import com.emop.wlt.user.wallet.dto.WalletInfoDTO;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ResetLoginPwdManagerTest {

    @InjectMocks
    private ResetLoginPwdManager resetLoginPwdManager;
    @Mock
    UserWalletQueryProvider userWalletQueryProvider;

    @Test
    public void testGetUserHighestWalletInfo1() {
        //只有匿名钱包
        WalletBasicInfoDTO walletBasicInfo = new WalletBasicInfoDTO();
        walletBasicInfo.setWalletId("1122334455667788");
        walletBasicInfo.setWalletLevel("WL04");
        WalletInfoDTO walletInfoDTO = new WalletInfoDTO();
        walletInfoDTO.setWalletBasicInfo(walletBasicInfo);
        walletInfoDTO.setCustomerIdInfo(null);
        List<WalletInfoDTO> walletInfoDTOList = new ArrayList<>();
        walletInfoDTOList.add(walletInfoDTO);
        when(userWalletQueryProvider.queryUserWalletFromInst(anyString())).thenReturn(walletInfoDTOList);

        UserWalletInfoDTO result = resetLoginPwdManager.getUserHighestWalletInfo("userId");
        Assert.assertEquals("1", result.getWalletTotalStatus());
        Assert.assertEquals("1122334455667788", result.getWalletId());
    }

    @Test
    public void testGetUserHighestWalletInfo2() {
        //有实名钱包
        WalletBasicInfoDTO walletBasicInfo = new WalletBasicInfoDTO();
        walletBasicInfo.setWalletId("1122334455667788");
        walletBasicInfo.setWalletLevel("WL03");
        CustomerIdInfoDTO customerIdInfoDTO = new CustomerIdInfoDTO();
        customerIdInfoDTO.setIdNumber("idNumber");
        customerIdInfoDTO.setIdType("IT01");
        WalletInfoDTO walletInfoDTO = new WalletInfoDTO();
        walletInfoDTO.setWalletBasicInfo(walletBasicInfo);
        walletInfoDTO.setCustomerIdInfo(customerIdInfoDTO);
        List<WalletInfoDTO> walletInfoDTOList = new ArrayList<>();
        walletInfoDTOList.add(walletInfoDTO);
        when(userWalletQueryProvider.queryUserWalletFromInst(anyString())).thenReturn(walletInfoDTOList);

        UserWalletInfoDTO result = resetLoginPwdManager.getUserHighestWalletInfo("userId");
        Assert.assertEquals("2", result.getWalletTotalStatus());
        Assert.assertEquals("1122334455667788", result.getWalletId());
        Assert.assertEquals("idNumber", result.getIdNumber());
        Assert.assertEquals("IT01", result.getIdType());
    }

    @Test
    public void testGetUserHighestWalletInfo3() {
        //无钱包
        when(userWalletQueryProvider.queryUserWalletFromInst(anyString())).thenReturn(List.of());

        UserWalletInfoDTO result = resetLoginPwdManager.getUserHighestWalletInfo("userId");
        Assert.assertEquals("0", result.getWalletTotalStatus());
    }
}
