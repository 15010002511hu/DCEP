package com.dcep.supergw.dto.dc991;

import com.dcep.common.annotation.Gateway;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.validator.Priority;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 探测请求报文<dcep.991.001.01>
 * 
 * @author duzhong
 * @version $Id: Dcep99100101DTO.java, v 0.1 2021年4月20日 上午11:32:47 duz Exp $
 *
 */
@JacksonXmlRootElement(localName = "ProbeReq", namespace = "http://www.dcep.com/dcep/99100101/")
@Getter
@Setter
@ToString
@Gateway(msgTp = "dcep.991.001.01")
public class Dcep99100101DTO extends GwDTO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7672067110658271831L;

	@JacksonXmlProperty(localName = "ProbeInf")
	@NotNull(groups = Priority.Highest.class)
	@Valid
	private ProbeInf probeInf;

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

}
