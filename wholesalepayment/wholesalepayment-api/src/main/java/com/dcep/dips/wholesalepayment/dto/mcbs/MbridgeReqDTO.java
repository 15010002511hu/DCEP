package com.dcep.dips.wholesalepayment.dto.mcbs;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MbridgeReqDTO implements Serializable {

    private static final long serialVersionUID = 3664439073340945444L;

    /**
     * 货币桥报文流水号
     * 货币桥MsgId
     */
    private String mcbsMsgId;
    
    /**
     * 货币桥报文体里的报文发送时间
     * CreDtTm
     */
    private String sndDtTm;
    
    /**
     * 货币桥批次号
     * SplntryData/BatchNo
     */
    private String mcbsBatId;
    
    /**
     * 货币桥报文编号
     * 报文头MsgTp
     */
    private String mcbsMsgTp;
    
    /**
     * 货币桥收发方向
     * 桥上发起请求/应答都需返回，建议取报文头Header/MsgDrn
     */
    private String msgDrn;
    
    /**
     * 货币桥交易类型
     * CtgyPurp/Cd
     */
    private String clrTp;
    
    /**
     * 发起机构编码
     * 货币桥InstgAgt/MmbId
     */
    private String senderPtyId;
    
    /**
     * 发起机构LEI编码
     * 货币桥InstgAgt/LEI
     */
    private String senderLEI;
    
    /**
     * 接收机构编码
     * 货币桥InstdAgt/MmbId
     */
    private String receiverPtyId;
    
    /**
     * 接收机构LEI编码
     * 货币桥InstdAgt/LEI
     */
    private String receiverLEI;
    
    /**
     * 付款机构编码
     * 货币桥Dbtr/MmbId
     */
    private String dbtrPtyId;
    
    /**
     * 付款机构LEI编码
     * 货币桥Dbtr/LEI
     */
    private String dbtrPtyLEI;
    
    /**
     * 收款机构编码
     * 货币桥Cdtr/MmbId
     */
    private String cdtrPtyId;
    
    /**
     * 收款机构LEI编码
     * 货币桥Cdtr/LEI
     */
    private String cdtrPtyLEI;
    
    /**
     * 付款钱包ID
     * DbtrAcct/Id
     */
    private String dbtrWltId;
    
    /**
     * 收款钱包ID
     * CdtrAcct/Id
     */
    private String cdtrWltId;
    
    /**
     * 发行注销编码
     * SplntryData/ParameterId
     */
    private String parameterId;
    
    /**
     * 原货币桥报文流水号
     * 203应答报文赋值: OrgnlMsgId
     */
    private String mcbsOrgMsgId;
    
    /**
     * 交易状态
     * 203应答报文赋值: TxInfAndSts/StsId
     */
    private String prcSts;
    
    /**
     * 分片标识号
     * 桥下发起：dcep报文msgId
     * 桥上发起：mcbs报文msgId
     */
    private String shardingMsgId;
    
    /**
     * 发起渠道
     */
    private String sndChnlSys;
    
    /**
     * 接收渠道
     */
    private String rcvChnlSys;

}
