DROP TABLE IF EXISTS `accounting_instr`;
CREATE TABLE `accounting_instr`
(
    `trans_id`        varchar(35)  NOT NULL COMMENT '交易流水号 调用记账服务的交易流水号',
    `msg_id`          char(32)     NOT NULL COMMENT '报文标识号 一笔msgId可对应多笔transId',
    `msg_tp`          char(15)     DEFAULT NULL COMMENT '报文编号',
    `orgnl_trans_id`  varchar(35)  DEFAULT NULL COMMENT '原交易流水号 原调用记账服务的交易流水号',
    `send_pty_id`     char(14)     NOT NULL COMMENT '发起机构',
    `end_to_end_Id`   varchar(35)  NOT NULL COMMENT '端到端流水号',
    `actg_biz_tp`     char(6)      NOT NULL COMMENT '记账业务类型 \"SWBC01：流动性调拨业务 SWBC02：零售净额轧差业务 SWBC03：批发实时结算业务 SWBC04：区块链服务业务 SWBC05：货币桥业务 SWBC06：JISR业务\"',
    `actg_biz_kind`   varchar(8)   NOT NULL COMMENT '记账业务种类',
    `biz_prty`        char(4)      NOT NULL COMMENT '业务优先级 \"NORM：普通 HIGH：紧急 URGT：特急\"',
    `mgmt_tp`         char(2)      DEFAULT NULL COMMENT '管理类型 \"00：注资调增 01：注资调减 10：预注资调增 11：预注资调减 20：入库 21：出库 31：清零 40: 正常记账 41: 冻结记账 42: 解冻并记账\"',
    `biz_dt`          char(8)      NOT NULL COMMENT '业务日期',
    `actg_dt`         char(8)      DEFAULT NULL COMMENT '记账日期 记账请求应答更新',
    `from_clr_sys_id` varchar(10)  NOT NULL COMMENT '转出方系统标识',
    `from_clr_mmb_id` varchar(35)  NOT NULL COMMENT '转出方机构标识',
    `from_wllt_id`    varchar(34)  DEFAULT NULL COMMENT '转出方钱包',
    `from_acct_no`    varchar(34)  DEFAULT NULL COMMENT '转出方账号',
    `to_clr_sys_id`   varchar(10)  DEFAULT NULL COMMENT '转入方系统标识 冻结时选输',
    `to_clr_mmb_id`   varchar(35)  DEFAULT NULL COMMENT '转入方机构标识 冻结时选输',
    `to_wllt_id`      varchar(34)  DEFAULT NULL COMMENT '转入方钱包',
    `to_acct_no`      varchar(34)  DEFAULT NULL COMMENT '转入方账号',
    `actg_model`      varchar(8)   NOT NULL COMMENT '记账模式 1-实时记账',
    `currency`        char(3)      NOT NULL COMMENT '币种',
    `amount`          bigint(20)   NOT NULL COMMENT '记账金额',
    `abstract_cd`     varchar(8)   NOT NULL COMMENT '摘要码',
    `abstract_desc`   varchar(128) NOT NULL COMMENT '摘要码描述',
    `actg_sts`        char(4)      NOT NULL COMMENT '记账状态 1-成功，记账请求应答更新',
    `actg_prc_cd`     varchar(10)  DEFAULT NULL COMMENT '记账处理码',
    `actg_prc_inf`    varchar(105) DEFAULT NULL COMMENT '记账处理信息',
    `partition_time`  datetime(3)  NOT NULL COMMENT '分区时间 数据库分区',
    `gmt_create`      datetime(3)  NOT NULL COMMENT '创建时间',
    `gmt_modified`    datetime(3)  NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`trans_id`, `partition_time`)
) DEFAULT CHARSET = utf8mb4 COMMENT = '批发交易表，用于记录核心要素，满足交易状态跟踪、清算记账跟踪、区块链数据推送、对账数据要求、各维度数据统计等功能';
CREATE UNIQUE INDEX `uidx_actg_msg_trans_id` ON `accounting_instr` (`msg_id`, `orgnl_trans_id`);


