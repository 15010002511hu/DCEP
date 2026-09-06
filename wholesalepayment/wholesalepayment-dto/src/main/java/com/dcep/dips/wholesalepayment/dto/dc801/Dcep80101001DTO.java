package com.dcep.dips.wholesalepayment.dto.dc801;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dcep.common.annotation.*;
import com.dcep.common.enums.ChannelEnums;
import com.dcep.common.enums.ErrorEnum;
import com.dcep.common.exception.DcepException;
import com.dcep.common.model.ActiveCurrencyAndAmount;
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
import com.dcep.dips.wholesalepayment.dto.CshBoxInf;
import com.dcep.dips.wholesalepayment.dto.RecordDTO;
import com.dcep.dips.wholesalepayment.dto.mcbs.MbridgeReqDTO;
import com.dcep.dips.wholesalepayment.enums.*;
import com.dcep.dips.wholesalepayment.utils.DtoCheckUtil;
import com.dcep.dips.wholesalepayment.utils.InfoCacheUtil;
import com.dcep.infocache.validation.CheckClrGrpHdr;
import com.dcep.infocache.validation.CheckGrpHdrMsgId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@JacksonXmlRootElement(localName = "DsptReq", namespace = "http://www.dcep.com/dcep/80101001/")
@Setter
@Getter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Gateway(msgTp = "dcep.801.010.01", channel = @Channel(classname = ChannelEnums.CENTRAL_PROCESS, services = {
        @RpcInfo(name = Constant.NAME_PAYMENT, methods = { @GwMethod(name = Constant.DBTR_SETTLE) })}))
@Clearing(presumeStatus = ClearingStatusEnum.PRESUME_SUCCESS, action = ClearingActionEnum.PREPARE, instgDrctPty = InstgDrctPtyEnum.DBTR, checkMode = ClearingCheckModeEnum.PAYER_CHECK, cdtDbtInd = ClearingProdCdtDbtIndEnum.CRDT)
@Record(saveMode = RecordSaveModeEnum.ALL)
@CheckBizCode(bizTypeCode = "dsptInf.dsptBizTp", bizCtgyCode = "dsptInf.dsptCtgyPurpCd")
public class Dcep80101001DTO extends GwDTO implements ClearingDTO {

    private static final long serialVersionUID = 1L;
    /**
     * 【业务头组件】
     */
    @JacksonXmlProperty(localName = "GrpHdr")
    @NotNull(groups = Priority.Highest.class)
    @Valid
    @CheckGrpHdrMsgId(groups = Priority.Lowest.class)
    @CheckClrGrpHdr(groups = Priority.Lowest.class)
    private GrpHdr grphdr;

    /**
     * 【原业务头组件】
     */
    @JacksonXmlProperty(localName = "OrgnlGrpHdr")
    @NotNull(groups = Priority.Highest.class)
    @Valid
    private OrgnlGrpHdr orgnlGrpHdr;

    /**
     * 【差错交易信息】
     */
    @JacksonXmlProperty(localName = "DsptInf")
    @NotNull(groups = Priority.Highest.class)
    @Valid
    private DsptInf dsptInf;

    /**
     * 【钱柜机构信息】
     */
    @JacksonXmlProperty(localName = "CshBoxInf")
    @Valid
    private CshBoxInf cshBoxInf;

    /**
     * 货币桥信息
     */
    @JsonIgnore
    private MbridgeReqDTO mbridgeReqDTO;
    private Object ClearingStatusEnum;

    /**
     * --------------RecordDTO接口方法-------------
     */
    @Override
    public String encode() {
        // 对象转换成档案记录JSON串
        return JSON.toJSONString(this);
    }

    @Override
    public RecordDTO decode(String encode) {
        // JSON对象转化为Java对象
        return JSONObject.toJavaObject(JSON.parseObject(encode), this.getClass());
    }

    @Override
    public String recMsgTp() {
        // 报文编号
        return MsgTpEnum.CRDT_ADJ_REQUREST.getCode();
    }

    @Override
    public String recMsgId() {
        // 报文标识号
        return grphdr.getMsgId();
    }

    @Override
    public String clrDbtrWltId() {
        // 付款方钱包ID
        return null;
    }

