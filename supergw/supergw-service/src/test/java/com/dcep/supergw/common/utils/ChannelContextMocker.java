package com.dcep.supergw.common.utils;

import com.dcep.common.annotation.Gateway;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.soap.SoapBody;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.utils.JarScanUtils;
import com.dcep.supergw.common.constant.Constant;
import com.dcep.supergw.common.enums.GwErrorEnum;
import com.dcep.supergw.common.exception.GwException;
import com.dcep.supergw.manager.bo.ChannelContext;
import com.dcep.supergw.manager.channel.AbstractChannel;
import com.dcep.supergw.manager.channel.action.AbstractAction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.AsyncContext;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import mockit.Mock;
import mockit.MockUp;

@Slf4j
public class ChannelContextMocker {

    static Map<String, Class<?>> cache = new HashMap<>();
    static String[] PACKAGE_NAMES = new String[]{
        "com.dcep"
    };

    public static ChannelContext newChannelContext(String msg) {
        ValidateUtilsMocker.mock();

        AsyncContext asyncContext = new MockUp<AsyncContext>(AsyncContext.class) {
            @Mock
            public void complete() {
                log.info("execute mock AsyncContext.complete()");
            }
        }.getMockInstance();

        AbstractChannel channel = new AbstractChannel("unittest") {

            @Override
            public void invokeAction(ChannelContext context) {
                log.info("execute mock AbstractChannel.invokeAction()");
            }

            @Override
            public void invokeException(ChannelContext context, GwException e) {
                log.info("execute mock AbstractChannel.invokeException()");
            }

            @Override
            public void invokeCallBack(ChannelContext context) {
                log.info("execute mock AbstractChannel.invokeCallBack()");
            }

            @Override
            public AbstractAction getChannel() {
                return new AbstractAction("unittest") {
                    @Override
                    public void doInvoke(ChannelContext context) {
                        log.info("execute mock AbstractAction.doInvoke()");
                    }

                    @Override
                    public void doException(ChannelContext context, GwException e) {
                        log.info("execute mock AbstractAction.doException()");
                    }

                    @Override
                    public void doCallBack(ChannelContext context) {
                        log.info("execute mock AbstractAction.doCallBack()");
                    }
                };
            }
        };

        ChannelContext mockChannelContext = new ChannelContext();
        mockChannelContext.setAttachment(Constant.SOAP_HEADER, SoapUtils.getSoapHeaderBean(msg.getBytes()));
        mockChannelContext.setAttachment(Constant.SERIALIZATION, msg.getBytes());
        mockChannelContext.setAttachment(Constant.DESERIALIZATION,
            toDto(SoapUtils.getSoapHeaderBean(msg.getBytes()), msg.getBytes()));
        mockChannelContext.setAttachment(Constant.SIGNATURE, "");
        mockChannelContext.setServletContext(asyncContext);
        mockChannelContext.setChannel(channel);

        return mockChannelContext;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Object toDto(SoapHeader header, byte[] xml) {
        getClzFroPkgs(PACKAGE_NAMES).stream()
            .filter(clz -> clz.isAnnotationPresent(Gateway.class))
            .forEach(clz -> cache.put(clz.getDeclaredAnnotation(Gateway.class).msgTp(), clz));
        Class clz = (header.getMsgTp() != "" ? cache.get(header.getMsgTp()) : null);
//		if (clz == null) {
//			throw new GwException(GwErrorEnum.MSGTP_ERROR,
//					"ParseXml2DTOUtils.parseXml2DTO，报文编号异常----messageType=" + header.getMsgTp());
//		}
        try {
            EnvelopeDTO envelopeDTO = new EnvelopeDTO();
            envelopeDTO.setSoapHeader(header);
            envelopeDTO.setSoapBody(new SoapBody(getSoapBodyBean(xml, clz)));

            return envelopeDTO;
        } catch (ConstraintViolationException e) {
            throw new GwException(GwErrorEnum.VALIDATION_ERROR, e.getMessage(), e);
        } catch (GwException e) {
            throw e;
        } catch (Exception e) {
            String errorMessage = "XML转DTO异常--";
            log.error(errorMessage, e);
            throw new GwException(GwErrorEnum.XML2DTO_ERROR, e.getMessage(), e);
        }
    }

    private static Object getSoapBodyBean(byte[] xml, Class clz) throws Exception {
        return XmlUtils.xmlToObject(getBodyBytes(xml), clz);
    }

    private static byte[] getBodyBytes(byte[] xml) {
        if (xml == null || xml.length == 0) {
            return null;
        }
        // 获取第一个":Body>"之后到最后一个":Body>"之前的内容(均不包含":Body>")
        byte[] result = subBytes(xml, indexOf(xml, 0, ":Body>".getBytes()) + 6,
            lastIndexOf(xml, xml.length, ":Body>".getBytes()));
        // 获取最后一个"</"之前的内容,从而可以去掉Body标签的前缀XXX
        result = subBytes(result, 0, lastIndexOf(result, result.length, "</".getBytes()));
        return result;
    }

    public static int indexOf(byte[] src, int offset, byte[] target) {
        int index = offset < 0 ? 0 : offset;
        while (index < src.length - target.length + 1) {
            if (match(src, index, target)) {
                return index;
            }
            index++;
        }
        return -1;
    }

    public static boolean match(byte[] src, int start, byte[] target) {
        int j = 0;
        for (int i = start; i < src.length && j < target.length; i++, j++) {
            if (src[i] != target[j]) {
                return false;
            }
        }
        return j >= target.length;
    }

    public static int lastIndexOf(byte[] src, int offset, byte[] target) {
        int index = (offset > src.length - target.length) ? src.length - target.length : offset;
        while (index >= 0) {
            if (match(src, index, target)) {
                return index;
            }
            index--;
        }
        return -1;
    }

    public static byte[] subBytes(byte[] msg, int start, int end) {
        if (start >= end) {
            return null;
        }
        byte[] subBytes = new byte[end - start];
        // 数组拷贝
        System.arraycopy(msg, start, subBytes, 0, subBytes.length);
        return subBytes;
    }

    public static List<Class<?>> getClzFroPkgs(String[] pkgs) {
        List<Class<?>> clazzs = new ArrayList<>();
        for (String pkg : pkgs) {
            clazzs.addAll(JarScanUtils.getClzFromPkg(pkg));
        }
        return clazzs;
    }
}
