package com.dcep.supergw.dto.dc305;

import com.dcep.common.annotation.Channel;
import com.dcep.common.annotation.CheckAccountTag;
import com.dcep.common.annotation.CheckAccountTag.Type;
import com.dcep.common.annotation.CheckBizCode;
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
import com.dcep.supergw.validation.Check305Biz;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author weiqianjing:<dcep.305.001.01>
 * @Date 2021/9/26 15:30
 * @Description:钱包用户在商户侧使用子钱包支付，在免密额度内，可以直接完成支付
 */
@JacksonXmlRootElement(localName = "SubWltPmtReq", namespace = "http://www.dcep.com/dcep/30500101/")
@Setter
@Getter
@ToString
@Gateway(msgTp = "dcep.305.001.01", channel = @Channel(classname = ChannelEnums.DIRECT_FORWARD))
@Check305Biz
@CheckBizCode(bizCtgyCode = "trxInf.trxCtgyCd", bizTypeCode = "trxInf.trxBizTp")
@CheckAccountTag(type = Type.WID, path = {"cdtrInf.mrchntWltId"})
@CheckAccountTag(type = Type.SUB_WALLET_TOKEN, path = {"sgnInf.ptcId"})
public class Dcep30500101DTO extends GwDTO implements TransEncryption {
    private static final long serialVersionUID = -3186135569514180027L;
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
     * 受理机构信息
     */
    @JacksonXmlProperty(localName = "AcqAgtInf")
    @NotNull(groups = Priority.Highest.class)
    @Valid
    private AcqAgtInf acqAgtInf;
    /**
     * 收款运营机构信息
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
    /**
     * 签约人信息 TT11必填
     */
    @JacksonXmlProperty(localName = "SgnInf")
    @Valid
    private SgnInf sgnInf;

    /**
     * TT12必填
     */
    @JacksonXmlProperty(localName = "PwdInf")
    @Valid
    private PwdInf pwdInf;

    /**
     * apud扩展信息
     */
    @JacksonXmlProperty(localName = "ExInf")
    @Valid
    private ExInf exInf;

    /**
     * 收款人合约实例信息
     */
    @JacksonXmlProperty(
            localName = "CdtrCtrctInst"
    )
    @Valid
    private CdtrCtrctInst cdtrCtrctInst;

    /**
     * 签约人合约实例信息
     */
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "SgnCtrctInst")
    @Valid
    private List<SgnCtrctInst> sgnCtrctInst;

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
        if (pwdInf != null) {
            this.pwdInf.transEncrypt(encryptionHelper);
        }
    }
}
