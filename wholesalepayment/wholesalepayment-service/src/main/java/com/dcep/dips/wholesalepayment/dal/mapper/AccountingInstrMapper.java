package com.dcep.dips.wholesalepayment.dal.mapper;

import com.dcep.dips.wholesalepayment.dal.model.AccountingInstrDO;
import org.apache.ibatis.annotations.Param;

public interface AccountingInstrMapper {
    /**
     * 插入一条记录
     * @param accountingInstrDO 记账指令数据对象
     * @return 影响的行数
     */
    int insert(AccountingInstrDO accountingInstrDO);

    /**
     * 根据主键查询记录
     * @param accountingInstrDO 记账指令数据对象
     * @return 记账指令数据对象
     */
    AccountingInstrDO selectByPrimaryKey(AccountingInstrDO accountingInstrDO);

    /**
     * 更新记录
     * @param accountingInstrDO 记账指令数据对象
     * @return 影响的行数
     */
    int updateByPrimaryKey(AccountingInstrDO accountingInstrDO);

    /**
     * 根据报文标识号查询记账指令记录(查找原始记录）
     * @param msgId 报文标识号
     * @return 记账指令数据对象
     */
    AccountingInstrDO selectByMsgId(String msgId);

    /**
     * 根据报文标识号查询记账指令记录(查找原始记录）
     * @param msgId 报文标识号
     * @return 记账指令数据对象
     */
    AccountingInstrDO selectByMsgIdPrepare(String msgId);

    /**
     * 根据报文标识号查询记账指令记录
     * @param accountingInstrDO 记账指令信息记录
     * @return 记账指令数据对象
     */
    AccountingInstrDO selectByMsgIdFinish(AccountingInstrDO accountingInstrDO);

    /**
     * 更新结算钱包系统返回的记账状态和应答信息
     * @param accountingInstrDO 记账指令更新对象
     * @param orgnlActgSts 原记账状态
     * @return 影响的行数
     */
    int updateActgSts(@Param("accountingInstrDO") AccountingInstrDO accountingInstrDO,
                      @Param("orgnlActgSts") String orgnlActgSts);

    int updateAccountingInstr(AccountingInstrDO accountingInstrDO);
}
