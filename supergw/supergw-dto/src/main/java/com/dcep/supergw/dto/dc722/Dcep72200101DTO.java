package com.dcep.supergw.dto.dc722;

import com.dcep.common.annotation.Gateway;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.GrpHdr;
import com.dcep.common.model.soap.OrgnlGrpHdr;
import com.dcep.common.model.soap.SoapHeader;
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
 * 硬件钱包单笔脱机转出确认报文<dcep.722.001.01>
 * 
 * @author duzhong
 * @version $Id: Dcep72200101DTO.java, v 0.1 2020年3月26日 上午11:32:47 duz Exp $
 *
 */
@JacksonXmlRootElement(localName = "ConvertNtfctn", namespace = "http://www.dcep.com/dcep/72100101/")
@Getter
@Setter
@ToString
@Gateway(msgTp = "dcep.722.001.01")
public class Dcep72200101DTO extends GwDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 842679323173224906L;

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
	 * 【原报文组件】
	 */
	@JacksonXmlProperty(localName = "OrgnlGrpHdr")
	@NotNull(groups = Priority.Highest.class)
	@Valid
	private OrgnlGrpHdr orgnlGrpHdr;

	/**
	 * 离线转出核对确认信息
	 */
	@JacksonXmlProperty(localName = "OfflineConvertCnfrmInf")
	@NotNull(groups = Priority.Highest.class)
	@Valid
	private OfflineConvertCnfrmInf offlineConvertCnfrmInf;

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
		sb.append(offlineConvertCnfrmInf.getRspsnSts());
		if (StringUtils.isNotEmpty(offlineConvertCnfrmInf.getRjctCd())) {
			sb.append("-");
			sb.append(offlineConvertCnfrmInf.getRjctCd());
		}
		return sb.toString();
	}
}
