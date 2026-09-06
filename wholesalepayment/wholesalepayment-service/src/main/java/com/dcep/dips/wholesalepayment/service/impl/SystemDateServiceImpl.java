package com.dcep.dips.wholesalepayment.service.impl;

import com.dcep.bcsp.core.api.DcepOnChainService;
import com.dcep.bcsp.core.dto.ClearNotifyDTO;
import com.dcep.common.exception.DcepException;
import com.dcep.common.model.Response;
import com.dcep.common.utils.DcepDateUtils;
import com.dcep.common.utils.MsgIdUtil;
import com.dcep.common.utils.ValidateUtils;
import com.dcep.dips.common.constant.CommonConstant;
import com.dcep.dips.operatingcontrol.dto.TaskRequestDTO;
import com.dcep.dips.operatingcontrol.dto.TaskResultDTO;
import com.dcep.dips.operatingcontrol.spi.BatchTaskCallbackService;
import com.dcep.dips.operatingcontrol.spi.BatchTaskService;
import com.dcep.dips.wholesalepayment.common.constants.Constant;
import com.dcep.dips.wholesalepayment.common.utils.CommonUtil;
import com.dcep.dips.wholesalepayment.common.utils.DtoUtil;
import com.dcep.dips.wholesalepayment.dal.model.SystemStatusDO;
import com.dcep.dips.wholesalepayment.dal.model.TaskExecutionControlDO;
import com.dcep.dips.wholesalepayment.dal.model.ZerooutCtrlDO;
import com.dcep.dips.wholesalepayment.dto.mcbs102.OrgnlGrpInf;
import com.dcep.dips.wholesalepayment.enums.ErrorEnum;
import com.dcep.dips.wholesalepayment.manager.*;
import com.dcep.gateway.mcbdc.dto.soap.GenericEnvelopeDTO;
import com.dcep.gateway.mcbdc.dto.soap.GenericGwDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;
import java.util.Set;

@Slf4j
@DubboService(group = "${spring.application.name}", version = "1.0.0")
public class SystemDateServiceImpl implements BatchTaskService {

    @Resource
    SystemStatusManager systemStatusManager;

    @Resource
    TaskExecutionManager taskExecutionManager;

    @Resource
    ChainZeroOutManager chainZeroOutManager;

    @Resource
    MbridgeZeroOutManager mbridgeZeroOutManager;

    @Resource
    CommonStsctrlManager commonStsctrlManager;

    @DubboReference(timeout = 7000)
    DcepOnChainService dcepOnChainService;

