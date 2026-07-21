package com.emop.wlt.user.query.provider.api;

import com.emop.common.model.Response;
import com.emop.gateway.dto.emap.EmapDTO;
import com.emop.gateway.dto.emap.RequestModel;
import com.emop.gateway.dto.emap.ResponseModel;
import com.emop.gateway.spi.annotation.GwOperType;
import com.emop.wlt.model.emap.emap602.Emap60200101ReqDTO;

/**
 * 钱包服务通知
 *
 * @since 1.0.0
 */
public interface WalletServiceNotifyProvider {

    @GwOperType(msgType = "emap.602.001.01", genericParam = Emap60200101ReqDTO.class)
    Response<ResponseModel<EmapDTO>> execute(RequestModel<EmapDTO> request);

}
