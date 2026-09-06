package com.dcep.dips.wholesalepayment.manager;

import com.dcep.common.model.Response;
import com.dcep.dips.wholesalepayment.dal.model.ZerooutCtrlDO;
import com.dcep.dips.wholesalepayment.dto.mcbs101.Mcbs10100101DTO;
import com.dcep.gateway.mcbdc.dto.soap.GenericEnvelopeDTO;
import com.dcep.gateway.mcbdc.dto.soap.GenericGwDTO;

public interface MbridgeZeroOutManager {

    /**
     * 更新清零空表更新记账指令表
     * @param mcbs10100101DTO,zerooutCtrlDO
     * @return
     */
    void recordZOCtrlAndAcctInstr(Mcbs10100101DTO mcbs10100101DTO,ZerooutCtrlDO zerooutCtrlDO);

    /**
     * 调用货币桥网关
     * @param genericReq
     * @return
     */
    Response<GenericEnvelopeDTO<GenericGwDTO>> mbridgeGateway(GenericEnvelopeDTO<GenericGwDTO> genericReq);
}
