package com.dcep.dips.wholesalepayment.manager.impl;

import com.dcep.clearingcenter.api.HvpsService;
import com.dcep.clearingcenter.dto.settlement.HvpsReqDTO;
import com.dcep.clearingcenter.dto.settlement.HvpsRspDTO;
import com.dcep.common.exception.DcepException;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.Response;
import com.dcep.dips.wholesalepayment.common.utils.CommonUtil;
import com.dcep.dips.wholesalepayment.common.utils.IdUtils;
import com.dcep.dips.wholesalepayment.dal.mapper.HvpsTransMapper;
import com.dcep.dips.wholesalepayment.dal.mapper.StorageForwardMapper;
import com.dcep.dips.wholesalepayment.dal.model.FundAdjustProdDO;
import com.dcep.dips.wholesalepayment.dal.model.HvpsTransDO;
import com.dcep.dips.wholesalepayment.dal.model.StorageForwardDO;
import com.dcep.dips.wholesalepayment.dto.FundingDTO;
import com.dcep.dips.wholesalepayment.dto.acctrans.ZeroOutReqDTO;
import com.dcep.dips.wholesalepayment.enums.HvpsAdjTypEnum;
import com.dcep.dips.wholesalepayment.enums.MsgTpEnum;
import com.dcep.dips.wholesalepayment.enums.WholesaleErrorEnum;
import com.dcep.dips.wholesalepayment.manager.ClearingCenterManager;
import com.dcep.dips.wholesalepayment.utils.AmtUtils;
import com.dcep.dips.wholesalepayment.utils.InfoCacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;

/**
 * 准备金系统管理类
 */
@Slf4j
@Service
public class ClearingCenterManagerImpl implements ClearingCenterManager {

    @DubboReference
    private HvpsService hvpsService;

    @Autowired
    @Qualifier("asyncBizPool")
    private ExecutorService asyncBizPool;

    @Autowired
    private HvpsTransMapper hvpsTransMapper;

    @Autowired
    private StorageForwardMapper storageForwardMapper;

    @Autowired
    private ClearingCenterManager clearingCenterManager;

    /**
     * 将机构请求异步转发大额
     * @param gwReqDTO 机构请求对象
     */
    public void asyncSendHvps(EnvelopeDTO<GwDTO> gwReqDTO) {
        FundingDTO fundingDTO = (FundingDTO) gwReqDTO.body();

        try {
            log.info("准备切换线程，异步发送准备金系统，msgId={}", fundingDTO.msgId());
            asyncBizPool.execute(() -> sendHvps(gwReqDTO, CommonUtil.getEnvInfo()));
        } catch (RejectedExecutionException re) { // 线程池任务已满,拒绝获取线程
            log.error("asyncBizPool all thread is busy, msgId:{}", fundingDTO.msgId(), re);
        } catch (Exception e) {
            log.error("ClearingCenterManager.asyncSendHvps error, msgId:{}", fundingDTO.msgId(), e);
        }
    }

    /**
     * 异步发送大额系统（存储转发也会走到这）
     *   机构发起的：注资调减、预注资调减
     * @param gwReqDTO 机构的请求DTO
     */
    public void sendHvps(EnvelopeDTO<GwDTO> gwReqDTO, String envInfo) {
        FundingDTO fundingDTO = (FundingDTO) gwReqDTO.body();
        log.info("切换到异步线程：msgId={}, envInfo={}", fundingDTO.msgId(), envInfo);

        try {
            // 安装全链路和环境信息
            CommonUtil.LogMDC(fundingDTO.msgId());
            CommonUtil.setEnvInfo(envInfo);

            // 组大额报文
            HvpsReqDTO hvpsReqDTO = new HvpsReqDTO();
            hvpsReqDTO.setMsgTp(fundingDTO.hvpsMsgTp());
            hvpsReqDTO.setEndToEndId(fundingDTO.msgId());
            hvpsReqDTO.setBizTp(fundingDTO.adjustTp());

            // 获取运营中心在大额的直参行号（清算行号）
            hvpsReqDTO.setDbtrClearingMemberId(InfoCacheUtil.getPbocHvpsClrBkNo());

            // 获取被调减机构在大额的间参行号（运营中心在大额直参行号下的间参行号）
            hvpsReqDTO.setDbtrBrnchId(InfoCacheUtil.getHvpsBkNo(fundingDTO.dbtrPtyId()));

            // 获取被调减机构、被调减机构的托管机构，在大额的清算行号（大额直参行号）
            String custodianInstNo = InfoCacheUtil.getCustodianInstNo(fundingDTO.dbtrPtyId());

            String hvpsClrBkNo = InfoCacheUtil.getHvpsClrBkNo(custodianInstNo);
            hvpsReqDTO.setCdtrClearingMemberId(hvpsClrBkNo);
            hvpsReqDTO.setCdtrBranchId(hvpsClrBkNo);
            hvpsReqDTO.setCurrency(fundingDTO.currency());
            hvpsReqDTO.setAmount(new BigDecimal(fundingDTO.adjustAmt()));

            Response<HvpsRspDTO> hvpsResp = sendHvps(hvpsReqDTO);
            if (!hvpsResp.isSuccess()) {
                log.warn("发送《准备金》失败，等待存储转发，msgId={}", fundingDTO.msgId());
                return;
            }

            log.info("《准备金》应答成功");
            clearingCenterManager.delStorageForwardAndInsertHvpsTrans(fundingDTO.msgId(), fundingDTO.msgTp(),
                    hvpsReqDTO, hvpsResp.getResult());

        } catch (Exception e) {
            log.error("调用《准备金》出现异常", e);
        }
    }

