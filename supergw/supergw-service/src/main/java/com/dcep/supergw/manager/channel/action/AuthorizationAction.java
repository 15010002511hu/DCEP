package com.dcep.supergw.manager.channel.action;

import com.dcep.common.annotation.GwMethod;
import com.dcep.common.annotation.RpcInfo;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.Response;
import com.dcep.common.model.msg.Dcep90000101DTO;
import com.dcep.supergw.common.constant.Constant;
import com.dcep.supergw.common.enums.GwErrorEnum;
import com.dcep.supergw.common.exception.GwException;
import com.dcep.supergw.manager.bo.ChannelContext;
import com.dcep.supergw.manager.dubbo.DynamicInvoker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthorizationAction extends AbstractAction {

    private final Logger log = LoggerFactory.getLogger(getClass());

    public AuthorizationAction() {
        super("AuthorizationAction");
    }

    @Override
    public void doInvoke(ChannelContext context) {
        //获取报文DTO
        EnvelopeDTO<?> dto = (EnvelopeDTO<?>) context.getAttachment(Constant.DESERIALIZATION);
        RpcInfo[] services = dto.body().routeServices(dto.getSoapHeader());
        for (RpcInfo service : services) {
            for (GwMethod method : service.methods()) {
                Response<EnvelopeDTO<GwDTO>> result = (Response<EnvelopeDTO<GwDTO>>) DynamicInvoker
                    .invokeDubbo(service.name(), method.name(), dto);
                log.info("Authorization action invoke service:{} , method:{},response:{}", service.methods(),
                    method.name(), result);
                if (!result.isSuccess()) {
                    throw new GwException(result.getErrorCode(), result.getErrorMsg());
                }
                GwDTO gwDTO = result.getResult().getSoapBody().getT();
                if (!(gwDTO instanceof Dcep90000101DTO)) {
                    //抛出异常
                    throw new GwException(GwErrorEnum.MANAGER_MAN_ERROR);
                }
                Dcep90000101DTO dcep900 = (Dcep90000101DTO) gwDTO;
                if (!"PR00".equals(dcep900.getCmonConfInf().getPrcSts())) {
                    //返回900
                    context.setAttachment(Constant.DESERIALIZATION, result.getResult());
                    context.setAttachment(Constant.SOAP_HEADER, result.getResult().getSoapHeader());
                    context.fireInvokeCallBack();
                }
            }
        }
        context.fireInvokeAction();
    }
}
