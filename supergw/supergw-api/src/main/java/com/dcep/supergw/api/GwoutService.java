/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.supergw.api;

import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.Response;
import com.dubbo.ldc.ZoneRouter;

/**
 * 网关统一出口服务
 * @author huyajun
 * @version $Id: CreditPayOutService.java, v 0.1 2019年8月2日 下午10:34:30 Administrator Exp $
 */
@ZoneRouter(ZoneRouter.Type.CZ)
public interface GwoutService {
    /**
     * 网关提供通用出口服务
     * 机构要调用网关的服务，传递具体的DTO报文就行。DTO必须继承GwDTO
     * 网关内部根据DTO的报文头  做服务路由。
     * 
     * @param req
     * @return
     */
    Response<EnvelopeDTO<GwDTO>> execute(EnvelopeDTO<GwDTO> req);
    
    /**
     *功能描述： 网关提供泛化调用出口服务
     *入参为标准soap格式dcep请求报文
     *出参为标准soap格式dcep应答报文
     * 修改时间：2020-07-07
     * 修改内容：TSM个人化交易需求新增
     * @param req
     * @return
     */
    String send(String req);
}
