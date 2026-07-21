package com.emop.wlt.user.query.provider;

import com.emop.common.enums.errorcode.BaseErrorEnum;
import com.emop.common.model.Response;
import com.emop.gateway.dto.emap.EmapDTO;
import com.emop.gateway.dto.emap.RequestModel;
import com.emop.gateway.dto.emap.ResponseModel;
import com.emop.wlt.aspect.annotation.EmapResponse;
import com.emop.wlt.aspect.emap.util.EmapResponseUtils;
import com.emop.wlt.model.emap.emap602.Emap60200101ReqDTO;
import com.emop.wlt.model.emap.emap602.Emap60200101RespDTO;
import com.emop.wlt.user.query.exception.ExceptionCast;
import com.emop.wlt.user.query.provider.api.WalletServiceNotifyProvider;
import com.emop.wlt.user.query.service.WalletUserIndexQueryService;
import com.emop.wlt.user.wallet.api.UserWalletManagerProvider;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WalletServiceNotifyProviderImpl implements WalletServiceNotifyProvider {

    @Autowired
    private WalletUserIndexQueryService walletUserIndexQueryService;
    @DubboReference
    UserWalletManagerProvider userWalletManagerProvider;

    @EmapResponse(outClazz = Emap60200101ReqDTO.class)
    @Override
    public Response<ResponseModel<EmapDTO>> execute(RequestModel<EmapDTO> request) {
        Emap60200101ReqDTO emap60200101ReqDTO = (Emap60200101ReqDTO) request.getMessageBody();
        if ("WSNSO1".equals(emap60200101ReqDTO.getWalletServiceNotifyScene())) {
            String walletId = emap60200101ReqDTO.getWalletId();
            String userId = walletUserIndexQueryService.selectByWalletId(walletId);
            if (StringUtils.isBlank(userId)) {
                ExceptionCast.buzCast(BaseErrorEnum.B14104);
            }
            userWalletManagerProvider.removeUserWallet(userId, walletId);
        }

        return EmapResponseUtils.emapOk(new Emap60200101RespDTO());
    }

}
