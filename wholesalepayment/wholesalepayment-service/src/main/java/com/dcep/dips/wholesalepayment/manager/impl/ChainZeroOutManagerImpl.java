package com.dcep.dips.wholesalepayment.manager.impl;

import com.alibaba.fastjson.JSONObject;
import com.dcep.common.exception.DcepException;
import com.dcep.common.model.Response;
import com.dcep.dips.acctrans.api.AccountingService;
import com.dcep.dips.acctrans.dto.accounting.transfer.TransferReqDTO;
import com.dcep.dips.acctrans.dto.accounting.transfer.TransferRespDTO;
import com.dcep.dips.common.constant.CommonConstant;
import com.dcep.dips.wholesalepayment.common.constants.Constant;
import com.dcep.dips.wholesalepayment.common.utils.IdUtils;
import com.dcep.dips.wholesalepayment.dal.mapper.AccountingInstrMapper;
import com.dcep.dips.wholesalepayment.dal.mapper.SystemStatusDOMapper;
import com.dcep.dips.wholesalepayment.dal.mapper.ZerooutCtrlDOMapper;
import com.dcep.dips.wholesalepayment.dal.model.AccountingInstrDO;
import com.dcep.dips.wholesalepayment.dal.model.ZerooutCtrlDO;
import com.dcep.dips.wholesalepayment.dto.chain.ZeroOutReportDTO;
import com.dcep.dips.wholesalepayment.dto.chain.ZeroOutResult;
import com.dcep.dips.wholesalepayment.enums.WholesaleErrorEnum;
import com.dcep.dips.wholesalepayment.manager.ChainZeroOutManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;

@Slf4j
@Service
public class ChainZeroOutManagerImpl implements ChainZeroOutManager {

    @Autowired
    @Qualifier("asyncBizPool")
    private ExecutorService asyncPool;

    @Autowired
    ZerooutCtrlDOMapper zerooutCtrlDOMapper;

    @Autowired
    AccountingInstrMapper accountingInstrMapper;

    @Autowired
    SystemStatusDOMapper systemStatusMapper;

    @DubboReference
    AccountingService accountingService;

