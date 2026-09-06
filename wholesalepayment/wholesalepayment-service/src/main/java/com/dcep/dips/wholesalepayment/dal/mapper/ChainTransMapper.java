package com.dcep.dips.wholesalepayment.dal.mapper;

import com.dcep.dips.wholesalepayment.dal.model.ChainTransDO;
import org.apache.ibatis.annotations.Mapper;

public interface ChainTransMapper {
    int deleteByPrimaryKey(String outMsgId);

    int insert(ChainTransDO record);

    ChainTransDO selectByPrimaryKey(String outMsgId);

    int updateByPrimaryKey(ChainTransDO record);

    ChainTransDO selectByMsgId(String msgId);
}
