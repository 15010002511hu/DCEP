/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.aspect;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.dcep.common.exception.DcepException;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.Response;
import com.dcep.common.utils.DcepDigestLogUtils;
import com.dcep.dips.common.enums.ClearingProdErrorEnum;
import com.dcep.dips.wholesalepayment.common.constants.Constant;
import com.dcep.dips.wholesalepayment.common.utils.CommonUtil;
import com.dcep.dips.wholesalepayment.dto.acctrans.ZeroOutReqDTO;
import com.dcep.dips.wholesalepayment.dto.funding.IncreaseReqDTO;
import com.dcep.dips.wholesalepayment.dto.hvps.ClearReportReqDTO;
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
 * @version $Id: FundingAspect.java, v 0.1 2019年8月26日 下午7:43:49 huyajun Exp $
 */
@Order(value = 1)
@Slf4j
//@Aspect
//@Component
public class FundingAspect {

	Logger logger = LoggerFactory.getLogger("funding-digest");

	Logger timeoutLogger = LoggerFactory.getLogger("funding-timeout");

	@NacosValue(value = "${timeout_log_interval}", autoRefreshed = true)
	String TIMEOUT;

	@Autowired
	private Environment env;

	@Pointcut("execution(* com.dcep.dips.wholesalepayment.service.impl.FundingServiceImpl.increase(..)) " +
			"|| execution(* com.dcep.dips.wholesalepayment.service.impl.FundingServiceImpl.hvpsReport(..))" +
			"|| execution(* com.dcep.dips.wholesalepayment.service.impl.FundingServiceImpl.zeroOutApply(..))")
	public void pointcutAllExcludeDecrease() {
	}

	@Pointcut("execution(* com.dcep.dips.wholesalepayment.service.impl.FundingServiceImpl.decrease(..))")
	public void pointcutDecrease() {
	}

	@Around(value = "pointcutDecrease()")
	public Response<?> execute(ProceedingJoinPoint jp) {
		String className = null;
		EnvelopeDTO<GwDTO> reqDTO = null;
		String msgSn = null;
		long elapse = 0;

		try {
			LocalDateTime  startTime = LocalDateTime.now();

			Class[] parameterTypes = ((MethodSignature) jp.getSignature()).getParameterTypes();

			className = jp.getTarget().getClass().getSimpleName();
			Method method = jp.getTarget().getClass().getMethod(jp.getSignature().getName(), parameterTypes);
			String invocation = className + "." + method.getName();

			Object[] args = jp.getArgs();
			reqDTO = (EnvelopeDTO<GwDTO>) args[0];
			msgSn = reqDTO.getSoapBody().getT().fetchMsgId();

			// dto的msgSn值传入日志上下文中
			CommonUtil.LogMDC(msgSn);
			log.info("wholesalepayment start:[" + invocation + "],startTime:[" + startTime.toString() + "],param:" + reqDTO);

			Response<?> result = (Response<?>) jp.proceed();

			elapse = Duration.between(startTime, LocalDateTime.now()).toMillis();

			// 若耗时超过设置时间，打印至超时日志中
            if (elapse >= Integer.valueOf(TIMEOUT)) {
                timeoutLogger.info("wholesalepayment end:[" + invocation + "],elapse:" + elapse + "ms,result[" + msgSn + "]");
            }

            log.info("wholesalepayment end:[" + invocation + "],elapse:" + elapse + "ms,result[" + msgSn + "]:" + result);

			// 调用监控日志接入
			logInfoDigest(className, reqDTO, msgSn, elapse, WholesaleErrorEnum.BUSI_SUCCESS.getCode());

			return result;
		} catch (DcepException dcepException) {
			log.error("wholesalepayment error", dcepException);

			// 系统错误处理码打印摘要日志告警
			if (StringUtils.isNotBlank(dcepException.getCode())
					&& dcepException.getCode().startsWith(Constant.SYSTEM_ERROR_CODE)) {
				logInfoDigest(className, reqDTO, msgSn, elapse, dcepException.getCode()); // 调用监控日志接入
			}

			throw dcepException;
		} catch (Throwable e) {
			log.error("wholesalepayment error", e);
			logInfoDigest(className, reqDTO, msgSn, elapse, ClearingProdErrorEnum.UNKNOWN_EXCEPTION.getCode()); // 调用监控日志接入

			throw new DcepException(ClearingProdErrorEnum.UNKNOWN_EXCEPTION.getCode(),
		            ClearingProdErrorEnum.UNKNOWN_EXCEPTION.getDescription());
		}
	}

