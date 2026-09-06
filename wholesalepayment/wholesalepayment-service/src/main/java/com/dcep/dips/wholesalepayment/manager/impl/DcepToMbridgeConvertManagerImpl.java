package com.dcep.dips.wholesalepayment.manager.impl;

import com.dcep.common.enums.ErrorEnum;
import com.dcep.common.exception.DcepException;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.dips.wholesalepayment.dto.dc203.Dcep20301001DTO;
import com.dcep.dips.wholesalepayment.dto.dc213.Dcep21301001DTO;
import com.dcep.dips.wholesalepayment.manager.DcepToMbridgeConvertManager;
import com.dcep.dips.wholesalepayment.manager.convert.Dcep203ToMbridge200;
import com.dcep.dips.wholesalepayment.manager.convert.Dcep213ToMbridge201;
import com.dcep.gateway.mcbdc.dto.soap.GenericEnvelopeDTO;
import com.dcep.gateway.mcbdc.dto.soap.GenericGwDTO;
import org.springframework.stereotype.Component;

@Component
public class DcepToMbridgeConvertManagerImpl implements DcepToMbridgeConvertManager {
    @Override
    public GenericEnvelopeDTO<GenericGwDTO> convertRequest(EnvelopeDTO<GwDTO> dcepReqEnvelopeDTO) {
        GenericEnvelopeDTO<GenericGwDTO> mBridgeReqEnvelopeDTO;
        if (dcepReqEnvelopeDTO.body() instanceof Dcep20301001DTO) {
            // 转货币桥200报文
            mBridgeReqEnvelopeDTO = Dcep203ToMbridge200.convert(dcepReqEnvelopeDTO);
        } else if (dcepReqEnvelopeDTO.body() instanceof Dcep21301001DTO) {
            // 转货币桥201报文
            mBridgeReqEnvelopeDTO = Dcep213ToMbridge201.convert(dcepReqEnvelopeDTO);
        } else {
            // 内部错误
            throw new DcepException(ErrorEnum.REQUEST_PARAM_ILLEGAL.getCode(),
                    "dcep req DTO type(" + dcepReqEnvelopeDTO.body().getClass().getName() + ")error");
        }
        return mBridgeReqEnvelopeDTO;
    }

    @Override
    public GenericEnvelopeDTO<GenericGwDTO> requestSupplement(GenericEnvelopeDTO<GenericGwDTO> mBridgeReqEnvelopeDTO, EnvelopeDTO<GwDTO> dcepReqEnvelopeDTO, String mcbsMsgId) {
        if (dcepReqEnvelopeDTO.body() instanceof Dcep20301001DTO) {
            // 补充货币桥200报文要素
            Dcep203ToMbridge200.supplement(mBridgeReqEnvelopeDTO, mcbsMsgId);
        } else if (dcepReqEnvelopeDTO.body() instanceof Dcep21301001DTO) {
            // 补充货币桥201报文要素
            Dcep213ToMbridge201.supplement(mBridgeReqEnvelopeDTO, mcbsMsgId);
        } else {
            // 内部错误
            throw new DcepException(ErrorEnum.REQUEST_PARAM_ILLEGAL.getCode(),
                    "dcep req DTO type" + dcepReqEnvelopeDTO.body().getClass().getName() + ")error");
        }
        return mBridgeReqEnvelopeDTO;
    }
}