    @Override
    public int recordZeroOutCtrl(ZerooutCtrlDO zeroOutCtrl) {
        log.info("ChainZeroOutManager.recordZeroOutCtrl start msgId:{}",zeroOutCtrl.getMsgId());
        //登记清零控制表
        int insert = zerooutCtrlDOMapper.insert(zeroOutCtrl);
        return insert;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void recordZOCtrlAndAcctInstr(ZeroOutReportDTO zeroOutReportDto,ZerooutCtrlDO zerooutCtrlDO) {
        //清零通知时发送给区块链平台的交易标识号，清零控制表和记账指令表对应
        String msgId = zeroOutReportDto.getMsgId();
        String orgnlMsgId = zeroOutReportDto.getOrgnlMsgId();
        log.info("ChainZeroOutManager.recordZOCtrlAndAcctInstr start msgId:{},orgnlMsgId:{}",msgId,orgnlMsgId);
        //清零结果list
        List<ZeroOutResult> zeroOutResultlList = zeroOutReportDto.getZeroOutResultlList();
        String curSysDt = systemStatusMapper.selectCurSysDt(CommonConstant.SysCode.WHOLESALE);
        for (ZeroOutResult zeroOutResult : zeroOutResultlList){
            //1.生成交易流水号
            String transId = IdUtils.randomTransIdWithBizDt(curSysDt);
            //orgnlMsgId：清零控制表唯一交易标识
            AccountingInstrDO actgInstrDO = new AccountingInstrDO(orgnlMsgId,transId,zeroOutResult,curSysDt);
            //登记记账指令表
            try {
                accountingInstrMapper.insert(actgInstrDO);
            }catch (Exception e){
                log.error("ChainZeroOutManager.recordZOCtrlAndAcctInstr insert error msgId:{},orgnlMsgId:{}",msgId,orgnlMsgId);
                //todo 单条插入失败如何处理
            }

            log.info("ChainZeroOutManager.recordZOCtrlAndAcctInstr insert success msgId:{},orgnlMsgId:{}",msgId,orgnlMsgId);
            //异步记账
            asyncTransfer(actgInstrDO);

        }
        //更新清零控制表，按照原交易流水号更新清零状态
        zerooutCtrlDO.setPrcSts(Constant.ZERO_OUT_CTRL_STATUS_02);
        zerooutCtrlDO.setGmtModified(new Date());
        //zerooutCtrlDO.setActgSts(Constant.ZERO_OUT_ACTG_STATUS_PR10)不能
        zerooutCtrlDOMapper.updateByPrimaryKey(zerooutCtrlDO);
        log.info("ChainZeroOutManager.recordZOCtrlAndAcctInstr end msgId:{},orgnlMsgId:{}",msgId,orgnlMsgId);
    }

    /**
     * 获取原交易
     *
     * @param msgId
     * @return
     */
    @Override
    public ZerooutCtrlDO selectByMsgId(String msgId) {
        return zerooutCtrlDOMapper.selectByPrimaryKey(msgId);
    }
    /**
     * 获取原交易
     *
     * @param taskId
     * @return
     */
    @Override
    public ZerooutCtrlDO selectByTaskId(String taskId) {
        return zerooutCtrlDOMapper.selectByTaskId(taskId);
    }

    /**
     * 按系统日期获取原交易
     *
     * @param sysdt,id
     * @return
     */
    @Override
    public ZerooutCtrlDO selectBySysDtId(String sysdt,String id) {
        return zerooutCtrlDOMapper.selectBySysDtId(sysdt,id);
    }

    /**
     * 清零数据更新
     *
     * @param zerooutCtrlDO
     * @return
     */
    @Override
    public void updateByPrimaryKey(ZerooutCtrlDO zerooutCtrlDO) {
        int i = zerooutCtrlDOMapper.updateByPrimaryKey(zerooutCtrlDO);
        if(i!=1){
            log.error("ChainZeroOutManager.updateByPrimaryKey error msgId:{}",zerooutCtrlDO.getMsgId());
            throw new DcepException(WholesaleErrorEnum.BUSI_UPDATE_DB_EXCEPTION.getCode(), WholesaleErrorEnum.BUSI_UPDATE_DB_EXCEPTION.getDescription());
        }
    }

    /**
     * 异步调用结算钱包系统,成功后更新记账指令表
     * @param accountingInstrDO
     */
    public void asyncTransfer(AccountingInstrDO accountingInstrDO) {
        // 组结算钱包系统请求报文
        try {
            log.info("异步调用结算钱包");
            asyncPool.execute(() -> {
                Response<TransferRespDTO> transfer = transfer(accountingInstrDO);
                if (!transfer.isSuccess()) {
                    // todo 调用结算钱包失败
                    log.info("MbridgeZeroOutManager asyncTransfer fail transId:{}", accountingInstrDO.getTransId());
                }
                //调用结算钱包成功更新《记账指令表》记账日期
                accountingInstrDO.setActgDt(transfer.getResult().getAccountingDate());
                accountingInstrMapper.updateAccountingInstr(accountingInstrDO);
            });
        } catch (RejectedExecutionException re) {
            log.error("MbridgeZeroOutManager.asySendTransfer error inst exception:{}", re);
        } catch (Exception e) {
            log.error("MbridgeZeroOutManager.asySendTransfer error inst exception:{}", e);
        }

    }

    /**
     * 调用结算钱包transfer
     *
     * @param accountingInstrDO
     * @return
     */
    @Override
    public Response<TransferRespDTO> transfer(AccountingInstrDO accountingInstrDO) {
        // 组结算钱包系统请求报文
        TransferReqDTO transferReqDTO = new TransferReqDTO();
        transferReqDTO.setTransId(accountingInstrDO.getTransId());
        transferReqDTO.setMsgId(accountingInstrDO.getMsgId());
        transferReqDTO.setMsgType("");
        // todo 记账业务类型
        // todo 记账业务种类
        transferReqDTO.setBizPriority(accountingInstrDO.getBizPrty());
        // todo 应用标识-0000：批发
        transferReqDTO.setEndToEndId(accountingInstrDO.getEndToEndId());
        // todo
        transferReqDTO.setCurrency(accountingInstrDO.getCurrency());
        transferReqDTO.setAmount(accountingInstrDO.getAmount());
        transferReqDTO.setAbstractCode(accountingInstrDO.getAbstractCd());
        transferReqDTO.setAbstractDescription(accountingInstrDO.getAbstractDesc());

        Response<TransferRespDTO> transfer;
        try {
            log.info("清零结果通知-请求结算钱包:{}", JSONObject.toJSONString(transferReqDTO));
            transfer = accountingService.transfer(transferReqDTO);
            log.info("清零结果通知-结算钱包响应:{}", JSONObject.toJSONString(transfer));
        } catch (Exception e) {
            log.error("清零结果通知-结算钱包抛出异常Exception，通讯异常，错误信息: message={}", e.getMessage(), e);
            return new Response<>(false, null,
                    WholesaleErrorEnum.UNKNOWN_EXCEPTION.getCode(),
                    WholesaleErrorEnum.UNKNOWN_EXCEPTION.getDescription());
        }

        if (null == transfer) {
            log.error("结算钱包返回空应答，通讯异常。");
            return new Response<>(false, null,
                    WholesaleErrorEnum.UNKNOWN_EXCEPTION.getCode(),
                    WholesaleErrorEnum.UNKNOWN_EXCEPTION.getDescription());
        }
        return transfer;
    }

}
