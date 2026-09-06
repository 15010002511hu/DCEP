package com.dcep.dips.wholesalepayment.manager.impl;

import com.dcep.common.enums.ErrorEnum;
import com.dcep.common.exception.DcepException;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.dips.wholesalepayment.dto.dc213.Dcep21301001DTO;
import com.dcep.dips.wholesalepayment.manager.MbridgeToDcepConvertManager;
import com.dcep.dips.wholesalepayment.manager.convert.Mbridge201ToDcep213;
import com.dcep.dips.wholesalepayment.manager.convert.MbridgeToDcep203;
import com.dcep.gateway.mcbdc.dto.mcbs200.Mcbs20000101DTO;
import com.dcep.gateway.mcbdc.dto.mcbs201.Mcbs20100101DTO;
import com.dcep.gateway.mcbdc.dto.mcbs202.Mcbs20200101DTO;
import com.dcep.gateway.mcbdc.dto.mcbs203.Mcbs20300101DTO;
import com.dcep.gateway.mcbdc.dto.soap.GenericEnvelopeDTO;
import com.dcep.gateway.mcbdc.dto.soap.GenericGwDTO;
import org.springframework.stereotype.Service;

@Service
public class MbridgeToDcepConvertManagerImpl implements MbridgeToDcepConvertManager {
    @Override
    public EnvelopeDTO<GwDTO> convertRequest(GenericEnvelopeDTO<GenericGwDTO> mBridgeReqEnvelopeDTO) {
        EnvelopeDTO<GwDTO> dcepReqEnvelopeDTO = new EnvelopeDTO<>();
        if (mBridgeReqEnvelopeDTO.body() instanceof Mcbs20000101DTO
                || mBridgeReqEnvelopeDTO.body() instanceof Mcbs20200101DTO) {
            // 转DCEP203报文
            dcepReqEnvelopeDTO = MbridgeToDcep203.convert(mBridgeReqEnvelopeDTO);
        } else if (mBridgeReqEnvelopeDTO.body() instanceof Mcbs20100101DTO ) {
            // 转DCEP213报文
            Mbridge201ToDcep213.convert(mBridgeReqEnvelopeDTO, dcepReqEnvelopeDTO);
        } else {
            // 内部错误
            throw new DcepException(ErrorEnum.REQUEST_PARAM_ILLEGAL.getCode(),
                    "mBridge req DTO type(" + mBridgeReqEnvelopeDTO.body().getClass().getName() + ")error");
        }
        return dcepReqEnvelopeDTO;
    }
}
