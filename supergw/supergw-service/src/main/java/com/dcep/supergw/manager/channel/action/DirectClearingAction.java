package com.dcep.supergw.manager.channel.action;

import com.dcep.common.annotation.RpcInfo;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.Response;
import com.dcep.common.model.msg.Dcep91100101DTO;
import com.dcep.supergw.common.constant.Constant;
import com.dcep.supergw.common.enums.GwErrorEnum;
import com.dcep.supergw.common.exception.GwException;
import com.dcep.supergw.manager.bo.ChannelContext;
import com.dcep.supergw.manager.dubbo.DynamicInvoker;

/**
 * @author : maxinyu
 * @version : DirectClearingAction.java v 0.1 2021-04-19
 * @description :
 */
public class DirectClearingAction extends AbstractAction {

    public DirectClearingAction() {
        super("DirectClearingAction");
    }

    @Override
    public void doInvoke(ChannelContext context) {
        Response<?> result = null;
        //获取DTO
        EnvelopeDTO<?> envelopeDTO = (EnvelopeDTO<?>) context.getAttachment(Constant.DESERIALIZATION);
        GwDTO dto = envelopeDTO.body();
        //若机构接收方机构应答了911报文，中断CooperateBankChannel上的Action执行
        if (dto instanceof Dcep91100101DTO) {
            context.fireInvokeCallBack();
            return;
        }
        //获取dto上面的注解 
        RpcInfo[] services = dto.routeServices(envelopeDTO.getSoapHeader());
        //获取注解上的服务信息，rpc调用
        result = (Response<?>) DynamicInvoker
            .invokeDubbo(services[0].name(), services[0].methods()[0].name(), envelopeDTO);

        //若返回值为null,抛出DCEPS9004异常，框架返回S9004的911报文
        if (result == null) {
            throw new GwException(GwErrorEnum.MANAGER_MAN_TIMEOUT);
        }
        //修改Context中Soapheader
        context.setAttachment(Constant.SOAP_HEADER, ((EnvelopeDTO<?>) result.getResult()).getSoapHeader());

        context.setAttachment(Constant.DESERIALIZATION, result.getResult());

        //转接返回的业务应答状态为成功，继续执行下一个Action
        if (result.isSuccess()) {
            context.fireInvokeAction();
        } else {
            //若转接业务应答状态为失败，中断CooperateBankChannel上的Action执行
            context.fireInvokeCallBack();
        }

    }
}
