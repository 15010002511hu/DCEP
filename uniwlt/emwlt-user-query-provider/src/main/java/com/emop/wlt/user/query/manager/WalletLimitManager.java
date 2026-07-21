package com.emop.wlt.user.query.manager;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.emop.infocache.WalletParamCache;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WalletLimitManager {

    @Autowired
    private WalletParamCache walletParamCache;

    private final static String INST_NO = "instNo";
    private final static String WALLET_LEVEL = "walletLevel";

    public String queryWalletLimit(String instNo, String walletLevel) {
        String fiLimitJson = walletParamCache.getFiLimitJson();
        JSONArray limitList = new JSONArray();
        JSONArray limitListTemp = JSONArray.parseArray(fiLimitJson);
        if (CollectionUtils.isNotEmpty(limitListTemp)) {
            for (Object temp : limitListTemp) {
                JSONObject limit = (JSONObject) temp;
                log.info("限额数据:{}", temp);
                //在有条件的情况下满足的add
                boolean instNoFlag = StringUtils.isBlank(instNo) || instNo.equals(limit.getString(INST_NO));
                boolean walletLevelFlag = StringUtils.isBlank(walletLevel) || walletLevel.equals(limit.getString(WALLET_LEVEL));

                if (instNoFlag && walletLevelFlag) {
                    limitList.add(temp);
                }
            }
        }
        return JSONObject.toJSONString(limitList);
    }
}
