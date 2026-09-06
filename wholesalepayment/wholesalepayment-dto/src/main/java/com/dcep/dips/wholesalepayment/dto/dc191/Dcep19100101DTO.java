package com.dcep.dips.wholesalepayment.dto.dc191;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dcep.common.annotation.*;
import com.dcep.common.encryption.DataEncryption;
import com.dcep.common.encryption.EncryptionHelper;
import com.dcep.common.enums.ChannelEnums;
import com.dcep.common.exception.DcepException;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.Response;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.utils.ValidateUtils;
import com.dcep.dips.common.util.DateUtils;
import com.dcep.dips.wholesalepayment.annotation.Clearing;
import com.dcep.dips.wholesalepayment.annotation.Record;
import com.dcep.dips.wholesalepayment.constants.Constant;
import com.dcep.dips.wholesalepayment.dto.ClearingStatus;
import com.dcep.dips.wholesalepayment.dto.RecordDTO;
import com.dcep.dips.wholesalepayment.dto.mcbs.MbridgeReqDTO;
import com.dcep.dips.wholesalepayment.enums.*;
import com.dcep.dips.wholesalepayment.utils.DtoCheckUtil;
import com.dcep.dips.wholesalepayment.validation.CheckClrBatId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * dcep.191.001.01DTO
 * @Author qiaopengyu
 * @date 2025-10-30 21:17:24
 */
@Data
@JacksonXmlRootElement(
        localName = "FICdtTrf",
        namespace = "http://www.dcep.com/dcep/19100101/"
)
@Setter
@Getter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
//TODO 修改值
@Gateway(msgTp = "dcep.191.001.01", channel = @Channel(classname = ChannelEnums.CENTRAL_PROCESS, services = {
        @RpcInfo(name = Constant.NAME_DCEPSND, methods = { @GwMethod(name = Constant.METHOD_MBRIDGE_PREPARE) }) }))
@Clearing(presumeStatus = ClearingStatusEnum.PRESUME_SUCCESS, action = ClearingActionEnum.PREPARE, confirmTimeout = 300, instgDrctPty = InstgDrctPtyEnum.DBTR,
        checkMode = ClearingCheckModeEnum.PAYER_CHECK, cdtDbtInd = ClearingProdCdtDbtIndEnum.CRDT, actgBizTp = ActgBizTpEnum.SWBC05)
@Record(saveMode = RecordSaveModeEnum.ALL)
@CheckClrBatId
@CheckBizCode(bizTypeCode = "cdtTrfTxInf.pmtTpInf.ctgyPurp.prtry", bizCtgyCode = "cdtTrfTxInf.purp.prtry")
public class Dcep19100101DTO extends GwDTO implements RecordDTO, DataEncryption {
    private static final long serialVersionUID = -1654341128763908619L;
    /**
     * 组件GroupHeader
     */
    @JacksonXmlProperty(
            localName = "GrpHd"
    )
    @NotNull
    @Valid
    private GrpHd grpHd;

    /**
     * 交易信息
     */
    @JacksonXmlElementWrapper(
            useWrapping = false
    )
    @JacksonXmlProperty(
            localName = "CdtTrfTxInf"
    )
    @NotNull
    @Valid
    private List<CdtTrfTxInf> cdtTrfTxInf;

    /**
     * 货币桥信息(内部使用，非报文标准部分展示)
     */
    @JsonIgnore
    private MbridgeReqDTO mbridgeReqDTO;

    @Override
    public void init() {
        rmtInf();
    }

    @Override
    public String fetchMsgId() {
        return grpHd.getMsgId();
    }

