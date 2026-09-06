package com.dcep.dips.wholesalepayment.service.impl;

import com.dcep.common.exception.DcepException;
import com.dcep.common.model.Response;
import com.dcep.common.utils.ValidateUtils;
import com.dcep.dips.wholesalepayment.api.ChainZeroOutService;
import com.dcep.dips.wholesalepayment.common.constants.Constant;
import com.dcep.dips.wholesalepayment.dal.model.TaskExecutionControlDO;
import com.dcep.dips.wholesalepayment.dal.model.ZerooutCtrlDO;
import com.dcep.dips.wholesalepayment.dto.chain.ZeroOutReportDTO;
import com.dcep.dips.wholesalepayment.enums.ErrorEnum;
import com.dcep.dips.wholesalepayment.manager.ChainZeroOutManager;
import com.dcep.dips.wholesalepayment.manager.TaskExecutionManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.dao.DuplicateKeyException;
import javax.annotation.Resource;

@Slf4j
@DubboService
public class ChainZeroOutServiceImpl implements ChainZeroOutService {

    @Resource
    private ChainZeroOutManager chainZeroOutManager;

    @Resource
    private TaskExecutionManager taskExecutionManager;

    /**
     * 接受区块链链上清零结果通知，并调用结算钱包服务结算记账
     *
     * @param zeroOutReportDto
     * @return
     * @throws DcepException
     */
    @Override
    public Response<String> report(ZeroOutReportDTO zeroOutReportDto) throws DcepException {
        //参数校验
        ValidateUtils.validate(zeroOutReportDto);
        String orgnlMsgId = zeroOutReportDto.getOrgnlMsgId();//原清零通知交易标识号
        String msgId = zeroOutReportDto.getMsgId();//清零结果通知交易标识号
        log.info("链上清零结果通知 ChainZeroOutService.report start: MsgId={},OrgnlMsgId={}",msgId,orgnlMsgId);
        //1.业务检查:查询原清零通知交易是否存在
        ZerooutCtrlDO orgnlZeroOutCtrlDO = chainZeroOutManager.selectByMsgId(orgnlMsgId);
        if (null == orgnlZeroOutCtrlDO) {
            return new Response<>(false, "失败",ErrorEnum.NO_MATCH_ORIGNAL.getCode(),ErrorEnum.NO_MATCH_ORIGNAL.getDescription());
        }
        //清零状态判断
        if (Constant.ZERO_OUT_CTRL_STATUS_00.equals(orgnlZeroOutCtrlDO.getPrcSts())) {
            return new Response<>(false, "失败",ErrorEnum.BUSI_DUPLICATION.getCode(), ErrorEnum.BUSI_DUPLICATION.getDescription());
        }
        //2、3遍历生成流水号、交易登记 4.异步线程池调用结算钱包服务-记账业务请求
        try {
            chainZeroOutManager.recordZOCtrlAndAcctInstr(zeroOutReportDto,orgnlZeroOutCtrlDO);
        }catch(DuplicateKeyException e){
            log.error("链上清零结果通知 ChainZeroOutService.report error: MsgId={},OrgnlMsgId={}",msgId,orgnlMsgId);
            return new Response<>(false, "失败",ErrorEnum.BUSI_DUPLICATION.getCode(), ErrorEnum.BUSI_DUPLICATION.getDescription());

        }
        //根据清零控制表taskid查询任务执行表,获取原任务信息
        TaskExecutionControlDO taskExecutionControlDO = taskExecutionManager.selectByTaskId(orgnlZeroOutCtrlDO.getTaskId());
        taskExecutionManager.asyncnotify(taskExecutionControlDO,orgnlZeroOutCtrlDO);

        log.info("链上清零结果通知 ChainZeroOutService.report end: MsgId={},OrgnlMsgId={}",msgId,orgnlMsgId);
        return new Response<>(true, "成功");
    }
}