DROP TABLE IF EXISTS `fund_adjust_prod`;
CREATE TABLE `fund_adjust_prod`
(
    `msg_id`          char(32)    NOT NULL COMMENT '报文标识号 dcep181、dcep183 msgId',
    `msg_tp`          char(15)    NOT NULL COMMENT '报文编号',
    `report_msg_id`   char(32)     DEFAULT NULL COMMENT '调整通知标识号 dcep185 msgId',
    `report_msg_tp`   char(15)     DEFAULT NULL COMMENT '报文编号',
    `sttlm_dt`        char(8)     NOT NULL COMMENT '结算日期',
    `send_pty_id`     char(14)    NOT NULL COMMENT '发起机构标识',
    `adjust_pty_id`   char(14)    NOT NULL COMMENT '被调整机构标识',
    `adjust_sys_id`   varchar(16) NOT NULL COMMENT '被调整机构系统标识 DCEP',
    `adjust_wlt_id`   varchar(34)  DEFAULT NULL COMMENT '被调整机构钱包ID',
    `adjust_tp`       char(4)     NOT NULL COMMENT '调整类型 \"PRFD：注资 FFIC：预注资 TRBH：清零 FFRD：预注资调减 FDRD：调减\"',
    `adjust_amt`      bigint(20)  NOT NULL COMMENT '调整金额 单位：分',
    `currency`        char(3)     NOT NULL COMMENT '交易币种',
    `cdt_dbt_ind`     char(1)      DEFAULT NULL COMMENT '借贷标识 C：贷记 D：借记',
    `biz_sts`         char(4)     NOT NULL COMMENT '业务状态 \"PR02: 处理中 PR10: 已结算\"',
    `biz_prc_cd`      char(16)     DEFAULT NULL COMMENT '业务处理码',
    `biz_prc_inf`     varchar(315) DEFAULT NULL COMMENT '业务处理信息',
    `biz_dt`          char(19)    NOT NULL COMMENT '业务处理时间',
    `account_balance` bigint(20)   DEFAULT NULL COMMENT '账户余额',
    `account_flag`    char(1)      DEFAULT NULL COMMENT '账户标识 \"A: A账户 B: B账户\"',
    `ci_limit`        bigint(20)   DEFAULT NULL COMMENT '注资最低限额',
    `net_quota`       bigint(20)   DEFAULT NULL COMMENT '轧差保证金额度',
    `env_info`        varchar(50) DEFAULT NULL COMMENT '环境信息',
    `gmt_create`      datetime(3) NOT NULL COMMENT '创建时间',
    `gmt_modified`    datetime(3) NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`msg_id`)
) DEFAULT CHARSET = utf8mb4 COMMENT = '用于记录结算钱包流动性调拨注资、预注资请求等业务数据';
CREATE INDEX `idx_fund_sttlmdt_adjustpty` ON `fund_adjust_prod` (`sttlm_dt`, `adjust_pty_id`);

DROP TABLE IF EXISTS `common_record`;
CREATE TABLE `common_record`
(
    `msg_id`         char(32)       NOT NULL COMMENT '报文标识号',
    `msg_tp`         char(15)       NOT NULL COMMENT '报文编号',
    `org_msg_id`     char(32) DEFAULT NULL COMMENT '原报文标识号',
    `org_msg_tp`     char(15) DEFAULT NULL COMMENT '原报文编号',
    `document`       varchar(60000) NOT NULL COMMENT '报文档案 密文存储',
    `partition_time` datetime       NOT NULL COMMENT '分区数据 数据库分区',
    `gmt_create`     datetime(3)    NOT NULL COMMENT '创建时间',
    `gmt_modified`   datetime(3)    NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`msg_id`, `msg_tp`, `partition_time`)
) DEFAULT CHARSET = utf8mb4 COMMENT = '交易档案表，存储所有交易类报文信息';


DROP TABLE IF EXISTS `common_stsctrl`;
CREATE TABLE `common_stsctrl`
(
    `msg_id`          char(32)    NOT NULL COMMENT '报文标识号',
    `biz_prty`        char(4)     NOT NULL COMMENT '处理优先级 异步处理队列优先级',
    `send_time`       datetime(3) NOT NULL COMMENT '报文发送时间',
    `interval_second` int(2)      NOT NULL COMMENT '计时间隔时间',
    `confirm_timeout` datetime(3) NOT NULL COMMENT '确认超时时间',
    `notify_timeout`  datetime(3) NOT NULL COMMENT '通知超时时间',
    `presume_status`  char(4)     NOT NULL COMMENT '推定状态',
    `lock_sts`        char(1)     NOT NULL COMMENT '记录锁状态 0-未锁，1-已锁',
    `env_info`        varchar(50) DEFAULT NULL COMMENT '环境信息',
    PRIMARY KEY (`msg_id`)
) DEFAULT CHARSET = utf8mb4 COMMENT = '交易控制表，用于支持交易类报文进行计时和超时处理';

