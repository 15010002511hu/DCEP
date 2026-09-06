package com.dcep.dips.wholesalepayment.batch;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.dcep.dips.wholesalepayment.common.utils.CommonUtil;
import com.dcep.dips.wholesalepayment.enums.ClearingSwitchEnum;
import com.dcep.dips.wholesalepayment.manager.StorageForwardAsyncProcessManager;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageForwardScheduledService {
	
	//存储转发 “asyncProcess-task” logger
	Logger logger = LoggerFactory.getLogger("asyncProcess-task");

    @NacosValue(value = "${async_process_switch}", autoRefreshed = true)
	String ASYNC_PROCESS_SWITCH;// 异步处理任务开关，0 开 1关
	
    @Resource
	StorageForwardAsyncProcessManager storageForwardAsyncProcessManager;

	public StorageForwardScheduledService() {
		// 应用启动初始延迟时间
		final long initialDelay = CommonUtil.RANDOM.nextInt(10);
		// 定时任务执行间隔延迟时间
		final long processDelay = 6L;

		ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(1, r -> {
			Thread t = new Thread(r);
			t.setName("storageForwardScheduledService");
			t.setDaemon(true);
			return t;
		});

		scheduler.scheduleAtFixedRate(this::process, initialDelay, processDelay, TimeUnit.SECONDS);
	}

	public void process() {
		try {
			if (!ClearingSwitchEnum.OPEN.getCode().equals(ASYNC_PROCESS_SWITCH)) {
				logger.info("async process switch close...");
				return;
			}

			// 执行任务
			logger.info("storageForwardScheduledService start");
			storageForwardAsyncProcessManager.operationCtr();

			// 异常情况（系统宕机等）补偿机制 todo 后续补充，先跑正向流程
			// storageForwardAsyncProcessManager.compensation();

		} catch (Exception e) {
			logger.error("storageForwardScheduledService process Exception:", e);
		}
	}
}
