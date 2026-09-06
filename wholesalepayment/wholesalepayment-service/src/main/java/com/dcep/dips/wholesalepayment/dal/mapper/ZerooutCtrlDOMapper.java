package com.dcep.dips.wholesalepayment.dal.mapper;

import com.dcep.dips.wholesalepayment.dal.model.ZerooutCtrlDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ZerooutCtrlDOMapper {

    int deleteByPrimaryKey(String msgId);

    int insert(ZerooutCtrlDO record);

    ZerooutCtrlDO selectByPrimaryKey(String msgId);

    ZerooutCtrlDO selectByTaskId(String msgId);

    ZerooutCtrlDO selectBySysDtId(String systd,String id);

    List<ZerooutCtrlDO> selectAll();

    int updateByPrimaryKey(ZerooutCtrlDO record);

    ZerooutCtrlDO selecPrcSts(@Param("sysDt") String sysDt, @Param("sysId") String sysId);
}