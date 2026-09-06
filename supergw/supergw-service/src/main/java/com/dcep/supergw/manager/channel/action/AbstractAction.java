package com.dcep.supergw.manager.channel.action;

import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.msg.Dcep91100101DTO;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.supergw.common.constant.Constant;
import com.dcep.supergw.common.exception.GwException;
import com.dcep.supergw.common.utils.GwMsgUtils;
import com.dcep.supergw.common.utils.InfoCacheUtils;
import com.dcep.supergw.manager.Action;
import com.dcep.supergw.manager.bo.ChannelContext;
import lombok.Getter;
import lombok.Setter;

/**
 * 责任链
 *
 * @author linlu
 * @date 20190822
 */
@Getter
@Setter
public abstract class AbstractAction implements Action {

    private AbstractAction nextAction = null;
    private AbstractAction preAction = null;

    private final String actionName;

    public AbstractAction(String actionName) {
        this.actionName = actionName;
    }


    //action的具体调用在此抽象方法的具体实现子类中
    @Override
    public void doInvoke(ChannelContext context) {
        //继续下一个action的调用
        context.fireInvokeAction();
    }

    @Override
    public void doException(ChannelContext context, GwException e) {
        //获取当前报文的报文头
        SoapHeader header = (SoapHeader) context.getAttachment(Constant.SOAP_HEADER);
        //生成911报文DTO
        EnvelopeDTO<Dcep91100101DTO> dto = GwMsgUtils.dcep911(
            header.getMsgSN(),
            InfoCacheUtils.getPbocInf(),
            context.isRequest() ? header.getSender() : header.getReceiver(),
            e.getCode(),
            e.getDescription(),
            InfoCacheUtils.getPbocInf(),
            e.getMessage()
        );
        //保持911报文DTO
        context.setAttachment(Constant.DESERIALIZATION, dto);
        context.setAttachment(Constant.SOAP_HEADER, dto.getSoapHeader());

        context.setExceptionCatched(true);
        context.fireInvokeCallBack();
    }

    @Override
    public void doCallBack(ChannelContext context) {
        context.fireInvokeCallBack();
    }
}
