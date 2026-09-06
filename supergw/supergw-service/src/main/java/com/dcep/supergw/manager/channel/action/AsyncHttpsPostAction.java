package com.dcep.supergw.manager.channel.action;

import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.msg.Dcep91100101DTO;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.infocache.manager.NacosConsume;
import com.dcep.supergw.common.constant.Constant;
import com.dcep.supergw.common.enums.GwErrorEnum;
import com.dcep.supergw.common.exception.GwException;
import com.dcep.supergw.common.utils.GwMsgUtils;
import com.dcep.supergw.common.utils.InfoCacheUtils;
import com.dcep.supergw.common.utils.LoggerUtils;
import com.dcep.supergw.common.utils.SoapUtils;
import com.dcep.supergw.common.utils.ValidateUtils;
import com.dcep.supergw.manager.bo.ChannelContext;
import com.dcep.supergw.manager.https.DcepHttpCallBack;
import com.dcep.supergw.manager.https.HttpsAsyncClient;
import com.dcep.supergw.manager.https.HttpsAsyncClientFactory;
import com.dcep.supergw.manager.https.interceptor.EnvFlagHttpCallBack;
import com.dcepex.trace.support.http.TraceHttpCallback;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.util.EntityUtils;
import org.slf4j.MDC;

@Slf4j
public class AsyncHttpsPostAction extends AbstractAction {

    public AsyncHttpsPostAction() {
        super("AsyncHttpsPostAction");
    }

    //AsyncHttpsPostAction在调用链末尾，无下个action调用
    @Override
    public void doInvoke(ChannelContext context) {
        HttpsAsyncClient client = HttpsAsyncClientFactory.getInstance();
        try {
            //获取DTO
            EnvelopeDTO<?> dto = (EnvelopeDTO<?>) context.getAttachment(Constant.DESERIALIZATION);
            //获取报文DTO
            HttpPost request = client.getPost(dto.getSoapHeader().getReceiver(),dto.getSoapHeader().getMsgTp(), SoapUtils.toXml(dto));
            //定义熔断
            client.post(request, new TraceHttpCallback<>(
                new EnvFlagHttpCallBack(
                    new DcepHttpCallBack(
                        new HttpsCallBack(context, request), dto.getSoapHeader().getReceiver()))));
        } catch (Throwable e) {
            client.logExceptionEvent();
            context.fireInvokeException(new GwException(GwErrorEnum.MANAGER_MAN_ERROR, e));
        }
    }

    @Override
    public void doCallBack(ChannelContext context) {
        //报文头反序列化
        byte[] xml = (byte[]) context.getAttachment(Constant.SERIALIZATION);
        SoapHeader header = SoapUtils.getSoapHeaderBean(xml);
        context.setAttachment(Constant.SOAP_HEADER, header);
        context.setRequest(false);
        //校验报文头及签名
        ValidateUtils.validateMsg(header, xml, (String) context.getAttachment(Constant.SIGNATURE));
        //报文反序列化
        EnvelopeDTO<?> dto = SoapUtils.toDto(header, xml);
        context.setAttachment(Constant.SOAP_HEADER, header);
        context.setAttachment(Constant.DESERIALIZATION, dto);
        context.fireInvokeCallBack();
    }

    @Override
    public void doException(ChannelContext context, GwException e) {
        //获取当前报文的报文头
        SoapHeader header = (SoapHeader) context.getAttachment(Constant.SOAP_HEADER);
        //生成911报文DTO
        EnvelopeDTO<Dcep91100101DTO> dto = GwMsgUtils.dcep911(
            header.getMsgSN(),
            InfoCacheUtils.getPbocInf(),
            context.isRequest() ? header.getSender() : header.getReceiver(),
            e.getCode(),
            e.getDescription(),
            context.isRequest() ? header.getReceiver() : header.getSender(),
            e.getMessage()
        );
        //保存911报文DTO
        context.setAttachment(Constant.DESERIALIZATION, dto);
        context.setAttachment(Constant.SOAP_HEADER, dto.getSoapHeader());

        context.setExceptionCatched(true);
        context.fireInvokeCallBack();
    }

    class HttpsCallBack implements FutureCallback<HttpResponse> {

        private final ChannelContext context;
        private final HttpPost request;

        public HttpsCallBack(ChannelContext context, HttpPost request) {
            this.context = context;
            this.request = request;
        }

        @Override
        public void completed(HttpResponse response) {
            SoapHeader header = (SoapHeader) context.getAttachment(Constant.SOAP_HEADER);
            LoggerUtils.mdc(header);
            byte[] result = {};
            String signature = "";
            HttpEntity entity = null;
            try {
                //检查交互状态
                int status = response.getStatusLine().getStatusCode();
                if (HttpStatus.SC_OK != status) {
                    throw new GwException(GwErrorEnum.RESP_PARAM_ILLEGAL, "HttpStatus=" + status);
                }

                //获取报文信息
                entity = response.getEntity();
                result = EntityUtils.toByteArray(entity);
                signature = NacosConsume.getIsSign() ? response.getLastHeader("Signature") == null ? ""
                    : response.getLastHeader("Signature").getValue() : "";

                //设置输入参数
                context.setAttachment(Constant.SIGNATURE, signature);
                context.setAttachment(Constant.SERIALIZATION, result);
                context.fireInvokeChannel();
            } catch (GwException e) {
                context.fireInvokeChannel(e);
            } catch (IOException e) {
                context.fireInvokeChannel(new GwException(GwErrorEnum.READ_MESSAGE_ERROR, e));
            } catch (Exception e) {
                context.fireInvokeChannel(new GwException(GwErrorEnum.READ_MESSAGE_ERROR, e));
            } finally {
                if (null != entity) {
                    try {
                        EntityUtils.consume(entity);
                    } catch (IOException e) {
                        log.error("HttpsCallBack.completed :{}", e);
                    }
                }
                try {
                    request.releaseConnection();
                } catch (Exception e) {
                    log.error("HttpsCallBack.completed :{}", e);
                }
                //记录返回报文日志
                LoggerUtils.logMsg(header.getReceiver(), signature, result);
                MDC.clear();
            }
        }

        @Override
        public void failed(Exception e) {
            try {
                LoggerUtils.mdc((SoapHeader) context.getAttachment(Constant.SOAP_HEADER));
                log.error("HttpsCallBack.failed :{}", e);
                context.fireInvokeChannel(new GwException(GwErrorEnum.MANAGER_MAN_TIMEOUT, "网关发送报文到机构超时"));
            } catch (Exception e1) {
                log.error("HttpsCallBack.failed.exception :{}", e1);
            } finally {
                try {
                    request.releaseConnection();
                } catch (Exception t) {
                    log.error("HttpsCallBack.failed", t);
                }
                MDC.clear();
            }
        }

        @Override
        public void cancelled() {
            try {
                SoapHeader header = (SoapHeader) context.getAttachment(Constant.SOAP_HEADER);
                LoggerUtils.mdc(header);
                log.error("HttpsCallBack.cancelled : {} Post cancelled!", header.getMsgSN());
                context.fireInvokeChannel(new GwException(GwErrorEnum.MANAGER_MAN_ERROR, "Post cancelled!"));
            } catch (Exception e1) {
                log.error("HttpsCallBack.cancelled.exception :{}", e1);
            } finally {
                try {
                    request.releaseConnection();
                } catch (Exception e) {
                    log.error("HttpsCallBack.cancelled", e);
                }
                MDC.clear();
            }
        }
    }
}

