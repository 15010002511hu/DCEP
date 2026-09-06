package com.dcep.dips.wholesalepayment.common.utils;

import com.dcep.common.Constants.CommonConstant;
import com.dcep.common.enums.ErrorEnum;
import com.dcep.common.exception.DcepException;
import com.dcep.dips.wholesalepayment.common.constants.Constant;
import com.dcep.dips.wholesalepayment.enums.ActgStsEnum;
import com.dcep.dips.wholesalepayment.enums.ClearingStatusEnum;
import com.dcepex.trace.support.TraceContext;
import com.dubbo.ldc.ZoneClient;
import com.dubbo.ldc.constant.EnvEnum;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
public class CommonUtil {

    public static final Random RANDOM = new SecureRandom();

	// 替换管道符“|”为全角管道符“｜”
	public static String replacePipe(String msg) {
		return StringUtils.isNotBlank(msg) ? msg.replace("|", "｜") : msg;
	}

	/**
	 * @version LogUtils.java
	 * @description 通过MDC将msgSn的值写入到log4j日志的上下文中
	 */
	public static void LogMDC(String msgSn) {
        MDC.put(CommonConstant.MSN_KEY, msgSn);
    }

    // 根据当前Zone信息获取环境标识位
    public static String getEnvVal() {
		// 根据当前Zone信息获取环境标识位
		try {
			return ZoneClient.getInstance().getLastDigitOfMsgId(true);
		} catch (Exception e) {
			// 兜底使用生产环境标识
			log.error("init env value exception: ", e);
			return  EnvEnum.PROD.getCode();
		}
    }

	public static void setPressFlag() {
		try{
			TraceContext.set(CommonConstant.LOAD_TRAFFIC_KEY, Constant.PRESS_FLAG_TRUE);
		}catch (Exception e){
			log.error("set pressFlag value exception: ", e);
		}
	}

	public static void setNotPressFlag() {
		try{
			TraceContext.set(CommonConstant.LOAD_TRAFFIC_KEY, Constant.PRESS_FLAG_FALSE);
		}catch (Exception e){
			log.error("set pressFlag value exception: ", e);
		}
	}

	public static boolean isPressFlag() {
		try{
			String key = TraceContext.get(CommonConstant.LOAD_TRAFFIC_KEY);
			return Constant.PRESS_FLAG_TRUE.equalsIgnoreCase(key);
		}catch (Exception e){
			log.error("get pressFlag value exception: ", e);
			return false;
		}
	}

	// 获取当前LDC数据库分片信息（LDC会根据机器/usr/dcep-config/server.yml配置映射指定的分片）
	public static List<String> getLdcSharding() {
		String[] shardings = ZoneClient.getInstance().activeRZSharding("default");
		List<String> shardingList = Arrays.asList(shardings);
		// 分片集合洗牌
		Collections.shuffle(shardingList);
		return shardingList;
	}

	// 获取交易结果业务报文的原文
    public static String getResMsgTp(String msgTp) {
        String resMsgTp = null;
        // 查询交易结果报文时,交易类型转换为返回结果的交易类型
//        if (MsgTpEnum.CSM_REQUEST.getCode().equals(msgTp)) {
//            resMsgTp = MsgTpEnum.CSM_RESULT_NOTICE.getCode();
//        } else if (MsgTpEnum.ORDR_CONF_RESPONSE.getCode().equals(msgTp)) {
//            resMsgTp = MsgTpEnum.ORDR_CONF_RESULT_NOTICE.getCode();
//        } else if (MsgTpEnum.REFUND_REQUREST.getCode().equals(msgTp)) {
//            resMsgTp = MsgTpEnum.REFUND_RESPONSE.getCode();
//        } else if (MsgTpEnum.DBT_REQUEST.getCode().equals(msgTp)) {
//            resMsgTp = MsgTpEnum.DBT_RESPONSE.getCode();
//        } else if (MsgTpEnum.HARD_WALLET_DBT_REQUEST.getCode().equals(msgTp)) {
//            resMsgTp = MsgTpEnum.HARD_WALLET_DBT_RESPONSE.getCode();
//        } else if (MsgTpEnum.CDT_REQUEST.getCode().equals(msgTp)) {
//            resMsgTp = MsgTpEnum.CDT_RESPONSE.getCode();
//        } else if (MsgTpEnum.CDT_COV_REQUREST.getCode().equals(msgTp)) {
//            resMsgTp = MsgTpEnum.CDT_COV_RESPONSE.getCode();
//        } else if (MsgTpEnum.COY_PAY_REQUEST.getCode().equals(msgTp)) {
//            resMsgTp = MsgTpEnum.COY_PAY_RESPONSE.getCode();
//        }
        return resMsgTp;
    }

