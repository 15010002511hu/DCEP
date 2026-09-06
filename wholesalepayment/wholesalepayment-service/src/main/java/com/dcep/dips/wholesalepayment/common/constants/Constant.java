/*
 * pbcdci.cn Inc.
 * Copyright © 2025 PBCDCI All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.common.constants;

public class Constant {

    public static final String SOAP_VERSION = "01";

    /**MCBS报文头版本号*/
    public static final String MCBS_SOAPHEADER_VER = "01";

    /**DCEP报文头版本号*/
    public static final String DCEP_SOAPHEADER_VER = "01";

    /**央行三位标识*/
    public static final String PBOC = "001";

    /**交易登记落库步骤*/
    public static final String PREPARE_INSERT_STEP = "insert_step";

    public static final String INSERT_STEP_ONE = "1";

    public static final String INSERT_STEP_TWO = "2";

    public static final String INSERT_STEP_THREE = "3";

    public static final String PREPARE_INSERT_MSGID = "insert_msgid";

    public static final String SYSTEM_ERROR_CODE = "DCEPS"; // 系统错误处理码前缀

    public static final int ERROR_CODE_LENGTH = 9; // 标准错误码长度

    public static final String SYSTEM = "hlht"; // 互联互通

    /**运营中心内部-异步推送间隔时间*/
    public static final int ASYNC_PUSH_INTERVAL = 4;

    /**向机构推送-异步推送间隔时间*/
    public static final int ASYNC_PUSH_INST_INTERVAL = 8;

    /**总推定时间*/
    public static final int TOTAL_PUSH_TIME = 600;

    public static final String PRESS_FLAG_TRUE = "true";

    public static final String PRESS_FLAG_FALSE = "false";

    public static final String DUPLICATION_EXCEPTION_LOG = "msgId duplication: msgId={}, msgTp={}";
    /**机构kafka消息topic */
    public static final String WHOLESALE_KAFKA_TOPIC = "wholesale-trade";
    /**货币桥kafka消息topic */
    public static final String MCBS_KAFKA_TOPIC = "mcbs-trade";
    /**JISR kafka消息topic */
    public static final String JISR_KAFKA_TOPIC = "jisr-trade";
    /**JISR货币桥系统标识*/
    public static final String GCSC = "GCSC";
    /**货币桥系统标识*/
    public static final String MCBS = "MCBS";
    /**DCEP系统标识*/
    public static final String DCEP = "DCEP";
    /**区块链系统标识*/
    public static final String BCSP = "BCSP";
    /**发行*/
    public static final String MBRIDGE_CTGYPURP_ISUE = "ISUE";
    /**注销*/
    public static final String MBRIDGE_CTGYPURP_REDT = "REDT";

    /**清零控制表-清零状态*/
    public static final String ZERO_OUT_CTRL_STATUS_03 = "03";   //清零中
    public static final String ZERO_OUT_CTRL_STATUS_02 = "02";   //AB账户切换中
    public static final String ZERO_OUT_CTRL_STATUS_01 = "01";   //清零失败
    public static final String ZERO_OUT_CTRL_STATUS_00 = "00";   //清零成功
    /**清零记账状态*/
    public static final String ZERO_OUT_ACTG_STATUS_PR10= "PR10";  //结算成功

    /** 任务编码 */
    public static final String TASK_CODE_B0101= "B0101"; //货币桥清零
    public static final String TASK_CODE_B0102= "B0102"; //JISR清零
    public static final String TASK_CODE_B0103= "B0103"; //区块链服务平台清零
    public static final String TASK_CODE_B0202= "B0202"; //货币桥清零完成通知
    public static final String TASK_CODE_C0201= "C0201"; //JISR清零完成通知
    public static final String TASK_CODE_D0401= "D0401"; //日切状态通知
    public static final String TASK_CODE_D0402= "D0402"; //终止T日业务受理通知
    public static final String TASK_CODE_E0203= "E0203"; //FMI对账通知

    /**任务执行结果*/
    public static final String TASK_STATUS_0 = "0";//执行中
    public static final String TASK_STATUS_1 = "1";//执行成功
    public static final String TASK_STATUS_2 = "2";//执行失败

    public static final String DCEP_MSGTYPE_203 = "dcep.203.010.01";
    public static final String DCEP_MSGTYPE_213 = "dcep.213.010.01";
    public static final String MCBS_MSGTYPE_100 = "mcbs.100.001.01";
    public static final String MCBS_MSGTYPE_101 = "mcbs.101.001.01";
    public static final String MCBS_MSGTYPE_102 = "mcbs.102.001.01";
    public static final String MCBS_MSGTYPE_200 = "mcbs.200.001.01";
    public static final String MCBS_MSGTYPE_201 = "mcbs.201.001.01";
    public static final String MCBS_MSGTYPE_202 = "mcbs.202.001.01";
    public static final String MCBS_MSGTYPE_203 = "mcbs.203.001.01";
    public static final String MCBS_MSGTYPE_900 = "mcbs.900.001.01";
    /**PBOC机构LEI码*/
    public static final String LEI_PBOC = "300300CHNPBC19481202";
    /**货币桥LEI码*/
    public static final String LEI_MCBS = "0000";
    /**报文发送方向：货币桥接收*/
    public static final String DIRECTION_FROM_HLHT_TO_MBRIDGE = "R";
    public static final String MBRIDGE_SETTLE_PRIORITY = "URGT";
    public static final String BATCH_DATE_PATTERN = "yyyyMMdd";
    public static final String BATCH_TIME_PATTERN = BATCH_DATE_PATTERN + "HH";
    public static final String NUM_OF_TXS_1 = "1";
    public static final String SETTLE_METHOD_CLRG = "CLRG";
    /**货币桥中国清算系统*/
    public static final String MBRIDGE_CHINA_CLEARINGCODE = "CNDCEP";
    /**货币桥报文扩展字段: DCEP报文标识号*/
    public static final String MBRIDGE_EXTRA_MSGID = "msgId";
    /**货币桥报文扩展字段: DCEP批次号*/
    public static final String MBRIDGE_EXTRA_BATCHID = "batchId";
    public static final String MBRIDGE_200_PLACE_AND_NAME = "/Envelope/FICdtTrf/CdtTrfTxInf";
    public static final String MBRIDGE_201_PLACE_AND_NAME = "/Envelope/FIDrctDbt/CdtInstr";
    public static final String MBRIDGE_203_PLACE_AND_NAME = "/Envelope/FIToFIPmtStsRpt/TxInfAndSts";
    public static final String INTERNAL_ERROR_MSG = "unknown internal error";
    public static final String MBRIDGE_REQHDLG_DESC_SUCC = "30I0000:处理成功";
    public static final String MBRIDGE_REQHDLG_DESC_NOTMATCH = "30O6002:应答报文或回执报文没有匹配的原业务";
    public static final String MBRIDGE_REQHDLG_DESC_TPERR = "30O1103:报文类型与业务类型不匹配";
    /**货币桥默认错误码*/
    public static final String MBRIDGE_DEFAULT_ERRCODE = "30S9999";
    /**货币桥默认错误信息*/
    public static final String MBRIDGE_DEFAULT_ERRMSG = "other system error";
    /**DCEP默认错误码*/
    public static final String DCEP_DEFAULT_ERRCODE = "R999";
    /**DCEP默认错误信息*/
    public static final String DCEP_DEFAULT_ERRMSG = "机构自定义原因失败说明";
    public static final String SVCSVL_PRTRY_TT00 = "TT00";
    public static final String DCEP_CTGYPURP_223 = "223";
    public static final String CHRGBR_DEBT = "DEBT";
    public static final String CHRGBR_CRED = "CRED";
    public static final String DCEP_PURP_22300001 = "22300001";
    public static final String DCEP_PURP_22300002 = "22300002";
    public static final String PREFIX_REASON = "/Reason/";
    public static final String PREFIX_PARAMETERID = "/ParameterId/";
    public static final String PREFIX_SNDCHNLSYS = "/SndChnlSys/";
    public static final String PREFIX_RCVCHNLSYS = "/RcvChnlSys/";
    public static final String PREFIX_POSTSCRIPT = "/Postscript/";
    public static final String PREFIX_STTLMDT = "/SttlmDt/";
    public static final String PREFIX_SYSWORKDT = "/SysWorkDt/";
    /**DCEP911报文明细内容最大长度*/
    public static final int DCEP_911_DETAIL_MAX = 511;
    /**发行和注销方式编码*/
    public static final String DCEP_PARAMETER_ID= "CLRZ";
    /**业务类型*/
    public static final String DCEP_PRTRY_REDT= "REDT";

    /**系统标识 **/
    public static final String SYSFLAG_A = "A";
    public static final String SYSFLAG_B = "B";
    public static final String SYSFLAG_N = "N";
    /**区块链系统版本号 **/
    public static final String BCSP_VERSION = "2.0";

}
