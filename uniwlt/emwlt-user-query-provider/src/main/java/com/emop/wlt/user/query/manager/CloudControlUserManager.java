package com.emop.wlt.user.query.manager;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.emop.doms.cloudcontrol.query.api.UserInfoProvider;
import com.emop.wlt.common.constant.CommonConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * @author bobo
 * @Description: 云控封装管理类
 * @date 2022/4/28
 */
@Component
@Slf4j
public class CloudControlUserManager {

    @NacosValue(value = "${uniwltApp.parameter.grayGroup:appParamGray}", autoRefreshed = true)
    private String grayGroup;
    private static final String QRCODE_PAY_GROUP_ID = "QRCODE_PAY";

    @DubboReference
    private UserInfoProvider cloudControlUserProvider;

    /**
     * 检查用户是否是灰度用户
     *
     * @param userPhone
     * @return 0 不是 1 是
     */
    public String checkUserIsGray(String userPhone) {
        if (StringUtils.isBlank(userPhone)) {
            return CommonConstant.NO;
        }
        try {
            List<String> userGroupCodes = cloudControlUserProvider.getUserGroupCodes(userPhone);
            if (userGroupCodes.contains(grayGroup)) {
                log.info("user is gray:{}", CommonConstant.YES);
                return CommonConstant.YES;
            }
        } catch (Exception e) {
            log.error("get user group error!", e);
        }
        return CommonConstant.NO;
    }

    /**
     * 检查用户是否是灰度用户
     *
     * @param userPhone
     * @return
     */
    public boolean checkUserIsGrayBoolean(String userPhone) {
        return Objects.equals(this.checkUserIsGray(userPhone), CommonConstant.YES);
    }

    /**
     * 检查用户是否是二维码支付白名单
     *
     * @param userPhone
     * @return
     */
    public boolean isQRCodePayWhite(String userPhone) {
        if (StringUtils.isBlank(userPhone)) {
            return false;
        }
        try {
            List<String> userGroupCodes = cloudControlUserProvider.getUserGroupCodes(userPhone);
            if (userGroupCodes.contains(QRCODE_PAY_GROUP_ID)) {
                return true;
            }
        } catch (Exception e) {
            log.error("get user group error!", e);
        }
        return false;
    }
}
