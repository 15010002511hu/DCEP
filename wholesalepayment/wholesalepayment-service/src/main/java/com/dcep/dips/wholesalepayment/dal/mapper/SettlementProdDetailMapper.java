package com.dcep.dips.wholesalepayment.dal.mapper;

import com.dcep.dips.wholesalepayment.dal.model.SettlementProdDetailDO;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SettlementProdDetailMapper {
    int deleteByPrimaryKey(@Param("msgId") String msgId, @Param("txId") String txId);

    int insert(SettlementProdDetailDO record);

    SettlementProdDetailDO selectByPrimaryKey(@Param("msgId") String msgId, @Param("txId") String txId);

    List<SettlementProdDetailDO> selectAll();

    int updateByPrimaryKey(SettlementProdDetailDO record);
}
