package com.dcep.dips.wholesalepayment.manager.impl;

import com.dcep.common.enums.ErrorEnum;
import com.dcep.common.exception.DcepException;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.Response;
import com.dcep.common.model.msg.Dcep90200101DTO;
import com.dcep.common.utils.ThreadPoolUtils;
import com.dcep.dips.wholesalepayment.common.constants.Constant;
import com.dcep.dips.wholesalepayment.common.utils.CommonUtil;
import com.dcep.dips.wholesalepayment.common.utils.DtoUtil;
import com.dcep.dips.wholesalepayment.dal.mapper.StorageForwardMapper;
import com.dcep.dips.wholesalepayment.dal.model.StorageForwardDO;
import com.dcep.dips.wholesalepayment.dto.ClearingDTO;
import com.dcep.dips.wholesalepayment.dto.dc112.Dcep11200101DTO;
import com.dcep.dips.wholesalepayment.dto.dc114.Dcep11400101DTO;
import com.dcep.dips.wholesalepayment.dto.dc185.Dcep18500101DTO;
import com.dcep.dips.wholesalepayment.dto.dc200.Dcep20000101DTO;
import com.dcep.dips.wholesalepayment.dto.dc201.Dcep20101001DTO;
import com.dcep.dips.wholesalepayment.dto.dc202.Dcep20201001DTO;
import com.dcep.dips.wholesalepayment.dto.dc203.Dcep20301001DTO;
import com.dcep.dips.wholesalepayment.dto.dc212.Dcep21201001DTO;
import com.dcep.dips.wholesalepayment.dto.dc213.Dcep21301001DTO;
import com.dcep.dips.wholesalepayment.dto.dc221.Dcep22101001DTO;
import com.dcep.dips.wholesalepayment.dto.dc222.Dcep22201001DTO;
import com.dcep.dips.wholesalepayment.dto.dc226.Dcep22601001DTO;
import com.dcep.dips.wholesalepayment.dto.dc227.Dcep22701001DTO;
import com.dcep.dips.wholesalepayment.dto.dc228.Dcep22801001DTO;
import com.dcep.dips.wholesalepayment.dto.dc263.Dcep26301001DTO;
import com.dcep.dips.wholesalepayment.dto.dc281.Dcep28101001DTO;
import com.dcep.dips.wholesalepayment.dto.dc282.Dcep28201001DTO;
import com.dcep.dips.wholesalepayment.dto.dc801.Dcep80101001DTO;
import com.dcep.dips.wholesalepayment.dto.dc802.Dcep80201001DTO;
import com.dcep.dips.wholesalepayment.enums.ClearingStatusEnum;
import com.dcep.dips.wholesalepayment.enums.LockEnum;
import com.dcep.dips.wholesalepayment.enums.MsgTpEnum;
import com.dcep.dips.wholesalepayment.enums.StorageTypeEnum;
import com.dcep.dips.wholesalepayment.manager.StorageForwardAsyncProcessManager;
import com.dcep.gateway.mcbdc.dto.mcbs200.Mcbs20000101DTO;
import com.dcep.gateway.mcbdc.dto.mcbs201.Mcbs20100101DTO;
import com.dcep.gateway.mcbdc.dto.mcbs203.Mcbs20300101DTO;
import com.dcep.gateway.mcbdc.dto.mcbs900.Mcbs90000101DTO;
import com.dcep.gateway.mcbdc.dto.soap.GenericEnvelopeDTO;
import com.dcep.gateway.mcbdc.dto.soap.GenericGwDTO;
import com.dcep.gateway.mcbdc.dto.soap.McbsEnvelopeDTO;
import com.dcep.gateway.mcbdc.dto.soap.McbsGwDTO;
import com.dcep.supergw.api.GwoutService;
import com.dcepex.trace.support.async.TraceExecutorService;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;

@Slf4j
@Component
public class StorageForwardAsyncProcessManagerImpl implements StorageForwardAsyncProcessManager {