DROP TABLE IF EXISTS `hvps_trans`;
CREATE TABLE `hvps_trans`
(
    `hvps_msg_id`     char(20)    NOT NULL COMMENT '大额报文标识号',
    `hvps_send_pty`   char(14)    NOT NULL COMMENT '大额报文发起机构编码',
    `hvps_msg_tp`     char(15)    NOT NULL COMMENT '大额报文编号 \"hvps.112/115/118\"',
    `msg_id`          char(32)    NOT NULL COMMENT '报文标识号 关联资金调整产品表msg_id',
    `end_to_end_Id`   varchar(35) DEFAULT NULL COMMENT '端到端流水号',
    `snd_dt`          char(19)    NOT NULL COMMENT '报文发送时间',
    `biz_tp`          char(4)     NOT NULL COMMENT '业务类型编码 大额业务类型编码',
    `biz_kind`        char(5)     NOT NULL COMMENT '业务种类编码 大额业务种类编码',
    `dbtr_clr_pty_id` char(14)     DEFAULT NULL COMMENT '付款清算行机构标识',
    `dbtr_pty_id`     char(14)    NOT NULL COMMENT '付款机构编码',
    `cdtr_clr_pty_id` char(14)     DEFAULT NULL COMMENT '收款清算行机构标识',
    `cdtr_pty_id`     char(14)    NOT NULL COMMENT '收款机构编码',
    `currency`        char(3)     NOT NULL COMMENT '交易币种',
    `sttlm_amt`       bigint(20)  NOT NULL COMMENT '结算金额 单位：分',
    `sttlm_dt`        char(8)      DEFAULT NULL COMMENT '结算日期 大额结算日期',
    `biz_sts`         char(4)     NOT NULL COMMENT '业务状态 大额业务状态',
    `biz_prc_cd`      char(16)     DEFAULT NULL COMMENT '业务处理码 大额业务处理码',
    `biz_prc_inf`     varchar(315) DEFAULT NULL COMMENT '业务处理信息 大额业务处理信息',
    `gmt_create`      datetime(3) NOT NULL COMMENT '登记时间戳',
    `gmt_modified`    datetime(3) NOT NULL COMMENT '更新时间戳',
    PRIMARY KEY (`hvps_msg_id`, `hvps_send_pty`)
) DEFAULT CHARSET = utf8mb4 COMMENT = '登记与大额系统对接的交易流水表';

CREATE UNIQUE INDEX `uidx_hvps_trans_msg_id` ON `hvps_trans` (`msg_id`);


DROP TABLE IF EXISTS `chain_trans`;
CREATE TABLE `chain_trans` (
                               `out_msg_id` char(32) NOT NULL COMMENT '外部报文流水号',
                               `msg_id` char(32) NOT NULL COMMENT '报文标识号',
                               `snd_dt` char(19) DEFAULT NULL COMMENT '报文发送时间',
                               `out_bat_id` char(13) DEFAULT NULL COMMENT '外部报文批次号',
                               `out_msg_tp` char(15) DEFAULT NULL COMMENT '外部报文编号',
                               `msg_drn` char(2) DEFAULT NULL COMMENT '收发方向',
                               `clr_typ` char(4) DEFAULT NULL COMMENT '交易类型',
                               `sender_pty_id` char(14) DEFAULT NULL COMMENT '发送机构编码',
                               `sender_lei` char(20) DEFAULT NULL COMMENT '发送机构LEI码',
                               `receiver_pty_id` char(14) DEFAULT NULL COMMENT '接收机构编码',
                               `receiver_lei` char(20) DEFAULT NULL COMMENT '接收机构LEI码',
                               `dbtr_pty_id` char(14) DEFAULT NULL COMMENT '付款机构编码',
                               `dbtr_pty_lei` char(20) DEFAULT NULL COMMENT '付款机构LEI码',
                               `dbtr_sys_id` varchar(10) DEFAULT NULL COMMENT '付款机构系统标识',
                               `cdtr_pty_id` char(14) DEFAULT NULL COMMENT '收款机构编码',
                               `cdtr_pty_lei` char(20) DEFAULT NULL COMMENT '收款机构LEI码',
                               `cdtr_sys_id` varchar(10) DEFAULT NULL COMMENT '收款机构系统标识',
                               `dbtr_wlt_id` char(16) DEFAULT NULL COMMENT '付款钱包ID',
                               `cdtr_wlt_id` char(16) DEFAULT NULL COMMENT '收款钱包ID',
                               `parameter_id` char(32) DEFAULT NULL COMMENT '发行与注销编码',
                               `prc_sts` char(4) DEFAULT NULL COMMENT '交易状态',
                               `gmt_create` datetime NOT NULL COMMENT '登记时间戳',
                               `gmt_modified` datetime NOT NULL COMMENT '更新时间戳',
                               PRIMARY KEY (`out_msg_id`),
                               UNIQUE KEY `uk_name_unique` (`msg_id`)
) DEFAULT CHARSET = utf8mb4   COMMENT = '登记与货币桥、区块链对接的交易流水表';
CREATE UNIQUE INDEX `uidx_chain_trans_msg_id` ON `chain_trans` (`msg_id`);

