package com.dcep.dips.wholesalepayment.dto.dc112;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dcep.common.annotation.*;
import com.dcep.common.encryption.DataEncryption;
import com.dcep.common.encryption.EncryptionHelper;
import com.dcep.common.enums.ChannelEnums;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.Response;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.validator.Priority;
import com.dcep.dips.common.constant.ChnlSysEnum;
import com.dcep.dips.wholesalepayment.annotation.Clearing;
import com.dcep.dips.wholesalepayment.annotation.Record;
import com.dcep.dips.wholesalepayment.constants.Constant;
import com.dcep.dips.wholesalepayment.dto.ClearingDTO;
import com.dcep.dips.wholesalepayment.dto.ClearingStatus;
import com.dcep.dips.wholesalepayment.dto.RecordDTO;
import com.dcep.dips.wholesalepayment.enums.*;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * dcep.112.001.01DTO
 *
 * @Author zhaotianwu
 * @date 2025-10-11 15:55:58
 */

@Gateway(msgTp = "dcep.112.001.01", channel = @Channel(classname = ChannelEnums.CENTRAL_PROCESS, services = {@RpcInfo(name = Constant.NAME_PAYMENT, methods = {@GwMethod(name = Constant.DBTR_SETTLE)})}))
@JacksonXmlRootElement(localName = "FICdtTrf", namespace = "http://www.dcep.com/dcep/11200101/")

@Setter
@Getter
@ToString(callSuper = true)
@Clearing(presumeStatus = ClearingStatusEnum.PRESUME_SUCCESS, action = ClearingActionEnum.PREPARE, instgDrctPty = InstgDrctPtyEnum.DBTR, checkMode = ClearingCheckModeEnum.PAYER_CHECK, cdtDbtInd = ClearingProdCdtDbtIndEnum.CRDT)
@Record(saveMode = RecordSaveModeEnum.ALL)

public class Dcep11200101DTO extends GwDTO implements ClearingDTO, DataEncryption {
    /**
     * 组件GroupHeader
     */
    @JacksonXmlProperty(localName = "GrpHdr")
    @NotNull(groups = Priority.Highest.class)
    @Valid
    private GrpHdr grpHdr;

    /**
     * 组件CreditTransferTransactionInformation
     */
    @JacksonXmlProperty(localName = "CdtTrfTxInf")
    @NotNull
    @Valid
    private CdtTrfTxInf cdtTrfTxInf;

    /**
     * @see com.dcep.common.model.GwDTO#init()
     */
    @Override
    public void init() {
        //todo init
//        rmtInf(this.getCdtTrfTxInf().getUstrds());
    }

    @Override
    public String fetchMsgId() {
        return grpHdr.getMsgId();
    }

