/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto.dc282;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dcep.common.annotation.Gateway;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.Response;
import com.dcep.common.model.soap.GrpHdr;
import com.dcep.common.model.soap.OrgnlGrpHdr;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.utils.CheckUtils;
import com.dcep.dips.wholesalepayment.annotation.Record;
import com.dcep.dips.wholesalepayment.dto.ClearingDTO;
import com.dcep.dips.wholesalepayment.dto.ClearingStatus;
import com.dcep.dips.wholesalepayment.dto.PrmtInf;
import com.dcep.dips.wholesalepayment.dto.RecordDTO;
import com.dcep.dips.wholesalepayment.enums.*;
import com.dcep.infocache.validation.CheckClrGrpHdr;
import com.dcep.infocache.validation.CheckClrOrgnlGrpHdr;
import com.dcep.infocache.validation.CheckGrpHdrMsgId;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@JacksonXmlRootElement(localName = "ConRefRsp",namespace = "http://www.dcep.com/dcep/28201001/")
@Setter
@Getter
@ToString
@Gateway(msgTp = "dcep.282.010.01", isReturn = true)
@Record(saveMode = RecordSaveModeEnum.ALL)
public class Dcep28201001DTO extends GwDTO implements ClearingDTO {

    /**  */
    private static final long serialVersionUID = 4384316990011927276L;

    /**
     * 【业务头组件】
     */
    @JacksonXmlProperty(localName = "GrpHdr")
    @NotNull
    @Valid
    @CheckGrpHdrMsgId
    @CheckClrGrpHdr
    private GrpHdr            grpHdr;

    /**
     * 【原业务头组件】
     */
    @JacksonXmlProperty(localName = "OrgnlGrpHdr")
    @NotNull
    @CheckClrOrgnlGrpHdr
    @Valid
    private OrgnlGrpHdr       orgnlGrpHdr;

    /**
     * ResponsionInformation
     */
    @JacksonXmlProperty(localName = "RspsnInf")
    @NotNull
    @Valid
    private RspsnInf          rspsnInf;

    /**
     * 【营销信息】
     */
    @JacksonXmlProperty(localName = "PrmtInf")
    @Valid
    private PrmtInf prmtInf;

