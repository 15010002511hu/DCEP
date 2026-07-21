package com.emop.wlt.user.management.assemble;

import com.emop.wlt.user.management.model.vo.CustomerIdInfoVO;
import com.emop.wlt.user.management.model.vo.WalletBasicInfoVO;
import com.emop.wlt.user.wallet.dto.CustomerIdInfoDTO;
import com.emop.wlt.user.wallet.dto.WalletBasicInfoDTO;

import java.util.Objects;

public class WalletInfoAssembleFactory {

    public static WalletBasicInfoVO assemble(WalletBasicInfoDTO walletBasicInfoDTO) {
        if (Objects.isNull(walletBasicInfoDTO)) {
            return null;
        }
        return WalletBasicInfoVO.builder()
                .walletId(walletBasicInfoDTO.getWalletId())
                .walletType(walletBasicInfoDTO.getWalletType())
                .walletLevel(walletBasicInfoDTO.getWalletLevel())
                .build();
    }

    public static CustomerIdInfoVO assemble(CustomerIdInfoDTO customerIdInfoDTO) {
        if (Objects.isNull(customerIdInfoDTO)) {
            return null;
        }
        return CustomerIdInfoVO.builder()
                .idNumber(customerIdInfoDTO.getIdNumber())
                .idType(customerIdInfoDTO.getIdType())
                .build();
    }

}
