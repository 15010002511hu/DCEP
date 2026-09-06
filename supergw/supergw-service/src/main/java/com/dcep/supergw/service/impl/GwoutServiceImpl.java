/**
 * DCEP.com.cn Inc. Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.supergw.service.impl;

import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.Response;
import com.dcep.common.model.msg.Dcep91100101DTO;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.supergw.api.GwoutService;
import com.dcep.supergw.common.aop.LogPointCut;
import com.dcep.supergw.common.config.BeansCondition;
import com.dcep.supergw.common.constant.Constant;
import com.dcep.supergw.common.enums.GwErrorEnum;
import com.dcep.supergw.common.exception.GwException;
import com.dcep.supergw.common.utils.GwMsgUtils;
import com.dcep.supergw.common.utils.InfoCacheUtils;
import com.dcep.supergw.common.utils.SoapUtils;
import com.dcep.supergw.common.utils.ValidateUtils;
import com.dcep.supergw.manager.bo.ChannelContext;
import com.dcep.supergw.manager.channel.AgencyProcessChannel;
import com.dcepex.trace.support.UDT;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 网关作为dubbo服务供内部系统调用时注册的dubbo服务,代理模式,AgencyProcessChannel
 *
 * @author huyajun
 */
@Slf4j
@DubboService(filter = {"providerRpcLogFilter"})
@BeansCondition(name = "beans.service", havingValue = "GwoutServiceImpl")
public class GwoutServiceImpl implements GwoutService {

    @Autowired
    AgencyProcessChannel channel;

    @SuppressWarnings("unchecked")
    @Override
    @LogPointCut
    public Response<EnvelopeDTO<GwDTO>> execute(EnvelopeDTO<GwDTO> req) {
        Response<EnvelopeDTO<GwDTO>> response = new Response<>();
        try {
            // 全链路业务监控增加，用于耗时统计
            UDT.addBizData(req.getSoapHeader());

            // 校验请求报文头
            ValidateUtils.validateEnvFlag(req.getSoapHeader());

            // 通道上下文初始化
            ChannelContext context = new ChannelContext();
            context.setAttachment(Constant.SOAP_HEADER, req.getSoapHeader());
            context.setAttachment(Constant.DESERIALIZATION, req);
            context.setChannel(channel);
            // 启动通道业务链处理
            context.fireInvokeAction();
            // 生成返回结果, 结果中包含响应报文DTO
            return new Response<>((EnvelopeDTO<GwDTO>) context.getAttachment(Constant.DESERIALIZATION));
        } catch (GwException e) {
            log.error("请求报文处理异常：", e);
            response.setErrorCode(e.getCode());
            response.setErrorMsg(e.getMessage());
        } catch (Exception e) {
            log.error("请求报文处理未知异常：", e);
            response.setErrorCode(GwErrorEnum.UNKNOWN_EXCEPTION.getCode());
            response.setErrorMsg(e.getMessage());
        }
        return response;
    }

    @Override
    public String send(String req) {
        log.warn("调用待作废接口: {}", req);
        SoapHeader header = null;
        try {
            //soap报文转dto
            header = SoapUtils.getSoapHeaderBean(req);
            EnvelopeDTO<GwDTO> reqDto = SoapUtils.toDto(header, req);

            //调用通用发报服务
            Response<EnvelopeDTO<GwDTO>> response = execute(reqDto);

            //dto转soap报文
            return SoapUtils.toXml(response.getResult());
        } catch (GwException e) {
            log.error("GwoutService.send: {}", e);
            //报文头解析失败, 直接抛出异常
            if (header == null) {
                throw e;
            }
            //组装911报文DTO
            EnvelopeDTO<Dcep91100101DTO> dcep911 = GwMsgUtils.dcep911(
                header.getMsgSN(),
                InfoCacheUtils.getPbocInf(),
                header.getSender(),
                e.getCode(),
                e.getDescription(),
                header.getSender(),
                e.getMessage()
            );
            //dto转soap报文
            return SoapUtils.toXml(dcep911);
        } catch (Throwable e) {
            log.error("GwoutService.send: {}", e);
            //报文头解析失败, 直接抛出异常
            if (header == null) {
                throw e;
            }
            //组装911报文DTO
            EnvelopeDTO<Dcep91100101DTO> dcep911 = GwMsgUtils.dcep911(
                header.getMsgSN(),
                InfoCacheUtils.getPbocInf(),
                header.getSender(),
                GwErrorEnum.UNKNOWN_EXCEPTION.getCode(),
                GwErrorEnum.UNKNOWN_EXCEPTION.getDescription(),
                header.getSender(),
                e.getMessage()
            );
            //dto转soap报文
            return SoapUtils.toXml(dcep911);
        }
    }
}
