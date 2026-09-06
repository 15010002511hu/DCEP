package com.dcep.supergw.manager.channel.action;

import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.msg.Dcep91100101DTO;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.supergw.common.constant.Constant;
import com.dcep.supergw.common.exception.GwException;
import com.dcep.supergw.common.utils.GwMsgUtils;
import com.dcep.supergw.common.utils.InfoCacheUtils;
import com.dcep.supergw.common.utils.SoapUtils;
import com.dcep.supergw.common.utils.ValidateUtils;
import com.dcep.supergw.manager.bo.ChannelContext;

public class CheckSignAction extends AbstractAction {

    public CheckSignAction() {
        super("CheckSignAction");
    }

    @Override
    public void doInvoke(ChannelContext context) {
        //报文头反序列化
        byte[] xml = (byte[]) context.getAttachment(Constant.SERIALIZATION);
        SoapHeader header = SoapUtils.getSoapHeaderBean(xml);
        context.setAttachment(Constant.SOAP_HEADER, header);
        context.setRequest(false);
        //校验报文头及签名
        ValidateUtils.validateMsg(header, xml, (String) context.getAttachment(Constant.SIGNATURE));
        //报文反序列化
        EnvelopeDTO<?> dto = SoapUtils.toDto(header, xml);
        context.setAttachment(Constant.SOAP_HEADER, header);
        context.setAttachment(Constant.DESERIALIZATION, dto);
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
            context.isRequest() ? header.getReceiver() : header.getSender(),
            e.getMessage()
        );
        //保存911报文DTO
        context.setAttachment(Constant.DESERIALIZATION, dto);
        context.setAttachment(Constant.SOAP_HEADER, dto.getSoapHeader());

        context.setExceptionCatched(true);
        context.fireInvokeCallBack();
    }
}
