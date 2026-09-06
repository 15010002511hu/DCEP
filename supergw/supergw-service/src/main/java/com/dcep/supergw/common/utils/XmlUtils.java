/**
 * DCEP.com.cn Inc. Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.supergw.common.utils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class XmlUtils {

    private static final XmlMapper XML_MAPPER = new XmlMapper();

    /**
     * 将XML报文转换为DTO对象
     *
     * @param xmlMessage
     * @param clazz
     * @return
     * @throws Exception
     */
    public static <T> T xmlToObject(String xmlMessage, Class<T> clazz) throws Exception {
        try {
            return XML_MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .readValue(xmlMessage, clazz);
        } catch (Exception e) {
            log.error("XmlUtils.xmlToObject，将xml转换为DTO异常", e);
            throw e;
        }
    }

    /**
     * 将byte数组形式的XML报文转换为DTO对象
     *
     * @param xmlMessage
     * @param clazz
     * @return
     * @throws Exception
     */
    public static <T> T xmlToObject(byte[] xmlMessage, Class<T> clazz) throws Exception {
        try {
            return XML_MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .readValue(xmlMessage, clazz);
        } catch (Exception e) {
            log.error("XmlUtils.xmlToObject，将byte[]形式的xml转换为DTO异常", e);
            throw e;
        }
    }


    /**
     * 将对象改为xml
     * Include.后的参数有 ALWAYS , NON_NULL 等
     * 本处采用ALWAYS
     *
     * @param object
     * @return
     * @throws Exception
     */
    public static <T> String objectToXml(T object) throws Exception {
        try {
            return XML_MAPPER.setSerializationInclusion(Include.NON_NULL)
                /*.writerWithDefaultPrettyPrinter()*/.writeValueAsString(object);
        } catch (Exception e) {
            log.error("XmlUtils.objectToXml，将DTO转换为xml报文异常", e);
            throw e;
        }
    }


}