package com.dcep.supergw.dto.dc453;

import com.dcep.common.annotation.Channel;
import com.dcep.common.annotation.Gateway;
import com.dcep.common.enums.ChannelEnums;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.GrpHdr;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.utils.CheckUtils;
import com.dcep.common.validator.Priority;
import com.dcep.common.validator.Priority.Highest;
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
 *	钱柜余额对账通知报文<dcep.453.001.01>
 *
 * @author duzhong
 */
@JacksonXmlRootElement(localName = "CshBoxAcctChckngNtfctn", namespace = "http://www.dcep.com/dcep/45300101/")
@Getter
@Setter
@ToString
@Gateway(msgTp = "dcep.453.001.01", channel = @Channel(classname = ChannelEnums.DIRECT_FORWARD))
public class Dcep45300101DTO extends GwDTO {


	/**
	 * 
	 */
	private static final long serialVersionUID = -4176359851117474887L;

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
	 * 【回执信息】
	 */
	@JacksonXmlProperty(localName = "ChckngInf")
	@NotNull
	@Valid
	private ChckngInf chckngInf;

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

	public Dcep45300101DTO() {
		super();
	}

	public Dcep45300101DTO(@NotNull(groups = Highest.class) @Valid GrpHdr grpHdr, @NotNull @Valid ChckngInf chckngInf) {
		super();
		this.grpHdr = grpHdr;
		this.chckngInf = chckngInf;
	}

}