/*
 * pbcdci.cn Inc. Copyright © 2024 PBCDCI All Rights Reserved.
 */
package com.dcep.supergw.dto.dc642;

import com.dcep.common.annotation.Gateway;
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
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

/**
 * 硬钱包余额查询应答报文
 * 
 * @author laimincai
 * @date 2024/04/24
 */
@JacksonXmlRootElement(localName = "HardWltBalQryRsp",
        namespace = "http://www.dcep.com/dcep/64200101/")
@Getter
@Setter
@ToString
@Gateway(msgTp = "dcep.642.001.01", isReturn = true)
public class Dcep64200101DTO extends GwDTO {

    /**
     * 
     */
    private static final long serialVersionUID = 5359593925160391647L;

    /**
     * 【业务头组件】
     */
    @JacksonXmlProperty(localName = "GrpHdr")
    @NotNull(groups = Priority.Highest.class)
    @CheckGrpHdrOrgId(groups = Priority.Lowest.class)
    @CheckGrpHdrMsgId(groups = Priority.Lowest.class)
    @Valid
    private GrpHdr grpHdr;

    @JacksonXmlProperty(localName = "BizQryRef")
    @NotNull(groups = Priority.Highest.class)
    @Valid
    private BizQryRef bizQryRef;

    /**
     * 【应答的原业务信息】
     */
    @JacksonXmlProperty(localName = "BizRpt")
    @Valid
    private BizRpt bizRpt;

    /**
     * 【应答拒绝信息】
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