    /**
     * 【收款人合约应答】
     */
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "CdtrCtrctRspsn")
    @Valid
    private List<CdtrCtrctRspsn> cdtrCtrctRspsnList;

    /**
     * --------------ClearingDTO接口方法-------------
     */

    @Override
    public String clrMsgTp() {
        // 报文编号
        return MsgTpEnum.REFUND_RESPONSE.getCode();
    }

    @Override
    public String clrEndToEndId() {
        return null;
    }

    @Override
    public String getClrMsgId() {
        // 报文标识号
        return orgnlGrpHdr.getOrgnlMsgId();
    }

    @Override
    public String clrBizTp() {
        // 业务类型编码
        return null;
    }

    @Override
    public String clrBizKind() {
        // 业务种类编码
        return null;
    }

    @Override
    public String clrDbtrPtyId() {
        // 付款运营机构=发送方
        return grpHdr.getInstdPty().getInstdDrctPty();
    }

    @Override
    public String clrCdtrPtyId() {
        // 收款运营机构=接收方
        return grpHdr.getInstgPty().getInstgDrctPty();
    }

    @Override
    public String clrCurrency() {
        // 交易币种
        return null;
    }

    @Override
    public String clrAmt() {
        // 交易金额
        return null;
    }

    @Override
    public String clrBizRspSts() {
        // 业务回执状态clrBizRspSts
        return rspsnInf.getRspsnSts();
    }

    @Override
    public String clrBizRjctCd() {
        // 业务拒绝码
        return rspsnInf.getRjctCd();
    }

    @Override
    public String clrRjctResn() {
        // 业务拒绝原因
        return rspsnInf.getRjctInf();
    }

    @Override
    public String clrTrxInf() {
        // 交易描述信息(付款报文,交易描述信息为收款人钱包ID)
        return null;
    }

    @Override
    public String clrFlag() {
        return null;
    }

    @Override
    public void fillBatchId(Response<ClearingStatus> response) {
        // 向报文中赋交易批次号
        rspsnInf.setBatchId(response.getResult().getBatchId());
    }

    @Override
    public void fillPlatPrcSts(String bizSts) {
        // 赋值业务状态(平台) 
    	rspsnInf.setPrcSts(bizSts);
    }

    /**
     * --------------RecordDTO接口方法-------------
     */
    @Override
    public String encode() {
        // 对象转换成档案记录JSON串
        return JSON.toJSONString(this);
    }

    @Override
    public RecordDTO decode(String encode) {
        return JSONObject.toJavaObject(JSON.parseObject(encode), this.getClass());
    }

    @Override
    public String recMsgTp() {
        // 报文编号
        return MsgTpEnum.REFUND_RESPONSE.getCode();
    }

    @Override
    public String recMsgId() {
        // 报文标识号
        return orgnlGrpHdr.getOrgnlMsgId();
    }

    @Override
    public String clrDbtrWltId() {
        // 收款方钱包id
        return null;
    }

    @Override
    public String clrDbtrSysId() {
        return null;
    }

    @Override
    public String clrCdtrWltId() {
        // 付款方钱包id
        return null;
    }

    @Override
    public String clrCdtrSysId() {
        return null;
    }

    @Override
    public String recOrgnlMsgTp() {
        // 原报文编号
        return orgnlGrpHdr.getOrgnlMT();
    }

    @Override
    public String recOrgnlMsgId() {
        // 原报文标识号
        return orgnlGrpHdr.getOrgnlMsgId();
    }


    /**
     * --------------GwDTO接口方法-------------
     */

    /**
     * @see GwDTO#init()
     */
    @Override
    public void init() {

    }

    /**
     * @see GwDTO#fetchMsgId()
     * 获取msgId
     */
    @Override
    public String fetchMsgId() {
        return grpHdr.getMsgId();
    }

    @Override
    public boolean check(SoapHeader soapHeader) {
        return CheckUtils.responseMsgChk(soapHeader, grpHdr, orgnlGrpHdr);
    }

    @Override
    public void clrBatId(String clrBatId) {
        rspsnInf.setBatchId(clrBatId);
    }

    @Override
    public String clrBatId() {
        return rspsnInf.getBatchId();
    }

    @Override
    public String clrTransTp() {
        return null;
    }

    @Override
    public void clrBizRspSts(String clrBizRspSts) {
        // 业务回执状态

    }

    @Override
    public void clrBizRjctCd(String clrBizRjctCd) {
        // 业务拒绝码

    }

    @Override
    public void clrRjctResn(String clrRjctResn) {
        // 业务拒绝原因

    }

    @Override
    public String clrCreDtTm() {
        return null;
    }

    @Override
    public String clrSendPtyId() {
        return null;
    }

    @Override
    public String clrSendSysId() {
        return null;
    }

    @Override
    public String clrRecvPtyId() {
        return null;
    }

    @Override
    public String clrRecvSysId() {
        return null;
    }

    @Override
	public String fetchResultCode() {
		if (rspsnInf.getRspsnSts() != null) {
			return rspsnInf.getRspsnSts() + "-"
					+ (ClearingStatusEnum.SUCCESS.getCode().equals(rspsnInf.getRspsnSts())
							? ClearingPrcCdEnum.BUSI_SUCCESS.getCode()
							: ClearingPrcCdEnum.BUSI_REJT.getCode());
		}
		return "";
	}

	@Override
	public OrgnlGrpHdr presumeConfirm() {
		return null;
	}

	@Override
	public String clrPresumeTm() {
		return null;
	}

	@Override
	public String clrAcctTp() {
		return ClrAcctTpEnum.PAY.getCode();
	}

    @Override
    public PrmtInf clrPrmtInf() {
        return prmtInf;
    }
}
