package com.dcep.supergw.manager.https;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.infocache.manager.NacosConsume;
import com.dcep.supergw.common.constant.Constant;
import com.dcep.supergw.common.enums.GwErrorEnum;
import com.dcep.supergw.common.exception.GwException;
import com.dcep.supergw.common.utils.InfoCacheUtils;
import com.dcep.supergw.common.utils.LoggerUtils;
import com.dcep.supergw.common.utils.SoapUtils;
import com.dcep.supergw.common.utils.ValidateUtils;
import java.io.IOException;

import com.dcepex.trace.support.TraceContext;
import com.dubbo.ldc.ZoneClient;
import com.dubbo.ldc.constant.EnvEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.util.Assert;

/**
 * @author linlu
 * @date 20190808 使用Apache的HttpClient实现的HTTPS连接
 */
@Slf4j
public class HttpsClient {

    private final static String HTTPS_PRE = "https";

    private final static String HTTP_PRE = "http";

    /**
     * 格式化URL
     */
    public static String formateUri(String url) {
        Assert.notNull(url, "URL为空");

        if (!url.startsWith(HTTPS_PRE) && !url.startsWith(HTTP_PRE)) {
            return HTTPS_PRE + "://" + url;
        }

        return url;
    }

    /**
     * Post连接设置
     */
    private static HttpPost getPost(String url, String content, String certId) {
        HttpPost post = new HttpPost(formateUri(url));
        //设置Header
        post.setHeader("Content-Type", "application/xml; charset=\"utf-8\"");
        post.setHeader("Signature", ValidateUtils.sign(content, certId));

        // 非生产环境设置环境位和流量分组信息
        String env = ZoneClient.getInstance().getEnv().name();
        if (StringUtils.isNotBlank(env)
                && StringUtils.equalsAnyIgnoreCase(env, EnvEnum.STABLE.name(), EnvEnum.DEV.name(), EnvEnum.SIT.name())) {
            // 首先放置流量分组信息
            log.info("================================================================");
            log.info("HttpsClient转发报文时DevGroup的值{}", TraceContext.get(Constant.HTTP_FLOW_GROUP));
            log.info("HttpsClient转发报文时Environment的值{}", TraceContext.get(Constant.HTTP_FLOW_ENV));
            log.info("================================================================");

            post.setHeader(Constant.HTTP_FLOW_GROUP, TraceContext.get(Constant.HTTP_FLOW_GROUP));
            post.setHeader(Constant.HTTP_FLOW_ENV, TraceContext.get(Constant.HTTP_FLOW_ENV));
        }

        //设置Body
        post.setEntity(new StringEntity(content, Constant.CHARTSET));
        return post;
    }

    /**
     * 发送Post
     */
    public static byte[] post(String url, SoapHeader soapHeader, String content, String certId) {
        HttpPost post = null;
        CloseableHttpResponse response = null;
        byte[] xml = {};
        String sender = null;
        String signature = null;
        HttpEntity entity = null;
        CloseableHttpClient client = null;

        client = HttpsClientFactory.getHttpClient();
        post = getPost(url, content, certId);
        //记录发送报文日志
        sender = InfoCacheUtils.getPbocInf();
        signature = post.getLastHeader("Signature").getValue();
        LoggerUtils.logMsg(sender, signature, content);
        //发送报文
        Entry agentEntry = null;
        Entry entry = null;
        try {
            String entryName = soapHeader.getReceiver();
            if (InfoCacheUtils.isHasAgentOrg(soapHeader.getReceiver())) {
                agentEntry = SphU.entry(InfoCacheUtils.fetchAgentInst(entryName), EntryType.OUT);
            }
            entry = SphU.entry(entryName, EntryType.OUT);
            response = client.execute(post);
            entity = response.getEntity();
            xml = EntityUtils.toByteArray(entity);
        } catch (BlockException e) {
            throw new GwException(GwErrorEnum.MANAGER_MAN_TIMEOUT.getCode(), "接收方故障");
        } catch (ClientProtocolException e) {
            Tracer.traceEntry(e, agentEntry);
            Tracer.traceEntry(e, entry);
            log.error("HttpsClient.post exception: {}", e);
            throw new GwException(GwErrorEnum.MANAGER_MAN_ERROR, e);
        } catch (IOException e) {
            Tracer.traceEntry(e, agentEntry);
            Tracer.traceEntry(e, entry);
            log.error("HttpsClient.post exception: {}", e);
            throw new GwException(GwErrorEnum.MANAGER_MAN_ERROR, e);
        } catch (Exception e) {
            Tracer.traceEntry(e, agentEntry);
            Tracer.traceEntry(e, entry);
            log.error("HttpsClient.post exception: {}", e);
            throw new GwException(GwErrorEnum.MANAGER_MAN_ERROR, e);
        } finally {
            if (null != entry) {
                entry.exit();
            }
            if (null != agentEntry) {
                agentEntry.exit();
            }
        }
        int status = response.getStatusLine().getStatusCode();
        if (HttpStatus.SC_OK != status) {
            log.error("HttpsClient.post exception: HttpStatus={}", status);
            throw new GwException(GwErrorEnum.MANAGER_RPC_ERROR, "HttpStatus=" + status);
        }
        //解析返回报文
        try {
            SoapHeader header = SoapUtils.getSoapHeaderBean(xml);
            sender = header.getSender();
            signature = NacosConsume.getIsSign() ? response.getLastHeader("Signature").getValue() : "";
            //验签并记录返回报文日志
            ValidateUtils.validateMsg(header, xml, signature);
        } catch (Exception e) {
            log.error("HttpsClient.post exception: {}", e);
            throw new GwException(GwErrorEnum.MANAGER_MAN_ERROR, e);
        } finally {
            LoggerUtils.logMsg(sender, signature, new String(xml));

        }

        return xml;
    }
}
