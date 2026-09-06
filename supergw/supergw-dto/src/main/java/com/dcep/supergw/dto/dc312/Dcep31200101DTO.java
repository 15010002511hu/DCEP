package com.dcep.supergw.dto.dc312;

import com.dcep.common.annotation.Gateway;
import com.dcep.common.encryption.DataEncryption;
import com.dcep.common.encryption.EncryptionHelper;
import com.dcep.common.enums.ChannelEnums;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.GrpHdr;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.validator.Priority;
import com.dcep.infocache.validation.CheckGrpHdrMsgId;
import com.dcep.infocache.validation.CheckGrpHdrOrgId;
import com.dcep.supergw.validation.Check312Biz;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

/**
 * 3.6.1.4 支付结果查询应答报文<dcep.312.001.01>
 *
 * @author duzhong
 * @version $Id: Dcep31200101DTO.java, v 0.1 2021年04月13日 上午10:07:20 duzhong Exp $
 */
@JacksonXmlRootElement(localName = "PmtRsQryRsp", namespace = "http://www.dcep.com/dcep/31200101/")
@Setter
@Getter
@ToString
@Gateway(msgTp = "dcep.312.001.01", isReturn = true)
@Check312Biz
public class Dcep31200101DTO extends GwDTO implements DataEncryption {

    /**
     *
     */
    private static final long serialVersionUID = -1283364706006772513L;

    public Dcep31200101DTO() {

    }

    /**
     * Body报文体
     */
    /**
     * 业务头组件GrpHdr
     */
    @JacksonXmlProperty(localName = "GrpHdr")
    @NotNull(groups = Priority.Highest.class)
    @CheckGrpHdrOrgId(groups = Priority.Lower.class)
    @CheckGrpHdrMsgId(groups = Priority.Lower.class)
    @Valid
    private GrpHdr grpHdr;

    @JacksonXmlProperty(localName = "BizQryRef")
    @NotNull
    @Valid
    private BizQryRef bizQryRef;

    @JacksonXmlProperty(localName = "BizRpt")
    @Valid
    private BizRpt bizRpt;

    @JacksonXmlProperty(localName = "OprlErr")
    @Valid
    private OprlErr oprlErr;


    @Override
    public String fetchMsgId() {
        return grpHdr.getMsgId();
    }

    @Override
    public boolean check(SoapHeader header) {
        return true;
    }

    @Override
    public ChannelEnums routeChannel(SoapHeader header) {

        return ChannelEnums.DIRECT_FORWARD;
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

    @Override
    public void init() {

    }

    @Override
    public void encryptData(EncryptionHelper encryptionHelper) {
        if (this.getBizRpt() != null) {
            this.getBizRpt().encryptData(encryptionHelper);
        }
    }

    @Override
    public void decryptData(EncryptionHelper encryptionHelper) {
        if (this.getBizRpt() != null) {
            this.getBizRpt().decryptData(encryptionHelper);
        }
    }
}
