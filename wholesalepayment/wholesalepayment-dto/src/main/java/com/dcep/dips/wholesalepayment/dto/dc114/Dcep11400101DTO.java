package com.dcep.dips.wholesalepayment.dto.dc114;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dcep.common.annotation.Channel;
import com.dcep.common.annotation.Gateway;
import com.dcep.common.annotation.GwMethod;
import com.dcep.common.annotation.RpcInfo;
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
 * dcep.114.001.01DTO
 *
 * @Author zhaotianwu
 * @date 2025-10-11 17:08:04
 */
@Gateway(msgTp = "dcep.114.001.01", channel = @Channel(classname = ChannelEnums.CENTRAL_PROCESS, services = {@RpcInfo(name = Constant.NAME_PAYMENT, methods = {@GwMethod(name = Constant.DBTR_SETTLE)})}))
@JacksonXmlRootElement(localName = "FICdtTrf", namespace = "http://www.dcep.com/dcep/11400101/")

@Setter
@Getter
@ToString(callSuper = true)
@Clearing(presumeStatus = ClearingStatusEnum.PRESUME_SUCCESS, action = ClearingActionEnum.PREPARE, instgDrctPty = InstgDrctPtyEnum.DBTR, checkMode = ClearingCheckModeEnum.PAYER_CHECK, cdtDbtInd = ClearingProdCdtDbtIndEnum.CRDT)
@Record(saveMode = RecordSaveModeEnum.ALL)

public class Dcep11400101DTO extends GwDTO implements ClearingDTO, DataEncryption {
    /**
     * 组件GroupHeader
     */
    @JacksonXmlProperty(localName = "GrpHdr")
    @NotNull(groups = Priority.Highest.class)
    @Valid
    private GrpHdr grpHdr;

    /**
     * 组件TransactionInformation
     */
    @JacksonXmlProperty(localName = "TxInf")
    @NotNull
    @Valid
    private TxInf txInf;

    @Override
    public void init() {
    }

    @Override
    public String fetchMsgId() {
        return grpHdr.getMsgId();
    }

    @Override
    public boolean check(SoapHeader soapHeader) {
        // todo 字段检查
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
        txInf.getSplmtryData().getEnvlp().setBtchId(clrBatId);
    }

    @Override
    public String clrBatId() {
        //机构可能会调用，所以这里实现了get方法
        //注意批次号在<SplmtryData>标签下
        return   txInf.getSplmtryData().getEnvlp().getBtchId();
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
        return MsgTpEnum.FI_RETUNE.getCode();
    }

    /**
     * 端到端流水号
     */
    @Override
    public String clrEndToEndId() {
        //已确认，不涉及。114报文中，没有端到端标识
        return null;
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
        return txInf.getPmtTpInf().getCtgyPurp().getPrtry();
    }

    /**
     * 业务种类编码
     */
    @Override
    public String clrBizKind() {
        return txInf.getSplmtryData().getEnvlp().getPurp().getPrtry();
    }

    /**
     * 付款运营机构
     */
    @Override
    public String clrDbtrPtyId() {
        //Agt不要使用自动生成的，使用同一个组件：com.dcep.common.model.soap.FinInstnId
        // 付款机构，不是发送方
        return txInf.getRtrChain().getDbtr().getAgt().getFinInstnId().getClrSysMmbId().getMmbId();
    }

    /**
     * 付款ID
     */
    @Override
    public String clrDbtrWltId() {
        return txInf.getRtrChain().getDbtrAcct().getId().getOthr().getId();
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
        return txInf.getRtrChain().getCdtr().getAgt().getFinInstnId().getClrSysMmbId().getMmbId();
    }

    /**
     * 收款ID
     */
    @Override
    public String clrCdtrWltId() {
        return txInf.getRtrChain().getCdtrAcct().getId().getOthr().getId();
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
        return txInf.getRtrdIntrBkSttlmAmt().getCcy();
    }

    /**
     * 交易金额
     */
    @Override
    public String clrAmt() {
        return txInf.getRtrdIntrBkSttlmAmt().getValue();
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
        return txInf.getRtrChain() != null ? txInf.getRtrChain().getCdtrAcct().getId().getOthr().getId() : null;
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
        return txInf.getInstgAgt().getFinInstnId().getClrSysMmbId().getMmbId();
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
        return txInf.getInstdAgt().getFinInstnId().getClrSysMmbId().getMmbId();
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
        // 向报文中赋交易批次号 ，与get，set一致，使用SplmtryData的btchid
        txInf.getSplmtryData().getEnvlp().setBtchId(response.getResult().getBatchId());
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
        return MsgTpEnum.FI_RETUNE.getCode();
    }

    @Override
    public String recOrgnlMsgId() {
        return txInf.getOrgnlGrpInf().getOrgnlMsgId();
    }

    @Override
    public String recOrgnlMsgTp() {
        return MsgTpEnum.FI_CDT.getCode();
    }

