package com.dcep.supergw.dto.dc992;

import com.dcep.common.annotation.Gateway;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.DCEPPrcInf;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.validator.Priority;
import com.dcep.common.validator.Priority.Highest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

/**
 *探测应答报文<dcep.992.001.01>
 * 
 * @author duzhong
 * @version $Id: Dcep99200101DTO.java, v 0.1 2021年4月20日 上午11:32:47 duz Exp $
 *
 */
@JacksonXmlRootElement(localName = "ProbeRsp", namespace = "http://www.dcep.com/dcep/99200101/")
@ToString
@Gateway(msgTp = "dcep.992.001.01")
public class Dcep99200101DTO extends GwDTO {


	/**
	 * 
	 */
	private static final long serialVersionUID = -2725227600271217772L;

	/**
	 * 平台处理信息
	 */
	@JacksonXmlProperty(localName = "DCEPPrcInf")
	@NotNull(groups = Priority.Highest.class)
	@Valid
	private DCEPPrcInf dcepPrcInf;

	@JacksonXmlProperty(localName = "ProbeRspnInf")
	@NotNull(groups = Priority.Highest.class)
	@Valid
	private ProbeRspnInf probeRspnInf;
	
	@Override
	public void init() {

	}

	@Override
	public String fetchMsgId() {
		return null;
	}

	@Override
	public boolean check(SoapHeader header) {
		return true;
	}
	public Dcep99200101DTO() {}
	
	public Dcep99200101DTO(@NotNull(groups = Highest.class) @Valid DCEPPrcInf dcepPrcInf,
			@NotNull(groups = Highest.class) @Valid ProbeRspnInf probeRspnInf) {
		super();
		this.dcepPrcInf = dcepPrcInf;
		this.probeRspnInf = probeRspnInf;
	}
	@JsonIgnore
	public DCEPPrcInf getDcepPrcInf() {
		return dcepPrcInf;
	}
	@JsonIgnore
	public void setDcepPrcInf(DCEPPrcInf dcepPrcInf) {
		this.dcepPrcInf = dcepPrcInf;
	}

	public ProbeRspnInf getProbeRspnInf() {
		return probeRspnInf;
	}

	public void setProbeRspnInf(ProbeRspnInf probeRspnInf) {
		this.probeRspnInf = probeRspnInf;
	}

	@Override
	public String fetchResultCode() {
		StringBuilder sb = new StringBuilder();
		sb.append(dcepPrcInf.getPrcSts());
		if (StringUtils.isNotEmpty(dcepPrcInf.getPrcCd())) {
			sb.append("-");
			sb.append(dcepPrcInf.getPrcCd());
		}
		return sb.toString();
	}
}