    // 异步处理任务日志信息归类为 “asyncProcess-task” logger
    Logger logger = LoggerFactory.getLogger("asyncProcess-task");

    // 异步处理线程池
    ExecutorService asyncProcessPool = new TraceExecutorService(ThreadPoolUtils.getThreadPoolFixSize(5, "AsyncProcess"));

    @DubboReference(timeout = 7000)
    GwoutService gwoutService;
    @DubboReference
    com.dcep.gateway.mcbdc.api.GwoutService mBridgeGwoutService;
    @Autowired
    StorageForwardMapper storageForwardMapper;
    //发给机构的通知，需要转发的通知报文，需要在这里注册下。
    Map<String, TypeReference> map = new HashMap<>();
    {
        map.put(MsgTpEnum.FUND_ADJUST_NOTICE.getCode(), new TypeReference<EnvelopeDTO<Dcep18500101DTO>>() {});//返回185
        map.put(MsgTpEnum.CDT_REQUEST_ASYN.getCode(), new TypeReference<EnvelopeDTO<Dcep20301001DTO>>() {});//返回203
        map.put(MsgTpEnum.DBT_REQUEST_ASYN.getCode(), new TypeReference<EnvelopeDTO<Dcep21301001DTO>>() {});//返回213
        map.put(MsgTpEnum.CDT_REQUEST_MCBS.getCode(), new TypeReference<McbsEnvelopeDTO<Mcbs20000101DTO>>() {});//返回mcbs200
        map.put(MsgTpEnum.DBT_REQUEST_MCBS.getCode(), new TypeReference<McbsEnvelopeDTO<Mcbs20100101DTO>>() {});//返回mcbs201
        map.put(MsgTpEnum.COMMON_PROCESS_CONFIRM_MCBS.getCode(), new TypeReference<McbsEnvelopeDTO<Mcbs20300101DTO>>() {});//返回mcbs203
        map.put(MsgTpEnum.SETTLE_NOTICE.getCode(), new TypeReference<EnvelopeDTO<Dcep20000101DTO>>() {});//返回200
        map.put(MsgTpEnum.CDT_REQUEST.getCode(),new TypeReference<EnvelopeDTO<Dcep20101001DTO>>() {});//返回201
        map.put(MsgTpEnum.CDT_RESPONSE.getCode(),new TypeReference<EnvelopeDTO<Dcep20201001DTO>>() {});//返回202
        map.put(MsgTpEnum.DBT_RESPONSE.getCode(),new TypeReference<EnvelopeDTO<Dcep21201001DTO>>() {});//返回212
        map.put(MsgTpEnum.RECOV_REQUEST.getCode(),new TypeReference<EnvelopeDTO<Dcep22101001DTO>>() {});//返回221
        map.put(MsgTpEnum.RECOV_RESPONSE.getCode(),new TypeReference<EnvelopeDTO<Dcep22201001DTO>>() {});//返回222
        map.put(MsgTpEnum.COV_RESPONSE.getCode(),new TypeReference<EnvelopeDTO<Dcep22601001DTO>>() {});//返回226
        map.put(MsgTpEnum.CDT_COV_REQUREST.getCode(),new TypeReference<EnvelopeDTO<Dcep22701001DTO>>() {});//返回227
        map.put(MsgTpEnum.CDT_COV_RESPONSE.getCode(),new TypeReference<EnvelopeDTO<Dcep22801001DTO>>() {});//返回228
        map.put(MsgTpEnum.ORDR_CONF_RESULT_NOTICE.getCode(),new TypeReference<EnvelopeDTO<Dcep26301001DTO>>() {});//返回263
        map.put(MsgTpEnum.REFUND_REQUREST.getCode(),new TypeReference<EnvelopeDTO<Dcep28101001DTO>>() {});//返回281
        map.put(MsgTpEnum.REFUND_RESPONSE.getCode(),new TypeReference<EnvelopeDTO<Dcep28201001DTO>>() {});//返回282
        map.put(MsgTpEnum.CRDT_ADJ_REQUREST.getCode(),new TypeReference<EnvelopeDTO<Dcep80101001DTO>>() {});//返回801
        map.put(MsgTpEnum.CRDT_ADJ_RESPONSE.getCode(),new TypeReference<EnvelopeDTO<Dcep80201001DTO>>() {});//返回802
        map.put(MsgTpEnum.FI_CDT.getCode(),new TypeReference<EnvelopeDTO<Dcep11200101DTO>>() {});//返回112
        map.put(MsgTpEnum.FI_RETUNE.getCode(),new TypeReference<EnvelopeDTO<Dcep11400101DTO>>() {});//返回112
    }

