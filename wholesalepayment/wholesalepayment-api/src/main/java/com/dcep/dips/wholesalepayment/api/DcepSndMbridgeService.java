/*
 * pbcdci.cn Inc.
 * Copyright © 2025 PBCDCI All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.api;

import com.dcep.common.exception.DcepException;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.Response;
import com.dcep.gateway.mcbdc.dto.soap.GenericEnvelopeDTO;
import com.dcep.gateway.mcbdc.dto.soap.GenericGwDTO;
import com.dubbo.ldc.ZoneRouter;

@ZoneRouter
public interface DcepSndMbridgeService {

    /**
     * 桥下发起上桥、下桥请求
     * @param req 请求对象
     * @return 应答对象
     */
    Response<EnvelopeDTO<GwDTO>> prepare(EnvelopeDTO<GwDTO> req) throws DcepException;

    /**
     * 桥下发起上桥、下桥请求
     * @param req 请求对象
     * @return 应答对象
     */
    Response<GenericEnvelopeDTO<GenericGwDTO>> finish(GenericEnvelopeDTO<GenericGwDTO> req) throws DcepException;

}
