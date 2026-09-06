package com.dcep.supergw.dto.dc320;

import com.dcep.common.annotation.Gateway;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.GrpHdr;
import com.dcep.common.model.soap.OrgnlGrpHdr;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.utils.CheckUtils;
import com.dcep.common.validator.Priority;
import com.dcep.infocache.validation.CheckGrpHdrMsgId;
import com.dcep.infocache.validation.CheckGrpHdrOrgId;
import com.dcep.supergw.validation.Check320Biz;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

/**
 * @author : maxinyu
 * @version : Dcep32000101DTO.java v 0.1 2023-01-05
 * Copyright 2023 PBCDCI ALL Rights
 * @description :
 */
@Getter
@Setter
@ToString
@Gateway(msgTp = "dcep.320.001.01", isReturn = true)
@JacksonXmlRootElement(localName = "GetPmtCodeRsp", namespace = "http://www.dcep.com/dcep/32000101/")
@Check320Biz(groups = Priority.Lowest.class)
public class Dcep32000101DTO extends GwDTO implements Serializable {
    private static final long serialVersionUID = -4624799806374487258L;

    @Valid
    @NotNull
    @JacksonXmlProperty(localName = "GrpHdr")
    @CheckGrpHdrMsgId(groups = Priority.Lowest.class)
    @CheckGrpHdrOrgId(groups = Priority.Lowest.class)
    private GrpHdr grpHdr;

    @Valid
    @NotNull
    @JacksonXmlProperty(localName = "OrgnlGrpHdr")
    private OrgnlGrpHdr orgnlGrpHdr;

    @Valid
    @NotNull
    @JacksonXmlProperty(localName = "RspsnInf")
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
