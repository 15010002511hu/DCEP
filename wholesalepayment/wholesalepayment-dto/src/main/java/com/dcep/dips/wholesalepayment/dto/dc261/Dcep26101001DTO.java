/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto.dc261;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dcep.common.annotation.Channel;
import com.dcep.common.annotation.CheckAccountTag;
import com.dcep.common.annotation.CheckAccountTag.Type;
import com.dcep.common.annotation.Gateway;
import com.dcep.common.encryption.DataEncryption;
import com.dcep.common.encryption.EncryptionHelper;
import com.dcep.common.enums.ChannelEnums;
import com.dcep.common.enums.ErrorEnum;
import com.dcep.common.exception.DcepException;
import com.dcep.common.model.Response;
import com.dcep.common.model.soap.GrpHdr;
import com.dcep.common.model.soap.OrgnlGrpHdr;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.utils.CheckUtils;
import com.dcep.common.validator.Priority;
import com.dcep.dips.wholesalepayment.dto.ClearingDTO;
import com.dcep.dips.wholesalepayment.dto.ClearingStatus;
import com.dcep.dips.wholesalepayment.dto.RecordDTO;
import com.dcep.dips.wholesalepayment.enums.ClrAcctTpEnum;
import com.dcep.dips.wholesalepayment.enums.MsgTpEnum;
import com.dcep.dips.wholesalepayment.utils.DtoCheckUtil;
import com.dcep.infocache.validation.CheckClrGrpHdr;
import com.dcep.infocache.validation.CheckGrpHdrMsgId;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@JacksonXmlRootElement(localName = "OrdrConfReq", namespace = "http://www.dcep.com/dcep/26101001/")
@Setter
@Getter
@ToString(callSuper = true)
@Gateway(msgTp = "dcep.261.010.01", channel = @Channel(classname = ChannelEnums.CLEARING_FORWARD))
@CheckAccountTag(type = Type.WID, path = {"cdtr.cdtrWltId"})
public class Dcep26101001DTO extends ClearingStatus implements ClearingDTO, DataEncryption {

    /**  */
    private static final long serialVersionUID = -2909127509295695338L;

    /**
     * 【业务头组件】
     */
    @JacksonXmlProperty(localName = "GrpHdr")
    @NotNull(groups = Priority.Highest.class)
    @Valid
    @CheckGrpHdrMsgId(groups = Priority.Lowest.class)
    @CheckClrGrpHdr(groups = Priority.Lowest.class)
    private GrpHdr grpHdr;

    /**
     * 【交易信息】
     */
    @JacksonXmlProperty(localName = "TrxInf")
    @NotNull(groups = Priority.Highest.class)
    @Valid
    private TrxInf trxInf;

    /**
     * 【付款机构信息】
     */
    @JacksonXmlProperty(localName = "Dbtr")
    @NotNull(groups = Priority.Highest.class)
    @Valid
    private Dbtr dbtr;

    /**
     * 【收款钱包信息】
     */
    @JacksonXmlProperty(localName = "Cdtr")
    @NotNull(groups = Priority.Highest.class)
    @Valid
    private Cdtr cdtr;

    /**
     * 【订单信息】
     */
    @JacksonXmlProperty(localName = "OrderInf")
    @Valid
    private OrderInf orderInf;

    /**
     * 【用户移动终端信息】
     */
    @JacksonXmlProperty(localName = "UserTerInf")
    @Valid
    private UserTerInf userTerInf;

    @Override
    public String clrMsgTp() {
        // 报文编号
        return MsgTpEnum.ORDR_CONF_REQUEST.getCode();
    }

    @Override
    public String clrEndToEndId() {
        return null;
    }

    @Override
    public String getClrMsgId() {
        // 报文标识号
        return grpHdr.getMsgId();
    }

    @Override
    public String clrBizTp() {
        // 业务类型编码
        return trxInf.getTrxBizTp();
    }

    @Override
    public String clrBizKind() {
        // 业务种类编码
        return trxInf.getTrxCtgyCd();
    }

    @Override
    public String clrDbtrPtyId() {
        // 付款运营机构(付款方为发送方)
        return dbtr.getDbtrPtyId();
    }

    @Override
    public String clrCdtrPtyId() {
        // 收款运营机构(收款方为接收方)
        return cdtr.getCdtrPtyId();
    }

    @Override
    public String clrCurrency() {
        // 交易币种
        return trxInf.getTrxAmt().getCcy();
    }

    @Override
    public String clrAmt() {
        // 交易金额
        return trxInf.getTrxAmt().getValue();
    }

    @Override
    public String clrBizRspSts() {
        // 业务回执状态
        return null;
    }

    @Override
    public String clrBizRjctCd() {
        // 业务拒绝码
        return null;
    }

    @Override
    public String clrRjctResn() {
        // 业务拒绝原因
        return null;
    }

    @Override
    public String clrTrxInf() {
        // 交易描述信息(付款报文,交易描述信息为收款人钱包ID)
        return null;
    }

    @Override
    public String clrFlag() {
        return null;
    }

    @Override
    public void fillBatchId(Response<ClearingStatus> response) {
        // 向报文中赋交易批次号
    }

    @Override
    public String encode() {
        // 对象转换成档案记录JSON串
        return JSON.toJSONString(this);
    }

