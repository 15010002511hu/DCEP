package com.dcep.supergw.manager.channel.action;

import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.infocache.manager.NacosConsume;
import com.dcep.supergw.common.constant.Constant;
import com.dcep.supergw.common.exception.GwException;
import com.dcep.supergw.common.utils.ChannelContextMocker;
import com.dcep.supergw.common.utils.InfoCacheUtils;
import com.dcep.supergw.common.utils.LoggerUtils;
import com.dcep.supergw.common.utils.SoapUtils;
import com.dcep.supergw.common.utils.TestMsgUtils;
import com.dcep.supergw.common.utils.ValidateUtils;
import com.dcep.supergw.manager.bo.ChannelContext;
import com.dcep.supergw.manager.https.HttpsAsyncClient;
import com.dcep.supergw.manager.https.HttpsAsyncClientFactory;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Locale;
import java.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;
import mockit.Expectations;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.nio.protocol.HttpAsyncRequestProducer;
import org.apache.http.nio.protocol.HttpAsyncResponseConsumer;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.Logger;

@RunWith(JMockit.class)
@Slf4j
public class AsyncHttpsPostActionTest {

    @Tested
    private AsyncHttpsPostAction asyncPost;

    @Mocked
    HttpsAsyncClient client;

    @Mocked
    ValidateUtils validateUtils;

    @Mocked
    InfoCacheUtils infoCacheUtils;

    @Mocked
    EntityUtils entityUtils;

    @Test
    public void test_asyncHttpsPostAction_doCallBack_succ() {
        ChannelContext context = ChannelContextMocker.newChannelContext(TestMsgUtils.dcep_201());
        new MockUp<SoapUtils>(SoapUtils.class) {
            @Mock
            public <T extends GwDTO> EnvelopeDTO<T> toDto(SoapHeader header, byte[] xml) {
                return null;
            }
        };
        asyncPost.doCallBack(context);
    }

    @Test(expected = Exception.class)
    public void test_asyncHttpsPostAction_doInvoke_failed() {
        ChannelContext context = ChannelContextMocker.newChannelContext(TestMsgUtils.dcep_201());
        context.setAttachment(Constant.DESERIALIZATION, null);
        HttpsAsyncClient client = Mockito.mock(HttpsAsyncClient.class);

        new MockUp<HttpsAsyncClientFactory>(HttpsAsyncClientFactory.class) {
            @Mock
            HttpsAsyncClient getInstance() {
                return client;
            }

        };
        asyncPost.doInvoke(context);
    }

    @Test
    public void test_asyncHttpsPostAction_doException_succ() {
        ChannelContext context = ChannelContextMocker.newChannelContext(TestMsgUtils.dcep_201());
        context.setRequest(false);
        asyncPost.doException(context, new GwException("unittest"));
        context.setRequest(true);
        asyncPost.doException(context, new GwException("unittest"));
        context.setAttachment("respmsg", "123");

        asyncPost.doException(context, new GwException("unittest"));

        context.isRequest();
    }

