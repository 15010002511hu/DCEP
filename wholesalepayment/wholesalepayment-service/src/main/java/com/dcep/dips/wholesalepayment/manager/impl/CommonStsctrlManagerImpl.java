/*
 * pbcdci.cn Inc.
 * Copyright © 2025 PBCDCI All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.manager.impl;

import cn.hutool.core.util.StrUtil;
import com.dcep.common.enums.ErrorEnum;
import com.dcep.common.exception.DcepException;
import com.dcep.common.model.Response;
import com.dcep.common.utils.ThreadPoolUtils;
import com.dcep.dips.wholesalepayment.common.utils.CommonUtil;
import com.dcep.dips.wholesalepayment.dal.mapper.AccountingInstrMapper;
import com.dcep.dips.wholesalepayment.dal.mapper.CommonStsctrlMapper;
import com.dcep.dips.wholesalepayment.dal.mapper.SettlementProdMapper;
import com.dcep.dips.wholesalepayment.dal.model.AccountingInstrDO;
import com.dcep.dips.wholesalepayment.dal.model.CommonStsctrlDO;
import com.dcep.dips.wholesalepayment.dal.model.SettlementProdDO;
import com.dcep.dips.wholesalepayment.enums.ActgStsEnum;
import com.dcep.dips.wholesalepayment.enums.ClearingStatusEnum;
import com.dcep.dips.wholesalepayment.enums.WholesaleErrorEnum;
import com.dcep.dips.wholesalepayment.manager.CommonManager;
import com.dcep.dips.wholesalepayment.manager.CommonStsctrlManager;
import com.dcep.dips.wholesalepayment.manager.PaymentManager;
import com.dcep.dips.wholesalepayment.manager.redo.RedoManager;
import com.dcepex.trace.support.async.TraceExecutorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;

@Slf4j
@Service
public class CommonStsctrlManagerImpl implements CommonStsctrlManager {

    ExecutorService pool = new TraceExecutorService(ThreadPoolUtils.getThreadPoolFixSize(10, "clearingsts"));

    @Resource
    private CommonStsctrlMapper commonStsctrlMapper;
    @Resource
    private SettlementProdMapper settlementProdMapper;
    @Resource
    private AccountingInstrMapper accountingInstrMapper;
    @Resource
    private CommonManager commonManager;
    @Resource
    private PaymentManager paymentManager;

    @Override
    public Response<String> endReturn(String currentSystemDate) throws DcepException {
        log.info("CommonStsctrlManager endReturn start date: {}", currentSystemDate);

        if (StrUtil.isBlank(currentSystemDate)) {
            log.error("CommonStsctrlManager endReturn request param is null");
            return new Response<>(false, null, ErrorEnum.VALIDATION_ERROR.getCode(), ErrorEnum.VALIDATION_ERROR.getCode());
        }
        List<SettlementProdDO> list = null;
        String msgId = null;
        List<Future<?>> result = new ArrayList<>();
        do {
            list = settlementProdMapper.selectQueueByCurrentSystemDate(currentSystemDate, msgId);
            if (list == null || list.size() == 0) {
                break;
            }
            Optional<SettlementProdDO> max = list.stream().max(Comparator.comparing(SettlementProdDO::getMsgId));
            msgId = max != null && max.get() != null ? max.get().getMsgId() : null;
            Future<?> submit = batch(list);
            result.add(submit);
        } while (list != null && list.size() > 0);

        for (Future<?> future : result){
            try {
                future.get();
            } catch (Exception e) {
                log.error("CommonStsctrlManager endReturn Error. Exception:{}", e);
            }
        }

        list = settlementProdMapper.selectQueueByCurrentSystemDate(currentSystemDate, null);
        if(list != null && list.size() > 0){
            //TODO 说明结算排队取消有失败的，需要加入监控，人工处理
            log.error("CommonStsctrlManager endReturn Error. list size:{}", list.size());
            return new Response<>(false, null, WholesaleErrorEnum.UNKNOWN_EXCEPTION.getCode(), WholesaleErrorEnum.UNKNOWN_EXCEPTION.getCode());
        }

        //说明结算排队数据已处理完成，通知返回
        log.info("CommonStsctrlManager endReturn end ");
        return new Response<>(true, null);
    }
    private Future<?> batch(List<SettlementProdDO> list){
        Future<?> submit = null;
        try {
            submit = pool.submit(() -> {
                revoke(list);
            });
        } catch (RejectedExecutionException re) {// 线程池任务已满,拒绝获取线程
            // 线程池任务已满,主流程执行
            revoke(list);
        } catch (Exception e) {// 其它异常
            log.error("CommonStsctrlManager batch Error. Exception:{}", e);
        }
        return submit;
    }

    private boolean revoke(List<SettlementProdDO> list) {
        for(SettlementProdDO settle:list) {
            try {
                if (ClearingStatusEnum.SETTLE_QUEUE.getCode().equals(settle.getBizSts())
                    || ClearingStatusEnum.WAIT_SETTLE.getCode().equals(settle.getBizSts())) {
                    commonManager.reversal(settle, true);
                }else if (ClearingStatusEnum.PROCESS.getCode().equals(settle.getBizSts())) {
                    AccountingInstrDO accountingInstrDO = accountingInstrMapper.selectByMsgId(settle.getMsgId());
                    paymentManager.presumeFail(settle, accountingInstrDO);
                }else if (ClearingStatusEnum.ACCEPTED.getCode().equals(settle.getBizSts())) {
                    //TODO 待确认
                }
            }catch (Exception e){
                log.error("CommonStsctrlManager revoke Error. Exception:{}", e);
            }
        }
        return true;
    }

    @Override
    public void processAll() {
        //超过一定时间需要重试的
        List<CommonStsctrlDO> list = commonStsctrlMapper.selectForProcess(new Date(), CommonUtil.getEnvInfo());
        if(list != null && list.size() > 0) {
            for (CommonStsctrlDO commonStsctrlDO : list) {
                try {
                    pool.execute(() -> {
                        process(commonStsctrlDO);
                    });
                } catch (RejectedExecutionException re) {// 线程池任务已满,拒绝获取线程
                    // 线程池任务已满,主流程执行
                    process(commonStsctrlDO);
                } catch (Exception e) {// 其它异常
                    log.error("StsCtrlManagerImpl Error MsgId:{}, Exception:{}", commonStsctrlDO.getMsgId(), e);
                }
            }
        }
        //超过一定时间需要解锁的
        List<CommonStsctrlDO> locked = commonStsctrlMapper.selectForProcessLocked(new Date(), CommonUtil.getEnvInfo());
        if(locked != null && locked.size() > 0){
            for (CommonStsctrlDO commonStsctrlDO : locked) {
                try {
                    pool.execute(() -> {
                        unLock(false,commonStsctrlDO);
                    });
                } catch (RejectedExecutionException re) {// 线程池任务已满,拒绝获取线程
                    // 线程池任务已满,主流程执行
                    unLock(false,commonStsctrlDO);
                } catch (Exception e) {// 其它异常
                    log.error("StsCtrlManagerImpl unlock Error MsgId:{}, Exception:{}", commonStsctrlDO.getMsgId(), e);
                }
            }
        }


    }

    @Override
    public void processLockedAll() {

    }

    @Override
    public void process(CommonStsctrlDO commonStsctrlDO) {
        // 是否终态标识
        boolean status = false;
        try {
            //抢占锁成功，进行后续处理
            if(!lock(commonStsctrlDO)){
                return;
            }
            status = redo(commonStsctrlDO);
        }catch (Exception e) {
            log.error("StsCtrlManagerImpl redo Error MsgId:{}, Exception:", commonStsctrlDO.getMsgId(), e);
        }finally {
            unLock(status,commonStsctrlDO);
        }
    }

    public boolean redo(CommonStsctrlDO commonStsctrlDO) throws DcepException {

        AccountingInstrDO accountingInstrDO = accountingInstrMapper.selectByMsgIdPrepare(commonStsctrlDO.getMsgId());
        if(accountingInstrDO==null){
            log.error("StsCtrlManagerImpl redo Error MsgId:{}, AccountingInstrDO is null", commonStsctrlDO.getMsgId());
            return false;
        }

        //结算排队无需补偿处理
        if(ActgStsEnum.QUEUED.getCode().equals(accountingInstrDO.getActgSts())){
            log.info("StsCtrlManagerImpl redo Error MsgId:{}, AccountingInstrDO is QUEUED", commonStsctrlDO.getMsgId());
            return false;
        }

        //重试
        RedoManager redoManager = RedoManager.get(accountingInstrDO.getMsgTp());
        if(redoManager==null){
            log.error("StsCtrlManagerImpl redo Error MsgId:{}, RedoManager is null", commonStsctrlDO.getMsgId());
            return false;
        }
        return redoManager.redo(commonStsctrlDO,accountingInstrDO);
    }

    // 加锁
    private boolean lock(CommonStsctrlDO commonStsctrlDO) {
        if (commonStsctrlMapper.lock(commonStsctrlDO) != 1) {
            return false;
        }
        log.info("Lock Succ MsgId:{}", commonStsctrlDO.getMsgId());
        return true;
    }

    // 释放锁
    private boolean unLock(boolean status ,CommonStsctrlDO commonStsctrlDO) {
        if(status){
            //正常都是在业务逻辑中删除超时控制。此处做个兜底
            commonStsctrlMapper.deleteByPrimaryKey(commonStsctrlDO);
            return true;
        }
        commonStsctrlDO.setSendTime(CommonUtil.queryRateSet(commonStsctrlDO.getGmtCreate()));
        if (commonStsctrlMapper.unLock(commonStsctrlDO) != 1) {
            //可能在业务逻辑中已删除。
            return false;
        }
        log.info("unLock Succ MsgId:{}", commonStsctrlDO.getMsgId());
        return true;
    }
}
