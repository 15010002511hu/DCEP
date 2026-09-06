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
 * @version : Dcep50800102Dto.java v 0.1 2020-11-23
 * @description : 子钱包推送商户应答报文
 */
@JacksonXmlRootElement(localName = "SubWltPushRsp", namespace = "http://www.dcep.com/dcep/50800102/")
@Getter
@Setter
@ToString
@Gateway(msgTp = "dcep.508.001.02", isReturn = true)
public class Dcep50800102DTO extends GwDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4651444643672961097L;

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
	private RspsnInf2 rspsInf;

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
