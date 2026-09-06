package com.dcep.supergw.core;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncListener;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * @author : maxinyu
 * @version : ServletChannelContext.java v 0.1 2020-11-16
 * @description :
 */
public class ServletChannelContext extends ChannelContext implements AsyncContext {

    private AsyncContext asyncContext;
    
    public ServletChannelContext() {
    }
    
    public ServletChannelContext(AsyncContext asyncContext) {
    	this.asyncContext = asyncContext;
    }


    public AsyncContext getAsyncContext() {
        return asyncContext;
    }

    public void setAsyncContext(AsyncContext asyncContext) {
        this.asyncContext = asyncContext;
    }

	@Override
	public ServletRequest getRequest() {
		return asyncContext.getRequest();
	}

	@Override
	public ServletResponse getResponse() {
		return asyncContext.getResponse();
	}

	@Override
	public boolean hasOriginalRequestAndResponse() {
		return asyncContext.hasOriginalRequestAndResponse();
	}

	@Override
	public void dispatch() {
		asyncContext.dispatch();
	}

	@Override
	public void dispatch(String path) {
		asyncContext.dispatch(path);
	}

	@Override
	public void dispatch(ServletContext context, String path) {
		asyncContext.dispatch(context, path);
	}

	@Override
	public void complete() {
		asyncContext.complete();
	}

	@Override
	public void start(Runnable run) {
		asyncContext.start(run);
	}

	@Override
	public void addListener(AsyncListener listener) {
		asyncContext.addListener(listener);
	}

	@Override
	public void addListener(AsyncListener listener, ServletRequest servletRequest, ServletResponse servletResponse) {
		asyncContext.addListener(listener, servletRequest, servletResponse);
	}

	@Override
	public <T extends AsyncListener> T createListener(Class<T> clazz) throws ServletException {
		return asyncContext.createListener(clazz);
	}

	@Override
	public void setTimeout(long timeout) {
		asyncContext.setTimeout(timeout);
	}

	@Override
	public long getTimeout() {
		return asyncContext.getTimeout();
	}
}
