package com.dcep.dips.wholesalepayment.manager.impl;

import com.dcep.common.model.Response;
import com.dcep.dips.operatingcontrol.dto.TaskResultDTO;
import com.dcep.dips.operatingcontrol.spi.BatchTaskCallbackService;
import com.dcep.dips.wholesalepayment.common.constants.Constant;
import com.dcep.dips.wholesalepayment.dal.mapper.TaskExecutionControlDOMapper;
import com.dcep.dips.wholesalepayment.dal.model.TaskExecutionControlDO;
import com.dcep.dips.wholesalepayment.dal.model.ZerooutCtrlDO;
import com.dcep.dips.wholesalepayment.manager.TaskExecutionManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;

@Slf4j
@Component
public class TaskExecutionManagerImpl implements TaskExecutionManager {

    @Autowired
    TaskExecutionControlDOMapper taskExecutionControlDOMapper;

    @DubboReference(timeout = 7000)
    BatchTaskCallbackService batchTaskCallbackService;

    @Resource
    @Qualifier("asyncBizPool")
    private ExecutorService asyncPool;

    /**
     * 登记任务执行控制表
     *
     * @param taskExecutionControlDO
     * @return
     */
    @Override
    public int recordTaskExecute(TaskExecutionControlDO taskExecutionControlDO) {
        return taskExecutionControlDOMapper.insert(taskExecutionControlDO);
    }

    @Override
    public TaskExecutionControlDO selectByTaskId(String taskId) {
        return taskExecutionControlDOMapper.selectByPrimaryKey(taskId);
    }

    /**
     * 异步回调时序控制
     *
     * @param taskExecutionControlDO
     * @param zerooutCtrlDO
     */
    @Override
    public void asyncnotify(TaskExecutionControlDO taskExecutionControlDO, ZerooutCtrlDO zerooutCtrlDO) {
        try {
            log.info("异步调用时序控制");
            asyncPool.execute(() -> {
                TaskResultDTO taskResultDTO = new TaskResultDTO();
                taskResultDTO.setTaskId(taskExecutionControlDO.getTaskId());
                taskResultDTO.setTaskCode(taskExecutionControlDO.getTaskCd());
                taskResultDTO.setTaskName(taskExecutionControlDO.getTaskNm());
                taskResultDTO.setSystemDate(zerooutCtrlDO.getSysDt());
                taskResultDTO.setTaskStatus(Constant.TASK_STATUS_1);
                Response<Void> notify = batchTaskCallbackService.notify(taskResultDTO);
                if (!notify.isSuccess()){
                    log.error("异步调用时序控制失败");
                }
            });
        } catch (Exception e) {
            log.error("BatchTaskCallbackService.asyncnotify error exception:{}", e);
        }
    }
}