    @Override
    public int operationCtr() {
        // 查询待处理数据
        List<StorageForwardDO> list = storageForwardMapper.selectForProcess(new Date(),
                CommonUtil.getEnvInfo());
        if (null == list || list.size() == 0) {
            return 0;
        }

        logger.info("Start While AsyncProcess  Total:{}", list.size());
        for (StorageForwardDO storageForwardDO : list) {
            try {
                asyncProcessPool.execute(() -> {
                    process(storageForwardDO);
                });
            } catch (RejectedExecutionException re) {// 线程池任务已满,拒绝获取线程
                // 线程池任务已满,主流程执行
               process(storageForwardDO);
            } catch (Exception e) {// 其他异常
                logger.error("StorageForwardAsyncProcess Error MsgId:{}, Exception:{}", storageForwardDO.getMsgId(), e);
            }
        }
        return list.size();
    }

    @Override
    public void compensation() {
        // 查询未正常处理数据 -> 更新异步处理表处理状态 -> 删除异步处理表数据
        List<StorageForwardDO> list = storageForwardMapper.selectUnFinishedForFinal( new Date(),
                CommonUtil.getEnvInfo());
        if (list == null || list.isEmpty()){
            return;
        }

        for (StorageForwardDO asyncProcessDO : list) {
            // 原交易msgSn打印
            CommonUtil.LogMDC(asyncProcessDO.getMsgId());
            logger.info("Compensation MsgId:{}", asyncProcessDO.getMsgId());

            boolean result = false;

            // 超过最大通知时间,不再通知
            if (new Date().compareTo(asyncProcessDO.getProcessTimeout()) > 0) {
                result = true;
            }

            this.unLock(result, asyncProcessDO);
        }
    }

    @Override
    public void process(StorageForwardDO storageForwardDO) {
        // 原交易msgSn打印
        CommonUtil.LogMDC(storageForwardDO.getMsgId());
        // 环境信息筛选 todo 环境检测函数有问题，这里先不检查环境
//        if (!CommonUtil.checkEnvInfo(storageForwardDO.getEnvInfo())) {
//            logger.debug("EnvInfo Filter MsgId={}, EnvInfo={}", storageForwardDO.getMsgId(), storageForwardDO.getEnvInfo());
//            return;
//        }
        // 赋值全链路环境信息
        CommonUtil.setEnvInfo(storageForwardDO.getEnvInfo());

        // 通知或推送状态
        boolean status = false;
        // 锁定信息
        if (!lock(storageForwardDO)) {
            return;
        }

        try {
            // 通知或推送
            status = this.noticeOrPush(storageForwardDO);
        } catch (Exception e) {
            logger.error("ClearingAsyncProcess Error MsgId:{}, Exception:{}", storageForwardDO.getMsgId(), e);
        } finally {
            // 解锁处理
            unLock(status, storageForwardDO);
        }

    }

    // 锁定信息
    private boolean lock(StorageForwardDO storageForwardDO) {
        // 多节点服务器竞争获得该数据操作权,锁定失败 RETURN

        //更新发送时间
        storageForwardDO.setSendTime(new Date());
        //更新修改时间
        storageForwardDO.setGmtModified(new Date());
        //设置锁定超时时间，防止这条记录一直处于锁定状态。后续会有任务来清除超时的锁定记录。
        storageForwardDO.setProcessTimeout(Date.from(Instant.now().plusSeconds(5)));

        if (storageForwardMapper.updateUuid(storageForwardDO) != 1) {
            return false;
        }
        logger.info("Lock Succ MsgId:{}", storageForwardDO.getMsgId());
        return true;
    }

