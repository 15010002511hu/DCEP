package com.emop.wlt.user.management.provider;

import com.emop.common.enums.errorcode.BaseErrorEnum;
import com.emop.wlt.common.util.Base64Utils;
import com.emop.wlt.encrypt.operator.SecurityOperator;
import com.emop.wlt.user.management.api.CFCAManagerProvider;
import com.emop.wlt.user.management.config.CFCAConfig;
import com.emop.wlt.user.management.exception.ExceptionCast;
import com.emop.wlt.user.management.manager.CFCARedisManager;
import com.emop.wlt.user.management.model.SIPMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author bobo
 * @Description: 内部操作实现类
 * @date 2022/3/29
 */
@DubboService
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class CFCAManagerProviderImpl implements CFCAManagerProvider {

    private final SecurityOperator securityOperator;
    private final CFCARedisManager cfcaRedisManager;
    private final CFCAConfig cfcaConfig;

    @Override
    public String decryptPassWord(String pwdEnc) {
        // 解析sipMessage
        SIPMessage sipMessage = SIPMessage.decode(pwdEnc);

        log.info("sipMessage: {}", sipMessage);

        // 根据serverRandomHash从redis获取serverRandom
        String serverRandom = Base64Utils.encode(sipMessage.getServerRandom());
        boolean exists = cfcaRedisManager.isExists(serverRandom);

        // 如果serverRandom不存在返回对应超时错误码
        if (!exists) {
            // 安全键盘秘钥过期
            ExceptionCast.buzCast(BaseErrorEnum.S06017);
        }

        // 获取对应的公钥证书
        String publicKeyHash = Base64Utils.encode(sipMessage.getPublickeyHash());
        String certId = cfcaConfig.getPubKeyCertId(publicKeyHash);

        // 如果证书不存在返回对应错误码
        if (StringUtils.isBlank(certId)) {
            // 安全键盘解密失败
            ExceptionCast.buzCast(BaseErrorEnum.S06006);
        }

        byte[] clientRandomCipher = sipMessage.getClientRandomCipher();
        byte[] iv = sipMessage.getServerRandom();
        byte[] cipherText = sipMessage.getCipherText();

        String password = securityOperator.decodeCFCAPassword(certId, iv, clientRandomCipher, cipherText);

        password = Base64Utils.encode(Hex.toHexString(Base64Utils.decode(password)));

        return password;
    }

}
