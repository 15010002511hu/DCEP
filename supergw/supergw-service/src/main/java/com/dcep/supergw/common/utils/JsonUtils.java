package com.dcep.supergw.common.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : maxinyu
 * @version : JsonUtils.java v 0.1 2020-09-10
 * @description :
 */
@Slf4j
public class JsonUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 将Json报文转换为DTO对象
     */
    public static <T> T jsonToObject(String xmlMessage, Class<T> clazz) throws Exception {
        try {
            return MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .readValue(xmlMessage, clazz);
        } catch (Exception e) {
            log.error("Json转化DTO异常", e);
            throw e;
        }
    }

    /**
     * 将对象改为xml Include.后的参数有 ALWAYS , NON_NULL 等 本处采用ALWAYS
     */
    public static <T> String objectToJson(T object) throws Exception {
        try {
            return MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL).writeValueAsString(object);
        } catch (Exception e) {
            log.error("DTO转化Json异常", e);
            throw e;
        }
    }


    /**
     * 序列化为bytes[]
     */
    public static <T> byte[] objectToJsonByte(T object) throws Exception {
        try {
            return MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL).writeValueAsBytes(object);
        } catch (Exception e) {
            log.error("DTO转化Json异常", e);
            throw e;
        }
    }

    public static <T> T jsonByteToObject(byte[] content, Class<T> clazz) throws Exception {
        try {
            return MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).readValue(content, clazz);
        } catch (Exception e) {
            log.error("Json bytes转化DTO异常", e);
            throw e;
        }
    }
}
