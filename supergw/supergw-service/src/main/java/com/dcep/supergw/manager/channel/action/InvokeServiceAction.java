package com.dcep.supergw.manager.channel.action;

import com.dcep.common.annotation.GwMethod;
import com.dcep.common.annotation.RpcInfo;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.Response;
import com.dcep.supergw.common.constant.Constant;
import com.dcep.supergw.common.exception.GwException;
import com.dcep.supergw.manager.bo.ChannelContext;
import com.dcep.supergw.manager.dubbo.DynamicInvoker;

/**
 * 普通调用内部系统Dubbo服务
 */
public class InvokeServiceAction extends AbstractAction {

    public InvokeServiceAction() {
        super("InvokeServiceAction");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void doInvoke(ChannelContext context) {
        //获取报文DTO
        EnvelopeDTO<?> dto = (EnvelopeDTO<?>) context.getAttachment(Constant.DESERIALIZATION);
        RpcInfo[] services = dto.body().routeServices(dto.getSoapHeader());
        for (RpcInfo service : services) {
            for (GwMethod method : service.methods()) {
                Response<EnvelopeDTO<GwDTO>> result = (Response<EnvelopeDTO<GwDTO>>) DynamicInvoker
                    .invokeDubbo(service.name(), method.name(), dto);
                if (!result.isSuccess()) {
                    throw new GwException(result.getErrorCode(), result.getErrorMsg());
                }
                context.setAttachment(Constant.DESERIALIZATION, result.getResult());
                context.setAttachment(Constant.SOAP_HEADER, result.getResult().getSoapHeader());
            }
        }
        context.fireInvokeCallBack();
    }
}