    @Test
    public void test_httpsCallBack_completed_statuscode_200() {
        ChannelContext context = ChannelContextMocker.newChannelContext(TestMsgUtils.dcep_201());
        HttpPost post = new HttpPost("https://0.0.0.0/unittest");

        AsyncHttpsPostAction.HttpsCallBack asyncPostCallBack = asyncPost.new HttpsCallBack(context, post);
        asyncPostCallBack.completed(new HttpResponse() {

            @Override
            public ProtocolVersion getProtocolVersion() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public boolean containsHeader(String name) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public Header[] getHeaders(String name) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Header getFirstHeader(String name) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Header getLastHeader(String name) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Header[] getAllHeaders() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void addHeader(Header header) {
                // TODO Auto-generated method stub

            }

            @Override
            public void addHeader(String name, String value) {
                // TODO Auto-generated method stub

            }

            @Override
            public void setHeader(Header header) {
                // TODO Auto-generated method stub

            }

            @Override
            public void setHeader(String name, String value) {
                // TODO Auto-generated method stub

            }

            @Override
            public void setHeaders(Header[] headers) {
                // TODO Auto-generated method stub

            }

            @Override
            public void removeHeader(Header header) {
                // TODO Auto-generated method stub

            }

            @Override
            public void removeHeaders(String name) {
                // TODO Auto-generated method stub

            }

            @Override
            public HeaderIterator headerIterator() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public HeaderIterator headerIterator(String name) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public HttpParams getParams() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void setParams(HttpParams params) {
                // TODO Auto-generated method stub

            }

            @Override
            public StatusLine getStatusLine() {
                return new StatusLine() {

                    @Override
                    public ProtocolVersion getProtocolVersion() {
                        // TODO Auto-generated method stub
                        return null;
                    }

                    @Override
                    public int getStatusCode() {
                        return HttpStatus.SC_OK;
                    }

                    @Override
                    public String getReasonPhrase() {
                        // TODO Auto-generated method stub
                        return null;
                    }

                };
            }

            @Override
            public void setStatusLine(StatusLine statusline) {
                // TODO Auto-generated method stub

            }

            @Override
            public void setStatusLine(ProtocolVersion ver, int code) {
                // TODO Auto-generated method stub

            }

            @Override
            public void setStatusLine(ProtocolVersion ver, int code, String reason) {
                // TODO Auto-generated method stub

            }

            @Override
            public void setStatusCode(int code) throws IllegalStateException {
                // TODO Auto-generated method stub

            }

            @Override
            public void setReasonPhrase(String reason) throws IllegalStateException {
                // TODO Auto-generated method stub

            }

            @Override
            public HttpEntity getEntity() {
                // TODO Auto-generated method stub
                return new BasicHttpEntity();
            }

            @Override
            public void setEntity(HttpEntity entity) {
                // TODO Auto-generated method stub

            }

            @Override
            public Locale getLocale() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void setLocale(Locale loc) {
                // TODO Auto-generated method stub

            }

        });

        asyncPostCallBack.completed(new HttpResponse() {

            @Override
            public ProtocolVersion getProtocolVersion() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public boolean containsHeader(String name) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public Header[] getHeaders(String name) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Header getFirstHeader(String name) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Header getLastHeader(String name) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Header[] getAllHeaders() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void addHeader(Header header) {
                // TODO Auto-generated method stub

            }

            @Override
            public void addHeader(String name, String value) {
                // TODO Auto-generated method stub

            }

            @Override
            public void setHeader(Header header) {
                // TODO Auto-generated method stub

            }

            @Override
            public void setHeader(String name, String value) {
                // TODO Auto-generated method stub

            }

            @Override
            public void setHeaders(Header[] headers) {
                // TODO Auto-generated method stub

            }

            @Override
            public void removeHeader(Header header) {
                // TODO Auto-generated method stub

            }

            @Override
            public void removeHeaders(String name) {
                // TODO Auto-generated method stub

            }

            @Override
            public HeaderIterator headerIterator() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public HeaderIterator headerIterator(String name) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public HttpParams getParams() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void setParams(HttpParams params) {
                // TODO Auto-generated method stub

            }

            @Override
            public StatusLine getStatusLine() {
                return new StatusLine() {

                    @Override
                    public ProtocolVersion getProtocolVersion() {
                        // TODO Auto-generated method stub
                        return null;
                    }

                    @Override
                    public int getStatusCode() {
                        return HttpStatus.SC_OK;
                    }

                    @Override
                    public String getReasonPhrase() {
                        // TODO Auto-generated method stub
                        return null;
                    }

                };
            }

            @Override
            public void setStatusLine(StatusLine statusline) {
                // TODO Auto-generated method stub

            }

            @Override
            public void setStatusLine(ProtocolVersion ver, int code) {
                // TODO Auto-generated method stub

            }

            @Override
            public void setStatusLine(ProtocolVersion ver, int code, String reason) {
                // TODO Auto-generated method stub

            }

            @Override
            public void setStatusCode(int code) throws IllegalStateException {
                // TODO Auto-generated method stub

            }

            @Override
            public void setReasonPhrase(String reason) throws IllegalStateException {
                // TODO Auto-generated method stub

            }

            @Override
            public HttpEntity getEntity() {
                // TODO Auto-generated method stub
                return new BasicHttpEntity();
            }

            @Override
            public void setEntity(HttpEntity entity) {
                // TODO Auto-generated method stub

            }

            @Override
            public Locale getLocale() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void setLocale(Locale loc) {
                // TODO Auto-generated method stub

            }

        });
    }

