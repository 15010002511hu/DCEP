package com.emop.wlt.user.management.mapi.component;

import com.alipay.gateway.adapterservice.annotation.OperationType;
import com.dubbo.ldc.ZoneRouter;
import com.emop.wlt.common.model.mapp.RequestModel;
import com.emop.wlt.common.model.mapp.ResponseModel;
import com.emop.wlt.user.management.model.response.Mapp03100101Resp;

/**
 * 生成安全键盘随机数和公钥
 */
@ZoneRouter(ZoneRouter.Type.GZ)
public interface CFCAProvider {

    /**
     * 生成安全键盘随机数和公钥
     */
    @OperationType(value = "mapp.031.001.01", name = "生成安全键盘随机数和公钥", desc = "mapp.031.001.01")
    ResponseModel<Mapp03100101Resp> createCFCARs(RequestModel requestModel);

}