DROP TABLE IF EXISTS `holiday`;
CREATE TABLE `holiday` (
                           `svc_tm_tp` varchar(4) NOT NULL COMMENT '时序类型 固定填写：ST24',
                           `work_day_flg` varchar(4) NOT NULL COMMENT '工作日标志',
                           `pre_work_day` varchar(8) NOT NULL COMMENT '上一工作日',
                           `cur_biz_dt` varchar(2) NOT NULL COMMENT '当前日期',
                           `next_work_day` varchar(8) NOT NULL COMMENT '下一工作日',
                           `gmt_create` datetime(3) NOT NULL COMMENT '创建时间',
                           `gmt_modified` datetime(3) NOT NULL COMMENT '更新时间',
                           PRIMARY KEY (`cur_biz_dt`)
) DEFAULT CHARSET = utf8mb4  COMMENT = '节假日表';


DROP TABLE IF EXISTS `onchain_payment_trans`;
CREATE TABLE `onchain_payment_trans` (
                                         `msg_id` char(32) NOT NULL COMMENT '报文标识号',
                                         `msg_tp` char(15) DEFAULT NULL COMMENT '报文编号',
                                         `biz_dt` char(19) NOT NULL COMMENT '交易时间',
                                         `bat_id` varchar(13) DEFAULT NULL COMMENT '交易批次号',
                                         `chnl_sys` char(8) NOT NULL COMMENT '渠道系统 \"BCSP：区块链服务平台 MCBS：货币桥 GCSC：JISR\"',
                                         `trans_amt` bigint(20) NOT NULL COMMENT '交易金额',
                                         `biz_tp` char(4) DEFAULT NULL COMMENT '业务类型',
                                         `trans_sts` char(4) DEFAULT NULL COMMENT '交易状态 \"PR00: 成功\"',
                                         `chain_dbtr_pty_id` varchar(14) DEFAULT NULL COMMENT '链上付款机构编码 货币桥机构编码',
                                         `dbtr_pty_id` char(14) NOT NULL COMMENT '付款机构编码 桥下机构编码',
                                         `dbtr_sys_id` varchar(10) DEFAULT NULL COMMENT '付款机构系统标识',
                                         `dbtr_pty_lei` char(20) DEFAULT NULL COMMENT '付款机构LEI码',
                                         `dbtr_wlt_id` char(16) DEFAULT NULL COMMENT '付款钱包ID',
                                         `chain_cdtr_pty_id` varchar(14) DEFAULT NULL COMMENT '链上收款机构编码 货币桥机构编码',
                                         `cdtr_pty_id` char(14) NOT NULL COMMENT '收款机构编码 桥下机构编码',
                                         `cdtr_sys_id` varchar(10) DEFAULT NULL COMMENT '收款机构系统标识',
                                         `cdtr_pty_lei` char(20) DEFAULT NULL COMMENT '收款机构LEI码',
                                         `cdtr_wlt_id` char(16) DEFAULT NULL COMMENT '收款钱包ID',
                                         `gmt_create` datetime(3) NOT NULL COMMENT '登记时间戳',
                                         `gmt_modified` datetime(3) NOT NULL COMMENT '更新时间戳',
                                         PRIMARY KEY (`msg_id`)
) DEFAULT CHARSET = utf8mb4 COMMENT = '同步链上的支付交易信息';

