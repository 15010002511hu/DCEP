package com.dcep.dips.wholesalepayment.manager.impl;

import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.Response;
import com.dcep.common.model.soap.SoapBody;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.utils.DcepDateUtils;
import com.dcep.dips.acctrans.dto.accounting.transfer.TransferRespDTO;
import com.dcep.dips.common.constant.CommonConstant;
import com.dcep.dips.wholesalepayment.common.constants.Constant;
import com.dcep.dips.wholesalepayment.common.utils.BatIdUtil;
import com.dcep.dips.wholesalepayment.common.utils.IdUtils;
import com.dcep.dips.wholesalepayment.common.utils.TimeUtil;
import com.dcep.dips.wholesalepayment.dal.mapper.AccountingInstrMapper;
import com.dcep.dips.wholesalepayment.dal.mapper.SystemStatusDOMapper;
import com.dcep.dips.wholesalepayment.dal.mapper.ZerooutCtrlDOMapper;
import com.dcep.dips.wholesalepayment.dal.model.AccountingInstrDO;
import com.dcep.dips.wholesalepayment.dal.model.ZerooutCtrlDO;
import com.dcep.dips.wholesalepayment.dto.dc203.*;
import com.dcep.dips.wholesalepayment.dto.mcbs101.ClrDtlInf;
import com.dcep.dips.wholesalepayment.dto.mcbs101.Mcbs10100101DTO;
import com.dcep.dips.wholesalepayment.enums.WholesaleErrorEnum;
import com.dcep.dips.wholesalepayment.manager.ChainZeroOutManager;
import com.dcep.dips.wholesalepayment.manager.MbridgeManager;
import com.dcep.dips.wholesalepayment.manager.MbridgeZeroOutManager;
import com.dcep.dips.wholesalepayment.manager.StorageForwardManager;
import com.dcep.dips.wholesalepayment.manager.convert.Mbridge101ToDcep203;
import com.dcep.gateway.mcbdc.api.GwoutService;
import com.dcep.gateway.mcbdc.dto.soap.GenericEnvelopeDTO;
import com.dcep.gateway.mcbdc.dto.soap.GenericGwDTO;
import com.dcepex.trace.support.async.TraceExecutorService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;

@Slf4j
@Service
public class MbridgeZeroOutManagerImpl implements MbridgeZeroOutManager {

    @Autowired
    @Qualifier("asyncBizPool")
    private ExecutorService asyncPool;

    @Autowired
    MbridgeManager mbridgeManager;

    @Autowired
    AccountingInstrMapper accountingInstrMapper;

    @Autowired
    ChainZeroOutManager chainZeroOutManager;

    @Autowired
    ZerooutCtrlDOMapper zerooutCtrlDOMapper;

    @Autowired
    SystemStatusDOMapper systemStatusMapper;

    @Autowired
    StorageForwardManager storageForwardManager;

    @DubboReference
    GwoutService mBridgeGwoutService;

    /**
     * 更新清零空表更新记账指令表
     *
     * @param mcbs10100101DTO@return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void recordZOCtrlAndAcctInstr(Mcbs10100101DTO mcbs10100101DTO,ZerooutCtrlDO zerooutCtrlDO) {
        //清零通知时发送给货币桥平台的交易标识号，清零控制表和记账指令表对应
        String orgnlmsgId = mcbs10100101DTO.getOrgnlGrpInf().getOrgnlMsgId();
        log.info("MbridgeZeroOutManager.recordZOCtrlAndAcctInstr start orgnlmsgId:{}",orgnlmsgId);
        //获取当前系统日期
        String curSysDt = systemStatusMapper.selectCurSysDt(CommonConstant.SysCode.WHOLESALE);
        //清零结果list
        List<ClrDtlInf> clrDtlInfList = mcbs10100101DTO.getClrDtls().getClrDtlInf();
        for (ClrDtlInf clrDtlInf : clrDtlInfList){
            //1.生成交易流水号
            String transId = IdUtils.randomTransIdWithBizDt(curSysDt);
            //orgnlmsgId：清零控制表唯一交易标识
            AccountingInstrDO actgInstrDO = new AccountingInstrDO(orgnlmsgId,transId,clrDtlInf,curSysDt);
            //登记记账指令表
            try {
                accountingInstrMapper.insert(actgInstrDO);
            }catch (Exception e){
                log.error("MbridgeZeroOutManager.recordZOCtrlAndAcctInstr insert error orgnlmsgId:{},transId:{}",orgnlmsgId,transId);
                //todo 单条插入失败如何处理
            }
            log.info("MbridgeZeroOutManager.recordZOCtrlAndAcctInstr insert success orgnlmsgId:{},transId:{}",orgnlmsgId,transId);
            //异步记账
            asyncTransfer(actgInstrDO);

            //组装dcep.203报文
            EnvelopeDTO<GwDTO> gwDTOEnvelopeDTO = Mbridge101ToDcep203.convertToDcep203(clrDtlInf, mcbs10100101DTO);
            //插入存储转发表
            storageForwardManager.saveForInst(gwDTOEnvelopeDTO,clrDtlInf.getFinInsTnId().getClrSysMmbId().getMmbId());
        }

        //更新清零控制表，按照原交易流水号更新清零状态
        zerooutCtrlDO.setPrcSts(Constant.ZERO_OUT_CTRL_STATUS_02);
        zerooutCtrlDO.setGmtModified(new Date());
        //zerooutCtrlDO.setActgSts(Constant.ZERO_OUT_ACTG_STATUS_PR10);//可不填
        zerooutCtrlDOMapper.updateByPrimaryKey(zerooutCtrlDO);
        log.info("MbridgeZeroOutManager.recordZOCtrlAndAcctInstr end orgnlmsgId:{}",orgnlmsgId);
    }

    public void asyncTransfer(AccountingInstrDO actgInstrDO){
        try {
            asyncPool.execute(() -> {
                Response<TransferRespDTO> transfer = chainZeroOutManager.transfer(actgInstrDO);
                if (!transfer.isSuccess()) {
                    // todo 调用结算钱包失败
                    log.info("clearing fail transId:{}", actgInstrDO.getTransId());
                }
                //调用结算钱包成功更新《记账指令表》记账日期
                actgInstrDO.setActgDt(transfer.getResult().getAccountingDate());
                accountingInstrMapper.updateAccountingInstr(actgInstrDO);
            });
        } catch (RejectedExecutionException re) {
            log.error("MbridgeZeroOutManager.asySendTransfer error inst exception:{}",
                    re);
        } catch (Exception e) {
            log.error("MbridgeZeroOutManager.asySendTransfer error inst exception:{}",
                    e);
        }
    }
    /**
     * 调用货币桥网关
     *
     * @param genericReq
     * @return
     */
    public Response<GenericEnvelopeDTO<GenericGwDTO>> mbridgeGateway(GenericEnvelopeDTO<GenericGwDTO> genericReq) {
        // 调用货币桥网关
        Response<GenericEnvelopeDTO<GenericGwDTO>> resp = null;
        try {
            resp = mBridgeGwoutService.execute(genericReq);
        } catch (Exception e) {
            log.error(Constant.INTERNAL_ERROR_MSG, e);
        }
        if (resp == null) {
            return new Response<>(false, null,
                    WholesaleErrorEnum.UNKNOWN_EXCEPTION.getCode(),
                    WholesaleErrorEnum.UNKNOWN_EXCEPTION.getDescription());
        }
        return resp;
    }

}
