/*
 * pbcdci.cn Inc.
 * Copyright © 2025 PBCDCI All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.utils;

import com.dubbo.ldc.ZoneClient;
import com.dubbo.ldc.constant.EnvEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * 获取环境标识位工具类
 *
 * @author laowei
 * @version 1.0.0 2025/8/7
 */
@Slf4j
public class EnvInfoUtil {
    /**
     * 根据当前Zone信息获取环境标识位
     * @return 环境标识位
     */
    public static String getEnvInfo() {
        try {
            return ZoneClient.getInstance().getLastDigitOfMsgId(true);
        } catch (Exception e) {
            // 兜底使用生产环境标识
            log.error("init env value exception: ", e);
            return EnvEnum.PROD.getCode();
        }
    }

    public static boolean checkEnvInfo(String envInfo) {
        String environment = ZoneClient.getInstance().getEnv().name();
        if (StringUtils.isBlank(environment)) {
            log.error("environment is null");
            return false;
        }
        if (StringUtils.equalsAnyIgnoreCase(environment, EnvEnum.PRE.name(), EnvEnum.PROD.name())) {
            return true;
        }
        //环境信息相同直接返回true，SIT/STABLE/PROD/PRE
        if (environment.equalsIgnoreCase(EnvEnum.SIT.name())) {
            return environment.equalsIgnoreCase(envInfo);
        } else if (environment.equalsIgnoreCase(EnvEnum.DEV.name())) {
            //DEV环境处理形同分组信息的数据
            return StringUtils.isNotBlank(envInfo) && envInfo.contains(ZoneClient.getInstance().getDevGroup());
        } else if (environment.equalsIgnoreCase(EnvEnum.STABLE.name())) {
            //STABLE环境
            return StringUtils.isBlank(envInfo) || envInfo.toLowerCase().contains(EnvEnum.STABLE.name().toLowerCase());
        }
        return false;
    }
}