    @Override
    public RecordDTO decode(String encode) {
        return JSONObject.toJavaObject(JSON.parseObject(encode), this.getClass());
    }

    @Override
    public String recMsgTp() {
        // 报文编号
        return MsgTpEnum.ORDR_CONF_REQUEST.getCode();
    }

    @Override
    public String recMsgId() {
        // 报文标识号
        return grpHdr.getMsgId();
    }

    @Override
    public String clrDbtrWltId() {
        // 付款方钱包id
        return null;
    }

    @Override
    public String clrDbtrSysId() {
        return null;
    }

    @Override
    public String  clrCdtrWltId() {
        // 收款方钱包id
        return cdtr.getCdtrWltId();
    }

    @Override
    public String clrCdtrSysId() {
        return null;
    }

    @Override
    public String recOrgnlMsgTp() {
        // 原报文编号
        return null;
    }

    @Override
    public String recOrgnlMsgId() {
        // 原报文标识号
        return null;
    }

    /**
     * --------------GwDTO接口方法-------------
     */

    /**
     * @see com.dcep.common.model.GwDTO#init()
     */
    @Override
    public void init() {

    }

    /**
     * @see com.dcep.common.model.GwDTO#fetchMsgId() 获取msgId
     */
    @Override
    public String fetchMsgId() {
        return grpHdr.getMsgId();
    }

    /**
     * @see com.dcep.common.model.GwDTO#check(SoapHeader)
     */
    @Override
    public boolean check(SoapHeader soapHeader) {
        if (("TT01".equals(trxInf.getTrxTp()) || "TT02".equals(trxInf.getTrxTp()))
                && StringUtils.isBlank(trxInf.getQrCodeTp())) {
            throw new DcepException(ErrorEnum.VALIDATION_ERROR.getCode(), "当交易类型为TT01或TT02时，二维码类型必填");
        }

        String qrCodeTp = trxInf.getQrCodeTp();
        if (StringUtils.isNotBlank(qrCodeTp)) {
            // 当二维码类型为QT03时，二维码信息必填
            if ("QT03".equals(qrCodeTp) && StringUtils.isBlank(trxInf.getQrCode())) {
                throw new DcepException(ErrorEnum.VALIDATION_ERROR.getCode(), "当二维码类型为QT03时，二维码必填");
            }

            // 当二维码类型为QT04时，订单信息必填
            if ("QT04".equals(qrCodeTp) && Objects.isNull(orderInf)) {
                throw new DcepException(ErrorEnum.VALIDATION_ERROR.getCode(), "当二维码类型为QT04时，订单信息必填");
            }
        }

        return !CheckUtils.requestMsgChk(soapHeader, grpHdr) ? false
                : DtoCheckUtil.checkPayerMsgInst(soapHeader.getSender(), this.clrDbtrPtyId(), soapHeader.getReceiver(),
                        this.clrCdtrPtyId());
    }

    @Override
    public void clrBatId(String clrBatId) {
        // 交易批次号
    }

    @Override
    public String clrBatId() {
        return null;
    }

    @Override
    public String clrTransTp() {
        return null;
    }

    @Override
    public void clrBizRspSts(String clrBizRspSts) {
        // 业务回执状态

    }

    @Override
    public void clrBizRjctCd(String clrBizRjctCd) {
        // 业务拒绝码

    }

    @Override
    public void clrRjctResn(String clrRjctResn) {
        // 业务拒绝原因

    }

    @Override
    public String clrCreDtTm() {
        return grpHdr.getCreDtTm();
    }

    @Override
    public String clrSendPtyId() {
        return grpHdr.getInstgPty().getInstgDrctPty();
    }

    @Override
    public String clrSendSysId() {
        return null;
    }

    @Override
    public String clrRecvPtyId() {
        return null;
    }

    @Override
    public String clrRecvSysId() {
        return null;
    }

    @Override
    public void encryptData(EncryptionHelper encryptionHelper) {
        List<String> encryptList = encryptionHelper.encrypt(fetchEncryptionFeatures());
        encryptionFeaturesAssign(encryptList);
    }

    @Override
    public void decryptData(EncryptionHelper encryptionHelper) {
        List<String> decryptList = encryptionHelper.decrypt(fetchEncryptionFeatures());
        encryptionFeaturesAssign(decryptList);
    }

    @Override
    public OrgnlGrpHdr presumeConfirm() {
        return null;
    }

    @Override
    public String clrPresumeTm() {
        // 取订单有效期
        return null;
    }

    @Override
    public String clrAcctTp() {
        return ClrAcctTpEnum.PAY.getCode();
    }

    public List<String> fetchEncryptionFeatures() {
        // 获取需加解密处理的敏感要素，若为空则赋值为null，保证前后顺序一致
        List<String> data = new ArrayList<>();
        if (null != cdtr && null != cdtr.getCdtrWltId()) {
            data.add(cdtr.getCdtrWltId());
        }
        return data;
    }

    public void encryptionFeaturesAssign(List<String> encryptionFeatures) {
        // 按顺序赋值加解密处理后的敏感要素
        if (null != cdtr && null != cdtr.getCdtrWltId()) {
            cdtr.setCdtrWltId(encryptionFeatures.get(0));
        }
    }

}
