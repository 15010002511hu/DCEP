package com.dcep.dips.wholesalepayment.dal.mapper;

import com.dcep.dips.wholesalepayment.dal.model.TaskExecutionControlDO;

public interface TaskExecutionControlDOMapper {
    /**
     * 插入任务执行控制表
     * @param record
     * @return
     */
    int insert(TaskExecutionControlDO record);

    /**
     * 按任务编号查询任务执行控制表
     * @param taskId
     * @return
     */
    TaskExecutionControlDO selectByPrimaryKey(String taskId);

    /**
     * 更新任务执行控制表
     * @param record
     * @return
     */
    int updateByPrimaryKey(TaskExecutionControlDO record);
}
