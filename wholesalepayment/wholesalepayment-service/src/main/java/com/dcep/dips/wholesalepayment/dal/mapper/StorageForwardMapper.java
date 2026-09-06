package com.dcep.dips.wholesalepayment.dal.mapper;

import com.dcep.dips.wholesalepayment.dal.model.StorageForwardDO;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface StorageForwardMapper {
    /**
     * 插入一条记录
     * @param storageForwardDO 存储转发数据对象
     * @return 影响的行数
     */
    int insert(StorageForwardDO storageForwardDO);

    /**
     * 根据主键查询记录
     * @param storageForwardDO 存储转发数据对象
     * @return 存储转发数据对象
     */
    StorageForwardDO selectByPrimaryKey(StorageForwardDO storageForwardDO);

    /**
     * 更新记录
     * @param storageForwardDO 存储转发数据对象
     * @return 影响的行数
     */
    int updateByPrimaryKey(StorageForwardDO storageForwardDO);

    /**
     * 删除记录
     * @param storageForwardDO 存储转发数据对象
     * @return 影响的行数
     */
    int deleteByPrimaryKey(StorageForwardDO storageForwardDO);
    /**
     * 更新记录，模拟删除，避免浪费测试数据
     * @return 影响的行数
     */
    int mockDelete(StorageForwardDO storageForwardDO);
    /**
     * 解锁记录
     * @param storageForwardDO 存储转发数据对象
     * @return 影响的行数
     */
    int updateForUnLock(StorageForwardDO storageForwardDO);

    /**
     * 查询记录
     * @return 结果集合
     */
    List<StorageForwardDO> selectForProcess(@Param("sysTime") Date sysTime, @Param("envInfo") String envInfo);

    /**
     * 更新记录，进行锁定
     * @return 影响的行数
     */
    int updateUuid(StorageForwardDO storageForwardDO);
    /**
     * 更新记录，进行解锁
     * @return 影响的行数
     */
    int updateUuidAndTime(StorageForwardDO storageForwardDO);

    /**
     * 查询未完成的最终转发任务
     *
     * @param sysTime 系统当前时间，用于判断任务是否超时
     * @param envInfo 环境信息，用于区分不同的运行环境
     * @return 返回未完成的转发任务列表
     */
    List<StorageForwardDO> selectUnFinishedForFinal(@Param("sysTime") Date sysTime, @Param("envInfo") String envInfo);


}
