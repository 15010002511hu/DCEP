package com.dcep.dips.wholesalepayment.dal.mapper;

import com.dcep.dips.wholesalepayment.dal.model.HvpsTransDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface HvpsTransMapper {
    /**
     * 根据主键查询《大额对接产品表》记录
     *
     * @param hvpsTransDO 包含主键信息的《大额对接产品表》记录
     * @return 查询到的《大额对接产品表》记录
     */
    HvpsTransDO selectByPrimaryKey(HvpsTransDO hvpsTransDO);

    /**
     * 根据msgId查询《大额对接产品表》记录
     *
     * @param msgId 批发报文标识号
     * @return 查询到的《大额对接产品表》记录
     */
    HvpsTransDO selectByMsgId(String msgId);

    /**
     * 插入新的《大额对接产品表》记录
     *
     * @param hvpsTransDO 要插入的《大额对接产品表》记录
     * @return 插入成功的记录数
     */
    int insert(HvpsTransDO hvpsTransDO);

    /**
     * 根据主键更新《大额对接产品表》记录
     *
     * @param hvpsTransDO 包含更新《大额对接产品表》记录
     * @return 更新成功的记录数
     */
    int updateByPrimaryKey(HvpsTransDO hvpsTransDO);

    int updateBizSts(@Param("orgnlHvpsTransDO") HvpsTransDO orgnlHvpsTransDO,
                     @Param("hvpsTransDO") HvpsTransDO hvpsTransDO);
}
