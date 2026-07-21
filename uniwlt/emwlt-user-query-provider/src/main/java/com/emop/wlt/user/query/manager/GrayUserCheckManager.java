package com.emop.wlt.user.query.manager;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.emop.doms.cloudcontrol.query.api.UserInfoProvider;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GrayUserCheckManager {

    @NacosValue(value = "${uniwltApp.parameter.grayGroup:appParamGray}", autoRefreshed = true)
    private String grayGroup;

    @DubboReference
    private UserInfoProvider userInfoProvider;

    /**
     * 是否灰度分组
     *
     * @param userPhone
     */
    public boolean isGray(String userPhone) {
        if (StringUtils.isBlank(userPhone)) {
            return false;
        }
        try {
            //获取版本号
            // 根据手机号查公管获取该手机号对应的配置
            List<String> userGroupCodes = userInfoProvider.getUserGroupCodes(userPhone);
            for (String userGroupCode : userGroupCodes) {
                if (grayGroup.equals(userGroupCode)) {
                    return true;
                }
            }
        } catch (Exception e) {
            log.error("根据手机号判断是否灰度异常", e);
        }
        return false;
    }

}
