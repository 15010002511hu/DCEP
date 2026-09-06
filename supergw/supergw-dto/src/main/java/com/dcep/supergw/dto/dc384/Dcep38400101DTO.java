/*
 * pbcdci.cn Inc.
 * Copyright © 2023 PBCDCI All Rights Reserved.
 */
package com.dcep.supergw.dto.dc384;

import com.dcep.common.annotation.Gateway;
import com.dcep.common.enums.ErrorEnum;
import com.dcep.common.exception.DcepException;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.GrpHdr;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.validator.Priority;
import com.dcep.infocache.validation.CheckGrpHdrMsgId;
import com.dcep.infocache.validation.CheckGrpHdrOrgId;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;

/**
 * 金额锁定状态查询应答
 * 
 * @author laimincai
 * @date 2023/12/29
 */
@Gateway(msgTp = "dcep.384.001.01", isReturn = true)
@JacksonXmlRootElement(localName = "LockTxStsQryRsp", namespace = "http://www.dcep.com/dcep/38400101/")
public class Dcep38400101DTO extends GwDTO {

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
     * 原业务信息
     */
    @Valid
    @NotNull
    @JacksonXmlProperty(localName = "BizQryRef")
    private BizQryRef bizQryRef;
    
    /*
     * 应答的原业务信息
     */
    @Valid
    @JacksonXmlProperty(localName = "BizRpt")
    private BizRpt bizRpt;
    
    /*
     * 应答拒绝信息
     */
    @Valid
    @JacksonXmlProperty(localName = "OprlErr")
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
        if(bizQryRef !=null && StringUtils.equals(bizQryRef.getQryRs(), "PR00") && bizRpt == null) {
            throw new DcepException(ErrorEnum.VALIDATION_ERROR.getCode(), "查询处理状态为PR00时, 原业务应答信息必填");
        }
        
        if(bizQryRef !=null && StringUtils.equals(bizQryRef.getQryRs(), "PR01") && oprlErr == null) {
            throw new DcepException(ErrorEnum.VALIDATION_ERROR.getCode(), "查询处理状态为PR01时, 应答拒绝信息必填");
        }
        
        return true;
    }

    public GrpHdr getGrpHdr() {
        return grpHdr;
    }

    public void setGrpHdr(GrpHdr grpHdr) {
        this.grpHdr = grpHdr;
    }

    public BizQryRef getBizQryRef() {
        return bizQryRef;
    }

    public void setBizQryRef(BizQryRef bizQryRef) {
        this.bizQryRef = bizQryRef;
    }

    public BizRpt getBizRpt() {
        return bizRpt;
    }

    public void setBizRpt(BizRpt bizRpt) {
        this.bizRpt = bizRpt;
    }

    public OprlErr getOprlErr() {
        return oprlErr;
    }

    public void setOprlErr(OprlErr oprlErr) {
        this.oprlErr = oprlErr;
    }

    @Override
    public String toString() {
        return "Dcep38400101DTO [grpHdr=" + grpHdr + ", bizQryRef=" + bizQryRef + ", bizRpt=" + bizRpt + ", oprlErr="
                + oprlErr + "]";
    }

    @Override
    public String fetchResultCode() {
        StringBuilder sb = new StringBuilder();
        sb.append(bizQryRef.getQryRs());
        if (oprlErr != null) {
            if (StringUtils.isNotEmpty(oprlErr.getRjctCd())) {
                sb.append("-");
                sb.append(oprlErr.getRjctCd());
            }
        }
        return sb.toString();
    }
}
