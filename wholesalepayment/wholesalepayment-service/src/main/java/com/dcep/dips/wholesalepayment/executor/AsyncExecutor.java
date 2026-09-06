package com.dcep.dips.wholesalepayment.executor;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.dcep.common.utils.ThreadPoolUtils;
import com.dcepex.trace.support.async.TraceExecutorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;

@Configuration(proxyBeanMethods = false)
public class AsyncExecutor {

    @NacosValue(value = "${async_biz_threads}")
    String ASYNC_BIZ_THREADS; // 异步业务处理线程数

    @Bean(name = "asyncBizPool")
    public ExecutorService asyncBizPool() {
        int asyncBizThreads = Integer.parseInt(ASYNC_BIZ_THREADS);
        return new TraceExecutorService(ThreadPoolUtils.getThreadPoolFixSize(asyncBizThreads, "wholesale-async-biz"));
    }
}
