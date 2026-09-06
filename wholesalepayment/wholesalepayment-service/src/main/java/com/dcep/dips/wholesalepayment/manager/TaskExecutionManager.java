package com.dcep.dips.wholesalepayment.manager;

import com.dcep.dips.wholesalepayment.dal.model.TaskExecutionControlDO;
import com.dcep.dips.wholesalepayment.dal.model.ZerooutCtrlDO;

public interface TaskExecutionManager {

    /**
     * 登记任务执行控制表
     * @param taskExecutionControlDO
     * @return
     */
    int recordTaskExecute(TaskExecutionControlDO taskExecutionControlDO);

    /**
     * 查询任务详情
     * @param taskId
     * @return
     */
    TaskExecutionControlDO selectByTaskId(String taskId);

    /**
     * 异步回调时序控制
     * @param taskExecutionControlDO
     * @param zerooutCtrlDO
     */
    void asyncnotify(TaskExecutionControlDO taskExecutionControlDO, ZerooutCtrlDO zerooutCtrlDO);
}
