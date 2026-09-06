package com.dcep.supergw.core.http;

import com.alibaba.csp.sentinel.AsyncEntry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.dcep.common.exception.DcepException;
import org.apache.http.HttpResponse;
import org.apache.http.concurrent.FutureCallback;

/**
 * @author : maxinyu
 * @version : DcepHttpCallBack.java v 0.1 2021-04-22
 * @description : 包装FutureCallback
 */
public class DcepHttpCallBack implements FutureCallback<HttpResponse> {

    private static final String ERRORCODE = "DCEPS0004";
    private static final String ERRORMSG = "接收方机构故障";

    private final FutureCallback<HttpResponse> callback;
    private AsyncEntry asyncEntry;

    public DcepHttpCallBack(FutureCallback<HttpResponse> callback, String name) throws BlockException {
        this.callback = callback;
        try {
            //初始化asyncEntry，若捕获BlockException异常，抛出DCEPS9003
            asyncEntry = SphU.asyncEntry(name);
        } catch (BlockException e) {
            throw new DcepException(ERRORCODE, ERRORMSG, e);
        }
    }


    @Override
    public void completed(HttpResponse result) {
        try {
            //执行completed逻辑，返回机构应答信息
            callback.completed(result);
        } finally {
            //释放entry
            if (null != this.asyncEntry) {
                asyncEntry.exit();
            }
        }
    }

    @Override
    public void failed(Exception ex) {
        try {
            //对异常进行计数
            Tracer.traceEntry(ex, asyncEntry);
            //执行failed逻辑，返回911报文
            callback.failed(ex);
        } finally {
            //释放entry
            if (null != this.asyncEntry) {
                asyncEntry.exit();
            }
        }

    }

    @Override
    public void cancelled() {
        try {
            //执行canceled方法，返回911报文
            callback.cancelled();
        } finally {
            //释放entry
            if (null != this.asyncEntry) {
                asyncEntry.exit();
            }
        }
    }
}