    // 赋值全链路环境信息
    public static void setEnvInfo(String envInfo) {
        if (StringUtils.isBlank(envInfo) || null == ZoneClient.getInstance().getEnv()) {
            // 无环境信息，直接返回
            return;
        }

        try {
            String environment = ZoneClient.getInstance().getEnv().name();
            // 非测试环境直接返回
            if (StringUtils.isBlank(environment) || !StringUtils.equalsAnyIgnoreCase(environment,
                    EnvEnum.STABLE.name(), EnvEnum.DEV.name(), EnvEnum.SIT.name())) {
                return;
            }

            //STABLE和SII环境
            if (StringUtils.equalsAnyIgnoreCase(envInfo, EnvEnum.STABLE.name(), EnvEnum.SIT.name())) {
                TraceContext.set(CommonConstant.ENVIRONMENT, envInfo);
                log.info("set environment succ env={}", envInfo);
            } else {
                //DEV环境信息存储规则（"STABLE/DEV|DEV-GROUP"）
                String[] env = envInfo.split("-", 2);
                if (env.length == 2) {
                    TraceContext.set(CommonConstant.DEV_GROUP, env[1]);
                    TraceContext.set(CommonConstant.ENVIRONMENT, EnvEnum.DEV.name());
                    log.info("set dev environment succ env={}, group={}", env[0], env[1]);
                } else {
                    log.error("split envInfo error!!");
                }
            }
        } catch (Exception e) {
            log.error("set envInfo error!!", e);
        }
    }

    // 获取环境信息，存入数据库环境信息字段
    public static String getEnvInfo() {
        try {
            EnvEnum envEnum = ZoneClient.getInstance().getEnv();
            if (null == envEnum) {
                return null;
            }

            String environment = ZoneClient.getInstance().getEnv().name();
            // 环境标为空直接返回
            if (StringUtils.isBlank(environment)) {
                return null;
            }
            //预发环境、生产环境、SIT环境返回环境信息
            if (StringUtils.equalsIgnoreCase(environment, EnvEnum.PROD.name())) {
                return EnvEnum.PROD.name();
            } else if (StringUtils.equalsIgnoreCase(environment, EnvEnum.PRE.name())) {
                return EnvEnum.PRE.name();
            } else if (StringUtils.equalsIgnoreCase(environment, EnvEnum.SIT.name())) {
                return EnvEnum.SIT.name();
            }
            // DEV、STABLE测试环境返回全链路环境标
            if (StringUtils.equalsAnyIgnoreCase(environment, EnvEnum.STABLE.name(), EnvEnum.DEV.name())) {
                String env = TraceContext.get(CommonConstant.ENVIRONMENT);
                if (StringUtils.isBlank(env)) {
                    // 全链路环境标为空，取本地环境位
                    env = environment;
                }
                if (EnvEnum.DEV.name().equalsIgnoreCase(env)) {
                    // 全链路环境标不为空且环境标为dev，取本地环境位拼接全链路环境标，兼容stable环境处理dev分组交易
                    env = environment + "|" + env + "-" + TraceContext.get(CommonConstant.DEV_GROUP);
                }
                log.info("test environment getEnvInfo succ env={}", env);
                return env;
            }
            return null;
        } catch (Exception e) {
            log.error("getEnvInfo error!!", e);
            return null;
        }
    }

