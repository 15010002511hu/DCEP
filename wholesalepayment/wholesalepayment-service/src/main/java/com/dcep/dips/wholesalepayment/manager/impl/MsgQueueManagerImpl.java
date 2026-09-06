package com.dcep.dips.wholesalepayment.manager.impl;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.dcep.common.utils.DcepDateUtils;
import com.dcep.common.utils.ThreadPoolUtils;
import com.dcep.dips.wholesalepayment.common.constants.Constant;
import com.dcep.dips.wholesalepayment.dal.mapper.ChainTransMapper;
import com.dcep.dips.wholesalepayment.dal.mapper.SettlementProdMapper;
import com.dcep.dips.wholesalepayment.dal.model.ChainTransDO;
import com.dcep.dips.wholesalepayment.dal.model.SettlementProdDO;
import com.dcep.dips.wholesalepayment.enums.ClearingSendModeEnum;
import com.dcep.dips.wholesalepayment.manager.MsgQueueManager;
import com.dcepex.trace.support.async.TraceExecutorService;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

@Slf4j
@Component
public class MsgQueueManagerImpl implements MsgQueueManager {

    ExecutorService kafkaPool = new TraceExecutorService(
        ThreadPoolUtils.getThreadPoolFixSize(15, "msgQueueSend"));

    @NacosValue(value = "${kafka_send_limitcount}", autoRefreshed = true)
    String KAFKA_SEND_LIMITCOUNT;

    @NacosValue(value = "${kafka_send_beforedays}", autoRefreshed = true)
    String KAFKA_SEND_BEFOREDAYS;

    @Autowired
    private SettlementProdMapper settlementProdMapper;
    @Autowired
    ChainTransMapper chainTransMapper;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    String COMMA = ",";

    /**
     * 推送kafka
     */
    @Override
    public void sendMsg(){
        //初始化宕机因素等状态处理中交易
        Date curTime = new Date();
        updateSendFailInfo(curTime);

        // 查询结算产品表终态交易满足推送交易信息
        // 默认查询前一天后一天，如果kafka_send_beforedays配置了值，则按该值查询前几天
        int beforeDays = 1;
        if (!StringUtils.isBlank(KAFKA_SEND_BEFOREDAYS) && Integer.valueOf(KAFKA_SEND_BEFOREDAYS) > 0) {
            beforeDays = Integer.valueOf(KAFKA_SEND_BEFOREDAYS);
        }
        List<SettlementProdDO> settlementProdDOList = settlementProdMapper.selectByPartitionTime(
                DcepDateUtils.fetchDateStr(curTime, beforeDays * (-1), DcepDateUtils.ISO_DATE_PATTERN),
                DcepDateUtils.getNextDate(curTime, DcepDateUtils.ISO_DATE_PATTERN),
                Long.valueOf(KAFKA_SEND_LIMITCOUNT));
        if (null == settlementProdDOList || settlementProdDOList.size() == 0){
            return;
        }
        // 遍历列表循环处理
        log.debug("Start While Send Kafka sharding:{}, Total:{}", settlementProdDOList.size());
        for (SettlementProdDO settlementProdDO : settlementProdDOList) {
            //推送要素
            try {
                kafkaPool.execute(() -> {
                    push(settlementProdDO);
                });
            } catch (RejectedExecutionException re) {// 线程池任务已满,拒绝获取线程
                // 线程池任务已满,主流程执行
                push(settlementProdDO);
            } catch (Exception e) {
                log.error("MsgQueueManager Error MsgId:{}, Exception:{}", settlementProdDO.getMsgId(), e);
            }
        }
    }

