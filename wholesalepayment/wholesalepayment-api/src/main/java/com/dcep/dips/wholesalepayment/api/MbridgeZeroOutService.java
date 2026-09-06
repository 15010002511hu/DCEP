package com.dcep.dips.wholesalepayment.api;

import com.dcep.common.exception.DcepException;
import com.dcep.common.model.Response;
import com.dcep.gateway.mcbdc.dto.soap.GenericEnvelopeDTO;
import com.dcep.gateway.mcbdc.dto.soap.GenericGwDTO;
import com.dubbo.ldc.ZoneRouter;

@ZoneRouter
public interface MbridgeZeroOutService {

    /**
     * 接收货币桥桥上清零结果通知，并同步调用结算钱包服务进行结算记账
     * @param genericGwDTO
     * @return
     * @throws DcepException
     */
    Response<GenericEnvelopeDTO<GenericGwDTO>> report(GenericEnvelopeDTO<GenericGwDTO> genericGwDTO) throws DcepException;
}
