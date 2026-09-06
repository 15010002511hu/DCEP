package com.dcep.dips.wholesalepayment.dal.model;

import com.dcep.dips.wholesalepayment.dal.bo.OnChainTransInfoBO;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author wangxiaoyu
 */
@ToString
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OnChainPaymentTransDO implements Serializable {

    private static final long serialVersionUID = -7089216725799137376L;
    /**
     *   报文标识号
     */
    private String msgId;

    /**
     *   报文编号
     */
    private String msgTp;

    /**
     *   交易时间
     */
    private String bizDt;

    /**
     *   交易批次号
     */
    private String batId;

    /**
     *   渠道系统 "BCSP：区块链服务平台 MCBS：货币桥 GCSC：JISR"
     */
    private String chnlSys;

    /**
     *   交易金额
     */
    private BigDecimal transAmt;

    /**
     *   业务类型
     */
    private String bizTp;

    /**
     *   交易状态 "PR00: 成功"
     */
    private String transSts;

    /**
     *   链上付款机构编码 货币桥机构编码
     */
    private String chainDbtrPtyId;

    /**
     *   付款机构编码 桥下机构编码
     */
    private String dbtrPtyId;

    /**
     *   付款机构系统标识
     */
    private String dbtrSysId;

    /**
     *   付款机构LEI码
     */
    private String dbtrPtyLei;

    /**
     *   付款钱包ID
     */
    private String dbtrWltId;

    /**
     *   链上收款机构编码 货币桥机构编码
     */
    private String chainCdtrPtyId;

    /**
     *   收款机构编码 桥下机构编码
     */
    private String cdtrPtyId;

    /**
     *   收款机构系统标识
     */
    private String cdtrSysId;

    /**
     *   收款机构LEI码
     */
    private String cdtrPtyLei;

    /**
     *   收款钱包ID
     */
    private String cdtrWltId;

    /**
     *   登记时间戳
     */
    private Date gmtCreate;

    /**
     *   更新时间戳
     */
    private Date gmtModified;

    public OnChainPaymentTransDO(OnChainTransInfoBO onChainTransInfoBO) {
        this.msgId = onChainTransInfoBO.getMsgId();
        this.msgTp = onChainTransInfoBO.getMsgTp();
        this.bizDt = onChainTransInfoBO.getBizDt();
        this.batId = onChainTransInfoBO.getBatId();
        this.chnlSys = onChainTransInfoBO.getChnlSys();
        this.transAmt = onChainTransInfoBO.getTransAmt();
        this.bizTp = onChainTransInfoBO.getBizTp();
        this.transSts = onChainTransInfoBO.getTransSts();
        this.chainDbtrPtyId = onChainTransInfoBO.getChainDbtrPtyId(); // todo
        this.dbtrPtyId = onChainTransInfoBO.getDbtrPtyId();
        this.dbtrSysId = onChainTransInfoBO.getDbtrSysId();
        this.dbtrPtyLei = onChainTransInfoBO.getDbtrPtyLei();
        this.dbtrWltId = onChainTransInfoBO.getDbtrWltId();
        this.chainCdtrPtyId = onChainTransInfoBO.getChainCdtrPtyId(); // todo
        this.cdtrPtyId = onChainTransInfoBO.getCdtrPtyId();
        this.cdtrSysId = onChainTransInfoBO.getCdtrSysId();
        this.cdtrPtyLei = onChainTransInfoBO.getCdtrPtyLei();
        this.cdtrWltId = onChainTransInfoBO.getCdtrWltId();
        this.gmtCreate = new Date();
        this.gmtModified = new Date();
    }
}
