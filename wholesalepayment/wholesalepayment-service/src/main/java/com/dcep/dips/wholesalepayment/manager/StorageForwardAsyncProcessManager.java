package com.dcep.dips.wholesalepayment.manager;

import com.dcep.dips.wholesalepayment.dal.model.StorageForwardDO;

/**
 * 
 * @author gcg
 * @version
 */
public interface StorageForwardAsyncProcessManager {

	/**
	 * 暴露给定时任务，开始执行查存储表，进行转发
	 *
	 * @return int 操作次数
	 */
	public int operationCtr();
	/**
	 * 执行补偿操作
	 *
	 * 说明：此方法用于在特定条件下执行补偿逻辑，以确保系统的稳定性和数据的一致性
	 */
	public void compensation();
	/**
	 * 处理存储转发对象
	 *
	 * @param storageForwardDO 存储转发对象，包含需要处理的数据
	 *
	 * 说明：此方法负责处理传入的存储转发对象，执行相应的业务逻辑
	 */
	public void process(StorageForwardDO storageForwardDO);


}
