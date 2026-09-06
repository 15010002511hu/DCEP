package com.dcep.dips.wholesalepayment.manager.impl;

import com.dcep.common.exception.DcepException;
import com.dcep.common.model.soap.GrpHdr;
import com.dcep.common.soap.SoapUtils;
import com.dcep.common.utils.DcepDateUtils;
import com.dcep.common.utils.MsgIdUtil;
import com.dcep.dips.common.dto.PrmtInf;
import com.dcep.dips.common.dto.dc417.Dcep41700101DTO;
import com.dcep.dips.common.dto.dc418.*;
import com.dcep.dips.common.enums.ClearingProdErrorEnum;
import com.dcep.dips.wholesalepayment.common.constants.Constant;
import com.dcep.dips.wholesalepayment.common.utils.CommonUtil;
import com.dcep.dips.wholesalepayment.dal.mapper.SettlementProdMapper;
import com.dcep.dips.wholesalepayment.dal.model.SettlementProdDO;
import com.dcep.dips.wholesalepayment.dto.ClearingDTO;
import com.dcep.dips.wholesalepayment.enums.ClearingStatusEnum;
import com.dcep.dips.wholesalepayment.enums.MsgTpEnum;
import com.dcep.dips.wholesalepayment.manager.ClearingDtlQueryManager;
import com.dcep.dips.wholesalepayment.manager.RecordManager;
import com.dcep.dips.wholesalepayment.utils.InfoCacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ClearingDtlQueryManagerImpl implements ClearingDtlQueryManager {

    @Autowired
    private SettlementProdMapper settlementProdMapper;
    @Autowired
    private RecordManager recordManager;
    @Override
    public Dcep41800101DTO queryClearingDtlFromOrg(Dcep41700101DTO req) {
        log.info("queryClearingDtlFromOrg.417req: MsgId={}", req.getGrpHdr().getMsgId());

        //查询原交易的msgid
        String orgnlMsgId = req.getOrgnlGrpHdr().getOrgnlMsgId();
        // 1. 查询数据库交易信息
        SettlementProdDO settlementProdDO = settlementProdMapper.selectByPrimaryKey(new SettlementProdDO(orgnlMsgId));
        log.debug("queryClearingDtlFromOrg.settlementProdDO:{}",
                settlementProdDO != null ? settlementProdDO.getMsgId() : "record not found in db");

        // 2. 交易信息参数校验
        this.dataValidate(req, settlementProdDO);

        // 3. 组装418报文头
        GrpHdr grpHdr = new GrpHdr(
                MsgIdUtil.randomMsgId(MsgTpEnum.TXN_DETAIL_RESPONSE_ABBR.getCode(), Constant.PBOC,
                        CommonUtil.getEnvVal()),
                DcepDateUtils.getDcepDateStrNow(), InfoCacheUtil.getPbocInf(),
                req.getGrpHdr().getInstgPty().getInstgDrctPty(), null);

        // 4. 组装418 BizQryRef
        BizQryRef bizQryRef = new BizQryRef(req.getGrpHdr().getMsgId(), req.getGrpHdr().getInstgPty().getInstgDrctPty(),
                ClearingStatusEnum.SUCCESS.getCode());

        // 5. 418正常返回 - 查询处理状态为PR00
        if (null != settlementProdDO) {
            // 5.1 获取原业务报文的原文
            ClearingDTO orgnlClearingDTO = (ClearingDTO) recordManager.resume(settlementProdDO.getMsgTp(), orgnlMsgId);

            // 清算表有数据而记录表没有数据，数据库表数据异常
            if (orgnlClearingDTO == null) {
                log.error("queryClearingDtlFromOrg: req.orgnlMsgId={} not found in db",
                        orgnlMsgId);
                throw new DcepException(ClearingProdErrorEnum.BUSI_DATA_NOTEXCEED.getCode(),
                        ClearingProdErrorEnum.BUSI_DATA_NOTEXCEED.getDescription());
            }

            String content = null;
            try {
                // 5.2 调用组件将DTO转为xml报文字符串
                content = SoapUtils.toXml(orgnlClearingDTO);
            } catch (Exception e) {
                throw new DcepException(ClearingProdErrorEnum.BUSI_COMP_ERROR.getCode(),
                        ClearingProdErrorEnum.BUSI_COMP_ERROR.getDescription());
            }

            // 5.3 组装Rsn
            Rsn rsn = null;
            if (StringUtils.isNotBlank(settlementProdDO.getBizPrcCd())
                    || StringUtils.isNotBlank(settlementProdDO.getBizPrcInf())) {
                rsn = new Rsn(null,
                        StringUtils.isNotBlank(settlementProdDO.getBizPrcCd()) ? settlementProdDO.getBizPrcCd() : null,
                        StringUtils.isNotBlank(settlementProdDO.getBizPrcInf()) ? settlementProdDO.getBizPrcInf() : null);
            }
            // 5.4 付款人钱包名称、付款人钱包ID信息(目前仅针对查询251/262交易的情况)
            String orgnlDbtrWltNm = null;
            String orgnlDbtrWltId = null;

            //收款人名称，针对201查询场景
            String orgnlCdtrNm = null;

            //5.5 211、251、262、281、613.发起411查询时，状态为终态且原交易商户营销信息不为空，则返回增加商户营销信息
            String resMsgTp = CommonUtil.getResMsgTp(settlementProdDO.getMsgTp());
            PrmtInf prmtInf = null;
            String resdtTp = null;
            String resdtCtryCd = null;
            String regrCtryCd = null;
            String sttlmDt = null;
            if (settlementProdDO.getSttlmDt() != null) {
                // 填充结算日期
                sttlmDt = settlementProdDO.getSttlmDt().substring(0, 4) + "-" + settlementProdDO.getSttlmDt().substring(4, 6) +
                        "-" + settlementProdDO.getSttlmDt().substring(6);
            }

            // 5.6 组装BizRpt
            BizRpt bizRpt = new BizRpt(
                    convertTrnRs(settlementProdDO.getBizSts()),
                    rsn,
                    new OrgnlMsgCntt(content, settlementProdDO.getMsgId(), settlementProdDO.getSendPtyId(),
                    settlementProdDO.getMsgTp(), orgnlDbtrWltNm, orgnlDbtrWltId, orgnlCdtrNm,
                    resdtTp, resdtCtryCd, regrCtryCd, sttlmDt, sttlmDt),
                    prmtInf);
            return new Dcep41800101DTO(grpHdr, bizQryRef, bizRpt, null);
        }

        // 6. 418未正常返回 - 查询处理状态为PR01
        bizQryRef.setQryRs(ClearingStatusEnum.FAILED.getCode());
        return new Dcep41800101DTO(grpHdr, bizQryRef, null,
                new OprlErr(new Err(ClearingProdErrorEnum.BUSI_DATA_NOTEXCEED.getCode(), null),
                        ClearingProdErrorEnum.BUSI_DATA_NOTEXCEED.getDescription()));
    }

    private String convertTrnRs(String bizSts) {
        if (ClearingStatusEnum.SUCCESS.getCode().equals(bizSts) || ClearingStatusEnum.FAILED.getCode().equals(bizSts) || ClearingStatusEnum.PROCESS.getCode().equals(bizSts)
                || ClearingStatusEnum.PRESUME_SUCCESS.getCode().equals(bizSts) || ClearingStatusEnum.PRESUME_FAILED.getCode().equals(bizSts) || ClearingStatusEnum.SETTLED.getCode().equals(bizSts)
                || ClearingStatusEnum.SETTLE_QUEUE.getCode().equals(bizSts) || ClearingStatusEnum.CANCELLED.getCode().equals(bizSts) || ClearingStatusEnum.DAYEND_RETURN.getCode().equals(bizSts)) {
            return bizSts;
        }else{
            return ClearingStatusEnum.PROCESS.getCode();
        }
    }

    private void dataValidate(Dcep41700101DTO req, SettlementProdDO settlementProdDO) {
        // 1. 为空直接返回
        if (null == settlementProdDO){
            return;
        }

        String msgId417 = req.getGrpHdr().getMsgId();
        // 2. 校验请求报文的发起运营机构 是否是 原交易的发起运营机构或接收机构或收付款钱柜运营机构
        String instgDrctPtyReq = req.getGrpHdr().getInstgPty().getInstgDrctPty();
        if (null != instgDrctPtyReq) {
            if (!(instgDrctPtyReq.equals(settlementProdDO.getCbtrPtyId())
                    || instgDrctPtyReq.equals(settlementProdDO.getDbtrPtyId()))) {
                log.info("417-GrpHdr-InstgPty not match: MsgId={}, OrgnlInstgPtyReq={}, CdtrPtyId={}, DbtrPtyId={}",
                    msgId417, instgDrctPtyReq, settlementProdDO.getCbtrPtyId(), settlementProdDO.getDbtrPtyId());
                throw new DcepException(ClearingProdErrorEnum.BUSI_NO_ACCESS.getCode(),
                        ClearingProdErrorEnum.BUSI_NO_ACCESS.getDescription());
            }
        }
        // 3. 校验报文编号是否一致
        String orgnlMTreq = req.getOrgnlGrpHdr().getOrgnlMT();
        if (!orgnlMTreq.equals(settlementProdDO.getMsgTp())) {
            log.info("417-OrgnlGrpHdr-OrgnlMT not match: MsgId={}, orgnlMTReq={}, orgnlMT={}", msgId417, orgnlMTreq,
                    settlementProdDO.getMsgTp());
            throw new DcepException(ClearingProdErrorEnum.BUSI_FIELD_NOTMATCH.getCode(),
                    "OrgnlGrpHdr-OrgnlMT" + ClearingProdErrorEnum.BUSI_FIELD_NOTMATCH.getDescription());
        }
        // 4. 校验原交易发起机构是否一致
        String orgnlInstgPty = req.getOrgnlGrpHdr().getOrgnlInstgPty();
        if (StringUtils.isNotBlank(settlementProdDO.getSendPtyId())
                && !orgnlInstgPty.equals(settlementProdDO.getSendPtyId())) {
            log.info("417-OrgnlGrpHdr-OrgnlInstgPty not match: MsgId={}, OrgnlInstgPtyReq={}, SendInst={}", msgId417,
                orgnlInstgPty, settlementProdDO.getSendPtyId());
            throw new DcepException(ClearingProdErrorEnum.BUSI_FIELD_NOTMATCH.getCode(),
                    "OrgnlGrpHdr-OrgnlInstgPty" + ClearingProdErrorEnum.BUSI_FIELD_NOTMATCH.getDescription());
        }
    }

}
