/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.manager.impl;

import com.dcep.common.annotation.Gateway;
import com.dcep.common.exception.DcepException;
import com.dcep.common.utils.JarScanUtils;
import com.dcep.dips.common.dto.dc412.Dcep41200101DTO;
import com.dcep.dips.wholesalepayment.dal.mapper.CommonRecordMapper;
import com.dcep.dips.wholesalepayment.dal.model.CommonRecordDO;
import com.dcep.dips.wholesalepayment.dto.RecordDTO;
import com.dcep.dips.wholesalepayment.enums.MsgTpEnum;
import com.dcep.dips.wholesalepayment.manager.RecordManager;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class RecordManagerImpl implements RecordManager {

    @Autowired
    CommonRecordMapper    commonRecordMapper;

    @Override
    public RecordDTO resume(String msgTp, String msgId) {

    	// 根据报文标识号及报文编号查找档案表唯一记录
        CommonRecordDO resumeRec = commonRecordMapper
            .selectByPrimaryKey(new CommonRecordDO(msgId, msgTp));
        if (resumeRec == null){
            return null;
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(resumeRec.getDocument());
            String context = root.path("soapBody").path("t").toString();
        	// 转化为DTO类
            return ((RecordDTO) DtoClassCache.get(msgTp).newInstance())
                .decode(context);

        } catch (Exception e) {
        	log.error("RecordManager resume Exception:{}", e);
        }

        return null;
    }

    @Override
    public Dcep41200101DTO resumeQryRsp(String msgId) {

        // 根据报文标识号及报文编号查找档案表唯一记录
        CommonRecordDO resumeRec = commonRecordMapper
            .selectByPrimaryKey(new CommonRecordDO(msgId, MsgTpEnum.TXN_STATE_RESPONSE.getCode()));
        if (resumeRec == null){
            return null;
        }
        try {
            // 转化为DTO类
            Dcep41200101DTO dcep41200101DTO = new Dcep41200101DTO();
            return dcep41200101DTO.decode(resumeRec.getDocument());
        } catch (Exception e) {
            log.error("RecordManager resume Exception:{}", e);
        }

        return null;
    }

    static Map<String, Class<?>> DtoClassCache = new HashMap<>();

    static {
    	try {
        JarScanUtils.getClzFromPkg("com.dcep.dips.wholesalepayment.dto").stream()
            .filter(clazz -> clazz.isAnnotationPresent(Gateway.class)).forEach(clazz -> {
                DtoClassCache.put(clazz.getDeclaredAnnotation(Gateway.class).msgTp(), clazz);
            });
        } catch (DcepException e) {
        	log.error("DtoClassCache init Exception:{}", e);
	    }
    }

    public static <T> T get(Class<T> clz, Object o) {
        if (clz.isInstance(o)) {
            return clz.cast(o);
        }
        return null;
    }
}
