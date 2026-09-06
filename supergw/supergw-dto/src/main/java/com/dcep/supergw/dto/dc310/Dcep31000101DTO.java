package com.dcep.supergw.dto.dc310;

import com.dcep.common.annotation.Gateway;
import com.dcep.common.enums.ChannelEnums;
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
 * 
 * 3.6.1.4 统一收单支付应答报文<dcep.310.001.01>
 * 
 * @author duzhong
 * @version $Id: Dcep31000101DTO.java, v 0.1 2021年04月13日 上午10:07:20 duzhong Exp
 *          $
 *
 */
@JacksonXmlRootElement(localName = "OrdrRefRsp", namespace = "http://www.dcep.com/dcep/31000101/")
@Setter
@Getter
@ToString
@Gateway(msgTp = "dcep.310.001.01", isReturn = true)
public class Dcep31000101DTO extends GwDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1283364706006772513L;

	public Dcep31000101DTO() {

	}
	/**
	 * Body报文体
	 */
	/**
	 * 业务头组件GrpHdr
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

	@JacksonXmlProperty(localName = "RspsnInf")
	@NotNull
	@Valid
	private RspsnInf rspsnInf;

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
	public ChannelEnums routeChannel(SoapHeader header) {

		return ChannelEnums.DIRECT_FORWARD;
	}

	@Override
	public String fetchResultCode() {
		StringBuilder sb = new StringBuilder();
		sb.append(rspsnInf.getRspsnSts());
		if (StringUtils.isNotEmpty(rspsnInf.getRjctCd())) {
			sb.append("-");
			sb.append(rspsnInf.getRjctCd());
		}
		return sb.toString();
	}
}
