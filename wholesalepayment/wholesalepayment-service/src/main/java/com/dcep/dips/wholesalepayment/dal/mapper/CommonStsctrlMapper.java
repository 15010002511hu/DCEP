package com.dcep.dips.wholesalepayment.dal.mapper;

import com.dcep.dips.wholesalepayment.dal.model.CommonStsctrlDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface CommonStsctrlMapper {
    /**
     * 插入一条记录
     * @param commonStsctrlDO 通用状态控制数据对象
     * @return 影响的行数
     */
    int insert(CommonStsctrlDO commonStsctrlDO);

    /**
     * 根据主键查询记录
     * @param commonStsctrlDO 通用状态控制数据对象
     * @return
     */
    CommonStsctrlDO selectByPrimaryKey(CommonStsctrlDO commonStsctrlDO);

    /**
     * 更新记录
     * @param commonStsctrlDO 通用状态控制数据对象
     * @return 影响的行数
     */
    int updateByPrimaryKey(CommonStsctrlDO commonStsctrlDO);

    /**
     * 更新记录
     * @param commonStsctrlDO 通用状态控制数据对象
     * @return 影响的行数
     */
    int deleteByPrimaryKey(CommonStsctrlDO commonStsctrlDO);

    List<CommonStsctrlDO> selectBySystemDate(String currentSystemDate);

    /**
     * 待重试数据
     * @param sysTime
     * @param envInfo
     * @return
     */
    List<CommonStsctrlDO> selectForProcess(@Param("sysTime") Date sysTime, @Param("envInfo") String envInfo);

    /**
     * 超过notifytime依然处于加锁的数据
     * @param sysTime
     * @param envInfo
     * @return
     */
    List<CommonStsctrlDO> selectForProcessLocked(@Param("sysTime") Date sysTime, @Param("envInfo") String envInfo);

    /**
     * 加锁
     * @param asyncProcessDO
     * @return
     */
    int lock(CommonStsctrlDO asyncProcessDO);
    /**
     * 释放锁
     * @param asyncProcessDO
     * @return
     */
    int unLock(CommonStsctrlDO asyncProcessDO);

    int updateSendTime(CommonStsctrlDO commonStsctrlDO);
}
