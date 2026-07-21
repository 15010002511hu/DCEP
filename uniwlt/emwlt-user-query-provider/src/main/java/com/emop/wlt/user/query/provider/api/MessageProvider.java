package com.emop.wlt.user.query.provider.api;

import com.emop.common.model.Response;
import com.emop.gateway.dto.emap.EmapDTO;
import com.emop.gateway.dto.emap.RequestModel;
import com.emop.gateway.dto.emap.ResponseModel;
import com.emop.gateway.spi.annotation.GwOperType;
import com.emop.wlt.model.emap.emap600.Emap60000101ReqDTO;

public interface MessageProvider {

    @GwOperType(msgType = "emap.600.001.01", genericParam = Emap60000101ReqDTO.class)
    Response<ResponseModel<EmapDTO>> execute(RequestModel<EmapDTO> request);

}
