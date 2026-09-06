package com.dcep.supergw.manager.channel.action;

import com.dcep.clearing.dto.ClearingDTO;
import com.dcep.clearing.dto.ClearingStatus;
import com.dcep.common.annotation.GwMethod;
import com.dcep.common.annotation.RpcInfo;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.Response;
import com.dcep.common.model.msg.Dcep90000101DTO;
import com.dcep.common.model.msg.Dcep90200101DTO;
import com.dcep.common.model.msg.Dcep91100101DTO;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.supergw.common.constant.Constant;
import com.dcep.supergw.common.enums.GwErrorEnum;
import com.dcep.supergw.common.exception.GwException;
import com.dcep.supergw.common.utils.GwMsgUtils;
import com.dcep.supergw.common.utils.InfoCacheUtils;
import com.dcep.supergw.manager.bo.ChannelContext;
import com.dcep.supergw.manager.dubbo.DynamicInvoker;

/**
 * 调用清算服务
 *
 * @author linlu
 * @date 20190822
 */
public class ClearingServiceAction extends AbstractAction {

    public ClearingServiceAction() {
        super("ClearingServiceAction");
    }

    @SuppressWarnings("unchecked")
    private Response<GwDTO> invokeDubbo(ChannelContext context) {
        Response<GwDTO> result = null;
        //获取报文DTO
        EnvelopeDTO<?> dto = (EnvelopeDTO<?>) context.getAttachment(Constant.DESERIALIZATION);
        //获取报文DTO上Gateway注解
        RpcInfo[] services = dto.body().routeServices(dto.getSoapHeader());
        for (RpcInfo service : services) {
            for (GwMethod method : service.methods()) {
                result = (Response<GwDTO>) DynamicInvoker.invokeDubbo(service.name(),
                    method.name(),
                    dto);
                if (!result.isSuccess()) {
                    throw new GwException(result.getErrorCode(), result.getErrorMsg());
                }
            }
        }

        if (result == null) {
            result = new Response<>(dto.body());
        }
        /*if (null == result) {
            context.fireInvokeException(new GwException(GwErrorEnum.MANAGER_RPC_ERROR, "No RpcInfo Found!"));
        }*/
        return result;
    }

    private void setResult(ChannelContext context, Dcep90000101DTO dto) {
        //获取当前报文的报文头
        SoapHeader header = (SoapHeader) context.getAttachment(Constant.SOAP_HEADER);
        //生成911报文DTO
        EnvelopeDTO<Dcep90000101DTO> dto900 = GwMsgUtils.dcep900Dto(
            header.getMsgSN(),
            InfoCacheUtils.getPbocInf(),
            dto.getGrpHdr().getInstdPty().getInstdDrctPty(),
            dto);
        //保持911报文DTO
        context.setAttachment(Constant.DESERIALIZATION, dto900);
        context.setAttachment(Constant.SOAP_HEADER, dto900.getSoapHeader());
    }

    private void setResult(ChannelContext context, ClearingStatus dto) {

            ClearingDTO clearing = (ClearingDTO) ((EnvelopeDTO<?>) context.getAttachment(
                Constant.DESERIALIZATION)).body();
            Response<ClearingStatus> clearingStatusResponse = new Response<>((ClearingStatus) dto);
            clearing.fillBatchId(clearingStatusResponse);
            clearing.fillPlatPrcSts();

    }

    private void setResult(ChannelContext context, com.dcep.dips.wholesalepayment.dto.ClearingStatus dto) {

        com.dcep.dips.wholesalepayment.dto.ClearingDTO clearing = (com.dcep.dips.wholesalepayment.dto.ClearingDTO) ((EnvelopeDTO<?>) context.getAttachment(
            Constant.DESERIALIZATION)).body();
        Response<com.dcep.dips.wholesalepayment.dto.ClearingStatus> clearingStatusResponse = new Response<>((com.dcep.dips.wholesalepayment.dto.ClearingStatus) dto);
        clearing.fillBatchId(clearingStatusResponse);
    }

    @Override
    public void doInvoke(ChannelContext context) {
        GwDTO dto = invokeDubbo(context).getResult();
        if (dto instanceof Dcep90000101DTO) {
            setResult(context, (Dcep90000101DTO) dto);
            context.fireInvokeCallBack();
        } else if (dto instanceof ClearingStatus) {
            setResult(context, (ClearingStatus) dto);
            context.fireInvokeAction();
        } else if (dto instanceof com.dcep.dips.wholesalepayment.dto.ClearingStatus) {
            setResult(context, (com.dcep.dips.wholesalepayment.dto.ClearingStatus) dto);
            context.fireInvokeAction();
        } else {
            context.fireInvokeException(new GwException(GwErrorEnum.MANAGER_RPC_ERROR, "Result Illegal!"));
        }
    }

    @Override
    public void doCallBack(ChannelContext context) {
        if (!context.isExceptionCatched()) {
            GwDTO dto = invokeDubbo(context).getResult();
            if (dto instanceof Dcep90000101DTO) {
                setResult(context, (Dcep90000101DTO) dto);
                context.fireInvokeCallBack();
            } else if (dto instanceof ClearingStatus) {
                setResult(context, (ClearingStatus) dto);
                context.fireInvokeCallBack();
            } else if (dto instanceof com.dcep.dips.wholesalepayment.dto.ClearingStatus) {
                setResult(context, (com.dcep.dips.wholesalepayment.dto.ClearingStatus) dto);
                context.fireInvokeCallBack();
            } else if (dto instanceof Dcep90200101DTO) {
                context.fireInvokeCallBack();
            } else if (dto instanceof Dcep91100101DTO) {
                context.fireInvokeCallBack();
            } else {
                context.fireInvokeException(new GwException(GwErrorEnum.MANAGER_RPC_ERROR, "Result Illegal!"));
            }
        } else {
            context.fireInvokeCallBack();
        }
    }
}