    /**
     * 将结算钱包请求异步转发大额
     * @param zeroOutReqDTO 结算钱包请求对象
     * @param fundAdjustProdDO 结算钱包请求对象
     */
    @Override
    public void asyncSendHvps(ZeroOutReqDTO zeroOutReqDTO, FundAdjustProdDO fundAdjustProdDO) {
        try {
            log.info("准备切换线程，异步发送准备金系统，msgId={}", fundAdjustProdDO.getMsgId());
            asyncBizPool.execute(() -> sendHvps(zeroOutReqDTO, fundAdjustProdDO, CommonUtil.getEnvInfo()));
        } catch (RejectedExecutionException re) { // 线程池任务已满,拒绝获取线程
            log.error("asyncBizPool all thread is busy, msgId:{}", fundAdjustProdDO.getMsgId(), re);
        } catch (Exception e) {
            log.error("ClearingCenterManager.asyncSendHvps error, msgId:{}", fundAdjustProdDO.getMsgTp(), e);
        }
    }

    /**
     * 异步发送大额系统（存储转发也会走到这）
     *   结算钱包发起的：注资清零
     * @param zeroOutReqDTO 结算钱包的请求DTO
     * @param fundAdjustProdDO 结算钱包的请求DTO
     */
    public void sendHvps(ZeroOutReqDTO zeroOutReqDTO, FundAdjustProdDO fundAdjustProdDO, String envInfo) {
        log.info("切换到异步线程：msgId={}, envInfo={}", fundAdjustProdDO.getMsgId(), envInfo);

        try {
            // 安装全链路和环境信息
            CommonUtil.LogMDC(fundAdjustProdDO.getMsgId());
            CommonUtil.setEnvInfo(envInfo);

            // 组大额报文
            HvpsReqDTO hvpsReqDTO = new HvpsReqDTO();
            hvpsReqDTO.setMsgTp(MsgTpEnum.CDT_FUND_DECREASE_OUT.getCode());
            hvpsReqDTO.setEndToEndId(fundAdjustProdDO.getMsgId());
            hvpsReqDTO.setBizTp(HvpsAdjTypEnum.ZERO_OUT.getCode());

            // 获取运营中心在大额直参行号(清算行号)
            hvpsReqDTO.setDbtrClearingMemberId(InfoCacheUtil.getPbocHvpsClrBkNo());

            // 获取被调减机构在大额的间参行号
            hvpsReqDTO.setDbtrBrnchId(InfoCacheUtil.getHvpsBkNo(fundAdjustProdDO.getAdjustPtyId()));

            // 获取被调减机构的托管机构，在大额直参行号(清算行号)
            String custodianInstNo = InfoCacheUtil.getCustodianInstNo(fundAdjustProdDO.getAdjustPtyId());
            String hvpsClrBkNo = InfoCacheUtil.getHvpsClrBkNo(custodianInstNo);
            hvpsReqDTO.setCdtrClearingMemberId(hvpsClrBkNo);
            hvpsReqDTO.setCdtrBranchId(hvpsClrBkNo);

            hvpsReqDTO.setCurrency(fundAdjustProdDO.getCurrency());
            hvpsReqDTO.setAmount(AmtUtils.toCents(fundAdjustProdDO.getAdjustAmt()));

            Response<HvpsRspDTO> response = sendHvps(hvpsReqDTO);
            if (!response.isSuccess()) {
                log.info("发《准备金》失败，等待后续存储转发重新调度，msgId={}", fundAdjustProdDO.getMsgId());
                return;
            }

            clearingCenterManager.delStorageForwardAndInsertHvpsTrans(fundAdjustProdDO.getMsgId(),
                    fundAdjustProdDO.getMsgTp(), hvpsReqDTO, response.getResult());

        } catch (Exception e) {
            log.error("调用《准备金》出现异常", e);
        }
    }

    public Response<HvpsRspDTO> sendHvps(HvpsReqDTO hvpsReqDTO) {
        Response<HvpsRspDTO> response;
        try {
            log.info("请求准备金：{}", hvpsReqDTO);
            response = hvpsService.sendHvpsMessage(hvpsReqDTO);
            log.info("准备金应答：{}", response);
        } catch (Exception e) {
            log.error("准备金应答：抛出异常", e);
            return new Response<>(false, null,
                    WholesaleErrorEnum.UNKNOWN_EXCEPTION.getCode(),
                    WholesaleErrorEnum.UNKNOWN_EXCEPTION.getDescription());
        }

        if (null == response) {
            log.error("准备金应答：为空");
            return new Response<>(false, null,
                    WholesaleErrorEnum.UNKNOWN_EXCEPTION.getCode(),
                    WholesaleErrorEnum.UNKNOWN_EXCEPTION.getDescription());
        }

        return response;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void delStorageForwardAndInsertHvpsTrans(String msgId, String msgTp, HvpsReqDTO hvpsReqDTO, HvpsRspDTO hvpsRspDTO) {
        log.info("摘除《准备金》的存储转发：msgId={},msgTp={}", msgId, msgTp);
        storageForwardMapper.deleteByPrimaryKey(new StorageForwardDO(msgId, msgTp));

        HvpsTransDO hvpsTransDO = new HvpsTransDO(hvpsReqDTO, hvpsRspDTO);
        log.info("插入《大额对接产品表》：{}", hvpsTransDO);
        hvpsTransMapper.insert(hvpsTransDO);
    }
}
