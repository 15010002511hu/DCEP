package com.dcep.dips.wholesalepayment.batch;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.dcep.dips.wholesalepayment.common.utils.CommonUtil;
import com.dcep.dips.wholesalepayment.enums.ClearingSwitchEnum;
import com.dcep.dips.wholesalepayment.manager.CommonStsctrlManager;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonStsctrScheduledService {
	
	//交易超时补偿 “stsctrl-task” logger
    Logger logger = LoggerFactory.getLogger("stsctrl-task");
	@NacosValue(value = "${async_redo_switch}", autoRefreshed = true)
	String ASYNC_REDO_SWITCH;// 异步处理任务开关，0 开 1关
	@Resource
	private CommonStsctrlManager stsCtrlManager;

	public CommonStsctrScheduledService() {
		// 应用启动初始延迟时间
		final long initialDelay = CommonUtil.RANDOM.nextInt(10)+5;
		// 定时任务执行间隔延迟时间
		final long processDelay = 8L;

		ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(1, r -> {
			Thread t = new Thread(r);
			t.setName("commonStsctrScheduledService");
			t.setDaemon(true);
			return t;
		});

		scheduler.scheduleAtFixedRate(this::process, initialDelay, processDelay, TimeUnit.SECONDS);
	}

	public void process() {
		try {
			if (!ClearingSwitchEnum.OPEN.getCode().equals(ASYNC_REDO_SWITCH)) {
				logger.info("async redo process switch close...");
				return;
			}

			logger.info("commonStsctrScheduledService start");
			stsCtrlManager.processAll();

		} catch (Exception e) {
			logger.error("commonStsctrScheduledService process Exception:", e);
		}
	}
}
