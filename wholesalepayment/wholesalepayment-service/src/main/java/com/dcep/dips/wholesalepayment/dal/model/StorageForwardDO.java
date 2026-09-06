package com.dcep.dips.wholesalepayment.dal.model;

import com.dcep.dips.wholesalepayment.common.utils.CommonUtil;
import lombok.*;

import java.util.Date;


/**
 * @author wangxiaoyu
 */
@ToString
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StorageForwardDO {
    /**
     *   报文标识号
     */
    private String msgId;

    /**
     *   报文编号
     */
    private String msgTp;

    /**
     *   处理类型 "0-动账消息推送 1-机构异步通知 2-机构异步冲正 3-加密前置补偿"
     */
    private String storgTp;

    /**
     *   存储处理信息 "动账消息推送bean 加密前置补偿bean 消息队列推送bean"
     */
    private String storgInf;

    /**
     *   锁状态 0-未锁，1-已锁
     */
    private String lockSts;

    /**
     *   接收机构
     */
    private String receiveInst;

    /**
     *   发送时间
     */
    private Date sendTime;

    /**
     *   发送间隔时间
     */
    private Integer intervalSecond;

    /**
     *   处理超时时间
     */
    private Date processTimeout;

    /**
     *   环境信息
     */
    private String envInfo;

    /**
     *   创建时间
     */
    private Date gmtCreate;

    /**
     *   修改时间
     */
    private Date gmtModified;

    public StorageForwardDO(String msgId) {
        this.msgId = msgId;
    }

    public StorageForwardDO(String msgId, String msgTp) {
        this.msgId = msgId;
        this.msgTp = msgTp;
    }

    public StorageForwardDO(String msgId, String msgTp, String storgTp,
                            String storgInf, String lockSts, String receiveInst,
                            Date sendTime, Integer intervalSecond,
                            Date processTimeout) {
        this.msgId = msgId;
        this.msgTp = msgTp;
        this.storgTp = storgTp;
        this.storgInf = storgInf;
        this.lockSts = lockSts;
        this.receiveInst = receiveInst;
        this.sendTime = sendTime;
        this.intervalSecond = intervalSecond;
        this.processTimeout = processTimeout;
        this.gmtCreate = new Date();
        this.gmtModified = new Date();
        this.envInfo = CommonUtil.getEnvInfo();
    }
}
