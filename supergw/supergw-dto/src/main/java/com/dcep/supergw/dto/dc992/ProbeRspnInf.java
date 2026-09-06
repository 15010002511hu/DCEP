package com.dcep.supergw.dto.dc992;

import com.dcep.common.validator.Priority;
import com.dcep.common.validator.Priority.Highest;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import lombok.ToString;

@ToString
public class ProbeRspnInf implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8864820796907817733L;
	/**
	 * 业务回执状态
	 */
	@JacksonXmlProperty(localName = "InstgId")
	@NotBlank(groups = Priority.Highest.class)
	private String instgId;

	@JacksonXmlProperty(localName = "GZone")
	@Valid
	private GZone gZone;

	@JacksonXmlProperty(localName = "CZone")
	@Valid
	private CZone cZone;

	@JacksonXmlElementWrapper(localName = "RZone")
	@JacksonXmlProperty(localName = "CtrlNbLst")
	@Valid
	private List<CtrlNbLst> rZone;

	public ProbeRspnInf(@NotBlank(groups = Highest.class) String instgId, @Valid GZone gZone, @Valid CZone cZone,
			@Valid List<CtrlNbLst> rZone) {
		super();
		this.instgId = instgId;
		this.gZone = gZone;
		this.cZone = cZone;
		this.rZone = rZone;
	}
	public ProbeRspnInf() {}
	public ProbeRspnInf(@NotBlank(groups = Highest.class) String instgId) {
		super();
		this.instgId = instgId;
	}
	public String getInstgId() {
		return instgId;
	}
	public void setInstgId(String instgId) {
		this.instgId = instgId;
	}
	public GZone getgZone() {
		return gZone;
	}
	public void setgZone(GZone gZone) {
		this.gZone = gZone;
	}
	public CZone getcZone() {
		return cZone;
	}
	public void setcZone(CZone cZone) {
		this.cZone = cZone;
	}
	public List<CtrlNbLst> getrZone() {
		return rZone;
	}
	public void setrZone(List<CtrlNbLst> rZone) {
		this.rZone = rZone;
	}

}
