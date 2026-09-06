/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.manager.impl;

import com.dcep.common.exception.DcepException;
import com.dcep.common.model.soap.GrpHdr;
import com.dcep.common.utils.DcepDateUtils;
import com.dcep.common.utils.MsgIdUtil;
import com.dcep.dips.common.dto.PrmtInf;
import com.dcep.dips.common.dto.dc411.Dcep41100101DTO;
import com.dcep.dips.common.dto.dc412.*;
import com.dcep.dips.common.enums.ClearingProdErrorEnum;
import com.dcep.dips.wholesalepayment.common.constants.Constant;
import com.dcep.dips.wholesalepayment.common.utils.CommonUtil;
import com.dcep.dips.wholesalepayment.dal.mapper.FundAdjustProdMapper;
import com.dcep.dips.wholesalepayment.dal.mapper.SettlementProdMapper;
import com.dcep.dips.wholesalepayment.dal.model.FundAdjustProdDO;
import com.dcep.dips.wholesalepayment.dal.model.SettlementProdDO;
import com.dcep.dips.wholesalepayment.enums.ClearingStatusEnum;
import com.dcep.dips.wholesalepayment.enums.MsgTpEnum;
import com.dcep.dips.wholesalepayment.manager.ClearingStsQueryManager;
import com.dcep.dips.wholesalepayment.utils.InfoCacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class ClearingStsQueryManagerImpl implements ClearingStsQueryManager {

	@Autowired
	private SettlementProdMapper settlementProdMapper;
    @Autowired
	private FundAdjustProdMapper fundAdjustProdMapper;

	@Override
	// 机构向中心发送的411报文，查询中心清算状态，中心向机构返回412报文
	public Dcep41200101DTO queryClearingStsFromOrg(Dcep41100101DTO req) {
		log.info("queryClearingStsFromOrg.411req: MsgId={}", req.getGrpHdr().getMsgId());

		// 1. 组412报文头信息
		GrpHdr grphdr = new GrpHdr(
				MsgIdUtil.randomMsgId(MsgTpEnum.TXN_STATE_RESPONSE_ABBR.getCode(), Constant.PBOC,
						CommonUtil.getEnvVal()),
				DcepDateUtils.getDcepDateStrNow(), InfoCacheUtil.getPbocInf(),
				req.getGrpHdr().getInstgPty().getInstgDrctPty(), null);

		// 2. 组BizQryRef
		BizQryRef bizQryRef = new BizQryRef(req.getGrpHdr().getMsgId(), req.getGrpHdr().getInstgPty().getInstgDrctPty(),
				ClearingStatusEnum.SUCCESS.getCode());

		//查询原交易的msgid
		String orgnlMsgId = req.getOrgnlGrpHdr().getOrgnlMsgId();
		String orgnlMsgTp = req.getOrgnlGrpHdr().getOrgnlMT();
		// 3. 数据库查询交易信息
		if(MsgTpEnum.CDT_FUND_DECREASE.getCode().equals(orgnlMsgTp) || MsgTpEnum.CDT_PRE_FUND_DECREASE.getCode().equals(orgnlMsgTp)){
			FundAdjustProdDO fundAdjustProdDO = fundAdjustProdMapper.selectByPrimaryKey(new FundAdjustProdDO(orgnlMsgId));
			log.debug("queryClearingStsFromOrg.fundAdjustProdDO:{}",
					fundAdjustProdDO != null ? fundAdjustProdDO.getMsgId() : "record not found in db");
			if (null != fundAdjustProdDO) {
				Rsn rsn = null;
				if (StringUtils.isNotBlank(fundAdjustProdDO.getBizPrcCd())
						|| StringUtils.isNotBlank(fundAdjustProdDO.getBizPrcInf())) {
					rsn = new Rsn(null,
							StringUtils.isNotBlank(fundAdjustProdDO.getBizPrcCd()) ? fundAdjustProdDO.getBizPrcCd() : null,
							StringUtils.isNotBlank(fundAdjustProdDO.getBizPrcInf()) ? fundAdjustProdDO.getBizPrcInf() : null);
				}

				String sttlmDt = null;
				if (fundAdjustProdDO.getSttlmDt() != null) {
					// 填充结算日期
					sttlmDt = fundAdjustProdDO.getSttlmDt().substring(0, 4) + "-" + fundAdjustProdDO.getSttlmDt().substring(4, 6) +
							"-" + fundAdjustProdDO.getSttlmDt().substring(6);
				}
				String batchId = generateBatchNumber(fundAdjustProdDO.getBizDt());
				return new Dcep41200101DTO(grphdr, bizQryRef,
						new BizRpt(
								convertTrnRs(fundAdjustProdDO.getBizSts()),
								rsn,
								new OrgnlTxInf(fundAdjustProdDO.getMsgId(), fundAdjustProdDO.getSendPtyId(),
										fundAdjustProdDO.getMsgTp(), null, null,
										batchId, null, null, null,
										null , null, null,
										null, null, null, sttlmDt, sttlmDt, null, null),
								null),
						null);
			}
		} else {
			SettlementProdDO settlementProdDO = settlementProdMapper.selectByPrimaryKey(new SettlementProdDO(orgnlMsgId));
			log.debug("queryClearingStsFromOrg.settlementProdDO:{}",
					settlementProdDO != null ? settlementProdDO.getMsgId() : "record not found in db");
			// 4. 交易信息参数校验（不对clearingProdDO对象做非空判断）
			this.dataValidate(req, settlementProdDO);
			// 5. 报文412正常返回
			if (null != settlementProdDO) {
				// 5.1 组装Rsn
				Rsn rsn = null;
				if (StringUtils.isNotBlank(settlementProdDO.getBizPrcCd())
						|| StringUtils.isNotBlank(settlementProdDO.getBizPrcInf())) {
					rsn = new Rsn(null,
							StringUtils.isNotBlank(settlementProdDO.getBizPrcCd()) ? settlementProdDO.getBizPrcCd() : null,
							StringUtils.isNotBlank(settlementProdDO.getBizPrcInf()) ? settlementProdDO.getBizPrcInf() : null);
				}

				// 5.2 付款人钱包名称、付款人钱包ID信息(目前仅针对查询251/262交易的情况)
				String orgnlDbtrWltId = null;
				String orgnlDbtrWltNm = null;
				if (MsgTpEnum.ORDR_CONF_RESPONSE.getCode().equals(settlementProdDO.getMsgTp())) {
					orgnlDbtrWltId = settlementProdDO.getDbtrId();
				}

				//收款人名称，针对201查询场景
				String orgnlCdtrNm = null;

				//5.3 211、251、262、281、613.发起411查询时，状态为终态且原交易商户营销信息不为空，则返回增加商户营销信息
				String resMsgTp = CommonUtil.getResMsgTp(settlementProdDO.getMsgTp());
				PrmtInf prmtInf = null;
				String resdtTp = null;
				String resdtCtryCd = null;
				String regrCtryCd = null;
				String bizPayMtd = null;
				RdrctInf rdrctInf = null;
				String sttlmDt = null;
				if (settlementProdDO.getSttlmDt() != null) {
					// 填充结算日期
					sttlmDt = settlementProdDO.getSttlmDt().substring(0, 4) + "-" + settlementProdDO.getSttlmDt().substring(4, 6) +
							"-" + settlementProdDO.getSttlmDt().substring(6);
				}

				return new Dcep41200101DTO(grphdr, bizQryRef,
						new BizRpt(
								convertTrnRs(settlementProdDO.getBizSts()),
								rsn,
								new OrgnlTxInf(settlementProdDO.getMsgId(), settlementProdDO.getSendPtyId(),
										settlementProdDO.getMsgTp(), settlementProdDO.getBizTp(), settlementProdDO.getBizKind(),
										settlementProdDO.getBatId(), orgnlDbtrWltNm, orgnlDbtrWltId, orgnlCdtrNm,
										null , null, null,
										resdtTp, resdtCtryCd, regrCtryCd, sttlmDt, sttlmDt, bizPayMtd, rdrctInf),
								prmtInf),
						null);
			}
		}

		// 6. 报文412返回未查询到指定数据
		bizQryRef.setQryRs(ClearingStatusEnum.FAILED.getCode());
		return new Dcep41200101DTO(grphdr, bizQryRef, null,
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

	// 交易信息参数校验
	private void dataValidate(Dcep41100101DTO req, SettlementProdDO settlementProdDO) {
		// 1. 不做空对象校验
		if (null == settlementProdDO){
			return;
		}

		String msgId411 = req.getGrpHdr().getMsgId();
		// 2. 校验报文编号是否一致
		String orgnlMTreq = req.getOrgnlGrpHdr().getOrgnlMT();
		if (!orgnlMTreq.equals(settlementProdDO.getMsgTp())) {
			log.info("411-OrgnlGrpHdr-OrgnlMT not match: MsgId={}, orgnlMTReq={}, orgnlMT={}", msgId411, orgnlMTreq,
					settlementProdDO.getMsgTp());
			throw new DcepException(ClearingProdErrorEnum.BUSI_FIELD_NOTMATCH.getCode(),
					"OrgnlGrpHdr-OrgnlMT" + ClearingProdErrorEnum.BUSI_FIELD_NOTMATCH.getDescription());
		}

		// 4. 校验原交易发起机构是否一致
		String orgnlInstgPty = req.getOrgnlGrpHdr().getOrgnlInstgPty();
		if (StringUtils.isNotBlank(settlementProdDO.getSendPtyId())
				&& !orgnlInstgPty.equals(settlementProdDO.getSendPtyId())) {
			log.info("411-OrgnlGrpHdr-OrgnlInstgPty not match: MsgId={}, OrgnlInstgPtyReq={}, SendInst={}", msgId411,
				orgnlInstgPty, settlementProdDO.getSendPtyId());
			throw new DcepException(ClearingProdErrorEnum.BUSI_FIELD_NOTMATCH.getCode(),
					"OrgnlGrpHdr-OrgnlInstgPty" + ClearingProdErrorEnum.BUSI_FIELD_NOTMATCH.getDescription());
		}

		// 5. 校验请求报文的发起运营机构 是否是 原交易的发起运营机构或接收机构
		String instgDrctPtyReq = req.getGrpHdr().getInstgPty().getInstgDrctPty();
		if (null != instgDrctPtyReq) {
			if (!(instgDrctPtyReq.equals(settlementProdDO.getCbtrPtyId())
					|| instgDrctPtyReq.equals(settlementProdDO.getDbtrPtyId()))) {
				log.info("411-GrpHdr-InstgPty not match: MsgId={}, OrgnlInstgPtyReq={}, CdtrPtyId={}, DbtrPtyId={}",
					msgId411, instgDrctPtyReq, settlementProdDO.getCbtrPtyId(), settlementProdDO.getDbtrPtyId());
				throw new DcepException(ClearingProdErrorEnum.BUSI_NO_ACCESS.getCode(),
						ClearingProdErrorEnum.BUSI_NO_ACCESS.getDescription());
			}
		}
	}

	private String generateBatchNumber(String dateTimeString) {
		// 定义输入日期时间的格式
		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
		// 解析输入的日期时间字符串
		LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, inputFormatter);
		// 获取小时和分钟
		int hour = dateTime.getHour();
		hour++;
		// 重新设置分钟和秒为0
		dateTime = dateTime.withHour(hour).withMinute(0).withSecond(0);

		// 定义输出批次号的格式
		DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("ByyyyMMddHH00");
		// 格式化为批次号
		return dateTime.format(outputFormatter);
	}
}
