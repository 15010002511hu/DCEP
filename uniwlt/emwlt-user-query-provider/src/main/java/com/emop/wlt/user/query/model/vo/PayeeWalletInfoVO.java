package com.emop.wlt.user.query.model.vo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PayeeWalletInfoVO {

    /**
     * 收款人英文名称
     * 当“收款人钱包"为实名钱包时返回，需要做掩码
     */
    private String payeeName;

    /**
     * 收款钱包ID，QDM01：快捷模式时必返
     */
    private String payeeWalletId;

    /**
     * 收款钱包类型，QDM01：快捷模式时必返
     */
    private String payeeWalletType;

    /**
     * 收款钱包等级，QDM01：快捷模式时必返
     */
    private String payeeWalletLevel;

    /**
     * 扫码Id，标识本次扫码转钱; QrType为QT01 时必传
     */
    private String scanId;

    /**
     * 钱包国家地区代码
     */
    private String countryCode;

}