DROP TABLE IF EXISTS `settlement_prod`;
CREATE TABLE `settlement_prod` (
                                   `msg_id` char(32) NOT NULL COMMENT '报文标识号',
                                   `msg_tp` char(15) NOT NULL COMMENT '报文编号',
                                   `sttlm_dt` char(8) DEFAULT NULL COMMENT '结算日期',
                                   `bat_id` char(13) DEFAULT NULL COMMENT '批次号',
                                   `biz_tp` varchar(4) NOT NULL COMMENT '业务类型编码',
                                   `biz_kind` varchar(8) NOT NULL COMMENT '业务种类编码',
                                   `trans_tp` char(1) NOT NULL COMMENT '交易类型 \"1: 普通转账 2: 三方转账 3: 中央对手方CCP\"',
                                   `send_pty_id` char(14) NOT NULL COMMENT '发起机构',
                                   `send_sys_id` varchar(16) DEFAULT NULL COMMENT '发起系统标识',
                                   `recv_pty_id` char(14) NOT NULL COMMENT '接收机构',
                                   `recv_sys_id` varchar(16) DEFAULT NULL COMMENT '接收系统标识',
                                   `dbtr_pty_id` char(14) NOT NULL COMMENT '付款机构',
                                   `dbtr_sys_id` varchar(16) DEFAULT NULL COMMENT '付款方系统标识',
                                   `dbtr_id` varchar(256) DEFAULT NULL COMMENT '付款方ID 付款方钱包/账户ID-密文',
                                   `cbtr_pty_id` char(14) NOT NULL COMMENT '收款机构',
                                   `cbtr_sys_id` varchar(16) DEFAULT NULL COMMENT '收款方系统标识',
                                   `cbtr_id` varchar(256) DEFAULT NULL COMMENT '收款方ID 收款方钱包/账户ID-密文',
                                   `currency` char(3) NOT NULL COMMENT '交易币种',
                                   `sttlm_amt` bigint(20) NOT NULL COMMENT '结算金额 单位：分',
                                   `cdt_dbt_ind` char(1) NOT NULL COMMENT '借贷标识 C：贷记 D：借记',
                                   `biz_sts` char(4) NOT NULL COMMENT '业务状态 \"PR00: 成功 PR01: 失败 PR02: 处理中 PR10: 清算排队 PR11: 待结算\"',
                                   `biz_prc_cd` char(16) DEFAULT NULL COMMENT '业务处理码',
                                   `biz_prc_inf` varchar(315) DEFAULT NULL COMMENT '业务处理信息',
                                   `biz_dt` char(19) NOT NULL COMMENT '业务处理时间',
                                   `biz_desc_inf` varchar(1024) DEFAULT NULL COMMENT '业务描述信息',
                                   `clr_flag` char(1) NOT NULL COMMENT '对账标识 \"0:不对账 1:对账\"',
                                   `clr_prod` char(1) NOT NULL COMMENT '清算产品 \"1:实时 2:不清算\"',
                                   `partition_time` datetime NOT NULL COMMENT '分区时间 数据库分区',
                                   `send_sts` char(1) DEFAULT NULL COMMENT '区块链数据推送状态 \"0:初始化 1:终态 2:推送处理中 3:推送成功\"',
                                   `gmt_create` datetime(3) NOT NULL COMMENT '创建时间',
                                   `gmt_modified` datetime(3) NOT NULL COMMENT '修改时间',
                                   PRIMARY KEY (`msg_id`, `partition_time`)
) DEFAULT CHARSET = utf8mb4 COMMENT = '结算产品表，用于记录核心要素，交易流水幂等控制，满足交易状态跟踪、对账数据要求、各维度数据统计等功能';

CREATE INDEX `idx_settlement_sttlmdt_clrflag` ON `settlement_prod` (`sttlm_dt`, `clr_flag`);


