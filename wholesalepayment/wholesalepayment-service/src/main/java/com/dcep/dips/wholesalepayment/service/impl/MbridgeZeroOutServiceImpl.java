package com.dcep.dips.wholesalepayment.service.impl;

import com.dcep.common.exception.DcepException;
import com.dcep.common.model.Response;
import com.dcep.dips.operatingcontrol.spi.BatchTaskCallbackService;
import com.dcep.dips.wholesalepayment.api.MbridgeZeroOutService;
import com.dcep.dips.wholesalepayment.common.constants.Constant;
import com.dcep.dips.wholesalepayment.common.utils.DtoUtil;
import com.dcep.dips.wholesalepayment.dal.model.TaskExecutionControlDO;
import com.dcep.dips.wholesalepayment.dal.model.ZerooutCtrlDO;
import com.dcep.dips.wholesalepayment.dto.mcbs101.Mcbs10100101DTO;
import com.dcep.dips.wholesalepayment.enums.ErrorEnum;
import com.dcep.dips.wholesalepayment.enums.McbsStatusEnum;
import com.dcep.dips.wholesalepayment.manager.ChainZeroOutManager;
import com.dcep.dips.wholesalepayment.manager.MbridgeZeroOutManager;
import com.dcep.dips.wholesalepayment.manager.TaskExecutionManager;
import com.dcep.gateway.mcbdc.dto.soap.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.dao.DuplicateKeyException;

import javax.annotation.Resource;

@Slf4j
@DubboService
public class MbridgeZeroOutServiceImpl implements MbridgeZeroOutService {

    @Resource
    private ChainZeroOutManager chainZeroOutManager;

    @Resource
    private TaskExecutionManager taskExecutionManager;

    @Resource
    private MbridgeZeroOutManager mbridgeZeroOutManager;

    @DubboReference(timeout = 7000)
    BatchTaskCallbackService batchTaskCallbackService;

    /**
     * 接收货币桥桥上清零结果通知，并同步调用结算钱包服务进行结算记账
     *
     * @param genericGwDTO
     * @return
     * @throws DcepException
     */
    @Override
    public Response<GenericEnvelopeDTO<GenericGwDTO>> report(GenericEnvelopeDTO<GenericGwDTO> genericGwDTO) throws DcepException {
        //是否为mcbs101
        if (!(genericGwDTO.body() instanceof Mcbs10100101DTO)) {
            log.error("桥上清零结果通知 报文类型非mcbs101");
            return new Response<>(false,assemblyMcbs900(genericGwDTO, McbsStatusEnum.FAIL.getCode(), Constant.MBRIDGE_REQHDLG_DESC_TPERR),McbsStatusEnum.FAIL.getCode(), Constant.MBRIDGE_REQHDLG_DESC_TPERR);
        }
        Mcbs10100101DTO mcbs101DTO = (Mcbs10100101DTO) genericGwDTO.body();
        String orgnlMsgId = mcbs101DTO.getOrgnlGrpInf().getOrgnlMsgId();//原清零通知交易标识号
        String msgId = mcbs101DTO.fetchMsgId();//清零结果通知交易标识号
        log.info("MbridgeZeroOutService.report start: msgId={},orgnlMsgId={}", msgId,orgnlMsgId);
        //1.业务检查:查询原清零结果通知交易是否存在
        ZerooutCtrlDO orgnlZeroOutCtrlDO = chainZeroOutManager.selectByMsgId(orgnlMsgId);
        if (null == orgnlZeroOutCtrlDO) {
            return new Response<>(false,assemblyMcbs900(genericGwDTO, McbsStatusEnum.FAIL.getCode(), ErrorEnum.NO_MATCH_ORIGNAL.getDescription()),McbsStatusEnum.FAIL.getCode(), ErrorEnum.NO_MATCH_ORIGNAL.getDescription());
        }
        //清零状态判断
        if (Constant.ZERO_OUT_CTRL_STATUS_00.equals(orgnlZeroOutCtrlDO.getPrcSts())) {
            return new Response<>(true,assemblyMcbs900(genericGwDTO,McbsStatusEnum.SUCD.getCode(),Constant.ZERO_OUT_CTRL_STATUS_00));
        }
        //2、3遍历生成流水号、交易登记 4.异步线程池调用结算钱包服务-记账业务请求
        try {
            mbridgeZeroOutManager.recordZOCtrlAndAcctInstr(mcbs101DTO,orgnlZeroOutCtrlDO);
        }catch (DuplicateKeyException e){
            log.error("桥上清零结果通知 MbridgeZeroOutService.report error: MsgId={},OrgnlMsgId={}",msgId,orgnlMsgId,e);
            return new Response<>(false,assemblyMcbs900(genericGwDTO, McbsStatusEnum.FAIL.getCode(),McbsStatusEnum.FAIL.getDescription()),McbsStatusEnum.FAIL.getCode(), McbsStatusEnum.FAIL.getDescription());
        }

        //根据清零控制表taskid查询任务执行表,获取原任务信息
        TaskExecutionControlDO taskExecutionControlDO = taskExecutionManager.selectByTaskId(orgnlZeroOutCtrlDO.getTaskId());
        taskExecutionManager.asyncnotify(taskExecutionControlDO,orgnlZeroOutCtrlDO);

        log.info("桥上清零结果通知 MbridgeZeroOutService.report end: MsgId={},OrgnlMsgId={}",msgId,orgnlMsgId);
        return new Response<>(true,assemblyMcbs900(genericGwDTO,McbsStatusEnum.SUCD.getCode(),McbsStatusEnum.SUCD.getDescription()),McbsStatusEnum.SUCD.getCode(),McbsStatusEnum.SUCD.getDescription());
    }


    /**
     * 组装Mcbs900报文
     * @return
     */
    private McbsEnvelopeDTO<McbsGwDTO> assemblyMcbs900(GenericEnvelopeDTO<GenericGwDTO> mBridgeReqEnvelopeDTO,String code,String desc) {
        McbsEnvelopeDTO<McbsGwDTO> mcbsEnvelopeDTOReq = (McbsEnvelopeDTO<McbsGwDTO>) mBridgeReqEnvelopeDTO;
        //收发方标识
        String receiverLEI = mcbsEnvelopeDTOReq.getSoapHeader().getReceiverLEI();
        String receiverCBMALEI = mcbsEnvelopeDTOReq.getSoapHeader().getReceiverCBMALEI();
        String senderLEI = mcbsEnvelopeDTOReq.getSoapHeader().getSenderLEI();
        String senderCBMALEI = mcbsEnvelopeDTOReq.getSoapHeader().getSenderCBMALEI();
        //收发互换
        mcbsEnvelopeDTOReq.getSoapHeader().setReceiverLEI(senderLEI);
        mcbsEnvelopeDTOReq.getSoapHeader().setReceiverCBMALEI(senderCBMALEI);
        mcbsEnvelopeDTOReq.getSoapHeader().setSenderLEI(receiverLEI);
        mcbsEnvelopeDTOReq.getSoapHeader().setSenderCBMALEI(receiverCBMALEI);

        GenericEnvelopeDTO<GenericGwDTO> envelopeDTO = DtoUtil.assemblyMcbs900(mcbsEnvelopeDTOReq, code, desc);

        return (McbsEnvelopeDTO<McbsGwDTO>)envelopeDTO;
    }

}