    @Override
    public boolean check(SoapHeader soapHeader) {
        // 报文头报文标识号
        String headerMsgId = soapHeader.getMsgSN().substring(0, 32);
        // 报文体报文标识号
        String bodyMsgId = grpHd.getMsgId();
        if (!headerMsgId.equals(bodyMsgId)) {
            throw new DcepException(com.dcep.common.enums.ErrorEnum.MSGSN_MSGID_NOT_MATCH_ERROR);
        }

        // 校验msgId的(13,16)这3位与msgTp的中间3位是否一致 报文编号校验11
        String bodyMsgIdMsgTp = bodyMsgId.substring(12, 15);
        String msgTpNum = soapHeader.getMsgTp().substring(5, 8);
        if (!msgTpNum.equals(bodyMsgIdMsgTp)) {
            throw new DcepException(com.dcep.common.enums.ErrorEnum.MSGTP_NOT_IN_MSGID_ERROR);
        }

        // 报文标识号与明细标识号校验
        for (CdtTrfTxInf txInf : cdtTrfTxInf){
            if (!headerMsgId.equals(txInf.getPmtId().getTxId())) {
                throw new DcepException(com.dcep.common.enums.ErrorEnum.MSGID_TXID_NOT_MATCH_ERROR);
            }
            // 校验附言Ustrds里标签字段
            ValidateUtils.validate(txInf.getRmtInf());

            if (!DtoCheckUtil.checkPayerMsgInst(soapHeader.getSender(), this.clrDbtrPtyId(txInf),
                    soapHeader.getReceiver(), this.clrCdtrPtyId(txInf))){
                return false;
            }
        }
        return true;
    }

    /**
     * 将ustrds附言字段的值赋值到RmtInf对象
     *
     * @param
     */
    void rmtInf() {
        List<CdtTrfTxInf> cdtTrfTxInf = this.getCdtTrfTxInf();
        if (cdtTrfTxInf != null && !cdtTrfTxInf.isEmpty()){
            for (CdtTrfTxInf info : cdtTrfTxInf){
                if (info != null) {
                    info.setRmtInf(new RmtInf(info.getUstrds()));
                }
            }
        }
    }

    @Override
    public void encryptData(EncryptionHelper encryptionHelper) {

    }

    @Override
    public void decryptData(EncryptionHelper encryptionHelper) {

    }

    public void clrBatId(String clrBatId) {
        for (CdtTrfTxInf txInf : cdtTrfTxInf){
            txInf.getPmtId().setInstrId(clrBatId);
        }
    }

    public String clrBatId(CdtTrfTxInf txInf) {
        return txInf.getPmtId().getInstrId();
    }

    public String clrTransTp() {
        //TODO 修改类型
        return TransTpEnum.NORMAL_TRANS.getCode();
    }

    public String clrMsgTp() {
        //TODO 修改类型
        return MsgTpEnum.CDT_REQUEST_ASYN.getCode();
    }

    public String clrEndToEndId(CdtTrfTxInf txInf) {
        return txInf.getPmtId().getEndToEndId();
    }

    public String getClrMsgId() {
        return grpHd.getMsgId();
    }

    public String clrBizTp(CdtTrfTxInf txInf) {
        return txInf.getPmtTpInf().getCtgyPurp().getPrtry();
    }

    public String clrBizKind(CdtTrfTxInf txInf) {
        return txInf.getPurp().getPrtry();
    }

    /**
     * 付款方机构号
     * @param txInf
     * @return
     */
    public String clrDbtrPtyId(CdtTrfTxInf txInf) {
        if (txInf != null) {
            return txInf.getDbtr().getFinInstnId().getClrSysMmbId().getMmbId();
        }
        return null;
    }

    /**
     * 付款方钱包编号
     * @param txInf
     * @return
     */
    public String clrDbtrWltId(CdtTrfTxInf txInf) {
        if (txInf != null) {
            return txInf.getDbtrAcct().getId().getOthr().getId();
        }
        return null;
    }

    /**
     * 收款方机构号
     * @param txInf
     * @return
     */
    public String clrCdtrPtyId(CdtTrfTxInf txInf) {
        if (txInf != null) {
            return txInf.getCdtr().getFinInstnId().getClrSysMmbId().getMmbId();
        }
        return null;
    }

    /**
     * 收款方钱包编号
     * @param txInf
     * @return
     */
    public String clrCdtrWltId(CdtTrfTxInf txInf) {
        if (txInf != null) {
            return txInf.getCdtrAcct().getId().getOthr().getId();
        }
        return null;
    }

    /**
     * 币种
     * @param txInf
     * @return
     */
    public String clrCurrency(CdtTrfTxInf txInf) {
        if (txInf != null) {
            return txInf.getIntrBkSttlmAmt().getCcy();
        }
        return null;
    }