    public List<String> fetchEncryptionFeatures() {

        /**
         * 付款方钱包ID
         * <RtrChain> <DbtrAcct> <Id> <Othr> <Id>
         * 收款方钱包ID
         * <RtrChain> <CdtrAcct> <Id> <Othr> <Id>
         *
         * 原收款方钱包ID
         * <OrgnlTxRef> <CdtrAcct> <Id> <Othr> <Id>
         * 原付款方钱包ID
         * <OrgnlTxRef> <DbtrAcct> <Id> <Othr> <Id>
         * */
        //付款方钱包ID
        List<String> data = new ArrayList<>();

        //付款方钱包ID
        if (txInf != null && txInf.getRtrChain() != null
                && txInf.getRtrChain().getDbtrAcct() != null
                && txInf.getRtrChain().getDbtrAcct().getId() != null
                && txInf.getRtrChain().getDbtrAcct().getId().getOthr() != null
                && txInf.getRtrChain().getDbtrAcct().getId().getOthr().getId() != null) {
            data.add(txInf.getRtrChain().getDbtrAcct().getId().getOthr().getId());
        }
        //收款方钱包ID
        if (txInf != null && txInf.getRtrChain() != null
                && txInf.getRtrChain().getCdtrAcct() != null
                && txInf.getRtrChain().getCdtrAcct().getId() != null
                && txInf.getRtrChain().getCdtrAcct().getId().getOthr() != null
                && txInf.getRtrChain().getCdtrAcct().getId().getOthr().getId() != null) {
            data.add(txInf.getRtrChain().getCdtrAcct().getId().getOthr().getId());
        }

        //付款方钱包ID
        if (txInf != null && txInf.getOrgnlTxRef() != null
                && txInf.getOrgnlTxRef().getDbtrAcct() != null
                && txInf.getOrgnlTxRef().getDbtrAcct().getId() != null
                && txInf.getOrgnlTxRef().getDbtrAcct().getId().getOthr() != null
                && txInf.getOrgnlTxRef().getDbtrAcct().getId().getOthr().getId() != null) {
            data.add(txInf.getOrgnlTxRef().getDbtrAcct().getId().getOthr().getId());
        }
        //收款方钱包ID
        if (txInf != null && txInf.getOrgnlTxRef() != null
                && txInf.getOrgnlTxRef().getCdtrAcct() != null
                && txInf.getOrgnlTxRef().getCdtrAcct().getId() != null
                && txInf.getOrgnlTxRef().getCdtrAcct().getId().getOthr() != null
                && txInf.getOrgnlTxRef().getCdtrAcct().getId().getOthr().getId() != null) {
            data.add(txInf.getOrgnlTxRef().getCdtrAcct().getId().getOthr().getId());
        }

        return data;

    }

    public void encryptionFeaturesAssign(List<String> encryptionFeatures) {
        int index = 0;
        //付款方钱包ID
        if (txInf != null && txInf.getRtrChain() != null
                && txInf.getRtrChain().getDbtrAcct() != null
                && txInf.getRtrChain().getDbtrAcct().getId() != null
                && txInf.getRtrChain().getDbtrAcct().getId().getOthr() != null
                && txInf.getRtrChain().getDbtrAcct().getId().getOthr().getId() != null) {
            txInf.getRtrChain().getDbtrAcct().getId().getOthr().setId(encryptionFeatures.get(index++));
        }
        //收款方钱包ID
        if (txInf != null && txInf.getRtrChain() != null
                && txInf.getRtrChain().getCdtrAcct() != null
                && txInf.getRtrChain().getCdtrAcct().getId() != null
                && txInf.getRtrChain().getCdtrAcct().getId().getOthr() != null
                && txInf.getRtrChain().getCdtrAcct().getId().getOthr().getId() != null) {
            txInf.getRtrChain().getCdtrAcct().getId().getOthr().setId(encryptionFeatures.get(index++));
        }

        //付款方钱包ID
        if (txInf != null && txInf.getOrgnlTxRef() != null
                && txInf.getOrgnlTxRef().getDbtrAcct() != null
                && txInf.getOrgnlTxRef().getDbtrAcct().getId() != null
                && txInf.getOrgnlTxRef().getDbtrAcct().getId().getOthr() != null
                && txInf.getOrgnlTxRef().getDbtrAcct().getId().getOthr().getId() != null) {
            txInf.getOrgnlTxRef().getDbtrAcct().getId().getOthr().setId(encryptionFeatures.get(index++));
        }
        //收款方钱包ID
        if (txInf != null && txInf.getOrgnlTxRef() != null
                && txInf.getOrgnlTxRef().getCdtrAcct() != null
                && txInf.getOrgnlTxRef().getCdtrAcct().getId() != null
                && txInf.getOrgnlTxRef().getCdtrAcct().getId().getOthr() != null
                && txInf.getOrgnlTxRef().getCdtrAcct().getId().getOthr().getId() != null) {
            txInf.getOrgnlTxRef().getCdtrAcct().getId().getOthr().setId(encryptionFeatures.get(index++));
        }
    }

    @Override
    public String clrSysWorkDt() {
        return txInf.getSplmtryData().getEnvlp().getSysWorkDt();
    }

    /**
     * 填充结算日期SplmtryData
     *
     * @param sttlmDt
     */
    @Override
    public void fillSttlmDt(String sttlmDt) {
        txInf.getSplmtryData().getEnvlp().setSttlmDt(sttlmDt);
    }
}