DROP TABLE IF EXISTS `settlement_prod_detail`;
CREATE TABLE `settlement_prod_detail` (
                                          `msg_id` char(32) NOT NULL COMMENT '报文标识号 关联结算产品表msgId',
                                          `tx_id` char(32) NOT NULL COMMENT '明细标识号',
                                          `sttlm_dt` char(8) NOT NULL COMMENT '结算日期',
                                          `instr_id` varchar(35) DEFAULT NULL COMMENT '业务交易编号 CCP业务交易编号',
                                          `biz_tp` char(4) NOT NULL COMMENT '业务类型编码',
                                          `biz_kind` char(5) NOT NULL COMMENT '业务种类编码',
                                          `dbtr_pty_id` char(14) NOT NULL COMMENT '付款机构',
                                          `dbtr_sys_id` varchar(16) DEFAULT NULL COMMENT '付款方系统标识',
                                          `cbtr_pty_id` char(14) NOT NULL COMMENT '收款机构',
                                          `cbtr_sys_id` varchar(16) DEFAULT NULL COMMENT '收款方系统标识',
                                          `currency` char(3) NOT NULL COMMENT '交易币种',
                                          `sttlm_amt` bigint(20) NOT NULL COMMENT '结算金额 单位：分',
                                          `cdt_dbt_ind` char(1) NOT NULL COMMENT '借贷标识 C：贷记 D：借记',
                                          `biz_sts` char(4) NOT NULL COMMENT '业务状态 \"PR00: 成功 PR01: 失败 PR02: 处理中 PR10: 清算排队 PR11: 待结算\"',
                                          `biz_prc_cd` char(16) DEFAULT NULL COMMENT '业务处理码',
                                          `biz_prc_inf` varchar(315) DEFAULT NULL COMMENT '业务处理信息',
                                          `biz_dt` char(19) NOT NULL COMMENT '业务处理时间',
                                          `partition_time` datetime NOT NULL COMMENT '分区时间 数据库分区',
                                          `gmt_create` datetime(3) NOT NULL COMMENT '创建时间',
                                          `gmt_modified` datetime(3) NOT NULL COMMENT '修改时间',
                                          PRIMARY KEY (`msg_id`, `tx_id`),
                                          KEY `idx_sttlm_dt` (`sttlm_dt`)
) DEFAULT CHARSET = utf8mb4 COMMENT = '用于记录批量业务报文中业务明细数据';
CREATE INDEX `idx_settle_dtl_sttlm_dt` ON `settlement_prod_detail` (`sttlm_dt`);

DROP TABLE IF EXISTS `storage_forward`;
CREATE TABLE `storage_forward` (
                                   `msg_id` char(32) NOT NULL COMMENT '报文标识号',
                                   `msg_tp` char(15) NOT NULL COMMENT '报文编号',
                                   `storg_tp` char(1) NOT NULL COMMENT '处理类型 \"0-动账消息推送 1-机构异步通知 2-机构异步冲正 3-加密前置补偿\"',
                                   `storg_inf` varchar(8000) DEFAULT NULL COMMENT '存储处理信息 \"动账消息推送bean 加密前置补偿bean 消息队列推送bean\"',
                                   `lock_sts` char(1) NOT NULL COMMENT '锁状态 0-未锁，1-已锁',
                                   `receive_inst` char(14) DEFAULT NULL COMMENT '接收机构',
                                   `send_time` datetime(3) NOT NULL COMMENT '发送时间',
                                   `interval_second` int(2) NOT NULL COMMENT '发送间隔时间',
                                   `process_timeout` datetime(3) NOT NULL COMMENT '处理超时时间',
                                   `env_info` varchar(50) DEFAULT NULL COMMENT '环境信息',
                                   `gmt_create` datetime(3) NOT NULL COMMENT '创建时间',
                                   `gmt_modified` datetime(3) NOT NULL COMMENT '修改时间',
                                   PRIMARY KEY (`msg_id`, `msg_tp`)
) DEFAULT CHARSET = utf8mb4 COMMENT = '存储转发表，用于对交易结果通知类报文、消息推送重试补偿';
CREATE INDEX `idx_storage_locksts_gmtcreate` ON `storage_forward` (`lock_sts`, `gmt_create`);