    /**
     * 金额
     * @param txInf
     * @return
     */
    public String clrAmt(CdtTrfTxInf txInf) {
        return txInf.getIntrBkSttlmAmt().getValue();
    }

    public void clrBizRspSts(String clrBizRspSts) {
        // 业务回执状态
    }

    public String clrBizRjctCd() {
        return null;
    }

    public void clrBizRjctCd(String clrBizRjctCd) {
        // 业务拒绝码
    }

    public void clrRjctResn(String clrRjctResn) {
        // 业务拒绝原因
    }

    public String clrTrxInf(CdtTrfTxInf txInf) {
        // 交易描述信息(付款报文,交易描述信息为收款人钱包ID)
        return txInf.getCdtrAcct() != null ? txInf.getCdtrAcct().getId().getOthr().getId() : null;
    }

    /**
     * 业务处理时间
     * @return
     */
    public String clrCreDtTm() {
        return grpHd.getCreDtTm();
    }

    /**
     * 发送方机构号
     * @return
     */
    public String clrSendPtyId() {
        return grpHd.getInstgAgt().getFinInstnId().getClrSysMmbId().getMmbId();
    }

    /**
     * 接收方机构号
     * @return
     */
    public String clrRecvPtyId() {
        return grpHd.getInstdAgt().getFinInstnId().getClrSysMmbId().getMmbId();
    }

    public void fillBatchId(Response<ClearingStatus> response, CdtTrfTxInf txInf) {
        // 向报文中赋交易批次号
        txInf.getPmtId().setInstrId(response.getResult().getBatchId());
    }

    public String clrAcctTp() {
        return ClrAcctTpEnum.PAY.getCode();
    }

    @Override
    public String encode() {
        return JSON.toJSONString(this);
    }

    @Override
    public Dcep19100101DTO decode(String encode) {
        return JSONObject.toJavaObject(JSON.parseObject(encode), this.getClass());
    }

    /**
     * 报文标识号
     * @return
     */
    @Override
    public String recMsgId() {
        return grpHd.getMsgId();
    }

    /**
     * 报文类型
     * @return
     */
    @Override
    public String recMsgTp() {
        //TODO
        return MsgTpEnum.CDT_REQUEST_ASYN.getCode();
    }

    @Override
    public String recOrgnlMsgId() {
        return null;
    }

    @Override
    public String recOrgnlMsgTp() {
        return null;
    }

    public MbridgeReqDTO clrMbridgeInf() {
        return mbridgeReqDTO;
    }

    /**
     * 发起渠道机构系统标识
     * @return
     */
    public String clrSendSysId() {
        //发起渠道机构系统标识
        return grpHd.getInstgAgt().getFinInstnId().getClrSysMmbId().getClrSysId().getCd();
    }

    /**
     * 接收渠道机构系统标识
     * @return
     */
    public String clrRecvSysId() {
        //接收渠道机构系统标识
        return grpHd.getInstdAgt().getFinInstnId().getClrSysMmbId().getClrSysId().getCd();
    }

    /**
     * 付款机构的系统
     * @param txInf
     * @return
     */
    public String clrDbtrSysId(CdtTrfTxInf txInf) {
        // 付款机构的系统
        if (txInf != null) {
            return txInf.getDbtr().getFinInstnId().getClrSysMmbId().getClrSysId().getCd();
        }
        return null;
    }

    /**
     * 收款机构的系统
     * @param txInf
     * @return
     */
    public String clrCdtrSysId(CdtTrfTxInf txInf) {
        // 收款机构的系统
        if (txInf != null) {
            return txInf.getCdtr().getFinInstnId().getClrSysMmbId().getClrSysId().getCd();
        }
        return null;
    }

    /**
     * 系统工作日期
     * @return
     */
    public String clrSysWorkDt(){
        return DateUtils.today();
    }

    /**
     * 是否对账标识
     * @return
     */
    public String clrFlag() {
        return ClrFlgEnum.YES.getCode();
    }

    public String clrPresumeTm() {
        return null;
    }

    /**
     * 业务优先级
     */
    public String clrBizPrty(CdtTrfTxInf txInf){
//        return BizPrtyEnum.NORM.getCode();
        if (txInf != null){
            return txInf.getSttlmPrty();
        }
        return null;
    }
}
