package com.dcep.dips.wholesalepayment.dal.model;

import com.dcep.dips.wholesalepayment.annotation.Clearing;
import com.dcep.dips.wholesalepayment.annotation.WholesalePayment;
import com.dcep.dips.wholesalepayment.common.constants.Constant;
import com.dcep.dips.wholesalepayment.common.utils.CommonUtil;
import com.dcep.dips.wholesalepayment.dto.FundingDTO;
import com.dcep.dips.wholesalepayment.dto.ClearingDTO;
import com.dcep.dips.wholesalepayment.dto.chain.OnChainAdjustReqDTO;
import com.dcep.dips.wholesalepayment.enums.BizPrtyEnum;
import com.dcep.dips.wholesalepayment.enums.ClearingStatusEnum;
import com.dcep.dips.wholesalepayment.enums.LockEnum;
import lombok.*;

import java.util.Calendar;
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
public class CommonStsctrlDO {
    /**
     *   报文标识号
     */
    private String msgId;

    /**
     *   处理优先级 异步处理队列优先级
     */
    private String bizPrty;

    /**
     *   报文发送时间
     */
    private Date sendTime;

    /**
     *   计时间隔时间
     */
    private Integer intervalSecond;

    /**
     *   确认超时时间
     */
    private Date confirmTimeout;

    /**
     *   通知超时时间
     */
    private Date notifyTimeout;

    /**
     *   推定状态
     */
    private String presumeStatus;

    /**
     *   记录锁状态 0-未锁，1-已锁
     */
    private String lockSts;

    /**
     *   环境信息
     */
    private String envInfo;

    /**
     *   创建时间
     */
    private Date gmtCreate;

    public CommonStsctrlDO(FundingDTO fundingDTO, WholesalePayment wholesalePayment) {
        this.msgId = fundingDTO.msgId();
        this.bizPrty = fundingDTO.bizPrty();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        this.sendTime = calendar.getTime();

        this.intervalSecond = wholesalePayment.intervalSecond();

        // 若推定配置时间不为空则取推定配置时间
        int confirmTimeout = fundingDTO.presumeTm() != null ? Integer.parseInt(fundingDTO.presumeTm())
                : wholesalePayment.confirmTimeout();

        calendar.add(Calendar.SECOND, confirmTimeout);
        this.confirmTimeout = calendar.getTime();

        int notityTimeout = fundingDTO.presumeTm() != null ? Constant.TOTAL_PUSH_TIME - Integer.parseInt(fundingDTO.presumeTm())
                : wholesalePayment.notityTimeout();

        calendar.add(Calendar.SECOND, notityTimeout);
        this.notifyTimeout = calendar.getTime();

        this.presumeStatus = wholesalePayment.presumeStatus().getCode();

        this.lockSts = LockEnum.UNLOCK.getCode();

        this.envInfo = CommonUtil.getEnvInfo();
    }

    public CommonStsctrlDO(String msgId) {
        this.msgId = msgId;
    }

    public CommonStsctrlDO(ClearingDTO clearingDTO, Clearing clearing) {
        this.msgId = clearingDTO.getClrMsgId();
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        setSendTime(calendar.getTime());
        // 若推定配置时间不为空则取推定配置时间
        calendar.add(Calendar.SECOND, clearingDTO.clrPresumeTm() != null ? Integer.parseInt(clearingDTO.clrPresumeTm())
                : clearing.confirmTimeout());
        this.confirmTimeout = calendar.getTime();
        calendar.add(Calendar.SECOND,
                clearingDTO.clrPresumeTm() != null
                        ? Constant.TOTAL_PUSH_TIME - Integer.parseInt(clearingDTO.clrPresumeTm())
                        : clearing.notityTimeout());
        this.notifyTimeout = calendar.getTime();
        setIntervalSecond(clearing.intervalSecond());
        setPresumeStatus(clearing.presumeStatus().getCode());
        this.lockSts = "0";
        this.bizPrty = clearingDTO.clrBizPrty();
        setEnvInfo(CommonUtil.getEnvInfo());
    }

    public CommonStsctrlDO(String msgId,String bizPrty) {
        this.msgId = msgId;
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        setSendTime(calendar.getTime());
        // 若推定配置时间不为空则取推定配置时间
        calendar.add(Calendar.SECOND, 300);
        this.confirmTimeout = calendar.getTime();
        calendar.add(Calendar.SECOND,  Constant.TOTAL_PUSH_TIME);
        this.notifyTimeout = calendar.getTime();
        setIntervalSecond(7);
        setPresumeStatus(ClearingStatusEnum.PRESUME_FAILED.getCode());
        this.lockSts = "0";
        this.bizPrty = bizPrty;
        setEnvInfo(CommonUtil.getEnvInfo());
    }

    public CommonStsctrlDO(String msgId, Date sendTime) {
        this.msgId = msgId;
        this.sendTime = sendTime;
    }
}
