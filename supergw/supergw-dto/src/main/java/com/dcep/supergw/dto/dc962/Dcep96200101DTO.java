/*
 * pbcdci.cn Inc.
 * Copyright © 2023 PBCDCI All Rights Reserved.
 */
package com.dcep.supergw.dto.dc962;

import com.dcep.common.annotation.Gateway;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.GrpHdr;
import com.dcep.common.model.soap.OrgnlGrpHdr;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.utils.CheckUtils;
import com.dcep.common.validator.Priority;
import com.dcep.infocache.validation.CheckGrpHdrMsgId;
import com.dcep.infocache.validation.CheckGrpHdrOrgId;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;

@Gateway(msgTp = "dcep.962.001.01", isReturn = true)
@JacksonXmlRootElement(localName = "GetBizApplKeyRsp", namespace = "http://www.dcep.com/dcep/96200101/")
public class Dcep96200101DTO extends GwDTO {

    private static final long serialVersionUID = 1L;

    /*
     * 业务头组件 
     */
    @Valid
    @NotNull
    @CheckGrpHdrMsgId(groups = Priority.Lowest.class)
    @CheckGrpHdrOrgId(groups = Priority.Lowest.class)
    @JacksonXmlProperty(localName = "GrpHdr")
    private GrpHdr grpHdr;

    /*
     * 原报文主键组件
     */
    @JacksonXmlProperty(localName = "OrgnlGrpHdr")
    @NotNull
    @Valid
    private OrgnlGrpHdr orgnlGrpHdr;

    /*
     * 业务回执状态
     */
    @JacksonXmlProperty(localName = "RspsnInf")
    @NotNull
    @Valid
    private RspsnInf rspsnInf;
    
    public Dcep96200101DTO() {}

    public Dcep96200101DTO(GrpHdr grpHdr, OrgnlGrpHdr orgnlGrpHdr, RspsnInf rspsnInf) {
        super();
        this.grpHdr = grpHdr;
        this.orgnlGrpHdr = orgnlGrpHdr;
        this.rspsnInf = rspsnInf;
    }

    @Override
    public void init() {

    }

    @Override
    public String fetchMsgId() {
        if (grpHdr != null) {
            return grpHdr.getMsgId();
        }
        return null;
    }

    @Override
    public boolean check(SoapHeader header) {
        return CheckUtils.responseMsgChk(header, this.grpHdr, this.orgnlGrpHdr);
    }

    public GrpHdr getGrpHdr() {
        return grpHdr;
    }

    public void setGrpHdr(GrpHdr grpHdr) {
        this.grpHdr = grpHdr;
    }

    public OrgnlGrpHdr getOrgnlGrpHdr() {
        return orgnlGrpHdr;
    }

    public void setOrgnlGrpHdr(OrgnlGrpHdr orgnlGrpHdr) {
        this.orgnlGrpHdr = orgnlGrpHdr;
    }

    public RspsnInf getRspsnInf() {
        return rspsnInf;
    }

    public void setRspsnInf(RspsnInf rspsnInf) {
        this.rspsnInf = rspsnInf;
    }

    @Override
    public String toString() {
        return "Dcep96200101DTO [grpHdr=" + grpHdr + ", orgnlGrpHdr=" + orgnlGrpHdr + ", rspsnInf=" + rspsnInf + "]";
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
