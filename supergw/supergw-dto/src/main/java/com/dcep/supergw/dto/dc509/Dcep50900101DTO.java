package com.dcep.supergw.dto.dc509;

import com.dcep.common.annotation.Channel;
import com.dcep.common.annotation.Gateway;
import com.dcep.common.enums.ChannelEnums;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.GrpHdr;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.utils.CheckUtils;
import com.dcep.common.validator.Priority;
import com.dcep.infocache.validation.CheckGrpHdrMsgId;
import com.dcep.infocache.validation.CheckGrpHdrOrgId;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *子钱包解除推送商户通知报文<dcep.509.001.01>
 *运营机构完成子钱包解除绑定时通知互联互通平台
 *
 * @author duzhong
 */
@JacksonXmlRootElement(localName = "ChdWltClPushNtfctn", namespace = "http://www.dcep.com/dcep/50900101/")
@Getter
@Setter
@ToString
@Gateway(msgTp = "dcep.509.001.01", channel = @Channel(classname = ChannelEnums.DIRECT_FORWARD))
public class Dcep50900101DTO extends GwDTO  {

	private static final long serialVersionUID = -4488796462184974006L;

	/**
	 * 【业务头组件】
	 */
	@JacksonXmlProperty(localName = "GrpHdr")
	@NotNull(groups = Priority.Highest.class)
	@CheckGrpHdrOrgId(groups = Priority.Lowest.class)
	@CheckGrpHdrMsgId(groups = Priority.Lowest.class)
	@Valid
	private GrpHdr grpHdr;

	/**
	 * 【协议信息】
	 */
	@JacksonXmlProperty(localName = "PtcInf")
	@NotNull
	@Valid
	private PtcInf ptcInf;
	
	/**
	 * 【协议信息】
	 */
	@JacksonXmlProperty(localName = "MrchntInf")
	@NotNull
	@Valid
	private MrchntInf mrchntInf;


	@Override
	public void init() {

	}

	@Override
	public String fetchMsgId() {
		return grpHdr.getMsgId();
	}

	@Override
	public boolean check(SoapHeader header) {
		return CheckUtils.requestMsgChk(header, grpHdr);
	}

	
}