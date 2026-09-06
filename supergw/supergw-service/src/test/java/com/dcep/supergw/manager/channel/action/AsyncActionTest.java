package com.dcep.supergw.manager.channel.action;

import com.dcep.supergw.common.exception.GwException;
import com.dcep.supergw.manager.bo.ChannelContext;
import java.util.concurrent.TimeUnit;

public class AsyncActionTest extends AbstractAction {

    public AsyncActionTest(String actionName) {
        super(actionName);
    }

    @Override
    public void doInvoke(ChannelContext context) {
        // 同步执行业务
        System.out.println(super.getActionName() + " invoked at " + Thread.currentThread().getName());
        try {
            TimeUnit.MICROSECONDS.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(super.getActionName() + " completed at " + Thread.currentThread().getName());

        // 异步执行回调
    	/*CompletableFuture.runAsync(()->{
    		context.fireInvokeAction();
    	}, context.getChannel().getPool());*/
        context.fireInvokeChannel();

        //throw new GwException("test");
    }

    @Override
    public void doException(ChannelContext context, GwException e) {
        System.out.println(super.getActionName() + " exceptional at " + Thread.currentThread().getName());
        context.fireInvokeCallBack();
    }
}
