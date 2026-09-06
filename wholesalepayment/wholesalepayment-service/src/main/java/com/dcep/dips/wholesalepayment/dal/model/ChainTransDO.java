package com.dcep.dips.wholesalepayment.dal.model;

import com.dcep.dips.wholesalepayment.dto.ClearingDTO;
import com.dcep.dips.wholesalepayment.dto.mcbs.MbridgeReqDTO;
import lombok.*;

import java.util.Date;

@ToString
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChainTransDO {
    /**
     *   外部报文流水号
     */
    private String outMsgId;

    /**
     *   报文标识号
     */
    private String msgId;

    /**
     *   报文发送时间
     */
    private String sndDt;

    /**
     *   外部报文批次号
     */
    private String outBatId;

    /**
     *   外部报文编号
     */
    private String outMsgTp;

    /**
     *   收发方向
     */
    private String msgDrn;

    /**
     *   交易类型
     */
    private String clrTyp;

    /**
     *   发送机构编码
     */
    private String senderPtyId;

    /**
     *   发送机构LEI码
     */
    private String senderLei;

    /**
     *   接收机构编码
     */
    private String receiverPtyId;

    /**
     *   接收机构LEI码
     */
    private String receiverLei;

    /**
     *   付款机构编码
     */
    private String dbtrPtyId;

    /**
     *   付款机构LEI码
     */
    private String dbtrPtyLei;

    /**
     *   付款机构系统标识
     */
    private String dbtrSysId;

    /**
     *   收款机构编码
     */
    private String cdtrPtyId;

    /**
     *   收款机构LEI码
     */
    private String cdtrPtyLei;

    /**
     *   收款机构系统标识
     */
    private String cdtrSysId;

    /**
     *   付款钱包ID
     */
    private String dbtrWltId;

    /**
     *   收款钱包ID
     */
    private String cdtrWltId;

    /**
     *   发行与注销编码
     */
    private String parameterId;

    /**
     *   交易状态
     */
    private String prcSts;

    /**
     *   登记时间戳
     */
    private Date gmtCreate;

    /**
     *   更新时间戳
     */
    private Date gmtModified;

    public ChainTransDO(ClearingDTO clearingDTO, String prcSts) {
        MbridgeReqDTO mbridgeReqDTO = clearingDTO.clrMbridgeInf();
        this.outMsgId = mbridgeReqDTO.getMcbsMsgId();
        this.msgId = clearingDTO.getClrMsgId();
        this.sndDt = mbridgeReqDTO.getSndDtTm();
        this.outBatId = mbridgeReqDTO.getMcbsBatId();
        this.outMsgTp = mbridgeReqDTO.getMcbsMsgTp();
        this.msgDrn = mbridgeReqDTO.getMsgDrn();
        this.clrTyp = mbridgeReqDTO.getClrTp();
        this.senderPtyId = mbridgeReqDTO.getSenderPtyId();
        this.senderLei = mbridgeReqDTO.getSenderLEI();
        this.receiverPtyId = mbridgeReqDTO.getReceiverPtyId();
        this.receiverLei = mbridgeReqDTO.getReceiverLEI();
        this.dbtrPtyId = mbridgeReqDTO.getDbtrPtyId();
        this.dbtrPtyLei = mbridgeReqDTO.getDbtrPtyLEI();
        this.dbtrSysId = "";// todo
        this.cdtrPtyId = mbridgeReqDTO.getCdtrPtyId();
        this.cdtrPtyLei = mbridgeReqDTO.getCdtrPtyLEI();
        this.cdtrSysId = "";// todo
        this.dbtrWltId = mbridgeReqDTO.getDbtrWltId();
        this.cdtrWltId = mbridgeReqDTO.getCdtrWltId();
        this.parameterId = mbridgeReqDTO.getParameterId();
        this.prcSts = prcSts;
        Date date = new Date();
        this.gmtCreate = date;
        this.gmtModified = date;
    }
}
