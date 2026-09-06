/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto.dc262;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dcep.common.annotation.*;
import com.dcep.common.annotation.CheckAccountTag.Type;
import com.dcep.common.encryption.DataEncryption;
import com.dcep.common.encryption.EncryptionHelper;
import com.dcep.common.enums.ChannelEnums;
import com.dcep.common.enums.ErrorEnum;
import com.dcep.common.exception.DcepException;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.Response;
import com.dcep.common.model.soap.GrpHdr;
import com.dcep.common.model.soap.OrgnlGrpHdr;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.utils.CheckUtils;
import com.dcep.common.validator.Priority;
import com.dcep.dips.common.constant.ChnlSysEnum;
import com.dcep.dips.wholesalepayment.annotation.Clearing;
import com.dcep.dips.wholesalepayment.annotation.Record;
import com.dcep.dips.wholesalepayment.constants.Constant;
import com.dcep.dips.wholesalepayment.dto.ClearingDTO;
import com.dcep.dips.wholesalepayment.dto.ClearingStatus;
import com.dcep.dips.wholesalepayment.dto.RecordDTO;
import com.dcep.dips.wholesalepayment.dto.common.Common;
import com.dcep.dips.wholesalepayment.dto.common.MrchntTerInf;
import com.dcep.dips.wholesalepayment.dto.common.SubMrchntInf;
import com.dcep.dips.wholesalepayment.enums.*;
import com.dcep.dips.wholesalepayment.utils.DtoCheckUtil;
import com.dcep.infocache.manager.NacosConsume;
import com.dcep.infocache.validation.CheckClrGrpHdr;
import com.dcep.infocache.validation.CheckClrOrgnlGrpHdr;
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

@JacksonXmlRootElement(localName = "OrdrQryRsp", namespace = "http://www.dcep.com/dcep/26201001/")
@Setter
@Getter
@ToString(callSuper = true)
@Gateway(msgTp = "dcep.262.010.01", channel = @Channel(classname = ChannelEnums.CLEARING_FORWARD, services = {
    @RpcInfo(name = Constant.NAME_PAYMENT, methods = {@GwMethod(name = Constant.ORDER_CONFIRM)})}))
@Clearing(presumeStatus = ClearingStatusEnum.PRESUME_FAILED, action = ClearingActionEnum.PREPARE, instgDrctPty = InstgDrctPtyEnum.CDTR, checkMode = ClearingCheckModeEnum.PAYEE_CHECK, confirmTimeout = 300, notityTimeout = 300, cdtDbtInd = ClearingProdCdtDbtIndEnum.DBIT)
@Record(saveMode = RecordSaveModeEnum.ALL)
@CheckBizCode(condition = "rspsnInf.rspsnSts,PR02", bizTypeCode = "trxInf.trxBizTp", bizCtgyCode = "trxInf.trxCtgyCd")
@CheckAccountTag(type = Type.WID, path = {"cdtr.cdtrWltId"})
public class Dcep26201001DTO extends GwDTO implements ClearingDTO, DataEncryption {

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
     * 【原业务头组件】
     */
    @JacksonXmlProperty(localName = "OrgnlGrpHdr")
    @NotNull(groups = Priority.Highest.class)
    @CheckClrOrgnlGrpHdr
    @Valid
    private OrgnlGrpHdr orgnlGrpHdr;

    /**
     * 【回执信息】
     */
    @JacksonXmlProperty(localName = "RspsnInf")
    @NotNull(groups = Priority.Highest.class)
    @Valid
    private RspsnInf rspsnInf;

    /**
     * 【交易信息】
     */
    @JacksonXmlProperty(localName = "TrxInf")
    @NotNull(groups = Priority.Highest.class)
    @Valid
    private TrxInf trxInf;

    /**
     * 【付款钱包信息】
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
     * 【二级商户信息】
     */
    @JacksonXmlProperty(localName = "SubMrchntInf")
    @Valid
    private SubMrchntInf subMrchntInf;

    /**
     * 【订单信息】
     */
    @JacksonXmlProperty(localName = "OrderInf")
    @Valid
    private OrderInf orderInf;

    /**
     * 【受理服务机构信息】
     */
    @JacksonXmlProperty(localName = "AcqAgtInf")
    @Valid
    private AcqAgtInf acqAgtInf;

    /**
     * 【合约信息】
     */
    @JacksonXmlProperty(localName = "CdtrCtrctInst")
    @Valid
    private CdtrCtrctInst cdtrCtrctInst;