    @Test
    public void test_httpsCallBack_completed_statuscode_2002() {
        ChannelContext context = ChannelContextMocker.newChannelContext(TestMsgUtils.dcep_201());
        HttpPost post = new HttpPost("https://0.0.0.0/unittest");

        new Expectations() {
            {
                NacosConsume.getIsSign();
                result = true;
            }
        };
        AsyncHttpsPostAction.HttpsCallBack asyncPostCallBack = asyncPost.new HttpsCallBack(context, post);

        asyncPostCallBack.completed(new HttpResponse() {

            @Override
            public ProtocolVersion getProtocolVersion() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public boolean containsHeader(String name) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public Header[] getHeaders(String name) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Header getFirstHeader(String name) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Header getLastHeader(String name) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Header[] getAllHeaders() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void addHeader(Header header) {
                // TODO Auto-generated method stub

            }

            @Override
            public void addHeader(String name, String value) {
                // TODO Auto-generated method stub

            }

            @Override
            public void setHeader(Header header) {
                // TODO Auto-generated method stub

            }

            @Override
            public void setHeader(String name, String value) {
                // TODO Auto-generated method stub

            }

            @Override
            public void setHeaders(Header[] headers) {
                // TODO Auto-generated method stub

            }

            @Override
            public void removeHeader(Header header) {
                // TODO Auto-generated method stub

            }

            @Override
            public void removeHeaders(String name) {
                // TODO Auto-generated method stub

            }

            @Override
            public HeaderIterator headerIterator() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public HeaderIterator headerIterator(String name) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public HttpParams getParams() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void setParams(HttpParams params) {
                // TODO Auto-generated method stub

            }

            @Override
            public StatusLine getStatusLine() {
                return new StatusLine() {

                    @Override
                    public ProtocolVersion getProtocolVersion() {
                        // TODO Auto-generated method stub
                        return null;
                    }

                    @Override
                    public int getStatusCode() {
                        return HttpStatus.SC_OK;
                    }

                    @Override
                    public String getReasonPhrase() {
                        // TODO Auto-generated method stub
                        return null;
                    }

                };
            }

            @Override
            public void setStatusLine(StatusLine statusline) {
                // TODO Auto-generated method stub

            }

            @Override
            public void setStatusLine(ProtocolVersion ver, int code) {
                // TODO Auto-generated method stub

            }

            @Override
            public void setStatusLine(ProtocolVersion ver, int code, String reason) {
                // TODO Auto-generated method stub

            }

            @Override
            public void setStatusCode(int code) throws IllegalStateException {
                // TODO Auto-generated method stub

            }

            @Override
            public void setReasonPhrase(String reason) throws IllegalStateException {
                // TODO Auto-generated method stub

            }

            @Override
            public HttpEntity getEntity() {
                // TODO Auto-generated method stub
                return new BasicHttpEntity();
            }

            @Override
            public void setEntity(HttpEntity entity) {
                // TODO Auto-generated method stub

            }

            @Override
            public Locale getLocale() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void setLocale(Locale loc) {
                // TODO Auto-generated method stub

            }

        });

        asyncPostCallBack.completed(new HttpResponse() {

            @Override
            public ProtocolVersion getProtocolVersion() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public boolean containsHeader(String name) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public Header[] getHeaders(String name) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Header getFirstHeader(String name) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Header getLastHeader(String name) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Header[] getAllHeaders() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void addHeader(Header header) {
                // TODO Auto-generated method stub

            }

            @Override
            public void addHeader(String name, String value) {
                // TODO Auto-generated method stub

            }

            @Override
            public void setHeader(Header header) {
                // TODO Auto-generated method stub

            }

            @Override
            public void setHeader(String name, String value) {
                // TODO Auto-generated method stub

            }

            @Override
            public void setHeaders(Header[] headers) {
                // TODO Auto-generated method stub

            }

            @Override
            public void removeHeader(Header header) {
                // TODO Auto-generated method stub

            }

            @Override
            public void removeHeaders(String name) {
                // TODO Auto-generated method stub

            }

            @Override
            public HeaderIterator headerIterator() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public HeaderIterator headerIterator(String name) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public HttpParams getParams() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void setParams(HttpParams params) {
                // TODO Auto-generated method stub

            }

            @Override
            public StatusLine getStatusLine() {
                return new StatusLine() {

                    @Override
                    public ProtocolVersion getProtocolVersion() {
                        // TODO Auto-generated method stub
                        return null;
                    }

                    @Override
                    public int getStatusCode() {
                        return HttpStatus.SC_OK;
                    }

                    @Override
                    public String getReasonPhrase() {
                        // TODO Auto-generated method stub
                        return null;
                    }

                };
            }

            @Override
            public void setStatusLine(StatusLine statusline) {
                // TODO Auto-generated method stub

            }

            @Override
            public void setStatusLine(ProtocolVersion ver, int code) {
                // TODO Auto-generated method stub

            }

            @Override
            public void setStatusLine(ProtocolVersion ver, int code, String reason) {
                // TODO Auto-generated method stub

            }

            @Override
            public void setStatusCode(int code) throws IllegalStateException {
                // TODO Auto-generated method stub

            }

            @Override
            public void setReasonPhrase(String reason) throws IllegalStateException {
                // TODO Auto-generated method stub

            }

            @Override
            public HttpEntity getEntity() {
                // TODO Auto-generated method stub
                return new BasicHttpEntity();
            }

            @Override
            public void setEntity(HttpEntity entity) {
                // TODO Auto-generated method stub

            }

            @Override
            public Locale getLocale() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void setLocale(Locale loc) {
                // TODO Auto-generated method stub

            }

        });
    }

