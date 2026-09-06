package com.dcep.supergw.dto.dc536;

import com.dcep.common.annotation.Gateway;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.GrpHdr;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.validator.Priority;
import com.dcep.infocache.validation.CheckGrpHdrMsgId;
import com.dcep.infocache.validation.CheckGrpHdrOrgId;
import com.dcep.supergw.validation.Check536Biz;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

@JacksonXmlRootElement(localName = "BankCardInfQryRsp", namespace = "http://www.dcep.com/dcep/53600101/")
@Getter
@Setter
@ToString
@Gateway(msgTp = "dcep.536.001.01", isReturn = true)
@Check536Biz(groups = Priority.Lowest.class)
public class Dcep53600101DTO extends GwDTO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5819398605194940686L;

	/**
	 * 业务头组件
	 */
	@JacksonXmlProperty(localName = "GrpHdr")
	@NotNull(groups = Priority.Highest.class)
	@CheckGrpHdrOrgId(groups = Priority.Lowest.class)
	@CheckGrpHdrMsgId(groups = Priority.Lowest.class)
	@Valid
	private GrpHdr grpHdr;

	/**
	 * 银行卡信息
	 */
	@JacksonXmlProperty(localName = "BizQryRef")
	@NotNull
	@Valid
	private BizQryRef bizQryRef;
	
	/**
	 * 银行卡信息
	 */
	@JacksonXmlProperty(localName = "CardInf")
	@Valid
	private CardInf cardInf;
	
	/**
	 * 应答拒绝信息
	 */
	@JacksonXmlProperty(localName = "OprlErr")
	@Valid
	private OprlErr oprlErr;

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
	public String fetchResultCode() {
		StringBuilder sb = new StringBuilder();
		sb.append(bizQryRef.getQryRs());
		if (oprlErr != null) {
			if (StringUtils.isNotEmpty(oprlErr.getErr().getRjctCd())) {
				sb.append("-");
				sb.append(oprlErr.getErr().getRjctCd());
			}
		}
		return sb.toString();
	}
}
