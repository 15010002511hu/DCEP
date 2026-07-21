package com.emop.wlt.user.management.mapi.business.impl;


import com.emop.gateway.api.sdk.service.dto.GwPojoMsgDTO;
import com.emop.wlt.aspect.annotation.AppResponse;
import com.emop.wlt.common.model.mapp.RequestModel;
import com.emop.wlt.common.model.mapp.ResponseModel;
import com.emop.wlt.common.util.InstNoUtils;
import com.emop.wlt.common.util.UserUtils;
import com.emop.wlt.flow.annotation.MappFlowJoin;
import com.emop.wlt.flow.context.util.FlowContextUtils;
import com.emop.wlt.gateway.manager.EmapGatewayManager;
import com.emop.wlt.model.emap.EmapDTOBuildHelper;
import com.emop.wlt.model.emap.emap315.Emap31500101ReqDTO;
import com.emop.wlt.user.management.mapi.business.WalletUpgradeProvider;
import com.emop.wlt.user.management.model.request.Mapp31900101Req;
import com.emop.wlt.user.management.model.response.Mapp31900101Resp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WalletUpgradeProviderImpl implements WalletUpgradeProvider {

    @Autowired
    private EmapGatewayManager emapGatewayManager;

    @MappFlowJoin(nodeCode = "MAPP319", groupNodeCode = "BNC319")
    @AppResponse
    @Override
    public ResponseModel<Mapp31900101Resp> walletUpgradePreCheck(RequestModel<Mapp31900101Req> request) {
        String walletId = FlowContextUtils.getWalletId();
        String instContext = FlowContextUtils.getInstContext();
        String userId = UserUtils.getUserId();

        Emap31500101ReqDTO emap31500101ReqDTO = new Emap31500101ReqDTO();
        emap31500101ReqDTO.setWalletId(walletId);
        emap31500101ReqDTO.setContextNo(instContext);
        emap31500101ReqDTO.setTargetLevel(request.getMessageBody().getTargetLevel());

        GwPojoMsgDTO gwPojoMsgDto = EmapDTOBuildHelper.createGwPojoMsgDTO(
                emap31500101ReqDTO, userId, InstNoUtils.getInstNm(walletId));
        emapGatewayManager.send(gwPojoMsgDto).getBody();

        return ResponseModel.<Mapp31900101Resp>builder().messageBody(new Mapp31900101Resp()).build();
    }

}
