package com.dcep.dips.wholesalepayment.dal.mapper;

import com.dcep.dips.wholesalepayment.dal.model.CommonRecordDO;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CommonRecordMapper {
    /**
     * 插入一条记录
     * @param commonRecordDO 通用记录数据对象
     * @return 影响的行数
     */
    int insert(CommonRecordDO commonRecordDO);

    /**
     * 根据主键查询记录
     * @param commonRecordDO 通用记录数据对象
     * @return 通用记录数据对象
     */
    CommonRecordDO selectByPrimaryKey(CommonRecordDO commonRecordDO);

    /**
     * 更新记录
     * @param commonRecordDO 通用记录数据对象
     * @return 影响的行数
     */
    int updateByPrimaryKey(CommonRecordDO commonRecordDO);
}