	@Around(value = "pointcutAllExcludeDecrease()")
	private Response<?> executeDubbo(ProceedingJoinPoint jp) {
		String className = null;
		String msgSn = null;
		long elapse = 0;

		String msgTp = "";
		String sender = "";
		String receiver = "";
		try {
			LocalDateTime  startTime = LocalDateTime.now();

			Class[] parameterTypes = ((MethodSignature) jp.getSignature()).getParameterTypes();

			className = jp.getTarget().getClass().getSimpleName();
			Method method = jp.getTarget().getClass().getMethod(jp.getSignature().getName(), parameterTypes);
			String invocation = className + "." + method.getName();

			if (parameterTypes[0].isAssignableFrom(IncreaseReqDTO.class)) {
				Object[] args = jp.getArgs();
				IncreaseReqDTO reqDTO = (IncreaseReqDTO) args[0];
				msgSn = String.format("%s.%s", reqDTO.getSendMemberId(), reqDTO.getMsgId());
				msgTp = reqDTO.getMsgTp();
				sender = reqDTO.getSendMemberId();
				receiver = reqDTO.getReceiveMemberId();
			} else if (parameterTypes[0].isAssignableFrom(ClearReportReqDTO.class)) {
				Object[] args = jp.getArgs();
				ClearReportReqDTO reqDTO = (ClearReportReqDTO) args[0];
				msgSn = String.format("%s.%s", reqDTO.getOrgnlSendPty(), reqDTO.getOrgnlMsgId());
				msgTp = reqDTO.getOrgnlMsgTp();
				sender = reqDTO.getOrgnlSendPty();
				receiver = "";
			} else if (parameterTypes[0].isAssignableFrom(ZeroOutReqDTO.class)) {
				Object[] args = jp.getArgs();
				ZeroOutReqDTO reqDTO = (ZeroOutReqDTO) args[0];
				msgSn = reqDTO.getTransId();
				msgTp = "hvps.112.001.01";
				sender = reqDTO.getDbtrClearingMemberId();
				receiver = reqDTO.getMemberId();
			} else {
				log.error("wholesalepayment error, aspect not implemented for method {}.", invocation);
				throw new DcepException(ClearingProdErrorEnum.UNKNOWN_EXCEPTION.getCode(),
						ClearingProdErrorEnum.UNKNOWN_EXCEPTION.getDescription());
			}

			// dto的msgSn值传入日志上下文中
			CommonUtil.LogMDC(msgSn);
			log.info("wholesalepayment start:[" + invocation + "],startTime:[" + startTime.toString() + "],param:" + jp.getArgs()[0]);

			Response<?> result = (Response<?>) jp.proceed();

			elapse = Duration.between(startTime, LocalDateTime.now()).toMillis();

			// 若耗时超过设置时间，打印至超时日志中
			if (elapse >= Integer.valueOf(TIMEOUT)) {
				timeoutLogger.info("wholesalepayment end:[" + invocation + "],elapse:" + elapse + "ms,result[" + msgSn + "]");
			}

			log.info("wholesalepayment end:[" + invocation + "],elapse:" + elapse + "ms,result[" + msgSn + "]:" + result);

			// 调用监控日志接入
			logInfoDigestDubbo(className, msgTp, sender, receiver, msgSn, elapse, WholesaleErrorEnum.BUSI_SUCCESS.getCode());

			return result;
		} catch (DcepException dcepException) {
			log.error("wholesalepayment error", dcepException);

			// 系统错误处理码打印摘要日志告警
			if (StringUtils.isNotBlank(dcepException.getCode())
					&& dcepException.getCode().startsWith(Constant.SYSTEM_ERROR_CODE)) {
				logInfoDigestDubbo(className, msgTp, sender, receiver, msgSn, elapse, dcepException.getCode()); // 调用监控日志接入
			}

			throw dcepException;
		} catch (Throwable e) {
			log.error("wholesalepayment error", e);
			logInfoDigestDubbo(className, msgTp, sender, receiver, msgSn, elapse, ClearingProdErrorEnum.UNKNOWN_EXCEPTION.getCode()); // 调用监控日志接入

			throw new DcepException(ClearingProdErrorEnum.UNKNOWN_EXCEPTION.getCode(),
					ClearingProdErrorEnum.UNKNOWN_EXCEPTION.getDescription());
		}
	}

	/**
	 * 打印监控日志。关注service的职责是否完成，不关注交易是否成功。
	 */
	private void logInfoDigest(String className, EnvelopeDTO<GwDTO> reqObj, String msgSn, long elapse, String code) {
		try {
			RpcContext context = RpcContext.getContext(); // 调用方信息
			String resultCode = "";

			if (code != null) {
				if (WholesaleErrorEnum.BUSI_SUCCESS.getCode().equals(code)) {
					resultCode = "0"; // 0 表示成功
				} else if (code.length() == Constant.ERROR_CODE_LENGTH) {
					resultCode = code.substring(Constant.DCEP.length()); // 取平台简称DCEP之后字符串
				} else {
					resultCode = code;// 不规范的错误码
				}
			}

            String resultLog = DcepDigestLogUtils.createDigestLog(Constant.SYSTEM,
                    ZoneClient.getInstance().getZoneName(), env.getProperty("spring.application.name"), className,
                    msgSn, reqObj.getSoapHeader().getMsgTp(), reqObj.getSoapHeader().getSender(),
                    reqObj.getSoapHeader().getReceiver(), context.getRemoteHost(), context.getLocalHost(),
                    context.getRemoteApplicationName(), elapse, resultCode, "");

			logger.info(resultLog);
		} catch (Exception e) {
			log.error("wholesalepayment error Digest log: ", e);
		}
	}

	/**
	 * 打印非机构测得监控摘要信息
	 */
	private void logInfoDigestDubbo(String className, String mgsTp, String sender, String receiver, String msgSn, long elapse, String code) {
		try {
			RpcContext context = RpcContext.getContext(); // 调用方信息
			String resultCode = "";

			if (code != null) {
				if (WholesaleErrorEnum.BUSI_SUCCESS.getCode().equals(code)) {
					resultCode = "0"; // 0 表示成功
				} else if (code.length() == Constant.ERROR_CODE_LENGTH) {
					resultCode = code.substring(Constant.DCEP.length()); // 取平台简称DCEP之后字符串
				} else {
					resultCode = code;// 不规范的错误码
				}
			}

			String resultLog = DcepDigestLogUtils.createDigestLog(Constant.SYSTEM,
					ZoneClient.getInstance().getZoneName(), env.getProperty("spring.application.name"), className,
					msgSn, mgsTp, sender, receiver, context.getRemoteHost(), context.getLocalHost(),
					context.getRemoteApplicationName(), elapse, resultCode, "");

			logger.info(resultLog);
		} catch (Exception e) {
			log.error("wholesalepayment error Digest log: ", e);
		}
	}
}