DROP TABLE IF EXISTS `system_status`;
CREATE TABLE `system_status` (
                                 `sys_cd` varchar(4) NOT NULL COMMENT '系统编号 0000:结算钱包系统；0001:批发支付系统；0002:零售支付系统',
                                 `svc_tm_tp` varchar(4) NOT NULL COMMENT '时序类型 固定填写ST24:24小时服务类型',
                                 `cur_sys_flg` char(1) NOT NULL COMMENT '当前系统标志 A-当前系统标识为A B-当前系统标识为B',
                                 `cur_sys_dt` varchar(8) NOT NULL COMMENT '当前账务日期',
                                 `start_tm` datetime(3) NOT NULL COMMENT '日切开始时间',
                                 `end_tm` datetime(3) NOT NULL COMMENT '日切结束时间',
                                 `pre_sys_dt` varchar(8) NOT NULL COMMENT '上一账务日期',
                                 `next_sys_dt` varchar(8) NOT NULL COMMENT '下一账务日期',
                                 `cur_sys_sts` varchar(4) NOT NULL COMMENT '当前系统状态 01：日间 02：夜间',
                                 `pre_sys_sts` varchar(4) NOT NULL COMMENT '上一系统状态',
                                 `eod_sts` char(4) NOT NULL COMMENT '日终进度 EP00--初始进度 EP01--分录平衡检查（此进度时，允许再做平衡检查） EP02--日终开始依赖条件检查 EP03--账务日切 EP04--日切后处理 EP05--总账明细汇总 EP06--总账平衡检查 EP07--总分核对 EP08--清算账户余额通知 EP22--积数累计 EP00--日终结束',
                                 `gmt_create` datetime(3) NOT NULL COMMENT '创建时间',
                                 `gmt_modified` datetime(3) NOT NULL COMMENT '更新时间',
                                 PRIMARY KEY (`sys_cd`)
) DEFAULT CHARSET = utf8mb4 COMMENT = '系统状态表';

DROP TABLE IF EXISTS `zeroout_ctrl`;
CREATE TABLE `zeroout_ctrl` (
                                `msg_id` char(32) NOT NULL COMMENT '交易标识号 清零通知报文标识号，对应记账指令表msgId',
                                `sys_dt` char(8) NOT NULL COMMENT '系统工作日期',
                                `sys_id` varchar(8) NOT NULL COMMENT '清零系统标识 MCBS：货币桥 GCSC：JISR货币桥 BCSP：区块链',
                                `cur_sys_flg` char(1) DEFAULT NULL COMMENT '当前系统标志 A-当前系统标识为A B-当前系统标识为B',
                                `prc_sts` varchar(4) NOT NULL COMMENT '清零处理状态 00：清零成功 01：清零失败 02：AB账户切换中 03：清零中',
                                `actg_sts` char(4) DEFAULT NULL COMMENT '清零记账状态 PR10：结算成功',
                                `document` varchar(60000) DEFAULT NULL COMMENT '清零通知报文档案',
                                `task_id` varchar(64) NOT NULL COMMENT '任务唯一ID',
                                `gmt_create` datetime(3) NOT NULL COMMENT '创建时间',
                                `gmt_modified` datetime(3) NOT NULL COMMENT '更新时间',
                                PRIMARY KEY (`msg_id`)
) DEFAULT CHARSET = utf8mb4 COMMENT = '清零控制表，记录货币桥、区块链服务平台等清零记录';
CREATE INDEX `idx_zeroout_ctrl_sysdt_sysid` ON `zeroout_ctrl` (`sys_dt`, `sys_id`);

DROP TABLE IF EXISTS `task_execution_control`;
CREATE TABLE `task_execution_control` (
                                          `task_id` varchar(64) NOT NULL COMMENT '任务唯一ID',
                                          `task_cd` varchar(8) NOT NULL COMMENT '任务编码',
                                          `task_nm` varchar(100) NOT NULL COMMENT '任务名称',
                                          `task_sts` varchar(4) NOT NULL COMMENT '任务执行状态',
                                          `content` varchar(4096) DEFAULT NULL COMMENT '执行内容',
                                          `error_cd` varchar(20) DEFAULT NULL COMMENT '错误码',
                                          `error_msg` varchar(256) DEFAULT NULL COMMENT '错误信息',
                                          `memo` varchar(256) DEFAULT NULL COMMENT '备注',
                                          `gmt_create` datetime NOT NULL COMMENT '创建时间',
                                          `gmt_modified` datetime NOT NULL COMMENT '更新时间',
                                          PRIMARY KEY (`task_id`)
) DEFAULT CHARSET = utf8mb4 COMMENT = '任务执行空表记录任务执行记录';

