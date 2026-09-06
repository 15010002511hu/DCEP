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
 * 转换DCEP请求报文到货币桥请求报文接口
 * @author laowei
 * @version $Id: DcepToMbridgeConvertManager.java, v 0.1 2022年5月31日 下午10:51:32 laowei Exp $
 */
public interface DcepToMbridgeConvertManager {

    /**
     * 转换DCEP请求报文到货币桥请求报文
     * @param dcepReqEnvelopeDTO DCEP请求
     * @return 货币桥请求
     */
    GenericEnvelopeDTO<GenericGwDTO> convertRequest(EnvelopeDTO<GwDTO> dcepReqEnvelopeDTO);

    /**
     * 补充货币桥请求报文必选数据项
     * @param mBridgeReqEnvelopeDTO 货币桥请求
     * @param dcepReqEnvelopeDTO    DCEP请求
     * @param mcbsMsgId        货币桥报文标识号
     * @return 货币桥请求
     */
    GenericEnvelopeDTO<GenericGwDTO> requestSupplement(GenericEnvelopeDTO<GenericGwDTO> mBridgeReqEnvelopeDTO,
        EnvelopeDTO<GwDTO> dcepReqEnvelopeDTO, String mcbsMsgId);
}