    // 解锁
    private void unLock(boolean status, StorageForwardDO storageForwardDO) {
        logger.info("UnLock Start msgId:{}, Status:{}", storageForwardDO.getMsgId(), status);

        //更新发送时间
        storageForwardDO.setSendTime(new Date());
        //更新修改时间
        storageForwardDO.setGmtModified(new Date());

        //通知成功删除 否则 更新状态 todo 测试阶段，只修改状态为2，不做删除
        boolean result = status ? storageForwardMapper.mockDelete(storageForwardDO) == 1
                : storageForwardMapper.updateUuidAndTime(storageForwardDO) == 1;
//        boolean result = status ? storageForwardMapper.deleteByPrimaryKey(storageForwardDO) == 1
//                : storageForwardMapper.updateUuidAndTime(storageForwardDO) == 1;
        logger.info("UnLock End  msgId:{}, result:{}", storageForwardDO.getMsgId(), result);
    }

    /**
     * 调用网关向机构发报
     *
     * @param req
     * @return
     */
    private boolean execute(EnvelopeDTO<GwDTO> req, StorageForwardDO storageForwardDO) {
        try {
            logger.info("call gw Start req:{}", req.toString());

            // 调用网关
            Response<EnvelopeDTO<GwDTO>> resp = gwoutService.execute(req);
            GwDTO outDTO = resp.getResult().body();
            String msgId = req.getSoapBody().getT().fetchMsgId();
            // 应答结果校验
            if (outDTO instanceof Dcep90200101DTO
                    && msgId.equals(((Dcep90200101DTO) outDTO).getConfInf().getOrgnlMsgId())) {
                logger.info("call gw End 902, msgId:{}, OrgnlMsgId:{}, outDTO:{}", msgId, storageForwardDO.getMsgId(),
                        outDTO);
                return true;
            } else if (outDTO instanceof ClearingDTO) {
                if(!ClearingStatusEnum.SUCCESS.getCode().equals(((ClearingDTO) outDTO).clrBizRspSts())){
                    logger.info("call gw End, response is not success, msgId:{}, OrgnlMsgId:{}, outDTO:{}", msgId, storageForwardDO.getMsgId(),
                            outDTO);
                    return true;
                }
                //如果不是902，需要再存入转发表里,等后续转发。
                Date sendTime = new Date();
                //设置锁定超时时间，防止这条记录一直处于锁定状态。后续会有任务来清除超时的锁定记录。
                Date processTimeout = Date.from(Instant.now().plusSeconds(5));
                //构造报文并插入存储转发表
                String clrMsgTp = ((ClearingDTO) outDTO).clrMsgTp();
                StorageForwardDO nextStorageForwardDO = new StorageForwardDO(
                        outDTO.fetchMsgId(),
                        clrMsgTp,
                        StorageTypeEnum.INST_ASYNC_NOTICE.getCode(),
                        DtoUtil.obj2JsonStr(resp.getResult()),
                        LockEnum.UNLOCK.getCode(),
                        null,//todo 接收inst为什么是空，那转发时是否需要
                        sendTime,
                        Constant.ASYNC_PUSH_INTERVAL,
                        processTimeout);
                storageForwardMapper.insert(nextStorageForwardDO);
                logger.info("call gw End msgtp:{}, msgId:{}, OrgnlMsgId:{}, outDTO:{}",clrMsgTp, msgId, storageForwardDO.getMsgId(),
                        outDTO);
                return true;
            } else {
                logger.info("call gw unregister forward clrMsgTp, msgId:{}, orgnlMsgId:{}, outDTO:{}", msgId, storageForwardDO.getMsgId(),
                        outDTO != null ? outDTO.toString() : "return DTO is null");
                return false;
            }

        } catch (Exception e) {
            logger.error("call gw Exception:", e);
            return false;
        }
    }

