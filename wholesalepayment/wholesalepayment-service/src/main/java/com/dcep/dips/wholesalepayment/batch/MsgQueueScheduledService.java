package com.dcep.dips.wholesalepayment.batch;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.dcep.dips.wholesalepayment.common.utils.CommonUtil;
import com.dcep.dips.wholesalepayment.enums.ClearingSwitchEnum;
import com.dcep.dips.wholesalepayment.manager.MsgQueueManager;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MsgQueueScheduledService {

	//消息队列推送 “sendKafka-task” logger
	Logger logger = LoggerFactory.getLogger("sendKafka-task");

    @NacosValue(value = "${async_process_switch}", autoRefreshed = true)
	String ASYNC_PROCESS_SWITCH;// 异步处理任务开关，0 开 1关

    @Resource
	MsgQueueManager msgQueueManager;

	public MsgQueueScheduledService() {
		// 应用启动初始延迟时间
		final long initialDelay = CommonUtil.RANDOM.nextInt(10);
		// 定时任务执行间隔延迟时间
		final long processDelay = 8L;

		ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(1, r -> {
			Thread t = new Thread(r);
			t.setName("msgQueueScheduledService");
			t.setDaemon(true);
			return t;
		});

		scheduler.scheduleAtFixedRate(this::process, initialDelay, processDelay, TimeUnit.SECONDS);
	}

	public void process() {
		try {
			if (!ClearingSwitchEnum.OPEN.getCode().equals(ASYNC_PROCESS_SWITCH)) {
				logger.info("kafka send switch close...");
				return;
			}

			// 执行任务
			logger.info("msgQueueScheduledService start");
			msgQueueManager.sendMsg();

		} catch (Exception e) {
			logger.error("msgQueueScheduledService process Exception:", e);
		}
	}
}
