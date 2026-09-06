package com.dcep.supergw.dto.dc401;

import com.dcep.common.annotation.Channel;
import com.dcep.common.annotation.Gateway;
import com.dcep.common.annotation.GwMethod;
import com.dcep.common.annotation.RpcInfo;
import com.dcep.common.enums.ChannelEnums;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.GrpHdr;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.validator.Priority;
import com.dcep.infocache.OrgCache;
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
 * 
 * 8.3.1 自由格式报文<dcep.401.001.01>
 * 
 * @author laimincai
 * @version $Id: Dcep40100101DTO.java, v 0.1 2019年10月11日 上午10:07:20 laimincai
 *          Exp $
 *
 */
@JacksonXmlRootElement(localName = "FreeFrmt",namespace = "http://www.dcep.com/dcep/40100101/")
@Setter
@Getter
@ToString
@Gateway(msgTp = "dcep.401.001.01", channel = @Channel(classname = ChannelEnums.CENTRAL_PROCESS, services = {
		@RpcInfo(name = "com.dcep.doms.fi.api.GwinService", methods = { @GwMethod(name = "execute") }) }))
public class Dcep40100101DTO extends GwDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1283364706006772513L;
	
	public Dcep40100101DTO() {
		
	}
	
	public Dcep40100101DTO(GrpHdr grpHdr, FreeFrmtInf freeFrmtInf) {
		this.setGrpHdr(grpHdr);
		this.setFreeFrmtInf(freeFrmtInf);
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
	 * 自由格式信息
	 */
	@JacksonXmlProperty(localName = "FreeFrmtInf")
	@NotNull
	@Valid
	private FreeFrmtInf freeFrmtInf;

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
		String revBankCode = header.getReceiver();
		String cenBankCode = OrgCache.getInstance().getPbocInf().getOrgCode();

		// 接收机构为是央行, 则走默认配置中心处理模式, 否则, 走直接转发模式
		if (revBankCode.equals(cenBankCode)) {
			return super.routeChannel(header);
		}

		return ChannelEnums.DIRECT_FORWARD;
	}


	@Override
	public String fetchResultCode() {
		return "PR00-";
	}
}