    @Override
    public boolean check(SoapHeader soapHeader) {
        //todo 报文字段检查，待补充内容很多,后续补充，先可以不做
        return true;
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

    /**
     * 交易批次号
     *
     * @param clrBatId
     */
    @Override
    public void clrBatId(String clrBatId) {
        //无需处理批次号，这里set实现了，但是实际没人会调用
        //注意批次号在<SplmtryData>标签下
        cdtTrfTxInf.getSplmtryData().getEnvlp().setBatchId(clrBatId);
    }

    @Override
    public String clrBatId() {
        //机构可能会调用，所以这里实现了get方法
        //注意批次号在<SplmtryData>标签下
        return   cdtTrfTxInf.getSplmtryData().getEnvlp().getBatchId();
    }

    @Override
    public String clrTransTp() {
        return TransTpEnum.NORMAL_TRANS.getCode();
    }

    /**
     * 报文编号
     */
    @Override
    public String clrMsgTp() {
        //新增一个112的枚举
        return MsgTpEnum.FI_CDT.getCode();
    }

    /**
     * 端到端流水号
     */
    @Override
    public String clrEndToEndId() {
        return cdtTrfTxInf.getPmtId().getEndToEndId();
    }

    /**
     * 报文标识号
     */
    @Override
    public String getClrMsgId() {
        return grpHdr.getMsgId();
    }

    /**
     * 业务类型编码
     */
    @Override
    public String clrBizTp() {
        return cdtTrfTxInf.getPmtTpInf().getCtgyPurp().getPrtry();
    }

    /**
     * 业务种类编码
     */
    @Override
    public String clrBizKind() {
        return cdtTrfTxInf.getPurp().getPrtry();
    }

    /**
     * 付款运营机构
     */
    @Override
    public String clrDbtrPtyId() {
        //InstgAgt里面的FinInstnId类型，不要使用自动生成的，使用同一个组件：com.dcep.common.model.soap.FinInstnId
        return cdtTrfTxInf.getInstgAgt().getFinInstnId().getClrSysMmbId().getMmbId();
    }

    /**
     * 付款ID
     */
    @Override
    public String clrDbtrWltId() {
        return cdtTrfTxInf.getDbtrAcct().getId().getOthr().getId();
    }

    /**
     * 付款系统标识
     */
    @Override
    public String clrDbtrSysId() {
        // DCEP("DCEP", "数字人民币系统"),
        return ChnlSysEnum.DCEP.getCode();
    }

    /**
     * 收款运营机构
     */
    @Override
    public String clrCdtrPtyId() {
        //InstdAgt里面的FinInstnId类型，不要使用自动生成的，使用同一个组件：com.dcep.common.model.soap.FinInstnId
        return cdtTrfTxInf.getInstdAgt().getFinInstnId().getClrSysMmbId().getMmbId();
    }

    /**
     * 收款ID
     */
    @Override
    public String clrCdtrWltId() {
        return cdtTrfTxInf.getCdtrAcct() != null ? cdtTrfTxInf.getCdtrAcct().getId().getOthr().getId() : null;
    }

    /**
     * 收款系统标识
     */
    @Override
    public String clrCdtrSysId() {
        // DCEP("DCEP", "数字人民币系统"),
        return ChnlSysEnum.DCEP.getCode();
    }

    /**
     * Database Column Remarks: 交易币种
     */
    @Override
    public String clrCurrency() {
        return cdtTrfTxInf.getIntrBkSttlmAmt().getCcy();
    }

    /**
     * 交易金额
     */
    @Override
    public String clrAmt() {
        return cdtTrfTxInf.getIntrBkSttlmAmt().getValue();
    }

    /**
     * 业务回执状态
     */
    @Override
    public String clrBizRspSts() {
        //因为，发送112给收款机构时，一定是成功的，所以这里就不用带状态值。
        return null;
    }

    /**
     * 业务回执状态，赋值方法
     *
     * @param clrBizRspSts
     */
    @Override
    public void clrBizRspSts(String clrBizRspSts) {
        //因为，发送112给收款机构时，一定是成功的，所以这里就不用带状态值。
    }

    /**
     * 业务拒绝码
     */
    @Override
    public String clrBizRjctCd() {
        // 无需处理
        return null;
    }

    /**
     * 业务拒绝码，赋值方法
     *
     * @param clrBizRjctCd
     */
    @Override
    public void clrBizRjctCd(String clrBizRjctCd) {
        // 无需处理
    }

    /**
     * 业务拒绝原因
     */
    @Override
    public String clrRjctResn() {
        // 无需处理
        return null;
    }

    /**
     * 业务拒绝原因，赋值方法
     *
     * @param clrRjctResn
     */
    @Override
    public void clrRjctResn(String clrRjctResn) {
        // 无需处理
    }

    /**
     * 交易描述信息
     */
    @Override
    public String clrTrxInf() {
        // 交易描述信息(付款报文,交易描述信息为收款人钱包ID), 但是目前只有一个收款钱包id。已确认无需处理。
        return cdtTrfTxInf.getCdtrAcct() != null ? cdtTrfTxInf.getCdtrAcct().getId().getOthr().getId() : null;
    }

    /**
     * 对账标识 "0:不对账 1:对账"
     */
    @Override
    public String clrFlag() {
        return ClrFlgEnum.YES.getCode();
    }

    /**
     * 账务时间
     */
    @Override
    public String clrCreDtTm() {
        return grpHdr.getCreDtTm();
    }

    /**
     * 发送方机构
     */
    @Override
    public String clrSendPtyId() {
        return cdtTrfTxInf.getInstgAgt().getFinInstnId().getClrSysMmbId().getMmbId();
    }

    /**
     * 发送系统标识
     */
    @Override
    public String clrSendSysId() {
        return ChnlSysEnum.DCEP.getCode();
    }

    /**
     * 接收方机构
     */
    @Override
    public String clrRecvPtyId() {
        return cdtTrfTxInf.getInstdAgt().getFinInstnId().getClrSysMmbId().getMmbId();
    }

    /**
     * 接收系统标识
     */
    @Override
    public String clrRecvSysId() {
        return ChnlSysEnum.DCEP.getCode();
    }

    /**
     * 超时推定时间
     */
    @Override
    public String clrPresumeTm() {
        return null;
    }

    /**
     * 赋值batchID
     *
     * @param response
     */
    @Override
    public void fillBatchId(Response<ClearingStatus> response) {
        // 向报文中赋交易批次号
        cdtTrfTxInf.getSplmtryData().getEnvlp().setBatchId(response.getResult().getBatchId());
    }

    /**
     * 记账类型
     */
    @Override
    public String clrAcctTp() {
        return ClrAcctTpEnum.PAY.getCode();
    }

    @Override
    public String encode() {
        return JSON.toJSONString(this);
    }

    @Override
    public RecordDTO decode(String encode) {
        return JSONObject.toJavaObject(JSON.parseObject(encode), this.getClass());
    }

    @Override
    public String recMsgId() {
        return grpHdr.getMsgId();
    }

    @Override
    public String recMsgTp() {
        return MsgTpEnum.FI_CDT.getCode();
    }

    @Override
    public String recOrgnlMsgId() {
        return null;
    }

    @Override
    public String recOrgnlMsgTp() {
        return null;
    }

    public List<String> fetchEncryptionFeatures() {
        //Identification
        /**
         * Message root	<FICdtTrf>
         * GroupHeader	<GrpHdr>
         * CreditTransferTransactionInformation	<CdtTrfTxInf>
         * --DebtorAccount	<DbtrAcct>
         * ----Identification	<Id>
         * ------Other	<Othr>
         * --------Identification	<Id>	√
         * 付款方钱包ID
         * --CreditorAccount	<CdtrAcct>
         * ----Identification	<Id>
         * ------Other	<Othr>
         * --------Identification	<Id>	√
         * 收款方钱包ID
         * */

        // 获取需加解密处理的敏感要素，若为空则赋值为null，保证前后顺序一致
        //付款方钱包ID
        List<String> data = new ArrayList<>();
        if (cdtTrfTxInf != null && cdtTrfTxInf.getDbtrAcct() != null
                && cdtTrfTxInf.getDbtrAcct().getId() != null
                && cdtTrfTxInf.getDbtrAcct().getId().getOthr() != null
                && cdtTrfTxInf.getDbtrAcct().getId().getOthr().getId() != null) {
            data.add(cdtTrfTxInf.getDbtrAcct().getId().getOthr().getId());
        }
        //收款方钱包ID
        if (cdtTrfTxInf != null && cdtTrfTxInf.getCdtrAcct() != null
                && cdtTrfTxInf.getCdtrAcct().getId() != null
                && cdtTrfTxInf.getCdtrAcct().getId().getOthr() != null
                && cdtTrfTxInf.getCdtrAcct().getId().getOthr().getId() != null) {
            data.add(cdtTrfTxInf.getCdtrAcct().getId().getOthr().getId());
        }
        return data;
    }

    public void encryptionFeaturesAssign(List<String> encryptionFeatures) {

        int index = 0;
        // 按顺序赋值加解密处理后的敏感要素
        //付款方钱包ID
        if (cdtTrfTxInf != null && cdtTrfTxInf.getDbtrAcct() != null
                && cdtTrfTxInf.getDbtrAcct().getId() != null
                && cdtTrfTxInf.getDbtrAcct().getId().getOthr() != null
                && cdtTrfTxInf.getDbtrAcct().getId().getOthr().getId() != null) {
            cdtTrfTxInf.getDbtrAcct().getId().getOthr().setId(encryptionFeatures.get(index++));
        }

        //收款方钱包ID
        if (cdtTrfTxInf != null && cdtTrfTxInf.getCdtrAcct() != null
                && cdtTrfTxInf.getCdtrAcct().getId() != null
                && cdtTrfTxInf.getCdtrAcct().getId().getOthr() != null
                && cdtTrfTxInf.getCdtrAcct().getId().getOthr().getId() != null) {
            cdtTrfTxInf.getCdtrAcct().getId().getOthr().setId(encryptionFeatures.get(index++));
        }

    }

    /**
     * 业务优先级
     */
    @Override
    public String clrBizPrty() {
        //实现ClearingDTO默认的default的3个方法：业务优先级、系统工作日期、填充结算日期
        return cdtTrfTxInf.getSttlmPrty();
    }

    /**
     * 系统工作日期
     */
    @Override
    public String clrSysWorkDt() {
        return cdtTrfTxInf.getSplmtryData().getEnvlp().getSysWorkDt();
    }

    /**
     * 填充结算日期SplmtryData
     *
     * @param sttlmDt
     */
    @Override
    public void fillSttlmDt(String sttlmDt) {
        cdtTrfTxInf.getSplmtryData().getEnvlp().setSttlmDt(sttlmDt);
    }
}
