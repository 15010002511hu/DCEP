/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.dcep.common.exception.DcepException;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.Response;
import com.dcep.common.utils.DcepDigestLogUtils;
import com.dcep.dips.common.enums.ClearingProdErrorEnum;
import com.dcep.dips.wholesalepayment.common.constants.Constant;
import com.dcep.dips.wholesalepayment.common.utils.CommonUtil;
import com.dcep.dips.wholesalepayment.dto.BaseDTO;
import com.dcep.dips.wholesalepayment.enums.WholesaleErrorEnum;
import com.dubbo.ldc.ZoneClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.rpc.RpcContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * 
 * @author huyajun
 * @version $Id: ClearingAspect.java, v 0.1 2019年8月26日 下午7:43:49 huyajun Exp $
 */
@Order(value = 2)
@Slf4j
@Aspect
@Component
public class InnerReqAspect {

	Logger logger = LoggerFactory.getLogger("wholesale-digest");
	Logger timeoutLogger = LoggerFactory.getLogger("wholesale-timeout");

	@NacosValue(value = "${timeout_log_interval}", autoRefreshed = true)
	String TIMEOUT;

	@Autowired
	private Environment env;


	@Pointcut("@annotation(com.dcep.dips.wholesalepayment.aspect.InnerReq)")
	public void pointcut() {
	}



	@Around(value = "pointcut()")
	public Object execute(ProceedingJoinPoint jp) throws Throwable {
		String className = null;
		String msgSn = null;
		BaseDTO reqObj = null;
		long elapse = 0;
		try {
			LocalDateTime  startTime = LocalDateTime.now();
			
			className = jp.getTarget().getClass().getSimpleName();
			Method method = jp.getTarget().getClass().getMethod(jp.getSignature().getName(),
					((MethodSignature) jp.getSignature()).getParameterTypes());
			String invocation = className + "." + method.getName();

			Object[] args = jp.getArgs();

			if(args[0] instanceof BaseDTO){
				reqObj = (BaseDTO) args[0];
				msgSn = reqObj.getReqMsgId();
			}

			// dto的msgSn值传入日志上下文中
			CommonUtil.LogMDC(msgSn);
			log.info("Wholesale inner Start:[" + invocation + "],startTime:[" + startTime.toString() + "],param:" + JSON.toJSONString(reqObj));

			Object result =	jp.proceed();
			elapse = Duration.between(startTime, LocalDateTime.now()).toMillis();
			
			// 若耗时超过设置时间，打印至超时日志中
            if (elapse >= Integer.valueOf(TIMEOUT)) {
                timeoutLogger.info("Wholesale inner end:[" + invocation + "],elapse:" + elapse + "ms,result[" + msgSn + "]");
            }

            log.info("Wholesale inner end:[" + invocation + "],elapse:" + elapse + "ms,result[" + msgSn + "]:" + result);
			// 调用监控日志接入
			logInfoDigest(className, reqObj, msgSn, elapse, WholesaleErrorEnum.BUSI_SUCCESS.getCode());

			return result;
		} catch (DcepException dcepException) {
			log.error("Wholesale inner error", dcepException);

			// 系统错误处理码打印摘要日志告警
			if (StringUtils.isNotBlank(dcepException.getCode())
					&& dcepException.getCode().startsWith(Constant.SYSTEM_ERROR_CODE)) {
				logInfoDigest(className, reqObj, msgSn, elapse, dcepException.getCode());// 调用监控日志接入
			}
			throw dcepException;
		} catch (Throwable e) {
			log.error("Wholesale inner error", e);
			logInfoDigest(className, reqObj, msgSn, elapse, ClearingProdErrorEnum.UNKNOWN_EXCEPTION.getCode());// 调用监控日志接入
			throw new DcepException(WholesaleErrorEnum.UNKNOWN_EXCEPTION.getCode(),
					WholesaleErrorEnum.UNKNOWN_EXCEPTION.getDescription());
		}

	}



	/**
	 * 打印监控日志。关注service的职责是否完成，不关注交易是否成功。
	 */
	private void logInfoDigest(String className, BaseDTO reqObj, String msgSn, long elapse, String code) {
		try {
			RpcContext context = RpcContext.getContext();// 调用方信息
			String resultCode = "";

			if (code != null) {
				if (ClearingProdErrorEnum.BUSI_SUCCESS.getCode().equals(code)) {
					resultCode = "0"; // 0 表示成功
				} else if (code.length() == Constant.ERROR_CODE_LENGTH) {
					resultCode = code.substring(Constant.DCEP.length());// 取平台简称DCEP之后字符串
				} else {
					resultCode = code;// 不规范的错误码
				}
			}

			String resultLog = DcepDigestLogUtils.createDigestLog(Constant.SYSTEM,
					ZoneClient.getInstance().getZoneName(), env.getProperty("spring.application.name"), className,
					msgSn, reqObj.getReqMsgTp(), reqObj.getReqSender(),
					reqObj.getReqReceiver(), context.getRemoteHost(), context.getLocalHost(),
					context.getRemoteApplicationName(), elapse, resultCode, "");

			logger.info(resultLog);
		} catch (Exception e) {
			log.error("Wholesale inner req error Digest log: ", e);
		}
	}

}
