package com.dcep.dips.wholesalepayment.dto.check;

import lombok.*;

/**
 * 资金调整产品链上对账BO
 */
@ToString
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FundAdjustInfDTO {
    /**
     *   调整通知标识号
     */
    private String msgId;

    /**
     *   报文编号dcep185
     */
    private String msgTp;

    /**
     *   资金调整参与机构
     */
    private String transPtyId;

    /**
     *   托管行参与机构
     */
    private String custodian;

    /**
     *   机构的结算钱包ID
     */
    private String walletId;

    /**
     *   资金调整金额，单元：原
     */
    private String amount;

    /**
     *   调整类型 "PRFD：注资 FFIC：预注资 TRBH：清零 FFRD：预注资调减 FDRD：调减"
     */
    private String operationType;

    /**
     *  业务状态，PR10-结算成功
     */
    private String prcSts;

    /**
     *   原大额发送行号
     */
    private String orgnlSendPty;

    /**
     *   原大额报文标识号
     */
    private String orgnlMsgId;

    /**
     *   原大额报文编号
     */
    private String orgnlMsgTp;
}
