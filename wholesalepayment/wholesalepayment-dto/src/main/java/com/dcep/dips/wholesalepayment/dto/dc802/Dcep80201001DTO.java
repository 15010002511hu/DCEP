package com.dcep.dips.wholesalepayment.dto.dc802;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dcep.common.annotation.Gateway;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.Response;
import com.dcep.common.model.soap.GrpHdr;
import com.dcep.common.model.soap.OrgnlGrpHdr;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.utils.CheckUtils;
import com.dcep.common.validator.Priority;
import com.dcep.dips.wholesalepayment.annotation.Record;
import com.dcep.dips.wholesalepayment.dto.ClearingDTO;
import com.dcep.dips.wholesalepayment.dto.ClearingStatus;
import com.dcep.dips.wholesalepayment.dto.RecordDTO;
import com.dcep.dips.wholesalepayment.dto.mcbs.MbridgeReqDTO;
import com.dcep.dips.wholesalepayment.enums.ClearingPrcCdEnum;
import com.dcep.dips.wholesalepayment.enums.ClearingStatusEnum;
import com.dcep.dips.wholesalepayment.enums.MsgTpEnum;
import com.dcep.dips.wholesalepayment.enums.RecordSaveModeEnum;
import com.dcep.infocache.validation.CheckClrGrpHdr;
import com.dcep.infocache.validation.CheckClrOrgnlGrpHdr;
import com.dcep.infocache.validation.CheckGrpHdrMsgId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@JacksonXmlRootElement(localName = "DsptRsp", namespace = "http://www.dcep.com/dcep/80201001/")
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Gateway(msgTp = "dcep.802.010.01", isReturn = true)
@Record(saveMode = RecordSaveModeEnum.ALL)
public class Dcep80201001DTO extends GwDTO implements ClearingDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 【业务头组件】
	 */
	@JacksonXmlProperty(localName = "GrpHdr")
	@NotNull(groups = Priority.Highest.class)
	@Valid
	@CheckGrpHdrMsgId(groups = Priority.Lowest.class)
	@CheckClrGrpHdr(groups = Priority.Lowest.class)
	private GrpHdr grpHdr;

	/**
	 * 【原业务头组件】
	 */
	@JacksonXmlProperty(localName = "OrgnlGrpHdr")
	@NotNull(groups = Priority.Highest.class)
	@Valid
	@CheckClrOrgnlGrpHdr(groups = Priority.Lowest.class)
	private OrgnlGrpHdr orgnlGrpHdr;

	/**
	 * 【应答信息】
	 */
	@JacksonXmlProperty(localName = "RspsnInf")
	@NotNull(groups = Priority.Highest.class)
	@Valid
	private RspsnInf rspsnInf;
	
	/**
     * 货币桥信息
     */
    @JsonIgnore
    private MbridgeReqDTO mbridgeReqDTO;

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
		// JSON对象转化为Java对象
		return JSONObject.toJavaObject(JSON.parseObject(encode), this.getClass());
	}

	@Override
	public String recMsgTp() {
		// 报文编号
		return MsgTpEnum.CRDT_ADJ_RESPONSE.getCode();
	}

	@Override
	public String recMsgId() {
		// 报文标识号
		return orgnlGrpHdr.getOrgnlMsgId();
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
	 * --------------ClearingDTO接口方法-------------
	 */

	@Override
	public void clrBatId(String clrBatId) {
		// 交易批次号
		rspsnInf.setBatchId(clrBatId);
	}

	@Override
	public String clrBatId() {
		// 交易批次号
		return rspsnInf.getBatchId();
	}

	@Override
	public String clrTransTp() {
		return null;
	}

	@Override
	public String clrMsgTp() {
		// 报文编号
		return MsgTpEnum.CRDT_ADJ_RESPONSE.getCode();
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
	public String clrDbtrWltId() {
		return null;
	}

	@Override
	public String clrDbtrSysId() {
		return null;
	}

	@Override
	public String clrCdtrPtyId() {
		// 收款运营机构=接收方
		return grpHdr.getInstgPty().getInstgDrctPty();
	}

	@Override
	public String clrCdtrWltId() {
		return null;
	}

	@Override
	public String clrCdtrSysId() {
		return null;
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
		// 业务回执状态
		return rspsnInf.getRspsnSts();
	}

	@Override
	public void clrBizRspSts(String clrBizRspSts) {
		// 业务回执状态
	}

	@Override
	public String clrBizRjctCd() {
		// 业务拒绝码
		return rspsnInf.getRjctCd();
	}

	@Override
	public void clrBizRjctCd(String clrBizRjctCd) {
		// 业务拒绝码
	}

	@Override
	public String clrRjctResn() {
		// 业务拒绝原因
		return rspsnInf.getRjctInf();
	}

	@Override
	public void clrRjctResn(String clrRjctResn) {
		// 业务拒绝原因
	}

	@Override
	public String clrTrxInf() {
		// 交易描述信息
		return null;
	}

	@Override
	public String clrFlag() {
		return null;
	}

	@Override
	public String clrCreDtTm() {
		// 账务时间
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
	 * --------------GwDTO接口方法-------------
	 */

	@Override
	public void init() {
	    // 赋值业务状态(平台)
        rspsnInf.setPrcSts(rspsnInf.getRspsnSts());

	}

	@Override
	public String fetchMsgId() {
		// 获取报文编号
		return grpHdr.getMsgId();
	}

	@Override
	public boolean check(SoapHeader soapHeader) {
		// DTO 校验，主要校验soapheader 和 soapbody 同时出现的值是否一致
		return CheckUtils.responseMsgChk(soapHeader, grpHdr, orgnlGrpHdr);
	}

	@Override
	public String fetchResultCode() {
		return rspsnInf.getRspsnSts() != null ? rspsnInf.getRspsnSts() + "-"
				+ (ClearingStatusEnum.SUCCESS.getCode().equals(rspsnInf.getRspsnSts())
						? ClearingPrcCdEnum.BUSI_SUCCESS.getCode()
						: ClearingPrcCdEnum.BUSI_REJT.getCode())
				: "";
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
		return null;
	}

    @Override
    public MbridgeReqDTO clrMbridgeInf() {
        return mbridgeReqDTO;
    }
    
    @Override
    public void fillMsgId(String msgId, String orgMsgId) {
        // 赋值报文标识号
        grpHdr.setMsgId(msgId);
        orgnlGrpHdr.setOrgnlMsgId(orgMsgId);
    }

}
