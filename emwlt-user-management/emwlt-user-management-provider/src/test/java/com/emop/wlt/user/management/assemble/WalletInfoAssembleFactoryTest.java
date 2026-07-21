package com.emop.wlt.user.management.assemble;

import com.emop.wlt.user.management.model.vo.CustomerIdInfoVO;
import com.emop.wlt.user.management.model.vo.WalletBasicInfoVO;
import com.emop.wlt.user.wallet.dto.CustomerIdInfoDTO;
import com.emop.wlt.user.wallet.dto.WalletBasicInfoDTO;
import org.junit.Assert;
import org.junit.Test;

public class WalletInfoAssembleFactoryTest {

    @Test
    public void testAssemble1() {
        WalletBasicInfoDTO walletBasicInfoDTO = new WalletBasicInfoDTO();
        walletBasicInfoDTO.setWalletId("0021234567890123");
        walletBasicInfoDTO.setWalletType("WT01");
        walletBasicInfoDTO.setWalletLevel("WL03");

        WalletBasicInfoVO actualResp = WalletInfoAssembleFactory.assemble(walletBasicInfoDTO);
        Assert.assertNotNull(actualResp);
        Assert.assertEquals("0021234567890123", actualResp.getWalletId());
        Assert.assertEquals("WT01", actualResp.getWalletType());
        Assert.assertEquals("WL03", actualResp.getWalletLevel());
    }

    @Test
    public void testAssemble2() {
        //异常 - walletBasicInfoDTO为null
        WalletBasicInfoVO actualResp = WalletInfoAssembleFactory.assemble((WalletBasicInfoDTO) null);
        Assert.assertNull(actualResp);
    }

    @Test
    public void testAssemble3() {
        CustomerIdInfoDTO customerIdInfoDTO = new CustomerIdInfoDTO();
        customerIdInfoDTO.setIdType("IT01");
        customerIdInfoDTO.setIdNumber("idNumber");

        CustomerIdInfoVO actualResp = WalletInfoAssembleFactory.assemble(customerIdInfoDTO);
        Assert.assertEquals("IT01", actualResp.getIdType());
        Assert.assertEquals("idNumber", actualResp.getIdNumber());
    }

    @Test
    public void testAssemble4() {
        //异常 - customerIdInfoDTO为null
        CustomerIdInfoVO actualResp = WalletInfoAssembleFactory.assemble((CustomerIdInfoDTO) null);
        Assert.assertNull(actualResp);
    }
}
