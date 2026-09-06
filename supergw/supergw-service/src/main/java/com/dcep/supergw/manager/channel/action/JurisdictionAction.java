package com.dcep.supergw.manager.channel.action;

import com.dcep.common.annotation.CheckBizCode;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.msg.Dcep91100101DTO;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.infocache.BizRelationCache;
import com.dcep.infocache.MessagePermissionCache;
import com.dcep.supergw.common.constant.Constant;
import com.dcep.supergw.common.enums.GwErrorEnum;
import com.dcep.supergw.common.enums.TradeParamWarningType;
import com.dcep.supergw.common.exception.GwException;
import com.dcep.supergw.common.utils.GwMsgUtils;
import com.dcep.supergw.common.utils.InfoCacheUtils;
import com.dcep.supergw.common.utils.ValidateUtils;
import com.dcep.supergw.manager.bo.ChannelContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : maxinyu
 * @version : JurisdictionAction.java v 0.1 2019-12-05
 * @description : 权限检查Action
 */
@Slf4j
public class JurisdictionAction extends AbstractAction {

    public JurisdictionAction() {
        super("JurisdictionAction");
    }

    @Override
    public void doInvoke(ChannelContext context) {
        //获取当前报文的报文头
        SoapHeader header = (SoapHeader) context.getAttachment(Constant.SOAP_HEADER);

        //获取信息缓存权限校验Bean
        MessagePermissionCache permissionCache = MessagePermissionCache.getInstance();
        //发报机构权限校验
        if (!permissionCache.getSenderMessagePermission(header.getSender(), header.getMsgTp())) {
            throw new GwException(GwErrorEnum.SENDER_NON_PRIVILEGED);
        }
        //收报机构权限校验
        if (!permissionCache.getReciverMessagePermission(header.getReceiver(), header.getMsgTp())) {
            throw new GwException(GwErrorEnum.RECEIVER_NON_PRIVILEGED);
        }

        // 只有校验开关打开时才走此逻辑
        if (!TradeParamWarningType.NO_CHECK.getType()
                .equals(ValidateUtils.getTradeparamInvalidWarningType())) {
            // 是否有发起和接收此类业务的权限
            checkbizTypeCodePermission(context);
        }

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
                header.getSender(),
                e.getCode(),
                e.getDescription(),
                header.getSender(),
                e.getMessage()
        );
        //保存911报文DTO
        context.setAttachment(Constant.DESERIALIZATION, dto);
        context.setAttachment(Constant.SOAP_HEADER, dto.getSoapHeader());
        //针对C->A模式，不走Callback
        if (dto.getSoapHeader().getReceiver().equals(InfoCacheUtils.getPbocInf())) {
            return;
        }
        context.setExceptionCatched(true);
        context.fireInvokeCallBack();
    }

    /**
     * 校验机构是否有发送该类业务类型的权限
     *
     * @param context 上下文信息
     */
    private void checkbizTypeCodePermission(ChannelContext context) {
        //获取当前报文的报文头
        SoapHeader header = (SoapHeader) context.getAttachment(Constant.SOAP_HEADER);

        //获取报文DTO
        EnvelopeDTO<?> dto = (EnvelopeDTO<?>) context.getAttachment(Constant.DESERIALIZATION);
        GwDTO body = dto.body();
        Class<? extends GwDTO> dtoClazz = body.getClass();
        if (dtoClazz.isAnnotationPresent(CheckBizCode.class)) {
            CheckBizCode annotation = dtoClazz.getAnnotation(CheckBizCode.class);
            String bizTypeCode = ValidateUtils.getExpressValue(annotation.bizTypeCode(), dto.getSoapBody().getT());

            BizRelationCache bizRelationCache = BizRelationCache.getInstance();

            // 发送方为央行时不校验权限
            if (InfoCacheUtils.getPbocInf().equals(header.getSender())) {
                return;
            }

            // 发送机构无权限
            if (!bizRelationCache.checkOrgBiz(header.getSender(), bizTypeCode)) {
                log.warn("机构：{} 无发送 {} 业务类型的权限", header.getSender(), bizTypeCode);
                if (TradeParamWarningType.CHECK_AND_BLOCK.getType().equals(ValidateUtils.getTradeparamInvalidWarningType())) {
                    throw new GwException(GwErrorEnum.SENDER_NON_SUCH_BIZ_PRIVILEGED);
                }
            }

        }
    }

}