    @Override
    public Response<TaskResultDTO> execute(TaskRequestDTO taskRequestDTO) {
        log.info("系统日切任务执行服务 SystemDateService.taskExecute start,taskID:{}",taskRequestDTO.getTaskId());
        //请求参数校验
        ValidateUtils.validate(taskRequestDTO);
        TaskExecutionControlDO taskExecutionControlDO = new TaskExecutionControlDO();
        //登记任务执行控制表
        try {
            taskExecutionManager.recordTaskExecute(taskExecutionControlDO);
        }catch (DuplicateKeyException e){//幂等判断
            log.error("SystemDateService.taskExecute fail,taskID:{}",taskRequestDTO.getTaskId());
            return new Response<>(false,null,ErrorEnum.BUSI_DUPLICATION.getCode(), ErrorEnum.BUSI_DUPLICATION.getDescription());
        }
        //业务参数校验
        if(!checkMapParam(taskRequestDTO.getParams())){
            return new Response<>(false,null,com.dcep.common.enums.ErrorEnum.REQUEST_PARAM_ILLEGAL.getCode(), com.dcep.common.enums.ErrorEnum.REQUEST_PARAM_ILLEGAL.getDescription());
        }
        //判断任务编码
        String taskCode = taskRequestDTO.getTaskCode();
        //货币桥清零通知||JISR清零通知
        if(taskCode.equals(Constant.TASK_CODE_B0101)||taskCode.equals(Constant.TASK_CODE_B0102)){
            log.info("SystemDateService.taskExecute B0101 货币桥清零");
            String msgIdB0101 = getMsgID();
            recordZeroOutCtrl(taskRequestDTO, msgIdB0101,taskCode.equals(Constant.TASK_CODE_B0101)?Constant.MCBS:Constant.GCSC);
            //调用货币桥网关发送mcbs.100.001.01报文
            GenericEnvelopeDTO<GenericGwDTO> envelopeDTO100B0101 = DtoUtil.assembly100Msg(msgIdB0101);
            Response<GenericEnvelopeDTO<GenericGwDTO>> responseB0101;
            try {
                responseB0101 = mbridgeZeroOutManager.mbridgeGateway(envelopeDTO100B0101);
            } catch (Exception e) {
                log.error("SystemDateService.taskExecute B0101 货币桥清零调用失败");
                return new Response<>(false, null, ErrorEnum.BUSI_COMP_ERROR.getCode(), ErrorEnum.BUSI_COMP_ERROR.getDescription());
            }
            TaskResultDTO taskResultDTOB0101 = assembleTaskReturn(taskRequestDTO, responseB0101);
            return new Response<>(responseB0101.isSuccess() ? true : false, taskResultDTOB0101, responseB0101.isSuccess() ? null : ErrorEnum.BUSI_COMP_ERROR.getCode(), responseB0101.isSuccess() ? null : ErrorEnum.BUSI_COMP_ERROR.getDescription());
        //货币桥清零完成通知||JISR清零完成通知
        }else if(taskCode.equals(Constant.TASK_CODE_C0201)||taskCode.equals(Constant.TASK_CODE_B0202)) {
            log.info("SystemDateService.taskExecute C0201/B0202 货币桥/JISR清零完成通知");
            //检查,更新数据
            ZerooutCtrlDO zerooutCtrlDO;
            try {
                zerooutCtrlDO = zeroOutFinishNotifyRecord(taskCode.equals(Constant.TASK_CODE_C0201) ? Constant.GCSC : Constant.MCBS);
            } catch (DcepException e) {
                log.error("SystemDateService.taskExecute C0201/B0202 货币桥/JISR清零完成通知失败");
                return new Response<>(false, null, e.getCode(), e.getMessage());
            }
            String msgIdB0202 = getMsgID();
            //原清零交易信息
            OrgnlGrpInf orgnlGrpInf = new OrgnlGrpInf();
            orgnlGrpInf.setOrgnlMsgId(zerooutCtrlDO.getMsgId());
            orgnlGrpInf.setOrgnlMsgNmId(Constant.MCBS_MSGTYPE_101);//todo 原交易类型
            orgnlGrpInf.setClrZeDt(zerooutCtrlDO.getSysDt());

            GenericEnvelopeDTO<GenericGwDTO> envelopeDTO = DtoUtil.assembly102Msg(orgnlGrpInf, msgIdB0202);
            //通知货币桥
            Response<GenericEnvelopeDTO<GenericGwDTO>> responseB0202;
            try {
                responseB0202 = mbridgeZeroOutManager.mbridgeGateway(envelopeDTO);
            } catch (Exception e) {
                log.error("SystemDateService.taskExecute B0202 货币桥清零调用失败");
                return new Response<>(false, null, ErrorEnum.BUSI_COMP_ERROR.getCode(), ErrorEnum.BUSI_COMP_ERROR.getDescription());
            }
            TaskResultDTO taskResultDTOB0202 = assembleTaskReturn(taskRequestDTO, responseB0202);
            return new Response<>(responseB0202.isSuccess() ? true : false, taskResultDTOB0202,responseB0202.getErrorCode(),responseB0202.getErrorMsg());
        }else{
            switch (taskCode) {
                //区块链服务平台清零
                case Constant.TASK_CODE_B0103:
                    log.info("SystemDateService.taskExecute B0103 区块链服务平台清零");
                    String msgIdB0103 = getMsgID();
                    recordZeroOutCtrl(taskRequestDTO, msgIdB0103,Constant.BCSP);
                    ClearNotifyDTO clearNotifyDTO = new ClearNotifyDTO();
                    clearNotifyDTO.setId(msgIdB0103);
                    String sysId = (String) taskRequestDTO.getParams().get("previousSystemFlag");
                    clearNotifyDTO.setClearZone(sysId.equals(Constant.SYSFLAG_A) ? 0 : 1);
                    //4.调用区块链服务平台
                    Response<Void> voidResponse;
                    try {
                        voidResponse = dcepOnChainService.clearNotify(clearNotifyDTO);
                    } catch (Exception e) {
                        log.error("SystemDateService.taskExecute B0103 区块链服务平台清零调用失败");
                        return new Response<>(false, null, ErrorEnum.BUSI_COMP_ERROR.getCode(), ErrorEnum.BUSI_COMP_ERROR.getDescription());
                    }
                    TaskResultDTO taskResultDTOB0103 = assembleTaskReturn(taskRequestDTO, voidResponse);
                    return new Response<>(voidResponse.isSuccess() ? true : false, taskResultDTOB0103, voidResponse.getErrorCode(),voidResponse.getErrorMsg());
                //日切状态通知
                case Constant.TASK_CODE_D0401:
                    log.info("SystemDateService.taskExecute D0401 日切状态通知");
                    Response<TaskResultDTO> responseD0401 = changeSystemStatus(taskRequestDTO);
                    if(!responseD0401.isSuccess()){
                        return responseD0401;
                    }
                    TaskResultDTO taskResultDTOD0401 = assembleTaskReturn(taskRequestDTO, new Response(true,null,null,null));
                    responseD0401.setResult(taskResultDTOD0401);
                    return responseD0401;
                //终止T日业务受理通知
                case Constant.TASK_CODE_D0402:
                    log.info("SystemDateService.taskExecute D0402 终止T日业务受理通知");
                    Response<TaskResultDTO> responseD0402 = changeSystemStatus(taskRequestDTO);
                    if(!responseD0402.isSuccess()){
                        return responseD0402;
                    }
                    Response<String> stringResponse;
                    //日终退回
                    try {
                        stringResponse = commonStsctrlManager.endReturn(taskRequestDTO.getSystemDate());
                    }catch (DcepException e) {
                        log.error("SystemDateService.taskExecute D0402 终止T日业务受理通知日终退回异常",e);
                        return new Response<>(false,null,e.getCode(),e.getMessage());
                    }
                    TaskResultDTO taskResultDTOD0402 = assembleTaskReturn(taskRequestDTO, new Response(true,null,null,null));
                    return new Response<>(stringResponse.isSuccess()? true : false,taskResultDTOD0402,stringResponse.getErrorCode(),stringResponse.getErrorMsg());
                //todo FMI对账通知
                case Constant.TASK_CODE_E0203:
                    log.info("SystemDateService.taskExecute E0203 FMI对账通知");
                    break;
                default:
                    log.error("SystemDateService.taskExecute fail,taskID:{}", taskRequestDTO.getTaskId());
                    return new Response<>(false,null,com.dcep.common.enums.ErrorEnum.REQUEST_PARAM_ILLEGAL.getCode(), com.dcep.common.enums.ErrorEnum.REQUEST_PARAM_ILLEGAL.getDescription());
            }
        }
        return new Response<>(false,assembleTaskReturn(taskRequestDTO, new Response(false,null,null,null)),com.dcep.common.enums.ErrorEnum.REQUEST_PARAM_ILLEGAL.getCode(), com.dcep.common.enums.ErrorEnum.REQUEST_PARAM_ILLEGAL.getDescription());
    }

