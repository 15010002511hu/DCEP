/*
 * pbcdci.cn Inc.
 * Copyright © 2023 PBCDCI All Rights Reserved.
 */
package com.dcep.supergw.manager.dubbo.filter;

import com.dcep.common.enums.ErrorEnum;
import com.dcep.common.exception.DcepException;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.Response;
import com.dcep.common.utils.DcepDigestLogUtils;
import com.dcep.common.utils.ExceptionCodeUtils;
import com.dcep.supergw.common.utils.LoggerUtils;
import com.dcepex.trace.support.UDT;
import com.dubbo.ldc.ZoneClient;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * 网关服务端摘要日志记录Filter
 *
 * @author laimincai
 * @date 2023/11/02
 */
public class ProviderRpcLogFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger("digest");

    // 当前系统名称
    private static final String SYSTEM = "hlht";

    @SuppressWarnings("unchecked")
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {

        long startTime = System.currentTimeMillis();

        // 获取入参信息，记录MDC
        EnvelopeDTO<GwDTO> dto = (EnvelopeDTO<GwDTO>) (invocation.getArguments()[0]);
        LoggerUtils.mdc(dto.getSoapHeader());

        // 获取调用方信息
        String clientIp = RpcContext.getContext().getRemoteHost();
        String clientAppName = RpcContext.getContext().getAttachment("remote.application");

        // 获取zone名称
        String zoneName = ZoneClient.getInstance().getZoneName();
        if (StringUtils.isEmpty(zoneName)) {
            zoneName = "";
        }

        // 获取当前应用名字
        String appName = invoker.getUrl().getParameter("application");
        String localIp = RpcContext.getContext().getLocalHost();

        // 获取调用接口和方法名
        String interfaceName = invoker.getInterface().getName();
        String methodName = invocation.getMethodName();
        String invoInfo = interfaceName + "." + methodName;

        // 执行下一个Filter
        Result result = invoker.invoke(invocation);
        try {

            // 获取业务处理结果码
            String resultCode = getResultCode(result);

            UDT.setResultCodeTag(resultCode);

            // 辅助信息
            String otherInfo = "";

            long elapsed = System.currentTimeMillis() - startTime;

            // 摘要日志信息
            String digest = DcepDigestLogUtils.createDigestLog(SYSTEM, zoneName, appName, invoInfo, dto, clientIp,
                localIp, clientAppName, elapsed, resultCode, otherInfo);

            LOGGER.info(digest);
        } catch (Exception e) {
            LOGGER.error("record rpc log error:{}", e);
        }
        MDC.clear();
        return result;

    }


    @SuppressWarnings("unchecked")
    private String getResultCode(Result result) {
        String resultCode = "-";

        if (result == null) {
            return resultCode + ErrorEnum.UNKNOWN_EXCEPTION.getCode();
        }

        if (result.hasException()) {
            Object exception = result.getException();
            if (exception instanceof DcepException) {
                resultCode = resultCode + ((DcepException) exception).getCode();
            } else if (exception instanceof RpcException) {
                resultCode = resultCode + ExceptionCodeUtils.rpcExceptionCode((RpcException) exception);
            } else {
                resultCode = resultCode + ErrorEnum.UNKNOWN_EXCEPTION.getCode();
            }
            return resultCode;
        }

        // 无异常，响应不符合要求
        if ((result.getValue() == null) || !(result.getValue() instanceof Response)) {
            return "";
        }

        // 响应信息
        Object obj = ((Response<EnvelopeDTO<GwDTO>>) result.getValue()).getResult();
        resultCode = obj instanceof EnvelopeDTO ? ((EnvelopeDTO<GwDTO>) obj).body().fetchResultCode()
            : obj instanceof GwDTO ? ((GwDTO) obj).fetchResultCode() : resultCode;

        return resultCode;
    }
}
