/*
 * pbcdci.cn Inc.
 * Copyright © 2025 PBCDCI All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.utils;

import com.dcep.common.enums.ErrorEnum;
import com.dcep.common.exception.DcepException;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

/**
 * 对象转换工具类
 *
 * @author laowei
 * @version 1.0.0 2025/8/7
 */
@Slf4j
public class ObjectMapperUtil {

    public static final ThreadLocal<ObjectMapper> OBJ_MAPPER_HOLDER = ThreadLocal.withInitial(() -> {
        ObjectMapper objMapper = new ObjectMapper();
        // 反序列化数组对象时接受单值
        objMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        // 反序列化忽略不识别的属性
        objMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 只序列化非null属性
        objMapper.setSerializationInclusion(Include.NON_NULL);
        return objMapper;
    });

    public static String obj2JsonStr(Object obj) {
        try {
            return OBJ_MAPPER_HOLDER.get().writeValueAsString(obj);
        } catch (Exception e) {
            String errorMsg = "obj2JsonStr failed: " + obj;
            log.error(errorMsg, e);
            throw new DcepException(ErrorEnum.REQUEST_PARAM_ILLEGAL.getCode(), errorMsg);
        }
    }

    public static <T> T jsonStr2Obj(String jsonStr, Class<T> clazz) {
        try {
            return OBJ_MAPPER_HOLDER.get().readValue(jsonStr, clazz);
        } catch (Exception e) {
            String errorMsg = "jsonStr2Obj failed, jsonStr: " + jsonStr;
            log.error(errorMsg, e);
            throw new DcepException(ErrorEnum.REQUEST_PARAM_ILLEGAL.getCode(), errorMsg);
        }
    }

    public static <T> T jsonStr2Obj(String jsonStr, TypeReference<T> valueTypeRef) {
        try {
            return OBJ_MAPPER_HOLDER.get().readValue(jsonStr, valueTypeRef);
        } catch (Exception e) {
            String errorMsg = "jsonStr2Obj failed, jsonStr: " + jsonStr;
            log.error(errorMsg, e);
            throw new DcepException(ErrorEnum.REQUEST_PARAM_ILLEGAL.getCode(), errorMsg);
        }
    }
}