    /**
     * 【商户受理终端信息】
     */
    @JacksonXmlProperty(localName = "MrchntTerInf")
    @Valid
    private MrchntTerInf mrchntTerInf;

    @Override
    public String clrMsgTp() {
        // 报文编号
        return MsgTpEnum.ORDR_CONF_RESPONSE.getCode();
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
        // 付款运营机构
        return dbtr.getDbtrPtyId();
    }

    @Override
    public String clrCdtrPtyId() {
        // 收款运营机构
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
        return rspsnInf.getRspsnSts();
    }

    @Override
    public String clrBizRjctCd() {
        // 业务拒绝码
        return rspsnInf.getRjctCd();
    }

    @Override
    public String clrRjctResn() {
        // 业务拒绝原因
        return rspsnInf.getRjctInf();
    }

    @Override
    public String clrTrxInf() {
        // 交易描述信息
        return null;
    }

    @Override
    public String clrFlag() {
        return ClrFlgEnum.YES.getCode();
    }

    @Override
    public void fillBatchId(Response<ClearingStatus> response) {
        // 向报文中赋交易批次号
        trxInf.setBatchId(response.getResult().getBatchId());
    }


    @Override
    public String fetchResultCode() {
        if (rspsnInf.getRspsnSts() != null) {
            return rspsnInf.getRspsnSts() + "-"
                + (ClearingStatusEnum.FAILED.getCode().equals(rspsnInf.getRspsnSts())
                ? ClearingPrcCdEnum.BUSI_REJT.getCode()
                : ClearingPrcCdEnum.BUSI_SUCCESS.getCode());
        }
        return "";
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
        return MsgTpEnum.ORDR_CONF_RESPONSE.getCode();
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
        return ChnlSysEnum.DCEP.getCode();
    }

    @Override
    public String clrCdtrWltId() {
        // 收款方钱包id
        return cdtr.getCdtrWltId();
    }

    @Override
    public String clrCdtrSysId() {
        return ChnlSysEnum.DCEP.getCode();
    }

    @Override
    public String recOrgnlMsgTp() {
        // 原报文编号
        return orgnlGrpHdr.getOrgnlMT();
    }

    @Override
    public String recOrgnlMsgId() {
        // 原报文标识号
        return orgnlGrpHdr.getOrgnlMsgId();
    }

    /**
     * --------------GwDTO接口方法-------------
     */

    /**
     * @see GwDTO#init()
     */
    @Override
    public void init() {

    }

    /**
     * @see GwDTO#fetchMsgId() 获取msgId
     */
    @Override
    public String fetchMsgId() {
        return grpHdr.getMsgId();
    }

