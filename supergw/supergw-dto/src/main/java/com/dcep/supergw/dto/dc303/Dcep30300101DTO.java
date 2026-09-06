package com.dcep.supergw.dto.dc303;

import com.dcep.common.annotation.Channel;
import com.dcep.common.annotation.CheckAccountTag;
import com.dcep.common.annotation.CheckAccountTag.Type;
import com.dcep.common.annotation.CheckBizCode;
import com.dcep.common.annotation.Gateway;
import com.dcep.common.enums.ChannelEnums;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.GrpHdr;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.validator.Priority;
import com.dcep.infocache.validation.CheckGrpHdrMsgId;
import com.dcep.infocache.validation.CheckGrpHdrOrgId;
import com.dcep.supergw.validation.Check303Biz;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * 3.6.1 统一收单支付请求报文<dcep.303.001.01>
 * 
 * @author duzhong
 * @version $Id: Dcep30300101DTO.java, v 0.1 2021年04月14日 上午10:07:20 duzhong Exp $
 *
 */
@JacksonXmlRootElement(localName = "PmtOrdrReq", namespace = "http://www.dcep.com/dcep/30300101/")
@Setter
@Getter
@ToString
@Gateway(msgTp = "dcep.303.001.01", channel = @Channel(classname = ChannelEnums.DIRECT_FORWARD))
@Check303Biz(groups = Priority.Lowest.class)
@CheckBizCode(bizCtgyCode = "trxInf.trxCtgyCd", bizTypeCode = "trxInf.trxBizTp")
@CheckAccountTag(type = Type.WID, path = {"cdtrInf.mrchntWltId"})
public class Dcep30300101DTO extends GwDTO {

    /**
     * 
     */
    private static final long serialVersionUID = -1283364706006772513L;

    public Dcep30300101DTO() {

    }

    /**
     * Body报文体
     */

    /**
     * 业务头组件GrpHdr
     */
    @JacksonXmlProperty(localName = "GrpHdr")
    @NotNull(groups = Priority.Highest.class)
    @CheckGrpHdrOrgId(groups = Priority.Lowest.class)
    @CheckGrpHdrMsgId(groups = Priority.Lowest.class)
    @Valid
    private GrpHdr grpHdr;

    /**
     * 交易信息
     */
    @JacksonXmlProperty(localName = "TrxInf")
    @NotNull(groups = Priority.Highest.class)
    @Valid
    private TrxInf trxInf;
    /**
     * 受理方机构信息
     */
    @JacksonXmlProperty(localName = "AcqAgtInf")
    @NotNull(groups = Priority.Highest.class)
    @Valid
    private AcqAgtInf acqAgtInf;
    /**
     * 收款方运营机构信息
     */
    @JacksonXmlProperty(localName = "CdtrInf")
    @NotNull(groups = Priority.Highest.class)
    @Valid
    private CdtrInf cdtrInf;
    /**
     * 二级商户信息
     */
    @Valid
    @JacksonXmlProperty(localName = "SubMrchntInf")
    private SubMrchntInf subMrchntInf;
    /**
     * 商户受理终端信息
     */
    @Valid
    @JacksonXmlProperty(localName = "MrchntTerInf")
    private MrchntTerInf mrchntTerInf;
    /**
     * 订单信息
     */
    @JacksonXmlProperty(localName = "OrdrInf")
    @NotNull(groups = Priority.Highest.class)
    @Valid
    private OrdrInf ordrInf;

    @JacksonXmlProperty(localName = "QrCodeInf")
    @NotNull
    @Valid
    private QrCodeInf qrCodeInf;

    /**
     * 收款人合约实例信息
     */
    @JacksonXmlProperty(
            localName = "CdtrCtrctInst"
    )
    @Valid
    private CdtrCtrctInst cdtrCtrctInst;

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
    public ChannelEnums routeChannel(SoapHeader header) {

        return ChannelEnums.DIRECT_FORWARD;
    }

    @Override
    public String fetchResultCode() {
        return "PR00-";
    }
}
