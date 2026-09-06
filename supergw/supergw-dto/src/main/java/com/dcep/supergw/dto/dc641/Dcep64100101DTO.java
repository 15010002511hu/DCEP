/*
 * pbcdci.cn Inc. Copyright © 2024 PBCDCI All Rights Reserved.
 */
package com.dcep.supergw.dto.dc641;

import com.dcep.common.annotation.Channel;
import com.dcep.common.annotation.Gateway;
import com.dcep.common.encryption.EncryptionHelper;
import com.dcep.common.encryption.TransEncryption;
import com.dcep.common.enums.ChannelEnums;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.GrpHdr;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.utils.CheckUtils;
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

/**
 * 硬钱包余额查询请求
 * 
 * @author laimincai
 * @date 2024/04/24
 */
@JacksonXmlRootElement(localName = "HardWltBalQryReq",
        namespace = "http://www.dcep.com/dcep/64100101/")
@Getter
@Setter
@ToString
@Gateway(msgTp = "dcep.641.001.01", channel = @Channel(classname = ChannelEnums.DIRECT_FORWARD))
public class Dcep64100101DTO extends GwDTO implements TransEncryption {

    /**
     * 
     */
    private static final long serialVersionUID = 4515360862174888764L;

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
     * 【钱包信息】
     */
    @JacksonXmlProperty(localName = "WltInf")
    @NotNull(groups = Priority.Highest.class)
    @Valid
    private WltInf wltInf;


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

    @Override
    public void transEncrypt(EncryptionHelper encryptionHelper) {
        if (wltInf != null) {
            wltInf.transEncrypt(encryptionHelper);
        }
    }

}
