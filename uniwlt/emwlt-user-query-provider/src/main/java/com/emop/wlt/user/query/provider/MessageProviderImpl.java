package com.emop.wlt.user.query.provider;

import com.emop.common.model.Response;
import com.emop.gateway.dto.emap.EmapDTO;
import com.emop.gateway.dto.emap.RequestModel;
import com.emop.gateway.dto.emap.ResponseModel;
import com.emop.wlt.aspect.annotation.EmapResponse;
import com.emop.wlt.aspect.emap.util.EmapResponseUtils;
import com.emop.wlt.model.emap.emap600.Emap60000101RespDTO;
import com.emop.wlt.user.query.provider.api.MessageProvider;
import com.emop.wlt.user.query.service.impl.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MessageProviderImpl implements MessageProvider {

    @Autowired
    private MessageService messageService;

    @EmapResponse(outClazz = Emap60000101RespDTO.class)
    @Override
    public Response<ResponseModel<EmapDTO>> execute(RequestModel<EmapDTO> request) {
        messageService.sendMessage(request);

        return EmapResponseUtils.emapOk(new Emap60000101RespDTO());
    }

}
