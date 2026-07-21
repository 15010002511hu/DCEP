package com.emop.wlt.user.query.api;

import com.dubbo.ldc.ZoneRouter;
import com.emop.wlt.user.query.dto.CustomerOutDTO;

/**
 * 实名信息查询服务
 *
 * @author fanjianyu
 * @version 1.0.0
 * @date 2021/7/13
 * @dubbo
 */

@ZoneRouter(value = ZoneRouter.Type.CZ)
public interface CustomerQueryProvider {

    /**
     * 根据cid查询实名信息
     */
    CustomerOutDTO queryCustomerInfo(String cid);
}