    //判断当前环境信息是否需要处理该条信息，处理返回true，不需要处理返回false
    public static boolean checkEnvInfo(String envInfo) {
//        String environment = ZoneClient.getInstance().getEnv().name();
//        if (StringUtils.isBlank(environment)) {
//            log.error("environment is null");
//            return false;
//        }
//        if (StringUtils.equalsAnyIgnoreCase(environment, EnvEnum.PRE.name(), EnvEnum.PROD.name())) {
//            return true;
//        }
//        //环境信息相同直接返回true，SIT/STABLE/PROD/PRE
//        if (environment.equalsIgnoreCase(EnvEnum.SIT.name())) {
//            return StringUtils.isBlank(envInfo) || environment.equalsIgnoreCase(envInfo);
//        } else if (environment.equalsIgnoreCase(EnvEnum.DEV.name())) {
//            //DEV环境处理形同分组信息的数据
//            return StringUtils.isNotBlank(envInfo) && envInfo.contains(ZoneClient.getInstance().getDevGroup());
//        } else if (environment.equalsIgnoreCase(EnvEnum.STABLE.name())) {
//            //STABLE环境
//            return StringUtils.isBlank(envInfo) || envInfo.toLowerCase().contains(EnvEnum.STABLE.name().toLowerCase());
//        }
        return false;
    }

    // 结算状态转换：实时结算业务场景、链上发起交易场景
    public static String actgStsToBizSts(String accountingStatus) {
        switch (ActgStsEnum.getEnum(accountingStatus)) {
            case SUCCESS:
                return ClearingStatusEnum.SETTLED.getCode();
            case FAILED:
                return ClearingStatusEnum.FAILED.getCode();
            case QUEUED:
                return ClearingStatusEnum.SETTLE_QUEUE.getCode();
            case QUEUE_CANCELED:
                return ClearingStatusEnum.CANCELLED.getCode();
            case QUEUE_RETURNED:
                return ClearingStatusEnum.DAYEND_RETURN.getCode();
            default:
                // 返回处理中等状态为异常场景
                throw new DcepException(ErrorEnum.UNKNOWN_EXCEPTION.getCode(), "未知的结算钱包记账状态");
        }
    }

    // 根据时常设置下一次查询频率
    public static Date queryRateSet(Date gmtCreate) {
        Date sysTeme = new Date();
        // 计算小时级时差
        long intervalToHours = (sysTeme.getTime() - gmtCreate.getTime()) / (1000 * 60 * 60);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sysTeme);

        // 1小时内
        if (intervalToHours < 1) {
            // 计算分钟级时差
            long intervalToMinute = (sysTeme.getTime() - gmtCreate.getTime()) / (1000 * 60);
            if (intervalToMinute < 5) {
                // 5分钟内7秒查询一次
                calendar.add(Calendar.SECOND, 7);
            } else {
                // 5分钟到1个小时内30秒查询一次
                calendar.add(Calendar.SECOND, 30);
            }
        } else if (intervalToHours == 1) {
            // 1小时到2个小时内5分钟查询一次
            calendar.add(Calendar.MINUTE, 5);
        } else if (intervalToHours == 2) {
            // 2小时到3个小时内10分钟查询一次
            calendar.add(Calendar.MINUTE, 10);
        } else if (intervalToHours > 2 && intervalToHours < 12) {
            // 3小时到12个小时内1小时查询一次
            calendar.add(Calendar.HOUR, 1);
        } else if (intervalToHours >= 12 && intervalToHours < 24) {
            // 12小时到24个小时内2小时查询一次
            calendar.add(Calendar.HOUR, 2);
        } else if (intervalToHours >= 24 && intervalToHours < 48) {
            // 24小时到48个小时内4小时查询一次
            calendar.add(Calendar.HOUR, 4);
        } else if (intervalToHours >= 48 && intervalToHours < 72) {
            // 48小时到72个小时内12小时查询一次
            calendar.add(Calendar.HOUR, 12);
        } else {
            // 72个小时后24小时查询一次
            calendar.add(Calendar.HOUR, 24);
        }

        return calendar.getTime();
    }
}
