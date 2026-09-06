/**
 * DCEP.com.cn Inc. Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.supergw.service.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.dcep.common.enums.ChannelEnums;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.supergw.common.config.BeansCondition;
import com.dcep.supergw.common.constant.Constant;
import com.dcep.supergw.common.enums.GwErrorEnum;
import com.dcep.supergw.common.exception.ExceptionUtil;
import com.dcep.supergw.common.exception.GwException;
import com.dcep.supergw.common.utils.SoapUtils;
import com.dcep.supergw.common.utils.ValidateUtils;
import com.dcep.supergw.manager.bo.ChannelContext;
import com.dcep.supergw.manager.channel.AbstractChannel;
import com.dcep.supergw.service.GwinService;
import java.util.Set;
import java.util.concurrent.RejectedExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author huyajun
 * @version $Id: MainInnerServiceImpl.java, v 0.1 2019年8月8日 下午3:29:44 Administrator Exp $
 */
@Service
@BeansCondition(name = "beans.service", havingValue = "GwinServiceImpl")
public class GwinServiceImpl implements GwinService {

    //实例化所有spring中托管的channels，并且已在构造函数中getChannel()建立好所有要用到的action
    @Autowired
    Set<AbstractChannel> channels;

    @Override
    @SentinelResource(value = "gwlimit", blockHandler = "handleException", blockHandlerClass = {ExceptionUtil.class})
    public void start(ChannelContext context) {
        try {
            //校验报文头及签名
            byte[] xml = (byte[]) context.getAttachment(Constant.SERIALIZATION);
            SoapHeader header = (SoapHeader) context.getAttachment(Constant.SOAP_HEADER);

            ValidateUtils.validateMsg(header, xml,
                (String) context.getAttachment(Constant.SIGNATURE));
            //报文反序列化
            EnvelopeDTO<?> dto = SoapUtils.toDto(header, xml);
            context.setAttachment(Constant.DESERIALIZATION, dto);
            //获取当前报文所需使用的channel名
            ChannelEnums channelEnums = dto.body().routeChannel(dto.getSoapHeader());
            //根据获取的channel名选定使用的channel实例
            AbstractChannel channel = getChannelInstance(channelEnums.getCode());
            //将channel实例设置进context
            context.setChannel(channel);
            //调用对应选定的channel类型的操作
            context.fireInvokeChannel();
        } catch (RejectedExecutionException e) {
            throw new GwException(GwErrorEnum.LIMITING_ERROR, e);
        }
    }

    /**
     *
     * @param className
     * @return
     */
    private AbstractChannel getChannelInstance(String className) {
        for (AbstractChannel channel : channels) {
            if (channel.getClass().getName().contains(className)) {
                return channel;
            }
        }
        throw new GwException(GwErrorEnum.SYSTEM_CONFIG_ERROR, "channel.classname[" + className + "]配置错误，找不到处理类");
    }
}