    /**
     * 货币桥/jisr清零完成通知数据查询
     * @return
     */
    private ZerooutCtrlDO zeroOutFinishNotifyRecord(String sysCode){
        //查询系统状态表
        SystemStatusDO systemStatusDO = systemStatusManager.selectByPrimaryKey(CommonConstant.SysCode.WHOLESALE);
        if (null == systemStatusDO) {
            log.error("SystemStatus table not exist");
            throw new DcepException(ErrorEnum.NO_MATCH_ORIGNAL.getCode(),ErrorEnum.NO_MATCH_ORIGNAL.getDescription());
        }
        //根据系统时间清零系统标识查询清零记录
        ZerooutCtrlDO zerooutCtrlDO = chainZeroOutManager.selectBySysDtId(systemStatusDO.getCurSysDt(), sysCode);
        if (null == zerooutCtrlDO) {
            log.error("ZerooutCtrl table not exist");
            throw new DcepException(ErrorEnum.NO_MATCH_ORIGNAL.getCode(), ErrorEnum.NO_MATCH_ORIGNAL.getDescription());
        }
        try {
            chainZeroOutManager.updateByPrimaryKey(zerooutCtrlDO);
        }catch (Exception e) {
            log.error("SystemStatus taskExecute ZerooutCtrl update fail",e);
            throw new DcepException(ErrorEnum.BUSI_UPDATE_DB_EXCEPTION.getCode(), ErrorEnum.BUSI_UPDATE_DB_EXCEPTION.getDescription());
        }
        return zerooutCtrlDO;
    }