    private void updateSendFailInfo(Date curTime) {
        //查询结算产品表获取指定范围交易
        List<SettlementProdDO> list = settlementProdMapper.selectSendFailInfo(
                DcepDateUtils.getLastDate(curTime, DcepDateUtils.ISO_DATE_PATTERN),
                DcepDateUtils.getNextDate(curTime, DcepDateUtils.ISO_DATE_PATTERN), new Date());
        if (null == list || list.size() == 0){
            return;
        }

        // 循环处理
        log.debug("Start While deal updateSendFailInfo sharding:{}, Total:{}",list.size());
        for (SettlementProdDO settlementProdDO : list) {
            settlementProdDO.setSendSts(ClearingSendModeEnum.FINISH.getCode());
            settlementProdDO.setGmtModified(new Date());
            settlementProdMapper.updateSendFailInfo(settlementProdDO);
        }
    }

    /**
     * 消息推送
     * @param settlementProdDO
     */
    public void push(SettlementProdDO settlementProdDO) {
        // 锁定信息
        if (!lock(settlementProdDO)){
            return;
        }
        // 推送信息
        sendQueue(settlementProdDO);
        // 解锁信息
        unLock(settlementProdDO);
    }
    /*
     * 锁定信息
     */
    private boolean lock(SettlementProdDO settlementProdDO) {
        settlementProdDO.setSendSts(ClearingSendModeEnum.PROCESS.getCode());
        settlementProdDO.setGmtModified(new Date());
        if (settlementProdMapper.updateSendStsLockByMsgId(settlementProdDO) == 1) {
            log.debug("kafka lock Succ msgId:{}", settlementProdDO.getMsgId());
            return true;
        }
        return false;
    }
    /*
     * 解锁信息
     */
    private void unLock(SettlementProdDO settlementProdDO) {
        log.debug("kafka Unlock MsgId:{}, sendSts:{}", settlementProdDO.getMsgId(), settlementProdDO.getSendSts());
        settlementProdDO.setGmtModified(new Date());
        settlementProdMapper.updateSendStsUnLockByMsgId(settlementProdDO);
    }
    /*
     * 推送信息到消息队列
     */
    private void sendQueue(SettlementProdDO settlementProdDO) {

        try {
            boolean sendPtyResult = false;
            boolean sendMbridgeResult = false;

            sendPtyResult = sendKafka(buildPtySendInfo(settlementProdDO), Constant.WHOLESALE_KAFKA_TOPIC, settlementProdDO);

            String topic = getMbridgeTopic(settlementProdDO);
            if (null != topic) {
                sendMbridgeResult = sendKafka(buildMbridgeSendInfo(settlementProdDO), topic, settlementProdDO);
            } else {
                sendMbridgeResult = true;
            }

            if (sendPtyResult && sendMbridgeResult) {
                //两笔推送成功更新推送成功
                settlementProdDO.setSendSts(ClearingSendModeEnum.SEND_SUCCESS.getCode());
            } else {
                //推送成功更新交易终态
                settlementProdDO.setSendSts(ClearingSendModeEnum.FINISH.getCode());
            }

        } catch (Exception e) {
            settlementProdDO.setSendSts(ClearingSendModeEnum.FINISH.getCode());
            log.error("kafka Send Error MsgId:{}, ErrorInfo:{}", settlementProdDO.getMsgId(), e);
        }
    }