    private static void mockNacosConsumeReturnTrue()
        throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Class<NacosConsume> NacosConsumeClass = NacosConsume.class;
        Field isSignField = NacosConsumeClass.getDeclaredField("isSign");
        Field isEncryptField = NacosConsumeClass.getDeclaredField("isEncrypt");
        isSignField.setAccessible(true);
        isEncryptField.setAccessible(true);
        isSignField.set(null, "true");
        isEncryptField.set(null, "true");
    }

    @Test
    public void test_httpsCallBack_completed_statuscode_500()
        throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        ChannelContext context = ChannelContextMocker.newChannelContext(TestMsgUtils.dcep_201());
        HttpPost post = new HttpPost("https://0.0.0.0/unittest");
        mockNacosConsumeReturnTrue();
        AsyncHttpsPostAction.HttpsCallBack asyncPostCallBack = asyncPost.new HttpsCallBack(context, post);
        asyncPostCallBack.completed(new HttpResponse() {

            @Override
            public ProtocolVersion getProtocolVersion() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public boolean containsHeader(String name) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public Header[] getHeaders(String name) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Header getFirstHeader(String name) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Header getLastHeader(String name) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Header[] getAllHeaders() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void addHeader(Header header) {
                // TODO Auto-generated method stub

            }

            @Override
            public void addHeader(String name, String value) {
                // TODO Auto-generated method stub

            }

            @Override
            public void setHeader(Header header) {
                // TODO Auto-generated method stub

            }

            @Override
            public void setHeader(String name, String value) {
                // TODO Auto-generated method stub

            }

            @Override
            public void setHeaders(Header[] headers) {
                // TODO Auto-generated method stub

            }

            @Override
            public void removeHeader(Header header) {
                // TODO Auto-generated method stub

            }

            @Override
            public void removeHeaders(String name) {
                // TODO Auto-generated method stub

            }

            @Override
            public HeaderIterator headerIterator() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public HeaderIterator headerIterator(String name) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public HttpParams getParams() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void setParams(HttpParams params) {
                // TODO Auto-generated method stub

            }

            @Override
            public StatusLine getStatusLine() {
                return new StatusLine() {

                    @Override
                    public ProtocolVersion getProtocolVersion() {
                        // TODO Auto-generated method stub
                        return null;
                    }

                    @Override
                    public int getStatusCode() {
                        return 500;
                    }

                    @Override
                    public String getReasonPhrase() {
                        // TODO Auto-generated method stub
                        return null;
                    }

                };
            }

            @Override
            public void setStatusLine(StatusLine statusline) {
                // TODO Auto-generated method stub

            }

            @Override
            public void setStatusLine(ProtocolVersion ver, int code) {
                // TODO Auto-generated method stub

            }

            @Override
            public void setStatusLine(ProtocolVersion ver, int code, String reason) {
                // TODO Auto-generated method stub

            }

            @Override
            public void setStatusCode(int code) throws IllegalStateException {
                // TODO Auto-generated method stub

            }

            @Override
            public void setReasonPhrase(String reason) throws IllegalStateException {
                // TODO Auto-generated method stub

            }

            @Override
            public HttpEntity getEntity() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void setEntity(HttpEntity entity) {
                // TODO Auto-generated method stub

            }

            @Override
            public Locale getLocale() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void setLocale(Locale loc) {
                // TODO Auto-generated method stub

            }

        });
    }

    @Test
    public void test_httpsCallBack_completed_statuscode_excp()
        throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        ChannelContext context = ChannelContextMocker.newChannelContext(TestMsgUtils.dcep_201());
        HttpPost post = new HttpPost("https://0.0.0.0/unittest");
        mockNacosConsumeReturnTrue();

        new MockUp<NacosConsume>(NacosConsume.class) {
            @Mock
            public boolean getIsSign() throws IOException {
                throw new IOException();
            }
        };

        AsyncHttpsPostAction.HttpsCallBack asyncPostCallBack = asyncPost.new HttpsCallBack(context, post);
        asyncPostCallBack.completed(new HttpResponse() {

            @Override
            public ProtocolVersion getProtocolVersion() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public boolean containsHeader(String name) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public Header[] getHeaders(String name) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Header getFirstHeader(String name) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Header getLastHeader(String name) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Header[] getAllHeaders() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void addHeader(Header header) {
                // TODO Auto-generated method stub

            }

            @Override
            public void addHeader(String name, String value) {
                // TODO Auto-generated method stub

            }

            @Override
            public void setHeader(Header header) {
                // TODO Auto-generated method stub

            }

            @Override
            public void setHeader(String name, String value) {
                // TODO Auto-generated method stub

            }

            @Override
            public void setHeaders(Header[] headers) {
                // TODO Auto-generated method stub

            }

            @Override
            public void removeHeader(Header header) {
                // TODO Auto-generated method stub

            }

            @Override
            public void removeHeaders(String name) {
                // TODO Auto-generated method stub

            }

            @Override
            public HeaderIterator headerIterator() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public HeaderIterator headerIterator(String name) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public HttpParams getParams() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void setParams(HttpParams params) {
                // TODO Auto-generated method stub

            }

            @Override
            public StatusLine getStatusLine() {
                return new StatusLine() {

                    @Override
                    public ProtocolVersion getProtocolVersion() {
                        // TODO Auto-generated method stub
                        return null;
                    }

                    @Override
                    public int getStatusCode() {
                        return HttpStatus.SC_OK;
                    }

                    @Override
                    public String getReasonPhrase() {
                        // TODO Auto-generated method stub
                        return null;
                    }

                };
            }

            @Override
            public void setStatusLine(StatusLine statusline) {
                // TODO Auto-generated method stub

            }

            @Override
            public void setStatusLine(ProtocolVersion ver, int code) {
                // TODO Auto-generated method stub

            }

            @Override
            public void setStatusLine(ProtocolVersion ver, int code, String reason) {
                // TODO Auto-generated method stub

            }

            @Override
            public void setStatusCode(int code) throws IllegalStateException {
                // TODO Auto-generated method stub

            }

            @Override
            public void setReasonPhrase(String reason) throws IllegalStateException {
                // TODO Auto-generated method stub

            }

            @Override
            public HttpEntity getEntity() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void setEntity(HttpEntity entity) {
                // TODO Auto-generated method stub

            }

            @Override
            public Locale getLocale() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void setLocale(Locale loc) {
                // TODO Auto-generated method stub

            }

        });
    }

    @Test
    public void test_httpsCallBack_completed_statuscode_excp2()
        throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        ChannelContext context = ChannelContextMocker.newChannelContext(TestMsgUtils.dcep_201());
        HttpPost post = new HttpPost("https://0.0.0.0/unittest");
        mockNacosConsumeReturnTrue();

        new MockUp<NacosConsume>(NacosConsume.class) {
            @Mock
            public boolean getIsSign() throws Exception {
                throw new Exception();
            }
        };

        AsyncHttpsPostAction.HttpsCallBack asyncPostCallBack = asyncPost.new HttpsCallBack(context, post);
        asyncPostCallBack.completed(new HttpResponse() {

            @Override
            public ProtocolVersion getProtocolVersion() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public boolean containsHeader(String name) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public Header[] getHeaders(String name) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Header getFirstHeader(String name) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Header getLastHeader(String name) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Header[] getAllHeaders() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void addHeader(Header header) {
                // TODO Auto-generated method stub

            }

            @Override
            public void addHeader(String name, String value) {
                // TODO Auto-generated method stub

            }

            @Override
            public void setHeader(Header header) {
                // TODO Auto-generated method stub

            }

            @Override
            public void setHeader(String name, String value) {
                // TODO Auto-generated method stub

            }

            @Override
            public void setHeaders(Header[] headers) {
                // TODO Auto-generated method stub

            }

            @Override
            public void removeHeader(Header header) {
                // TODO Auto-generated method stub

            }

            @Override
            public void removeHeaders(String name) {
                // TODO Auto-generated method stub

            }

            @Override
            public HeaderIterator headerIterator() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public HeaderIterator headerIterator(String name) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public HttpParams getParams() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void setParams(HttpParams params) {
                // TODO Auto-generated method stub

            }

            @Override
            public StatusLine getStatusLine() {
                return new StatusLine() {

                    @Override
                    public ProtocolVersion getProtocolVersion() {
                        // TODO Auto-generated method stub
                        return null;
                    }

                    @Override
                    public int getStatusCode() {
                        return HttpStatus.SC_OK;
                    }

                    @Override
                    public String getReasonPhrase() {
                        // TODO Auto-generated method stub
                        return null;
                    }

                };
            }

            @Override
            public void setStatusLine(StatusLine statusline) {
                // TODO Auto-generated method stub

            }

            @Override
            public void setStatusLine(ProtocolVersion ver, int code) {
                // TODO Auto-generated method stub

            }

            @Override
            public void setStatusLine(ProtocolVersion ver, int code, String reason) {
                // TODO Auto-generated method stub

            }

            @Override
            public void setStatusCode(int code) throws IllegalStateException {
                // TODO Auto-generated method stub

            }

            @Override
            public void setReasonPhrase(String reason) throws IllegalStateException {
                // TODO Auto-generated method stub

            }

            @Override
            public HttpEntity getEntity() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void setEntity(HttpEntity entity) {
                // TODO Auto-generated method stub

            }

            @Override
            public Locale getLocale() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void setLocale(Locale loc) {
                // TODO Auto-generated method stub

            }

        });
    }

    @Test
    public void test_httpsCallBack_failed() {
        ChannelContext context = ChannelContextMocker.newChannelContext(TestMsgUtils.dcep_201());
        HttpPost post = new HttpPost("https://0.0.0.0/unittest");

        AsyncHttpsPostAction.HttpsCallBack asyncPostCallBack = asyncPost.new HttpsCallBack(context, post);
        asyncPostCallBack.failed(new Exception("unittest"));
    }

    @Test
    public void test_httpsCallBack_cancelled() {
        ChannelContext context = ChannelContextMocker.newChannelContext(TestMsgUtils.dcep_201());
        HttpPost post = new HttpPost("https://0.0.0.0/unittest");

        AsyncHttpsPostAction.HttpsCallBack asyncPostCallBack = asyncPost.new HttpsCallBack(context, post);
        asyncPostCallBack.cancelled();
    }

    @BeforeClass
    public static void mock_dependency_before_test() {
        new MockUp<InfoCacheUtils>(InfoCacheUtils.class) {
            @Mock
            public String getInstUrl(String instId) {
                return "https://0.0.0.0/unittest";
            }

            @Mock
            public String getPbocCertDnOrNickname() {
                return "";
            }

            @Mock
            public String getInstCert(String inst) {
                return "";
            }

            @Mock
            public String getPbocInf() {
                return "00000000000000";
            }
        };

        new MockUp<HttpsAsyncClient>(HttpsAsyncClient.class) {

            private CloseableHttpAsyncClient mockClient = new CloseableHttpAsyncClient() {

                @Override
                public <T> Future<T> execute(HttpAsyncRequestProducer requestProducer,
                    HttpAsyncResponseConsumer<T> responseConsumer, HttpContext context,
                    FutureCallback<T> callback) {
                    log.info("execute mock CloseableHttpAsyncClient.execute()");
                    return null;
                }

                @Override
                public void close() throws IOException {
                    log.info("execute mock CloseableHttpAsyncClient.close()");
                }

                @Override
                public boolean isRunning() {
                    log.info("execute mock CloseableHttpAsyncClient.isRunning()");
                    return true;
                }

                @Override
                public void start() {
                    log.info("execute mock CloseableHttpAsyncClient.start()");
                }
            };
            ;

            @Mock
            public void $init() {
                log.info("execute mock HttpsAsyncClient.$init()");
            }

            @Mock
            public CloseableHttpAsyncClient getClient() {
                log.info("execute mock HttpsAsyncClient.getClient()");
                return mockClient;
            }

            @Mock
            public Future<HttpResponse> post(HttpPost request, FutureCallback<HttpResponse> callback) {
                log.info("execute mock HttpsAsyncClient.post()");
                return mockClient.execute(request, callback);
            }
        };

        new MockUp<LoggerUtils>(LoggerUtils.class) {
            @Mock
            public Logger getInstLogger(String instId) {
                return log;
            }

            @Mock
            public void MDC(SoapHeader header) {
                log.info("execute mock LoggerUtils.MDC()");
            }
        };

        new MockUp<EntityUtils>(EntityUtils.class) {
            @Mock
            public String toString(final HttpEntity entity, final String defaultCharset)
                throws IOException, ParseException {
                return TestMsgUtils.dcep_202();
            }
        };

        new MockUp<NacosConsume>(NacosConsume.class) {
            @Mock
            public boolean getIsSign() {
                return false;
            }
        };

        new MockUp<NacosConsume>(NacosConsume.class) {
            @Mock
            public boolean getIsEncrypt() {
                return false;
            }
        };

        new MockUp<ValidateUtils>(ValidateUtils.class) {
            @Mock
            public void check(EnvelopeDTO envelopeDTO) {
            }
        };
    }
}