    /**
     * 通知和推送
     *
     * @param storageForwardDO
     * @return
     * @see #noticeOrPushByMsgTp(StorageForwardDO) 发送多个不同报文到机构
     */
    private boolean noticeOrPush(StorageForwardDO storageForwardDO) {

        // 超过通知时间,不予通知,直接返回成功
        if (new Date().compareTo(storageForwardDO.getProcessTimeout()) > 0) {
            return true;
        }

        boolean result = true;
        //当前8种类型
        String storgTp = storageForwardDO.getStorgTp();
        // 机构异步通知/动账消息推送
        if (StorageTypeEnum.INST_ASYNC_NOTICE.getCode().equals(storgTp)) {
            result = noticeOrPushByMsgTp(storageForwardDO);
        }
        // 大额资金调减重发
        else if (storgTp.equals(StorageTypeEnum.HVPS_ADJUST_DECREASE.getCode())) {
            //todo 对应业务老师提供通知方法
        }
        // 大额资金清零重发
        else if (storgTp.equals(StorageTypeEnum.HVPS_ZERO_OUT.getCode())) {
            //todo 对应业务老师提供通知方法

        }
        // 货币桥异步通知
        else if (storgTp.equals(StorageTypeEnum.MCBS_ASYNC_NOTICE.getCode())) {
            result = noticeOrPushByMsgTp(storageForwardDO);
        } else {
            result = false;
            logger.info("noticeOrPush End  msgId:{}, result:{}", storageForwardDO.getMsgId(), result);
            throw new DcepException(com.dcep.common.enums.ErrorEnum.UNKNOWN_EXCEPTION.getCode(), "未知的处理类型");
        }
        return result;
    }

    /**
     * 发送报文，根据报文类型， 实时全额相关通知
     *
     * @param storageForwardDO
     * @return
     */
    private boolean noticeOrPushByMsgTp(StorageForwardDO storageForwardDO) {
        String msgTp = storageForwardDO.getMsgTp();
        TypeReference typeReference = map.get(msgTp);
        if (typeReference==null) {
            throw new DcepException(ErrorEnum.UNKNOWN_EXCEPTION.getCode(), "未知的报文类型");
        }
        Object object = DtoUtil.jsonStr2Obj(storageForwardDO.getStorgInf(), typeReference);

        if (object instanceof McbsEnvelopeDTO) {
            return execute((McbsEnvelopeDTO) object, storageForwardDO);
        } else if (object instanceof EnvelopeDTO) {
            ((EnvelopeDTO<?>) object).body().init();
        }

        return execute((EnvelopeDTO) object, storageForwardDO);
    }

    private boolean execute(McbsEnvelopeDTO<McbsGwDTO> req, StorageForwardDO storageForwardDO) {
        try {
            logger.info("call mbirdge gw Start req:{}", req.toString());

            // 调用网关
            Response<GenericEnvelopeDTO<GenericGwDTO>> resp = mBridgeGwoutService.execute(req);
            if (resp == null){
                logger.error("call mbirdge gw end, return null, OrgnlMsgId:{}", storageForwardDO.getMsgId());
                return false;
            }
            GenericGwDTO outDTO = resp.getResult().body();
            String msgId = req.getSoapBody().getT().fetchMsgId();
            // 应答结果校验
            if (outDTO instanceof Mcbs90000101DTO
                    && msgId.equals(((Mcbs90000101DTO) outDTO).getReceiptDetails().getOriginMsgId().getMsgId())) {
                logger.info("call mbirdge gw End mcbs900, msgId:{}, OrgnlMsgId:{}, outDTO:{}", msgId, storageForwardDO.getMsgId(),
                        outDTO);
                return true;
            } else {
                logger.info("call mbirdge gw unregister forward clrMsgTp, msgId:{}, orgnlMsgId:{}, outDTO:{}", msgId, storageForwardDO.getMsgId(),
                        outDTO != null ? outDTO.toString() : "return DTO is null");
                return false;
            }
        } catch (Exception e) {
            logger.error("call mbirdge gw Exception:", e);
            return false;
        }
    }
}
