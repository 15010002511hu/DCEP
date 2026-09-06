package com.dcep.supergw.dto.dc508;

import com.dcep.common.annotation.Gateway;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.GrpHdr;
import com.dcep.common.model.soap.OrgnlGrpHdr;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.utils.CheckUtils;
import com.dcep.common.validator.Priority;
import com.dcep.infocache.validation.CheckGrpHdrMsgId;
import com.dcep.infocache.validation.CheckGrpHdrOrgId;
import com.dcep.supergw.validation.Check508Biz;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

/**
 * @author : duz
 * @version : Dcep50800101Dto.java v 0.1 2020-11-23
 * @description : 银行账户挂接管理应答报文<dcep.508.001.01>运营机构向互联互通平台返回子钱包推送商户结果
 */
@JacksonXmlRootElement(localName = "ChdWltPushRsp", namespace = "http://www.dcep.com/dcep/50800101/")
@Getter
@Setter
@ToString
@Check508Biz
@Gateway(msgTp = "dcep.508.001.01", isReturn = true)

public class Dcep50800101DTO extends GwDTO {
	private static final long serialVersionUID = 7761136704595953298L;

	/**
	 * 业务头组件
	 */
	@JacksonXmlProperty(localName = "GrpHdr")
	@NotNull(groups = Priority.Highest.class)
	@CheckGrpHdrOrgId(groups = Priority.Lower.class)
	@CheckGrpHdrMsgId(groups = Priority.Lower.class)
	@Valid
	private GrpHdr grpHdr;

	/**
	 * 原报文主键组件
	 */
	@JacksonXmlProperty(localName = "OrgnlGrpHdr")
	@NotNull
	@Valid
	private OrgnlGrpHdr orgnlGrpHdr;

	/**
	 * 业务信息
	 */
	@JacksonXmlProperty(localName = "RspsnInf")
	@NotNull
	@Valid
	private RspsnInf rspsInf;

	@Override
	public void init() {

	}

	@Override
	public String fetchMsgId() {
	    return grpHdr.getMsgId();
	}

	@Override
	public boolean check(SoapHeader header) {
		return CheckUtils.responseMsgChk(header, grpHdr, orgnlGrpHdr);
	}

	@Override
	public String fetchResultCode() {
		StringBuilder sb = new StringBuilder();
		sb.append(rspsInf.getRspsnSts());
		if (StringUtils.isNotEmpty(rspsInf.getRjctCd())) {
			sb.append("-");
			sb.append(rspsInf.getRjctCd());
		}
		return sb.toString();
	}

}
