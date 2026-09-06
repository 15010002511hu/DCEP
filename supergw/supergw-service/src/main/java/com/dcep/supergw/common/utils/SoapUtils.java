package com.dcep.supergw.common.utils;

import com.dcep.common.enums.MessageTypeEnum;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.SoapBody;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.supergw.common.config.DtoMappingConfig;
import com.dcep.supergw.common.constant.Constant;
import com.dcep.supergw.common.enums.AgentPushIdEnum;
import com.dcep.supergw.common.enums.GwErrorEnum;
import com.dcep.supergw.common.enums.MsgTpEnum;
import com.dcep.supergw.common.exception.GwException;
import com.dcep.supergw.dto.model.Envelope;
import com.dcep.uniwlt.model.dto.dcep486.Dcep48600102DTO;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author : maxinyu
 * @version : .java v 0.1 2019-12-06
 * @description : soap报文解析工具类
 */
@Slf4j
public class SoapUtils {

    /**
     * 将一个soap报文字节流转换成为相对应的DTO对象
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <T extends GwDTO> EnvelopeDTO<T> toDto(SoapHeader header, byte[] xml) {
        /**
         * 根据msgTp查找对应Dto的Class，若是没找到则抛出异常
         */
        Class clz = DtoMappingConfig.getClzByMsgTp(header.getMsgTp());
        if (clz == null) {
            throw new GwException(GwErrorEnum.MSGTP_ERROR,
                "ParseXml2DTOUtils.parseXml2DTO，报文编号异常----messageType=" + header.getMsgTp());
        }
        try {
            EnvelopeDTO envelopeDTO = new EnvelopeDTO();
            envelopeDTO.setSoapHeader(header);
            envelopeDTO.setSoapBody(new SoapBody(getSoapBodyBean(xml, clz)));
            //限流
            if (MsgTpEnum.DCEP_486_001_02.equals(header.getMsgTp())) {
                Dcep48600102DTO dcep48600102DTO = (Dcep48600102DTO) envelopeDTO.body();
                if (dcep48600102DTO.getWltInf() != null && StringUtils
                    .isNotBlank(dcep48600102DTO.getWltInf().getPushId())) {
                    String pushId = dcep48600102DTO.getWltInf().getPushId();
                    //选择pushId的代理
                    String agentPushId = AgentPushIdEnum.getEnum(pushId).getAgentId();
                    String resourceName = String
                        .join("-", header.getSender(), header.getMsgTp(), agentPushId);
                    //限流
                    FlowUtils.flow(resourceName);
                }
            }
            if (!MessageTypeEnum.DCEP_911_001_01.getCode().equals(header.getMsgTp())) {
                // 对dto进行解密操作
                SecretUtils.decrypt(envelopeDTO);
                // 对解密后的dto进行校验
                ValidateUtils.validate(envelopeDTO);
                // 业务检查前的初始化操作
                envelopeDTO.body().init();
                // 对dto业务检查
                ValidateUtils.check(envelopeDTO);
            }

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

    /**
     * 将一个soap报文字符串转换成为相对应的DTO对象
     */
    public static <T extends GwDTO> EnvelopeDTO<T> toDto(SoapHeader header, String xml) {
        try {
            return toDto(header, xml.getBytes(Constant.CHARTSET));
        } catch (GwException e) {
            throw e;
        } catch (Exception e) {
            throw new GwException(GwErrorEnum.XML2DTO_ERROR, e);
        }
    }

    /**
     * 获取Soapheader对象
     *
     * @return SoapHeader
     */
    public static SoapHeader getSoapHeaderBean(byte[] xml) {
        try {
            return XmlUtils.xmlToObject(xml, Envelope.class).getSoapHeader();
        } catch (Exception e) {
            log.error("SOAP报文头解析异常: {}", new String(xml));
            throw new GwException(GwErrorEnum.SOAPHEADER_ERROR, e);
        }
    }

    /**
     * 获取Soapheader对象
     *
     * @return SoapHeader
     */
    public static SoapHeader getSoapHeaderBean(String xml) {
        try {
            return getSoapHeaderBean(xml.getBytes(Constant.CHARTSET));
        } catch (GwException e) {
            throw e;
        } catch (Exception e) {
            throw new GwException(GwErrorEnum.SOAPHEADER_ERROR, e);
        }
    }

    /**
     * 获取soapBodyBean
     */
    private static <T extends GwDTO> T getSoapBodyBean(byte[] xml, Class<T> clz) {
        try {
            return XmlUtils.xmlToObject(getBodyBytes(xml), clz);
        } catch (Exception e) {
            throw new GwException(GwErrorEnum.SOAPHEADER_ERROR, e);
        }
    }

    /**
     * @return 返回<XXX:Body></XXX:Body>的byte[]数组 不包括<XXX:Body></XXX:Body>标签
     */
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

    /**
     * 在byte数组中获取子数组
     *
     * @param msg 父数组
     * @param start 开始索引(包括在子数组)
     * @param end 结束索引(不包括在子数组)
     * @return 子数组
     */
    public static byte[] subBytes(byte[] msg, int start, int end) {
        if (start >= end) {
            return null;
        }
        byte[] subBytes = new byte[end - start];
        // 数组拷贝
        System.arraycopy(msg, start, subBytes, 0, subBytes.length);
        return subBytes;
    }

    /**
     * 在byte数组中搜索第一个指定的数组
     *
     * @param src 搜索源
     * @param offset 搜索开始索引
     * @param target 搜索目标
     * @return target从offset处开始第一次出现的位置
     */
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

    /**
     * 在byte数组src中判断start位置开始是否匹配target
     *
     * @param src 匹配源字符数组
     * @param start 匹配开始索引
     * @param target 匹配目标
     * @return boolean是否匹配上
     */
    public static boolean match(byte[] src, int start, byte[] target) {
        int j = 0;
        for (int i = start; i < src.length && j < target.length; i++, j++) {
            if (src[i] != target[j]) {
                return false;
            }
        }
        return j >= target.length;
    }

    /**
     * 在byte数组中搜索最后一个指定的数组
     *
     * @param src 搜索源
     * @param offset 搜索开始索引
     * @param target 搜索目标
     * @return target从offset往前搜索最后一次出现的位置
     */
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

    /**
     * @param envelopeDTO
     * @param <T>
     * @return
     */
    public static <T extends GwDTO> String toXml(EnvelopeDTO<T> envelopeDTO) {
        StringBuilder sb = new StringBuilder();
        SoapHeader header = envelopeDTO.getSoapHeader();
        SoapHeaderUtils.setPbocSignSn(envelopeDTO);
        // 获取密钥
        if (!MessageTypeEnum.DCEP_911_001_01.getCode().equals(header.getMsgTp())) {
            // 对dto进行校验
            ValidateUtils.validate(envelopeDTO);
            // 对dto进行业务检查
            ValidateUtils.check(envelopeDTO);
            // 对dto进行转加密
            SecretUtils.transEncrypt(envelopeDTO);
            // 对dto进行加密
            SecretUtils.encrypt(envelopeDTO);

        }
        sb.append(
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:head=\"http://www.dcep.com/dcep/header/\">");
        sb.append("<soap:Header>" + "<head:Ver>" + header.getVer() + "</head:Ver>" + "<head:SndDtTm>"
            + header.getSndDtTm() + "</head:SndDtTm>" + "<head:MsgTp>" + header.getMsgTp() + "</head:MsgTp>"
            + "<head:MsgSN>" + header.getMsgSN() + "</head:MsgSN>"
            + "<head:Sender>" + header.getSender() + "</head:Sender>"
            + (!StringUtils.isEmpty(InfoCacheUtils.getInstLEI(header.getSender()))
            ? "<head:SenderLEI>" + InfoCacheUtils.getInstLEI(header.getSender()) + "</head:SenderLEI>"
            : "")
            + "<head:Receiver>" + header.getReceiver() + "</head:Receiver>"
            + (!StringUtils.isEmpty(InfoCacheUtils.getInstLEI(header.getReceiver()))
            ? "<head:ReceiverLEI>" + InfoCacheUtils.getInstLEI(header.getReceiver()) + "</head:ReceiverLEI>"
            : "")
            + "<head:SignSN>" + header.getSignSN() + "</head:SignSN>"
            + (!StringUtils.isEmpty(header.getNcrptnSN())
            ? "<head:NcrptnSN>" + header.getNcrptnSN() + "</head:NcrptnSN>"
            : "")
            + (!StringUtils.isEmpty(header.getDgtlEnvlp())
            ? "<head:DgtlEnvlp>" + header.getDgtlEnvlp() + "</head:DgtlEnvlp>"
            : "")
            + "</soap:Header>");

        JacksonXmlRootElement rootElement = envelopeDTO.body().getClass()
            .getDeclaredAnnotation(JacksonXmlRootElement.class);
        try {
            sb.append(XmlUtils.objectToXml(envelopeDTO.getSoapBody()).replaceAll("#localName", rootElement.localName())
                .replaceAll("#namespace", rootElement.namespace()));
            sb.append("</soap:Envelope>");
        } catch (Exception e) {
            throw new GwException(GwErrorEnum.DTO2XML_ERROR, e);
        }
        return sb.toString();
    }
}
