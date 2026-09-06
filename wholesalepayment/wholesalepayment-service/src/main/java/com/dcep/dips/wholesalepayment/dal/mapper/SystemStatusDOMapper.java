package com.dcep.dips.wholesalepayment.dal.mapper;

import com.dcep.dips.wholesalepayment.dal.model.SystemStatusDO;
import java.util.List;

public interface SystemStatusDOMapper {

    int deleteByPrimaryKey(String sysCode);

    int insert(SystemStatusDO record);

    SystemStatusDO selectByPrimaryKey(String sysCode);

    List<SystemStatusDO> selectAll();

    int updateByPrimaryKey(SystemStatusDO record);

    String selectCurSysDt(String sysCd);
}