    @Override
    public String clrDbtrSysId() {
        return ChnlSysEnum.DCEP.getCode();
    }

    @Override
    public String clrCdtrWltId() {
        // 收款方钱包id
        return null;
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
     * --------------ClearingDTO接口方法-------------
     */

    @Override
    public void clrBatId(String clrBatId) {
        // 交易批次号
        dsptInf.setBatchId(clrBatId);
    }

    @Override
    public String clrBatId() {
        // 交易批次号
        return dsptInf.getBatchId();
    }

    @Override
    public String clrTransTp() {
        return TransTpEnum.NORMAL_TRANS.getCode();
    }

    @Override
    public String clrMsgTp() {
        // 报文编号
        return MsgTpEnum.CRDT_ADJ_REQUREST.getCode();
    }

    @Override
    public String clrEndToEndId() {
        return null;
    }

    @Override
    public String getClrMsgId() {
        // 报文标识号
        return grphdr.getMsgId();
    }

    @Override
    public String clrBizTp() {
        // 业务类型编码
        return dsptInf.getDsptBizTp();
    }

    @Override
    public String clrBizKind() {
        // 业务种类编码
        return dsptInf.getDsptCtgyPurpCd();
    }

    @Override
    public String clrDbtrPtyId() {
        // 付款运营机构
        return grphdr.getInstgPty().getInstgDrctPty();
    }

    @Override
    public String clrCdtrPtyId() {
        // 收款运营机构
        return grphdr.getInstdPty().getInstdDrctPty();
    }

    @Override
    public String clrCurrency() {
        // 交易币种
        return dsptInf.getDsptAmt().getCcy();
    }

    @Override
    public String clrAmt() {
        // 交易金额
        return dsptInf.getDsptAmt().getValue();
    }

    @Override
    public String clrBizRspSts() {
        // 业务回执状态
        return null;
    }

    @Override
    public void clrBizRspSts(String clrBizRspSts) {
        // 业务回执状态
    }

    @Override
    public String clrBizRjctCd() {
        // 业务拒绝码
        return null;
    }

    @Override
    public void clrBizRjctCd(String clrBizRjctCd) {
        // 业务拒绝码
    }

    @Override
    public String clrRjctResn() {
        // 业务拒绝原因
        return null;
    }

    @Override
    public void clrRjctResn(String clrRjctResn) {
        // 业务拒绝原因
    }

    @Override
    public String clrTrxInf() {
        // 交易描述信息(贷记调整差错报文为原报文编号-原报文标识号)
        return orgnlGrpHdr.getOrgnlMT() + "-" + orgnlGrpHdr.getOrgnlMsgId();
    }

    @Override
    public String clrFlag() {
        return ClrFlgEnum.YES.getCode();
    }

    @Override
    public String clrCreDtTm() {
        // 账务时间
        return grphdr.getCreDtTm();
    }

    @Override
    public String clrSendPtyId() {
        return grphdr.getInstgPty().getInstgDrctPty();
    }

    @Override
    public String clrSendSysId() {
        return ChnlSysEnum.DCEP.getCode();
    }

    @Override
    public String clrRecvPtyId() {
        return grphdr.getInstdPty().getInstdDrctPty();
    }

    @Override
    public String clrRecvSysId() {
        return ChnlSysEnum.DCEP.getCode();
    }

    @Override
    public void fillBatchId(Response<ClearingStatus> response) {
        // 向报文中赋交易批次号
        dsptInf.setBatchId(response.getResult().getBatchId());
    }

    @Override
    public OrgnlGrpHdr presumeConfirm() {
        OrgnlGrpHdr orgnlGrpHdr = new OrgnlGrpHdr();
        orgnlGrpHdr.setOrgnlInstgPty(this.getGrphdr().getInstgPty().getInstgDrctPty());
        orgnlGrpHdr.setOrgnlMsgId(this.getGrphdr().getMsgId());
        orgnlGrpHdr.setOrgnlMT(MsgTpEnum.CRDT_ADJ_REQUREST.getCode());
        return orgnlGrpHdr;
    }

    /**
     * --------------GwDTO接口方法-------------
     */
    @Override
    public void init() {

    }

    @Override
    public String fetchMsgId() {
        // 获取msgId
        return grphdr.getMsgId();
    }

    /**
     * DTO 校验，主要校验soapheader 和 soapbody 同时出现的值，要一致
     * 
     * @return return true 校验成功；return false 校验失败
     */
    @Override
    public boolean check(SoapHeader soapHeader) {
        // 原交易报文编号权限校验
        String orgnlMT = orgnlGrpHdr.getOrgnlMT();

        // 支付类报文：201、211、251、255、262、281、613
        if (MsgTpEnum.CDT_REQUEST.getCode().equals(orgnlMT) || MsgTpEnum.DBT_REQUEST.getCode().equals(orgnlMT)
                || MsgTpEnum.REFUND_REQUREST.getCode().equals(orgnlMT)
                || MsgTpEnum.ORDR_CONF_RESPONSE.getCode().equals(orgnlMT)
                || MsgTpEnum.CDT_REQUEST_ASYN.getCode().equals(orgnlMT)
                || MsgTpEnum.DBT_REQUEST_ASYN.getCode().equals(orgnlMT)) {
            // 支付类差错业务种类编码为08001
            if (!("08001".equals(dsptInf.getDsptCtgyPurpCd()) || "60000001".equals(dsptInf.getDsptCtgyPurpCd()))) {
                throw new DcepException(com.dcep.dips.wholesalepayment.enums.ErrorEnum.BIZ_KIND_ILLEGAL.getCode(),
                        com.dcep.dips.wholesalepayment.enums.ErrorEnum.BIZ_KIND_ILLEGAL.getDescription());
            }

            // 兑换类报文：221、225、227
        } else if (MsgTpEnum.RECOV_REQUEST.getCode().equals(orgnlMT) || MsgTpEnum.COV_REQUREST.getCode().equals(orgnlMT)
                || MsgTpEnum.CDT_COV_REQUREST.getCode().equals(orgnlMT)) {
            // 兑换类差错业务种类编码为08002
            if (!("08002".equals(dsptInf.getDsptCtgyPurpCd()) || "60000002".equals(dsptInf.getDsptCtgyPurpCd()))) {
                throw new DcepException(com.dcep.dips.wholesalepayment.enums.ErrorEnum.BIZ_KIND_ILLEGAL.getCode(),
                        com.dcep.dips.wholesalepayment.enums.ErrorEnum.BIZ_KIND_ILLEGAL.getDescription());
            }

        } else {
            throw new DcepException(ErrorEnum.VALIDATION_ERROR.getCode(), "差错贷记调账交易不支持对该交易类型进行调账");
        }

        return !CheckUtils.requestMsgChk(soapHeader, grphdr) ? false
                : DtoCheckUtil.checkPayerMsgInst(soapHeader.getSender(), grphdr.getInstgPty().getInstgDrctPty(),
                        soapHeader.getReceiver(), grphdr.getInstdPty().getInstdDrctPty());
    }

    /**
     * 计算当前DTO使用哪种channel处理, 默认返回注解配置的channel模式 dcep正常交易使用合作银行channel模式
     * 货币桥差错交易使用中央处理channel模式：原交易为203、213报文
     */
    @Override
    public ChannelEnums routeChannel(SoapHeader header) {
        if (MsgTpEnum.CDT_REQUEST_ASYN.getCode().equals(orgnlGrpHdr.getOrgnlMT())
                || MsgTpEnum.DBT_REQUEST_ASYN.getCode().equals(orgnlGrpHdr.getOrgnlMT())) {
            // 返回中央处理channel
            return ChannelEnums.CENTRAL_PROCESS;
        }
        // 返回默认channel
        return super.routeChannel(header);
    }

    /**
     * 计算当前DTO使用哪种Dubbo服务处理, 默认返回注解配置的RpcInfo dcep正常交易返回调用交易转接api
     * 货币桥差错交易返回调用路由服务api：原交易为203、213报文
     */
    @Override
    public RpcInfo[] routeServices(SoapHeader header) {
        if (MsgTpEnum.CDT_REQUEST_ASYN.getCode().equals(orgnlGrpHdr.getOrgnlMT())
                || MsgTpEnum.DBT_REQUEST_ASYN.getCode().equals(orgnlGrpHdr.getOrgnlMT())) {
            // 返回路由服务应用服务方法
            return new RpcInfo[] { this.getClass().getAnnotation(Gateway.class).channel().services()[1] };
        }
        // 返回交易转接应用服务方法
        return new RpcInfo[] { this.getClass().getAnnotation(Gateway.class).channel().services()[0] };
    }

    @Override
    public String clrPresumeTm() {
        return null;
    }

    @Override
    public String clrAcctTp() {
        // 原交易报文编号
        String orgnlMT = orgnlGrpHdr.getOrgnlMT();
        // 支付类报文：201、211、251、255、262、281、203、213
        if (MsgTpEnum.CDT_REQUEST.getCode().equals(orgnlMT) || MsgTpEnum.DBT_REQUEST.getCode().equals(orgnlMT)
                || MsgTpEnum.REFUND_REQUREST.getCode().equals(orgnlMT)
                || MsgTpEnum.ORDR_CONF_RESPONSE.getCode().equals(orgnlMT)
                || MsgTpEnum.CDT_REQUEST_ASYN.getCode().equals(orgnlMT)
                || MsgTpEnum.DBT_REQUEST_ASYN.getCode().equals(orgnlMT)) {
            return ClrAcctTpEnum.PAY.getCode();

            // 兑换类报文221：801+221=227
        } else if (MsgTpEnum.RECOV_REQUEST.getCode().equals(orgnlMT)) {
            return InfoCacheUtil.checkInstType(this.clrDbtrPtyId()) ? ClrAcctTpEnum.PAY.getCode()
                    : ClrAcctTpEnum.CDT_COV.getCode();

            // 兑换类报文225/227：801+225/227=221
        } else if (MsgTpEnum.COV_REQUREST.getCode().equals(orgnlMT)
                || MsgTpEnum.CDT_COV_REQUREST.getCode().equals(orgnlMT)) {
            return InfoCacheUtil.checkInstType(this.clrCdtrPtyId()) ? ClrAcctTpEnum.PAY.getCode()
                    : ClrAcctTpEnum.CASH_IN.getCode();
        } else {
            throw new DcepException(ErrorEnum.VALIDATION_ERROR.getCode(), "差错贷记调账交易不支持对该交易类型进行调账");
        }
    }

    @Override
    public String clrSndChnlSys() {
        // 发起渠道，货币桥中渠道信息不为空，返回渠道信息
        if (mbridgeReqDTO != null && StringUtils.isNotBlank(mbridgeReqDTO.getSndChnlSys())) {
            return mbridgeReqDTO.getSndChnlSys();
        }
        return null;
    }

    @Override
    public String clrRcvChnlSys() {
        // 接收渠道，货币桥中渠道信息不为空，返回渠道信息
        if (mbridgeReqDTO != null && StringUtils.isNotBlank(mbridgeReqDTO.getRcvChnlSys())) {
            return mbridgeReqDTO.getRcvChnlSys();
        }
        return null;
    }

    @Override
    public MbridgeReqDTO clrMbridgeInf() {
        return mbridgeReqDTO;
    }

    @Override
    public void fillMsgId(String msgId, String orgMsgId) {
        // 赋值报文标识号
        grphdr.setMsgId(msgId);
        orgnlGrpHdr.setOrgnlMsgId(orgMsgId);
    }

    @Override
    public void fillDsptInf(String msgId, String orgMsgId, String orgMsgTp, String orgInstgPty,
            ActiveCurrencyAndAmount orgTxAmt) {
        // 赋值差错报文信息
        grphdr.setMsgId(msgId);
        orgnlGrpHdr.setOrgnlMsgId(orgMsgId);
        orgnlGrpHdr.setOrgnlMT(orgMsgTp);
        orgnlGrpHdr.setOrgnlInstgPty(orgInstgPty);
        dsptInf.getOrgnlTxRef().setOrgnlTxAmt(orgTxAmt);
    }

    @Override
    public String clrSysWorkDt(){
        return dsptInf.getSysWorkDt();
    }

    @Override
    public void fillSttlmDt(String sttlmDt) {
        dsptInf.setSttlmDt(sttlmDt);
    }
}
