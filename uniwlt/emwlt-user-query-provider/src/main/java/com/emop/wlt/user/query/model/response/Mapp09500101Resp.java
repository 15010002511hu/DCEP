package com.emop.wlt.user.query.model.response;


import com.emop.wlt.user.query.model.vo.MerchantInfoVO;
import com.emop.wlt.user.query.model.vo.PayeeWalletInfoVO;
import lombok.Data;

/**
 * 一码通 二维码查询相应
 *
 * @author hanyabo
 * @date 2022/12/26 2:10 PM
 */
@Data
public class Mapp09500101Resp {

    /**
     * 二维码类型： QT01：个人收款码 QT02：个人付款码 QT03： 商户静态码 QT04：商户动态码
     */
    private String qrType;

    /**
     * 二维码码制类型 QET01：数字人民币收款码 QET02：银联标准收款码 QET03：聚合收款码 QET04：微信收款码 QET05：支付宝收款码
     */
    private String qrCodeEncodingType;

    /**
     * 解码模式 QDM01：快捷模式 QDM02：网关模式-查询跳转URL QDM03：网关模式-访问URL
     */
    private String qrCodeDecodingMode;

    /**
     * 收单跳转地址
     * 当qrCodeDecodingMode为QDM02和QDM03时，必返；当为QDM03时，直接返回原二维码。
     */
    private String acquiringRedirectUrl;

    /**
     * 获取APP用户信息的临时授权码，10分钟有效，并且只能访问一次。QDM02必返
     */
    private String userAuthCode;

    /**
     * 二维码交易金额
     * 如收款方设置了金额，付款方不可修改；如收款方未设置金额，付款方需输入金额。
     */
    private String transactionAmount;

    /**
     * 原二维码数据
     */
    private String qrCode;

    /**
     * 收款钱包信息，个人收款码时必返
     */
    private PayeeWalletInfoVO payeeWalletInfo;

    /**
     * 商户信息
     */
    private MerchantInfoVO merchantInfo;

}