    /*
     * 推送信息到消息队列
     */
    private boolean sendKafka(String sendInfo, String topic, SettlementProdDO settlementProdDO) {
        log.info("kafka Send Start MsgId:{}, dataInfo:{}", settlementProdDO.getMsgId(), sendInfo);
        // 发送消息到kafka
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, null, sendInfo);
        try {
            // 同步模式
            SendResult<String, String> result = future.get();
            log.info("kafka Send Succ MsgId:{}, result:{}", settlementProdDO.getMsgId(), result.toString());
        } catch (Exception e) {
            log.info("kafka Send Fail MsgId:{}, result:{}", settlementProdDO.getMsgId(), e);
            return false;
        }
        return true;
    }

    private String buildPtySendInfo(SettlementProdDO settlementProdDO) {
        // 消息组装
        StringBuffer sb = new StringBuffer();
        sb.append(settlementProdDO.getSttlmDt()).append(COMMA);//结算日期
        sb.append(settlementProdDO.getBizDt()).append(COMMA);//业务处理时间
        sb.append(settlementProdDO.getMsgTp()).append(COMMA);//报文编号
        sb.append(settlementProdDO.getMsgId()).append(COMMA);//报文标识号
        sb.append(settlementProdDO.getMsgId()).append(COMMA);//明细标识号
        sb.append(settlementProdDO.getSendPtyId()).append(COMMA);//发起机构编码
        sb.append(settlementProdDO.getDbtrPtyId()).append(COMMA);//付款机构编码
        sb.append(settlementProdDO.getDbtrSysId()).append(COMMA);//付款机构系统标识
        sb.append(settlementProdDO.getCbtrPtyId()).append(COMMA);//收款机构编码
        sb.append(settlementProdDO.getCbtrSysId()).append(COMMA);//收款机构系统标识
        sb.append(settlementProdDO.getSttlmAmt()).append(COMMA);//金额
        sb.append(settlementProdDO.getBizSts()).append(COMMA);//结算状态
        return sb.toString();
    }

    private String buildMbridgeSendInfo(SettlementProdDO settlementProdDO) {
        // 货币桥对账数据消息组装
        ChainTransDO chainTransDO = chainTransMapper.selectByMsgId(settlementProdDO.getMsgId());
        StringBuffer sbm = new StringBuffer();
        sbm.append(chainTransDO.getOutMsgId()).append(COMMA); // 货币桥报文流水号
        sbm.append(chainTransDO.getSndDt()).append(COMMA);// 货币桥时间
        sbm.append(settlementProdDO.getSttlmDt()).append(COMMA);// 结算日期
        sbm.append(chainTransDO.getMsgDrn()).append(COMMA);// 收发方向
        sbm.append(chainTransDO.getClrTyp()).append(COMMA);// 交易类型
        sbm.append(chainTransDO.getSenderPtyId()).append(COMMA);// 发起机构编码
        sbm.append(chainTransDO.getSenderLei()).append(COMMA);// 发起机构LEI
        sbm.append(chainTransDO.getReceiverPtyId()).append(COMMA);// 接收机构编码
        sbm.append(chainTransDO.getReceiverLei()).append(COMMA);// 接收机构LEI
        sbm.append(chainTransDO.getDbtrPtyId()).append(COMMA);// 付款机构编码
        sbm.append(chainTransDO.getDbtrPtyLei()).append(COMMA);// 付款机构LEI
        sbm.append(chainTransDO.getCdtrPtyId()).append(COMMA);// 收款机构编码
        sbm.append(chainTransDO.getCdtrPtyLei()).append(COMMA);// 收款机构LEI
        sbm.append(settlementProdDO.getCurrency()).append(COMMA);// 币种
        sbm.append(settlementProdDO.getSttlmAmt()).append(COMMA);// 金额
        sbm.append(chainTransDO.getDbtrWltId()).append(COMMA);// 付款钱包ID
        sbm.append(chainTransDO.getCdtrWltId()).append(COMMA);// 收款钱包ID
        sbm.append(chainTransDO.getPrcSts()).append(COMMA);// 交易状态
        sbm.append(chainTransDO.getParameterId());// 发行注销方式编码
        return sbm.toString();
    }

    private String getMbridgeTopic(SettlementProdDO settlementProdDO) {
        if ((Constant.DCEP.equals(settlementProdDO.getDbtrSysId()) && Constant.MCBS.equals(settlementProdDO.getCbtrSysId()))
            || (Constant.MCBS.equals(settlementProdDO.getDbtrSysId()) && Constant.DCEP.equals(settlementProdDO.getCbtrSysId()))) {
            return Constant.MCBS_KAFKA_TOPIC;
        }

        if ((Constant.DCEP.equals(settlementProdDO.getDbtrSysId()) && Constant.GCSC.equals(settlementProdDO.getCbtrSysId()))
            || (Constant.GCSC.equals(settlementProdDO.getDbtrSysId()) && Constant.DCEP.equals(settlementProdDO.getCbtrSysId()))) {
            return Constant.JISR_KAFKA_TOPIC;
        }
        return null;
    }
}
