package com.dcep.dips.wholesalepayment.dal.model;

import lombok.*;

import java.util.Date;

@ToString
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemStatusDO {
    /**
     *   系统编号
     *   0000:结算钱包系统
     *   0001:批发支付系统
     *   0002:零售支付系统
     */
    private String sysCd;

    /**
     *   时序类型 固定填写ST24:24小时服务类型
     */
    private String svcTmTp;

    /**
     *   当前系统标志
     *   A-当前系统标识为A
     *   B-当前系统标识为B
     */
    private String curSysFlg;

    /**
     *   当前账务日期
     */
    private String curSysDt;

    /**
     *   日切开始时间
     */
    private Date startTm;

    /**
     *   日切结束时间
     */
    private Date endTm;

    /**
     *   上一账务日期
     */
    private String preSysDt;

    /**
     *   下一账务日期
     */
    private String nextSysDt;

    /**
     *   当前系统状态 01：日间 02：夜间
     */
    private String curSysSts;

    /**
     *   上一系统状态
     */
    private String preSysSts;

    /**
     *   日终进度
     *   EP00--初始进度
     *   EP01--分录平衡检查（此进度时，允许再做平衡检查）
     *   EP02--日终开始依赖条件检查
     *   EP03--账务日切
     *   EP04--日切后处理
     *   EP05--总账明细汇总
     *   EP06--总账平衡检查
     *   EP07--总分核对
     *   EP08--清算账户余额通知
     *   EP22--积数累计
     *   EP00--日终结束
     */
    private String eodSts;

    private Date gmtCreate;

    /**
     *   更新时间
     */
    private Date gmtModified;

}