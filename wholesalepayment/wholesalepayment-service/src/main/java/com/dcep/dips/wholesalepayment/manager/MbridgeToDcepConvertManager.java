/*
 * pbcdci.cn Inc.
 * Copyright © 2022 PBCDCI All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.manager;

import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.gateway.mcbdc.dto.soap.GenericEnvelopeDTO;
import com.dcep.gateway.mcbdc.dto.soap.GenericGwDTO;

/**
 * 转换货币桥请求报文到DCEP请求报文接口
 * @author laowei
 * @version $Id: MbridgeToDcepConvertManager.java, v 0.1 2022年5月31日 下午11:07:00 laowei Exp $
 */
public interface MbridgeToDcepConvertManager {

    /**
     * 转换货币桥请求报文到DCEP请求报文
     * @param mBridgeReqEnvelopeDTO 货币桥请求
     * @return DCEP请求
     */
    EnvelopeDTO<GwDTO> convertRequest(GenericEnvelopeDTO<GenericGwDTO> mBridgeReqEnvelopeDTO);
}
