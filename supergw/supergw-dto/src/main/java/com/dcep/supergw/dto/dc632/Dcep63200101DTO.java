package com.dcep.supergw.dto.dc632;

import com.dcep.common.annotation.Gateway;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.GrpHdr;
import com.dcep.common.model.soap.OrgnlGrpHdr;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.utils.CheckUtils;
import com.dcep.common.validator.Priority;
import com.dcep.infocache.validation.CheckGrpHdrMsgId;
import com.dcep.infocache.validation.CheckGrpHdrOrgId;
import com.dcep.supergw.validation.Check632Biz;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

/**
 * 	脱机交易状态查询应答报文<dcep.632.001.01>
 * 
 * @author duzhong
 * @version : Dcep63200101DTO.java v 0.1 2020-09-07
 * @description 运营机构收到“脱机交易状态查询”报文，返回“脱机交易状态查询应答”报文，返回收款方对应交易索引的脱机交易状态。响应报文中交易顺序与请求报文保持一致。
 */
@JacksonXmlRootElement(localName = "OfflineTxRsp", namespace = "http://www.dcep.com/dcep/63200101/")
@Getter
@Setter
@ToString
@Gateway(msgTp = "dcep.632.001.01", isReturn = true)
@Check632Biz
public class Dcep63200101DTO extends GwDTO {


	/**
	 * 
	 */
	private static final long serialVersionUID = -1725108708732785706L;

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
	 * 个人化申请信息
	 */
	@JacksonXmlProperty(localName = "RspsnInf")
	@NotNull(groups = Priority.Highest.class)
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
