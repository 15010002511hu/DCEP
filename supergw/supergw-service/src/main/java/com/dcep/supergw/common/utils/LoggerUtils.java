package com.dcep.supergw.common.utils;

import com.dcep.common.model.soap.SoapHeader;
import com.dcep.infocache.OrgCache;
import com.dcep.infocache.api.dto.OrgDTO;
import com.dcep.infocache.enums.LocalCacheEnum;
import com.dcep.supergw.common.constant.Constant;
import com.dcep.supergw.common.enums.GwErrorEnum;
import com.dcep.supergw.common.exception.GwException;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * @author nieyanping
 * @version LoggerUtils.java, v 0.1, 2019/8/27 21:03
 * @description 获取相应的logger
 */
@Slf4j
public class LoggerUtils {

    private static OrgCache orgCache = OrgCache.getInstance();

    private volatile static ConcurrentHashMap<String, Logger> loggerMap = new ConcurrentHashMap<>();

    private static final String DEFAULT_MESSAGE_LOGGER_NAME = "default";

    private static Logger getInstLogger(String instId) {
        Logger logger = null;
        try {
            // 根据机构编号获取机构信息
            OrgDTO instInfo = orgCache.getFiInf(instId);

            if (instInfo == null) {
                throw new GwException(GwErrorEnum.REQUEST_PARAM_ILLEGAL, "机构ID非法，输入机构号为：" + instId);
            }

            // 获取机构内部编码
            String innerCode = instInfo.getOrgInnerCode();

            if (StringUtils.isEmpty(innerCode)) {
                innerCode = DEFAULT_MESSAGE_LOGGER_NAME;
            }

            // 获取机构类型
            String orgType = instInfo.getOrgType();

            /**
             * 	20210607：为避免info日志成为热点，将非央行、运营机构发送的报文输出到默认文件
             */
            if (!LocalCacheEnum.ORG_TYPE_0.getCode().equals(orgType) && // 央行
                !LocalCacheEnum.ORG_TYPE_1.getCode().equals(orgType) && // 运营机构-银行
                !LocalCacheEnum.ORG_TYPE_2.getCode().equals(orgType)) { // 运营机构-非银行
                innerCode = DEFAULT_MESSAGE_LOGGER_NAME;
            }

            logger = getLogger(innerCode);
        } catch (Exception e) {
            log.error("getInstLogger failed", e);
        }

        return logger;
    }

    public static Logger getLogger(String logKey) {
        Logger logger = null;
        if (!loggerMap.containsKey(logKey)) {
            synchronized (loggerMap) {
                if (!loggerMap.containsKey(logKey)) {
                    loggerMap.put(logKey, LoggerFactory.getLogger(logKey));
                }
            }
        }
        logger = loggerMap.get(logKey);
        return logger;
    }

    /**
     * @return void
     * @description 报文记录到日志中
     * @date 2019/8/30 19:47
     */
    public static void logMsg(String sender, String sign, String xml) {
        try {
            Logger logger = getInstLogger(sender);
            if (logger != null) {
                logger.info("<----record begin  {\"signature\":\"{}\",\"xml\":\"{}}\" record end---->",
                    sign, xml);
            }
        } catch (Exception e) {
            log.error("LoggerUtil.logMsg: {}", e);
        }
    }

    /**
     * @return void
     * @description 报文记录到日志中
     * @date 2019/8/30 19:47
     */
    public static void logMsg(String sender, String sign, byte[] xml) {
        try {
            Logger logger = getInstLogger(sender);

            if (logger != null) {
                logger.info("<----record begin  {\"signature\":\"{}\",\"xml\":\"{}}\" record end---->",
                    sign, new String(xml, Constant.CHARTSET));
            }
        } catch (Exception e) {
            log.error("LoggerUtil.logMsg: {}", e);
        }
    }

    public static void mdc(SoapHeader header) {
        MDC.put("msgSn", header.getMsgSN());
        MDC.put("msgTp", header.getMsgTp());
        MDC.put("sender", header.getSender());
        MDC.put("receiver", header.getReceiver());
        MDC.put("appName", EnviromentUtils.get("spring.application.name"));
    }
}