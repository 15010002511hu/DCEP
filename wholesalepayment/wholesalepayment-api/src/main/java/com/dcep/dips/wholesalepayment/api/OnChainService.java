/*
 * pbcdci.cn Inc.
 * Copyright © 2025 PBCDCI All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.api;

import com.dcep.common.exception.DcepException;
import com.dcep.common.model.Response;
import com.dcep.dips.wholesalepayment.dto.chain.OnChainTransReqDTO;
import com.dcep.gateway.mcbdc.dto.soap.GenericEnvelopeDTO;
import com.dcep.gateway.mcbdc.dto.soap.GenericGwDTO;
import com.dubbo.ldc.ZoneRouter;

@ZoneRouter
public interface OnChainService {

    /**
     * 链上同步记账服务：接收区块链链上支付交易同步请求
     * @param req 请求对象
     * @return 应答对象
     */
    Response<String> chainAccounting(OnChainTransReqDTO req) throws DcepException;
    /**
     * 桥上同步记账服务：接收货币桥桥上支付交易同步请求
     * @param req 请求对象
     * @return 应答对象
     */
    Response<GenericEnvelopeDTO<GenericGwDTO>> mbridgeAccounting(GenericEnvelopeDTO<GenericGwDTO> req) throws DcepException;

}
