/*
 * pbcdci.cn Inc.
 * Copyright © 2025 PBCDCI All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.api;

import com.dcep.common.exception.DcepException;
import com.dcep.common.model.Response;
import com.dcep.gateway.mcbdc.dto.soap.GenericEnvelopeDTO;
import com.dcep.gateway.mcbdc.dto.soap.GenericGwDTO;
import com.dubbo.ldc.ZoneRouter;

@ZoneRouter
public interface McbsSndMbridgeService {

    /**
     * 桥上请求
     * @param req 请求对象
     * @return 应答对象
     */
    Response<GenericEnvelopeDTO<GenericGwDTO>> process(GenericEnvelopeDTO<GenericGwDTO> req) throws DcepException;

    /**
     * 桥上发起支付状态查询
     * @param req 请求对象
     * @return 应答对象
     */
    Response<GenericEnvelopeDTO<GenericGwDTO>> querySts(GenericEnvelopeDTO<GenericGwDTO> req) throws DcepException;

}
