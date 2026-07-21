package com.emop.wlt.user.management.manager;

import com.emop.wlt.common.constant.DatePattern;
import com.emop.wlt.common.model.dto.MobileNumber;
import com.emop.wlt.message.push.api.AppMessageProvider;
import com.emop.wlt.message.push.dto.AppMessageDTO;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.emop.wlt.common.constant.CommonConstant.BOC_MACAO_CODE;
import static com.emop.wlt.common.enums.VerifySceneTypeEnum.VST19;

@Service
@Slf4j
public class MessagePushManager {

    private static final String PUSH_ID_0300001 = "0300001";
    private static final String PUSH_ID_0300005 = "0300005";
    private static final String PUSH_ID_0300004 = "0300004";
    private static final String MOBILE_NUMBER = "mobileNumber";
    private static final String EVENT = "event";
    private static final String LOST_LOGOUT = "lostlogout";
    private static final String USER_TOKEN = "userToken";
    private static final String RESET_PWD_TIME = "resetPWDTime";
    private static final String DEVICE_NAME = "deviceName";
    private static final String DEFAULT_DEVICE_NAME = "Anonymous";
    private static final String RESET_LOGOUT = "resetlogout";
    private static final String VERIFY_SCENE_TYPE = "verifySceneType";
    private static final String PHONE_NUMBER = "phoneNumber";
    private static final String PHONE_AREA_CODE = "phoneAreaCode";
    private static final String DEVICE_ID = "deviceId";

    @DubboReference
    private AppMessageProvider appMessageProvider;

    /**
     * 发送挂失踢出登录消息
     *
     * @param userId
     * @param phone
     * @param token
     */
    public void sendForceLoginOutMsg(String userId, String phone, String token) {
        Map<String, Object> templateData = new HashMap<>();
        templateData.put(MOBILE_NUMBER, phone);
        templateData.put(USER_TOKEN, token);
        templateData.put(EVENT, LOST_LOGOUT);
        AppMessageDTO appMessageDTO = new AppMessageDTO();
        appMessageDTO.setPushId(PUSH_ID_0300004);
        appMessageDTO.setUserId(userId);
        appMessageDTO.setInst(BOC_MACAO_CODE);
        appMessageDTO.setTemplateData(templateData);

        appMessageProvider.sendAppMessage(appMessageDTO);
    }

    /**
     * 发送重置登录密码消息推送
     */
    public void sendResetLoginPwdMessage(String msgId, String userId, String mobileNumber) {
        try {
            MobileNumber mobileNumberObject = MobileNumber.buildFrom(mobileNumber);
            Map<String, Object> templateData = new HashMap<>();
            templateData.put(RESET_PWD_TIME, LocalDateTime.now().format(DatePattern.UTC_SIMPLE_FORMAT));
            templateData.put(VERIFY_SCENE_TYPE, VST19.getCode());
            templateData.put(PHONE_NUMBER, mobileNumberObject.getPhoneNumber());
            templateData.put(PHONE_AREA_CODE, mobileNumberObject.getAreaCode());
            AppMessageDTO appMessageDTO = new AppMessageDTO();
            appMessageDTO.setPushId(PUSH_ID_0300001);
            appMessageDTO.setUserId(userId);
            appMessageDTO.setInst(BOC_MACAO_CODE);
            appMessageDTO.setMsgId(msgId);
            appMessageDTO.setTemplateData(templateData);
            appMessageProvider.sendAppMessage(appMessageDTO);
        } catch (Exception e) {
            log.error("非登录态重置登录密码消息推送异常", e);
        }
    }

    /**
     * 发送重置登录密码踢出登录消息推送
     */
    public void sendResetLoginPwdLogoutMessage(String msgId, String userId, String token, String deviceName) {
        try {
            if (StringUtils.isEmpty(deviceName)) {
                deviceName = DEFAULT_DEVICE_NAME;
            }
            Map<String, Object> templateData = new HashMap<>();
            templateData.put(RESET_PWD_TIME, LocalDateTime.now().format(DatePattern.NORM_DATETIME_MINUTE_FORMAT));
            templateData.put(DEVICE_NAME, deviceName);
            templateData.put(EVENT, RESET_LOGOUT);
            templateData.put(USER_TOKEN, token);
            AppMessageDTO appMessageDTO = new AppMessageDTO();
            appMessageDTO.setPushId(PUSH_ID_0300005);
            appMessageDTO.setUserId(userId);
            appMessageDTO.setMsgId(msgId);
            appMessageDTO.setTemplateData(templateData);
            appMessageProvider.sendAppMessage(appMessageDTO);
        } catch (Exception e) {
            log.error("非登录态重置登录密码发送重置登录密码踢出提醒异常", e);
        }
    }

}
