package com.dcep.dips.wholesalepayment.dal.model;

import lombok.*;
import java.util.Date;

@ToString
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskExecutionControlDO {
    /**
     * 任务唯一ID
     */
    private String taskId;
    /**
     * 任务编码
     */
    private String taskCd;
    /**
     * 任务名称
     */
    private String taskNm;
    /**
     * 任务执行状态 "0 初始状态 1 执行成功 2 执行失败"
     */
    private String taskSts;
    /**
     * 执行内容
     */
    private String content;
    /**
     * 错误码
     */
    private String errorCd;
    /**
     * 错误信息
     */
    private String errorMsg;
    /**
     * 备注
     */
    private String memo;
    /**
     * 创建时间
     */
    private Date gmtCreate;
    /**
     * 更新时间
     */
    private Date gmtModified;
}
