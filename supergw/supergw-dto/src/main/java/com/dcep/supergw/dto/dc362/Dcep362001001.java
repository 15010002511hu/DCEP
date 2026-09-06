package com.dcep.supergw.dto.dc362;

import com.dcep.common.annotation.Gateway;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.GrpHdr;
import com.dcep.common.model.soap.OrgnlGrpHdr;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.utils.CheckUtils;
import com.dcep.common.validator.Priority;
import com.dcep.supergw.validation.Check362Biz;
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
 * @version : Dcep362001001.java v 0.1 2023-01-05
 * Copyright 2023 PBCDCI ALL Rights
 * @description :
 */
@Getter
@Setter
@ToString
@Gateway(msgTp = "dcep.362.001.01", isReturn = true)
@Check362Biz(groups = Priority.Lowest.class)
@JacksonXmlRootElement(localName = "OrdrPmtRsp", namespace = "http://www.dcep.com/dcep/36200101/")
public class Dcep362001001 extends GwDTO implements Serializable {

    private static final long serialVersionUID = -7678646552691987582L;

    @JacksonXmlProperty(localName = "GrpHdr")
    @NotNull
    @Valid
    GrpHdr grpHdr;

    @Valid
    @NotNull
    @JacksonXmlProperty(localName = "OrgnlGrpHdr")
    private OrgnlGrpHdr orgnlGrpHdr;

    @Valid
    @NotNull
    @JacksonXmlProperty(localName = "RspsnInf")
    private RspsnInf rspsnInf;

    @Valid
    @JacksonXmlProperty(localName = "PrmtInf")
    private PrmtInf prmtInf;

    @Override
    public void init() {

    }

    @Override
    public String fetchMsgId() {
        return this.grpHdr.getMsgId();
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
