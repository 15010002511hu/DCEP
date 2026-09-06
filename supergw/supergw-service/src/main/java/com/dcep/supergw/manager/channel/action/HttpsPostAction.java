package com.dcep.supergw.manager.channel.action;

import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.msg.Dcep91100101DTO;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.supergw.common.constant.Constant;
import com.dcep.supergw.common.exception.GwException;
import com.dcep.supergw.common.utils.GwMsgUtils;
import com.dcep.supergw.common.utils.InfoCacheUtils;
import com.dcep.supergw.common.utils.SoapUtils;
import com.dcep.supergw.manager.bo.ChannelContext;
import com.dcep.supergw.manager.https.HttpsClient;

/**
 * HTTPS服务
 *
 * @author linlu
 * @date 20190822
 */
public class HttpsPostAction extends AbstractAction {

    public HttpsPostAction() {
        super("HttpsPostAction");
    }

    //HttpsPostAction在调用链末尾，无下个action调用
    @Override
    public void doInvoke(ChannelContext context) {
        //获取报文DTO
        EnvelopeDTO<?> dto = (EnvelopeDTO<?>) context.getAttachment(Constant.DESERIALIZATION);
        //获取机构URL
        String url = InfoCacheUtils.getInstUrl(dto.getSoapHeader().getReceiver(),dto.getSoapHeader().getMsgTp());
        //发送报文并同步等待返回
        String draft = SoapUtils.toXml(dto);
        String signCert = InfoCacheUtils.getPbocSignCertDnOrNickname(dto.getSoapHeader().getReceiver());
        byte[] recvDraft = HttpsClient.post(url, dto.getSoapHeader(), draft, signCert);
        //解析Soap头
        SoapHeader header = SoapUtils.getSoapHeaderBean(recvDraft);

        /*20200930版本
         * 这里的header接下去不会用到，且代理模式下对应的receive有可能会是央行（比如下面的toDto异常时会封装911报文），
         * 从而导致获取加签证书失败，故去掉原设置新header的代码
         * context.setAttachment(Constant.SOAP_HEADER, header)
         * byduz
         */
        //设置返回参数
        context.setAttachment(Constant.SERIALIZATION, recvDraft);
        //报文反序列化
        context.setAttachment(Constant.DESERIALIZATION, SoapUtils.toDto(header, recvDraft));
    }

    @Override
    public void doException(ChannelContext context, GwException e) {
        //获取当前报文的报文头
        SoapHeader header = (SoapHeader) context.getAttachment(Constant.SOAP_HEADER);
        //生成911报文DTO
        EnvelopeDTO<Dcep91100101DTO> dto = GwMsgUtils.dcep911(
            header.getMsgSN(),
            InfoCacheUtils.getPbocInf(),
            //0930版本证书获取修改，原来的逻辑会导致尝试去获取央行到央行的证书（该证书并不会被用到） by duz
            header.getReceiver(),
            e.getCode(),
            e.getDescription(),
            header.getReceiver(),
            e.getMessage()
        );
        //保持911报文DTO
        context.setAttachment(Constant.DESERIALIZATION, dto);
        context.setAttachment(Constant.SOAP_HEADER, dto.getSoapHeader());
    }

}
