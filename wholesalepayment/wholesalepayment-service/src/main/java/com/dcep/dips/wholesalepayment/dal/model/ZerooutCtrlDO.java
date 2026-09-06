package com.dcep.dips.wholesalepayment.dal.model;

import com.dcep.dips.wholesalepayment.common.constants.Constant;
import com.dcep.dips.wholesalepayment.enums.ZerooutPrcStsEnum;
import lombok.*;

import java.util.Date;

@ToString
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ZerooutCtrlDO {
    /**
     *   交易标识号 清零通知报文标识号，对应记账指令表msgId
     */
    private String msgId;

    /**
     *   系统工作日期
     */
    private String sysDt;

    /**
     *   清零系统标识
     *   MCBS：货币桥
     *   GCSC：JISR货币桥
     *   BCSP：区块链
     */
    private String sysId;

    /**
     *   当前系统标志
     *   A-当前系统标识为A
     *   B-当前系统标识为B
     */
    private String curSysFlg;

    /**
     *   清零处理状态
     *   00：清零成功
     *   01：清零失败
     *   02：AB账户切换中
     *   03：清零中
     * @see ZerooutPrcStsEnum
     */
    private String prcSts;

    /**
     *   清零记账状态 PR10：结算成功
     */
    private String actgSts;

    /**
     *   清零通知报文档案
     */
    private String document;
    /**
     * 任务唯一ID
     */
    private String taskId;
    /**
     *   创建时间
     */
    private Date gmtCreate;

    /**
     *   更新时间
     */
    private Date gmtModified;

    public ZerooutCtrlDO(String orgnlMsgId) {
        this.msgId = orgnlMsgId;
    }

    public ZerooutCtrlDO(String msgId,String sysDt,String sysId,String curSysFlg,String taskId) {
        this.msgId = msgId;
        this.sysDt = sysDt;
        this.sysId = sysId;
        this.curSysFlg = curSysFlg;
        this.prcSts = Constant.ZERO_OUT_CTRL_STATUS_03;//首次插入新数据默认处理中
        this.actgSts = null;//"PR10"
        this.document = null;
        this.taskId = taskId;
        this.gmtCreate = new Date();
        this.gmtModified = new Date();
    }
    /**
     * 按原交易流水号更新清零状态
     * @param orgnlMsgId
     * @param prcSts
     */
    public ZerooutCtrlDO(String orgnlMsgId, String prcSts) {
        this.msgId = orgnlMsgId;
        this.prcSts = prcSts;
        this.gmtModified = new Date();
    }
}