    /**
     * 组装任务返回结果
     * @param taskRequestDTO
     * @return
     */
    private TaskResultDTO assembleTaskReturn(TaskRequestDTO taskRequestDTO,Response response){
        TaskResultDTO taskResultDTO = new TaskResultDTO();
        taskResultDTO.setTaskId(taskRequestDTO.getTaskId());
        taskResultDTO.setTaskCode(taskRequestDTO.getTaskCode());
        taskResultDTO.setTaskName(taskRequestDTO.getTaskName());
        taskResultDTO.setSystemDate(taskRequestDTO.getSystemDate());
        taskResultDTO.setTaskStatus(response.isSuccess()?Constant.TASK_STATUS_1:Constant.TASK_STATUS_2);
        return taskResultDTO;
    }

    /**
     * 清零通知服务(清零控制表用)生成流水号
     * @return
     */
    private String getMsgID() {
        return DcepDateUtils.formateDate(new Date(), DcepDateUtils.DATE_PATTERN) + CommonConstant.PBOC_SHORT_PTY_ID + CommonConstant.SysCode.WHOLESALE
                + MsgIdUtil.getRandomNum(14) + "00" + CommonUtil.getEnvVal();
    }

    /**
     * 组装数据,登记清零控制表
     * @param taskRequestDTO
     */
    private void recordZeroOutCtrl(TaskRequestDTO taskRequestDTO, String msgId,String zeroOutSystemFlag){
        String sysDt = (String)taskRequestDTO.getParams().get("currentSystemDate");
        String curSysFlg = (String)taskRequestDTO.getParams().get("currentSystemFlag");
        String taskId = taskRequestDTO.getTaskId();
        ZerooutCtrlDO zerooutCtrlDO = new ZerooutCtrlDO(msgId,zeroOutSystemFlag,sysDt,curSysFlg,taskId);
        //登记清零控制表
        chainZeroOutManager.recordZeroOutCtrl(zerooutCtrlDO);
    }

    /**
     * 校验Map参数
     * @param params
     */
    private boolean checkMapParam(Map<String, Object> params) {
        if(null==params||params.isEmpty()){
            log.error("参数校验失败,Param为空");
            return false;
        }
        Set<Map.Entry<String, Object>> entries = params.entrySet();
        for (Map.Entry<String, Object> entry : entries){
            Object value = entry.getValue();
            if(null==value){
                log.error("参数校验失败,Param含空值参数,{}",entry.getKey());
                return false;
            }
        }
        return true;
    }


    /**修改当前账务日期，当前系统状态
     *
     * @param taskRequestDTO
     * @return
     */
    public Response<TaskResultDTO> changeSystemStatus(TaskRequestDTO taskRequestDTO) {
        //查询系统状态表
        SystemStatusDO systemStatusDO = systemStatusManager.selectByPrimaryKey(CommonConstant.SysCode.WHOLESALE);
        if (null == systemStatusDO) {
            log.error("SystemStatus table not init");
            return new Response<>(false,null,ErrorEnum.NO_MATCH_ORIGNAL.getCode(),ErrorEnum.NO_MATCH_ORIGNAL.getDescription());
        }
        Map<String, Object> params = taskRequestDTO.getParams();

        systemStatusDO.setPreSysSts((String)params.get("originalSystemStatus")); //原系统状态>上一系统状态
        systemStatusDO.setPreSysDt((String)params.get("originalSystemDate"));//原系统日期>上一系统日期
        systemStatusDO.setNextSysDt((String)params.get("nextSystemDate"));//下一系统日期

        //更新当前账务日期、当前系统状态、日终进度状态
        systemStatusDO.setCurSysDt((String)params.get("currentSystemDate"));
        systemStatusDO.setCurSysSts((String)params.get("currentSystemStatus"));
        //todo 日终进度状态
        systemStatusDO.setEodSts((String)params.get("EodStatus"));
        try {
            systemStatusManager.updateByPrimaryKey(systemStatusDO);
        }catch (Exception e) {
            log.error("SystemStatus change fail",e);
            return new Response<>(false,null,ErrorEnum.UNKNOWN_EXCEPTION.getCode(),ErrorEnum.UNKNOWN_EXCEPTION.getDescription());
        }
        return new Response<>(true,null,null,null);
    }

}
