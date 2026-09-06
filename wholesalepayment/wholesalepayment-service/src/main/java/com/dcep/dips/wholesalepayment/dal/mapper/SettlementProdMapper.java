package com.dcep.dips.wholesalepayment.dal.mapper;

import com.dcep.dips.wholesalepayment.dal.model.SettlementProdDO;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SettlementProdMapper {
    /**
     * 插入一条记录
     * @param settlementProdDO 结算产品数据对象
     * @return 影响的行数
     */
    int insert(SettlementProdDO settlementProdDO);

    /**
     * 根据主键查询记录
     * @param settlementProdDO 结算产品数据对象
     * @return 结算产品数据对象
     */
    SettlementProdDO selectByPrimaryKey(SettlementProdDO settlementProdDO);

    /**
     * 查询结算排队数据
     * @param currentSystemDate 当前系统日期
     * @param msgId 从该msgId的下一个开始查询
     * @return
     */
    List<SettlementProdDO> selectQueueByCurrentSystemDate(@Param("currentSystemDate") String currentSystemDate,@Param("msgId") String msgId);

    /**
     * 更新记录
     * @param settlementProdDO 结算产品数据对象
     * @return 影响的行数
     */
    int updateByPrimaryKey(SettlementProdDO settlementProdDO);

    /**
     * 结算钱包调整应答后，修改结算产品表的业务状态
     * @param settlementProdDO 结算产品数据对象
     * @return 影响的行数
     */
    int updateBizSts(@Param("do") SettlementProdDO settlementProdDO, @Param("orgnlBizSts") String orgnlBizSts);

    int updateByMsgId(@Param("do") SettlementProdDO settlementProdDO);

    /**
     * 根据分区时间查询结算产品信息参与清算(clr_flag=1)状态为终态(send_sts=1)的交易
     * @param lastDate 起始时间
     * @param nextDate 结束时间
     * @param limitCount 查询条数
     * @return
     */
    List<SettlementProdDO> selectByPartitionTime(@Param("lastDate") String lastDate, @Param("nextDate") String nextDate, @Param("limitCount") Long limitCount);

    /**
     *
     * @param lastDate 起始时间
     * @param nextDate 结束时间
     * @param time 修改时间
     * @return
     */
    List<SettlementProdDO> selectSendFailInfo(@Param("lastDate") String lastDate, @Param("nextDate") String nextDate, @Param("sysTime") Date time);

    /**
     * 更新结算产品表推送处理中(send_sts=2)交易状态为终态
     * @param settlementProdDO
     * @return
     */
    int updateSendFailInfo(@Param("do")SettlementProdDO settlementProdDO);

    /**
     * 锁定信息
     * @param settlementProdDO
     * @return
     */
    int updateSendStsLockByMsgId(@Param("do")SettlementProdDO settlementProdDO);

    /**
     * 解锁信息
     * @param settlementProdDO
     * @return
     */
    int updateSendStsUnLockByMsgId(@Param("do")SettlementProdDO settlementProdDO);

}
