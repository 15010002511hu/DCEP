package com.dcep.supergw.dto.dc311;

import com.dcep.common.annotation.Channel;
import com.dcep.common.annotation.Gateway;
import com.dcep.common.enums.ChannelEnums;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.GrpHdr;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.validator.Priority;
import com.dcep.infocache.validation.CheckGrpHdrMsgId;
import com.dcep.infocache.validation.CheckGrpHdrOrgId;
import com.dcep.supergw.validation.Check311Biz;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * 
 * 3.6.1 支付结果查询请求报文<dcep.311.001.01>
 * 
 * @author duzhong
 * @version $Id: Dcep31100101DTO.java, v 0.1 2021年04月14日 上午10:07:20 duzhong Exp
 *          $
 *
 */
@JacksonXmlRootElement(localName = "PmtRsQryReq", namespace = "http://www.dcep.com/dcep/31100101/")
@Check311Biz(groups = Priority.Lowest.class)
@Setter
@Getter
@ToString
@Gateway(msgTp = "dcep.311.001.01", channel = @Channel(classname = ChannelEnums.DIRECT_FORWARD))
public class Dcep31100101DTO extends GwDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1283364706006772513L;

	public Dcep31100101DTO() {

	}

	/**
	 * Body报文体
	 */

	/**
	 * 业务头组件GrpHdr
	 */
	@JacksonXmlProperty(localName = "GrpHdr")
	@NotNull(groups = Priority.Highest.class)
	@CheckGrpHdrOrgId(groups = Priority.Lowest.class)
	@CheckGrpHdrMsgId(groups = Priority.Lowest.class)
	@Valid
	private GrpHdr grpHdr;

	/**
	 * 原报文主键组件
	 */
	@JacksonXmlProperty(localName = "OrgnlGrpHdr")
	@NotNull
	@Valid
	private OrgnlGrpHdr orgnlGrpHdr;

	@Override
	public void init() {

	}

	@Override
	public String fetchMsgId() {
		return grpHdr.getMsgId();
	}

	@Override
	public boolean check(SoapHeader header) {
		return true;
	}

	@Override
	public ChannelEnums routeChannel(SoapHeader header) {

		return ChannelEnums.DIRECT_FORWARD;
	}

	@Override
	public String fetchResultCode() {
		return "PR00-";
	}
}
