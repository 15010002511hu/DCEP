/**
 * DCEP.com.cn Inc. Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.supergw.common.utils;

import com.dcep.common.Constants.CommonConstant;
import com.dcep.common.enums.MessageTypeEnum;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.msg.Dcep90000101DTO;
import com.dcep.common.model.msg.Dcep91100101DTO;
import com.dcep.common.model.soap.SoapBody;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.utils.DcepDateUtils;
import com.dcep.supergw.common.enums.GwErrorEnum;
import com.dcep.supergw.common.exception.GwException;
import com.dcepex.trace.support.TraceContext;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * 网关内部 返回的报文：丢弃报文等。
 *
 * @author huyajun
 * @version $Id: GwOwnMsgCreateUtils.java, v 0.1 2019年7月17日 下午5:23:25  huyajun  Exp $
 */
@Slf4j
public class GwMsgUtils {

    private final static int DCEP_911_DETAIL_MAX = 511;

    private final static String VER = "01";

    /**
     * 生成丢弃报文
     */
    public static String dcep911(SoapHeader header, GwErrorEnum error, String detail) {
        return GwMsgUtils.dcep911(header.getMsgSN(),
            InfoCacheUtils.getPbocInf(),
            header.getSender(),
            error,
            header.getSender(),
            detail);
    }

    /**
     * 生成丢弃报文
     */
    public static String dcep911(SoapHeader header, String code, String msg, String detail) {
        return SoapUtils.toXml(GwMsgUtils.dcep911(header.getMsgSN(),
            InfoCacheUtils.getPbocInf(),
            header.getSender(),
            code,
            msg,
            header.getSender(),
            detail));
    }

    /**
     * 生成丢弃报文
     */
    public static String dcep911(String msgSn, String sender, String receiver, GwErrorEnum error, String actor,
        String detail) {
        return SoapUtils.toXml(dcep911(msgSn,
            sender,
            receiver,
            error.getCode(),
            error.getDescription(),
            actor,
            detail));
    }

    /**
     * 生成丢弃报文
     */
    public static EnvelopeDTO<Dcep91100101DTO> dcep911(String msgSn, String sender, String receiver, String code,
        String msg, String actor, String detail) {
        EnvelopeDTO<Dcep91100101DTO> dto = new EnvelopeDTO<>();

        SoapHeader soapHeader = new SoapHeader(VER, DcepDateUtils.getDcepDateStrNow(),
            MessageTypeEnum.DCEP_911_001_01.getCode(), msgSn, sender, receiver);

        SoapBody<Dcep91100101DTO> soapBody = new SoapBody<>();
        Dcep91100101DTO fault = new Dcep91100101DTO();
        fault.setFaultcode(code);
        fault.setFaultstring(msg);
        fault.setFaultactor(actor);
        if (null != detail) {
            fault.setDetail(detail.length() <= 256 ? detail : detail.substring(0, DCEP_911_DETAIL_MAX));
        }
        soapBody.setT(fault);

        dto.setSoapHeader(soapHeader);
        dto.setSoapBody(soapBody);
        SoapHeaderUtils.setPbocSignSn(dto);

        return dto;
    }

    /**
     * 针对上行报文返回结果报文
     *
     * @param resp
     * @param inst TODO
     */
    public static void writerXmlToInst(HttpServletResponse resp, String xml, String inst) {
        if (log.isDebugEnabled()) {
            log.debug("GwMsgUtils.writerXmlToInst: xml = {}", xml);
        }
        String signStr = null;
        try {
            PrintWriter writer = resp.getWriter();

            String certId = InfoCacheUtils.getPbocSignCertDnOrNickname(inst);
            signStr = ValidateUtils.sign(xml, certId);
            resp.setHeader("Signature", signStr);
            resp.setHeader(CommonConstant.LOAD_TRAFFIC_KEY,
                TraceContext.get(CommonConstant.LOAD_TRAFFIC_KEY));
            writer.write(xml);
            writer.flush();
        } catch (IOException e) {
            log.error("MainServer.writerXmlToInst error: {}", e);
            throw new GwException(GwErrorEnum.SENDER_ERROR, e);
        } finally {
            //返回报文打印日志
            LoggerUtils.logMsg(InfoCacheUtils.getPbocInf(), signStr, xml);
        }

    }

    /**
     * @param msgSn
     * @param sender
     * @param receiver
     * @param dcep90000101DTO
     * @return com.dcep.common.model.EnvelopeDTO<com.dcep.common.model.msg.Dcep90000101DTO>
     * @description 生成900报文dto
     * @date 2019/9/23 14:33
     */
    public static EnvelopeDTO<Dcep90000101DTO> dcep900Dto(String msgSn, String sender, String receiver,
        Dcep90000101DTO dcep90000101DTO) {
        SoapHeader soapHeader = new SoapHeader(VER, DcepDateUtils.getDcepDateStrNow(),
            MessageTypeEnum.DCEP_900_001_01.getCode(), msgSn, sender, receiver);

        SoapBody<Dcep90000101DTO> soapBody = new SoapBody<>();
        soapBody.setT(dcep90000101DTO);

        EnvelopeDTO<Dcep90000101DTO> envelopeDTO = new EnvelopeDTO<>();
        envelopeDTO.setSoapHeader(soapHeader);
        envelopeDTO.setSoapBody(soapBody);
        return envelopeDTO;
    }
}
