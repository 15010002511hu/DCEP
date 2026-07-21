package com.emop.wlt.user.management.mapi.component.impl;

import com.emop.wlt.aspect.annotation.AppResponse;
import com.emop.wlt.common.model.mapp.RequestModel;
import com.emop.wlt.common.model.mapp.ResponseModel;
import com.emop.wlt.encrypt.operator.SecurityOperator;
import com.emop.wlt.user.management.config.CFCAConfig;
import com.emop.wlt.user.management.manager.CFCARedisManager;
import com.emop.wlt.user.management.mapi.component.CFCAProvider;
import com.emop.wlt.user.management.model.response.Mapp03100101Resp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.emop.wlt.user.management.constant.CFCAConstant.SERVER_RANDOM_LENGTH;
/**
 * 生成安全键盘随机数和公钥
 */
@Service
@Slf4j
public class CFCAProviderImpl implements CFCAProvider {
    @Autowired
    private SecurityOperator securityOperator;
    @Autowired
    private CFCARedisManager cfcaRedisManager;
    @Autowired
    private CFCAConfig cfcaConfig;

    @Override
    @AppResponse
    public ResponseModel<Mapp03100101Resp> createCFCARs(RequestModel requestModel) {
        // 1.生成服务端随机数
        byte[] serverRandomBytes = securityOperator.generateRandom(SERVER_RANDOM_LENGTH);

        // 2.redis存储
        String serverRandom = cfcaRedisManager.saveServerRandom(serverRandomBytes);
        // 3.获取nacos默认公钥
        String defaultPubKey = cfcaConfig.getDefaultPubKey();
        return ResponseModel.<Mapp03100101Resp>builder()
            .messageBody(
                Mapp03100101Resp.builder()
                    .serverRandom(serverRandom)
                    .pwdPublicKey(defaultPubKey)
                    .build()
            ).build();
    }
}
