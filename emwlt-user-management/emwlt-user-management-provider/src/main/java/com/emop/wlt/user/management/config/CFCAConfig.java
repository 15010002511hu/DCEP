package com.emop.wlt.user.management.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.AbstractListener;
import java.util.Optional;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * @author bobo
 * @Description:
 * @date 2022/3/29
 */
@Component
@Getter
@Slf4j
public class CFCAConfig implements InitializingBean {

    private static final String DATA_ID = "uniwlt.cfca.json";
    private static final String DEAFULT_GROUP = "DEFAULT_GROUP";
    private static final long TIMEOUTMS = 1000;
    private static final String DEFAULT_PUB_KEY_HASH = "defaultPubKeyHash";
    private static final String PUB_KEY_HASH_KEY_VALUE = "pubKeyJsonObj";
    private static final String PUB_KEY_VALUE = "value";
    private static final String PUB_KEY_CERT_ID = "certId";
    @NacosInjected
    private ConfigService configService;
    private String defaultPubKeyHash;
    private JSONObject pubKeyJsonObj;

    /**
     * 获取默认公钥
     *
     * @return
     */
    public String getDefaultPubKey() {
        return Optional.ofNullable(this.pubKeyJsonObj)
            .map(item -> item.getJSONObject(this.defaultPubKeyHash))
            .map(item -> item.getString(PUB_KEY_VALUE))
            .orElse("");
    }

    /**
     * 获取对应私钥别名
     *
     * @param pubKeyHash
     * @return
     */
    public String getPubKeyCertId(String pubKeyHash) {
        return Optional.ofNullable(this.pubKeyJsonObj)
            .map(item -> item.getJSONObject(pubKeyHash))
            .map(item -> item.getString(PUB_KEY_CERT_ID))
            .orElse("");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        String context = configService.getConfigAndSignListener(DATA_ID, DEAFULT_GROUP, TIMEOUTMS,
            new AbstractListener() {
                @Override
                public void receiveConfigInfo(String context) {
                    try {
                        updateConfig(context);
                    } catch (Exception e) {
                        log.info("refresh nacos cfca error:", e);
                    }
                }
            });
        this.updateConfig(context);
    }

    /**
     * 更新nacos配置
     *
     * @param context
     */
    private void updateConfig(String context) {
        log.info("load nacos cfca config :{}", context);
        JSONObject jsonObject = JSON.parseObject(context);
        String defaultPubKeyHash = jsonObject.getString(DEFAULT_PUB_KEY_HASH);
        this.defaultPubKeyHash = defaultPubKeyHash;
        JSONObject pubKeyJsonObj = jsonObject.getJSONObject(PUB_KEY_HASH_KEY_VALUE);
        this.pubKeyJsonObj = pubKeyJsonObj;
    }
}