    /**
     * @see GwDTO#check(SoapHeader)
     */
    @Override
    public boolean check(SoapHeader soapHeader) {
        // 1.校验262msgId的(13,16)这3位与msgTp的中间3位是否一致
        /**
         * 262报文的msgId需返回262的msgId类型，不能返回其他msgId类型
         */
        String bodyMsgIdMsgTp = grpHdr.getMsgId().substring(12, 15);
        String msgTpNum = soapHeader.getMsgTp().substring(5, 8);
        if (!msgTpNum.equals(bodyMsgIdMsgTp)) {
            throw new DcepException(ErrorEnum.MSGTP_NOT_IN_MSGID_ERROR);
        }

        // 2.262报文通信级标识号msgSn = msgid261+4，需检查控制位是否一致，避免跨机房访问数据库
        if (!this.getClrMsgId().substring(29, 31).equals(soapHeader.getMsgSN().substring(29, 31))) {
            throw new DcepException(ErrorEnum.VALIDATION_ERROR.getCode(), "通信级标识号与报文标识号控制位不匹配");
        }

        // 3.业务回执状态为PR02时，报文检查
        if (ClearingStatusEnum.PROCESS.getCode().equals(rspsnInf.getRspsnSts())) {
            // 交易批次号必填
            if (StringUtils.isBlank(trxInf.getBatchId())) {
                throw new DcepException(ErrorEnum.VALIDATION_ERROR.getCode(), "当业务回执状态为PR02时，交易批次号必填");
            }
            // 业务类型及业务种类必填
            if (StringUtils.isBlank(trxInf.getTrxBizTp()) || StringUtils.isBlank(trxInf.getTrxCtgyCd())) {
                throw new DcepException(ErrorEnum.VALIDATION_ERROR.getCode(), "当业务回执状态为PR02时，业务类型及业务种类必填");
            }
            // 受理订单号必填
            if (null != cdtr && StringUtils.isBlank(cdtr.getOutOrdrNo())) {
                throw new DcepException(ErrorEnum.VALIDATION_ERROR.getCode(), "当业务回执状态为PR02时，受理订单号必填");
            }
            // 订单信息必填
            if (Objects.isNull(orderInf)) {
                throw new DcepException(ErrorEnum.VALIDATION_ERROR.getCode(), "当业务回执状态为PR02时，订单信息必填");
            }
            // 居民类型必填
            if (StringUtils.isBlank(orderInf.getResdtTp())) {
                throw new DcepException(ErrorEnum.VALIDATION_ERROR.getCode(), "当业务回执状态为PR02时，居民类型必填");
            }
            // 常驻国家/地区代码必填
            if (StringUtils.isBlank(orderInf.getResdtCtryCd())) {
                throw new DcepException(ErrorEnum.VALIDATION_ERROR.getCode(), "当业务回执状态为PR02时，常驻国家/地区代码必填");
            }
        }

        //当商户属性为MP02或MP03时，网络交易平台名称、商户经营地址必填
        if ((null != cdtr) && ("MP02".equals(cdtr.getMrchntPrprty()) || "MP03".equals(cdtr.getMrchntPrprty()))) {
            // 网络交易平台名称必填
            if (null != mrchntTerInf && StringUtils.isBlank(mrchntTerInf.getPltfrmNm())) {
                throw new DcepException(ErrorEnum.VALIDATION_ERROR.getCode(), "当商户属性为MP02或MP03时，网络交易平台名称必填");
            }
        }

        //当商户属性为MP01或MP03时，网络交易平台名称、商户经营地址必填
        if ((null != cdtr) && ("MP01".equals(cdtr.getMrchntPrprty()) || "MP03".equals(cdtr.getMrchntPrprty()))) {
            // 商户经营地址必填
            if (null != mrchntTerInf && StringUtils.isBlank(mrchntTerInf.getMrchntBizAddr())) {
                throw new DcepException(ErrorEnum.VALIDATION_ERROR.getCode(), "当商户属性为MP01或MP03时，商户经营地址必填");
            }
        }
        
        if (ClearingSwitchEnum.OPEN.getCode().equals(NacosConsume.getCredttmInterval())) {
            // 商户英文简称必填
            if ((null != cdtr) && StringUtils.isBlank(cdtr.getMrchntEnAbbrNm())) {
                throw new DcepException(ErrorEnum.VALIDATION_ERROR.getCode(), "商户英文简称必填");
            }
        }

        return !CheckUtils.responseMsgChk(soapHeader, grpHdr, orgnlGrpHdr) ? false
                : DtoCheckUtil.checkPayeeMsgInst(soapHeader.getSender(), this.clrDbtrPtyId(), soapHeader.getReceiver(),
                        this.clrCdtrPtyId());
    }

    @Override
    public void clrBatId(String clrBatId) {
        // 交易批次号
        trxInf.setBatchId(clrBatId);
    }

    @Override
    public String clrBatId() {
        return trxInf.getBatchId();
    }

    @Override
    public String clrTransTp() {
        return TransTpEnum.NORMAL_TRANS.getCode();
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
        return ChnlSysEnum.DCEP.getCode();
    }

    @Override
    public String clrRecvPtyId() {
        return grpHdr.getInstdPty().getInstdDrctPty();
    }

    @Override
    public String clrRecvSysId() {
        return ChnlSysEnum.DCEP.getCode();
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
        OrgnlGrpHdr orgnlGrpHdr = new OrgnlGrpHdr();
        orgnlGrpHdr.setOrgnlInstgPty(this.getGrpHdr().getInstgPty().getInstgDrctPty());
        orgnlGrpHdr.setOrgnlMsgId(this.getGrpHdr().getMsgId());
        orgnlGrpHdr.setOrgnlMT(MsgTpEnum.ORDR_CONF_RESPONSE.getCode());
        return orgnlGrpHdr;
    }

    @Override
    public String clrPresumeTm() {
        // 取订单有效期
        if (orderInf != null && StringUtils.isNotBlank(orderInf.getOrdrExp())) {
            long ordrExp = Long.parseLong(orderInf.getOrdrExp());
            if (ordrExp <= 5){
                return Common.MIN_ORDER_EXPIRY_TIME;
            }
            if (ordrExp > 5 && ordrExp <= 300){
                return orderInf.getOrdrExp();
            }
            if (ordrExp > 300){
                return Common.MAX_ORDER_EXPIRY_TIME;
            }
        }
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

    @Override
    public String clrSysWorkDt(){
        return trxInf.getSysWorkDt();
    }
}
