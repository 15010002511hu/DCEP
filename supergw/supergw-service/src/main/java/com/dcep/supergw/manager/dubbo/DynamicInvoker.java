package com.dcep.supergw.manager.dubbo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.RpcException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import com.dcep.common.exception.DcepException;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.supergw.common.constant.Constant;
import com.dcep.supergw.common.enums.GwErrorEnum;
import com.dcep.supergw.common.exception.GwException;
import com.dcepex.trace.support.TraceContext;
import com.dubbo.ldc.ZoneClient;
import com.dubbo.ldc.constant.EnvEnum;
import com.dubbo.ldc.constant.LDCConstants;
import lombok.extern.slf4j.Slf4j;

/**
 * 动态调用Dubbo服务
 *
 * @author linlu
 * @date 20190809
 */
@Slf4j
@Service
public class DynamicInvoker implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    /**
     * 执行Dubbo服务
     */
    public static Object invokeDubbo(String service, String method, EnvelopeDTO<?> dto) {
        setDevGroup();
        return invoke(service, method, new Class[] {EnvelopeDTO.class}, new Object[] {dto});
    }

    /**
     * BUGFIXED-20250401: 解决一体化环境LDC取不到流量分组,导致无法从STABLE网关调用DEV服务问题
     */
    private static void setDevGroup() {
        String env = TraceContext.get(Constant.HTTP_FLOW_ENV);

        if (StringUtils.isNotBlank(env) && StringUtils.equalsAnyIgnoreCase(env,
                EnvEnum.STABLE.name(), EnvEnum.DEV.name(), EnvEnum.SIT.name())) {
            // 与本地当前实际环境进行匹配判断，如果不是stable和dev及sit，就打印告警并且不设置环境和分组信息
            String localEnv = ZoneClient.getInstance().getEnv().name();
            if (!StringUtils.equalsAnyIgnoreCase(localEnv, EnvEnum.STABLE.name(),
                    EnvEnum.DEV.name(), EnvEnum.SIT.name())) {
                log.warn("机构将{}的报文发送到{}了", env, localEnv);
                return;
            }

            RpcContext.getContext().setAttachment(
                    LDCConstants.TRACER_PREFIX + LDCConstants.DEV_GROUP,
                    TraceContext.get(Constant.HTTP_FLOW_GROUP));
            RpcContext.getContext()
                    .setAttachment(LDCConstants.TRACER_PREFIX + LDCConstants.ENVIRONMENT, env);
        }
    }

    /**
     * 执行Bean服务
     */
    public static Object invoke(String service, String method, Class<?>[] types, Object[] params) {
        try {
            Object instance = context.getBean(service);
            Method caller = instance.getClass().getMethod(method, types);
            return caller.invoke(instance, params);
        } catch (NoSuchMethodException e) {
            throw new GwException(GwErrorEnum.SYSTEM_CONFIG_ERROR, "未找到Dubbo服务方法", e);
        } catch (IllegalAccessException e) {
            throw new GwException(GwErrorEnum.MANAGER_MAN_ERROR, e);
        } catch (InvocationTargetException e) {
            // 将异常信息打印放到最前面，避免无法归类的异常时没有堆栈信息
            log.error("dubbo invoke failed, {}", e);
            if (e.getTargetException() instanceof DcepException) {
                DcepException dcepException = (DcepException) e.getTargetException();
                throw new GwException(dcepException);
            } else if (e.getTargetException() instanceof RpcException) {
                RpcException rpcException = (RpcException) e.getTargetException();
                GwErrorEnum code = GwErrorEnum.UNKNOWN_EXCEPTION;
                // 根据不同的错误码抛出不同的异常
                switch (rpcException.getCode()) {
                    // 没有服务提供方
                    case RpcException.NO_INVOKER_AVAILABLE_AFTER_FILTER:
                        code = GwErrorEnum.SYSTEM_MODULE_ERROR;
                        break;
                    // 超时
                    case RpcException.TIMEOUT_EXCEPTION:
                        code = GwErrorEnum.MANAGER_RPC_ERROR;
                        break;
                    // 网络异常
                    case RpcException.NETWORK_EXCEPTION:
                        code = GwErrorEnum.ENVIROMENT_ERROR;
                        break;
                    // 序列化异常
                    case RpcException.SERIALIZATION_EXCEPTION:
                        code = GwErrorEnum.SYSTEM_CONFIG_ERROR;
                        break;
                    default:
                        break;
                }
                throw new GwException(code);
            } else {
                throw new GwException(GwErrorEnum.MANAGER_MAN_ERROR);
            }
        } catch (Exception e) {
            log.error("dubbo invoke failed, {}", e);
            throw new GwException(GwErrorEnum.UNKNOWN_EXCEPTION);
        }
    }
}
