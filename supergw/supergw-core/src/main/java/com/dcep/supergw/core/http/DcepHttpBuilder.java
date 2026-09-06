package com.dcep.supergw.core.http;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.dcep.common.exception.DcepException;
import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpExecutionAware;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.execchain.ClientExecChain;

/**
 * @author : maxinyu
 * @version : DcepHttpBuilder.java v 0.1 2021-04-19
 * @description : 同步HttpBuilder,此类包装了HttClientBuilder，重写了decorateMainExec方法。HttpClientBuilder中使用该方法初始化ClientExecChain对象
 * Http最终使用的是ClientExecChain.execute方法发送。详见@see{@link org.apache.http.impl.client.InternalHttpClient}
 */
public class DcepHttpBuilder extends HttpClientBuilder {

    private static final String ERRORCODE = "DCEPS0004";
    private static final String ERRORMSG = "接收方机构故障";

    @Override
    protected ClientExecChain decorateMainExec(ClientExecChain mainExec) {
        return new ClientExecChain() {
            @Override
            public CloseableHttpResponse execute(HttpRoute route, HttpRequestWrapper request, HttpClientContext clientContext, HttpExecutionAware execAware) throws IOException, HttpException {
                String resourceName = request.getTarget().getHostName() + "-" + request.getTarget().getPort();
                Entry entry = null;
                try {
                    //使用ip+port作为资源名称
                    entry = SphU.entry(resourceName, EntryType.OUT);
                    //执行Http框架中发送操作
                    return mainExec.execute(route, request, clientContext, execAware);
                } catch (BlockException e) {
                    //抛出业务异常
                    throw new DcepException(ERRORCODE, ERRORMSG, e);
                } catch (Throwable e) {
                    //对异常进行计数
                    Tracer.traceEntry(e, entry);
                    throw new DcepException(e.getMessage(), e);
                } finally {
                    if (entry != null) {
                        entry.exit();
                    }
                }
            }
        };
    }


}
