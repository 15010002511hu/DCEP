package com.emop.wlt.user.management.manager;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.emop.common.enums.errorcode.BaseErrorEnum;
import com.emop.infocache.ParamCache;
import com.emop.wlt.common.enums.AppUserStatusEnum;
import com.emop.wlt.common.model.dto.MobileNumber;
import com.emop.wlt.user.entity.MappingIndex;
import com.emop.wlt.user.entity.User;
import com.emop.wlt.user.info.api.UserInfoProvider;
import com.emop.wlt.user.info.dto.TokenInfoDTO;
import com.emop.wlt.user.info.dto.UserInfoLoginParamDTO;
import com.emop.wlt.user.management.constant.UserDefault;
import com.emop.wlt.user.management.exception.ExceptionCast;
import com.emop.wlt.user.management.model.RegisterDTO;
import com.emop.wlt.user.management.model.vo.Geolocation;
import com.emop.wlt.user.management.service.MappingIndexManagementService;
import com.emop.wlt.user.management.service.UserService;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class RegisterManager {

    @NacosValue(value = "${uniwlt.registerArea.verify:true}", autoRefreshed = true)
    private boolean registerAreaVerify;
    @NacosValue(value = "${uniwlt.registerArea.list:[\"澳门\"]}", autoRefreshed = true)
    private String registerAreaList;
    private static final String APP_USER_REGISTER_COUNTRY_CODE_LIST = "1001AppUserRegisterCountryCodeList";
    @Autowired
    private MappingIndexManagementService mappingIndexManagementService;
    @Autowired
    private UserService userService;
    @Autowired
    private ParamCache paramCache;
    @DubboReference
    private UserInfoProvider userInfoProvider;

    public void registerPreCheck(String mobileNumber, Geolocation geolocation) {
        checkMobileNumberFormat(mobileNumber);
        checkRegistered(mobileNumber);
        checkLocationInfo(geolocation);
    }

    public TokenInfoDTO register(RegisterDTO registerDTO) {

        MappingIndex mapppingIndex = null;
        String userId = mappingIndexManagementService.selectByPhone(registerDTO.getMobileNumber());
        if (StringUtils.isBlank(userId)) {
            userId = registerDTO.getUserId();
            mapppingIndex = new MappingIndex();
            mapppingIndex.setMappingKey(registerDTO.getMobileNumber());
            mapppingIndex.setMappingValue(userId);
        }

        User user = new User();
        user.setUserId(userId);
        user.setDeviceId(registerDTO.getDeviceId());
        user.setDeviceType(registerDTO.getDeviceType());
        user.setPushFlag(UserDefault.PUSH_CODE);
        user.setStatus(AppUserStatusEnum.NORMAL.getValue());
        user.setPhone(registerDTO.getMobileNumber());
        user.setAppInnerVersion(registerDTO.getInnerVersion());
        user.setCountryRegionCode(registerDTO.getCountryAndRegionCode());

        mappingIndexManagementService.saveUserLogic(mapppingIndex, user);
        return getLoginTokenV2(userId, registerDTO.getMobileNumber(), registerDTO.getDeviceId(),
            registerDTO.getDeviceType());
    }

    private TokenInfoDTO getLoginTokenV2(String userId, String mobileNumber, String deviceId, String deviceType) {
        UserInfoLoginParamDTO userInfoLoginParamDTO = new UserInfoLoginParamDTO();
        userInfoLoginParamDTO.setUserId(userId);
        userInfoLoginParamDTO.setMobileNumber(mobileNumber);
        userInfoLoginParamDTO.setDeviceId(deviceId);
        userInfoLoginParamDTO.setDeviceType(deviceType);
        return userInfoProvider.registerAndLoginV2(userInfoLoginParamDTO);
    }

    private void checkRegistered(String mobileNumber) {
        String userId = mappingIndexManagementService.selectByPhone(mobileNumber);
        if (StringUtils.isNotEmpty(userId)) {
            User user = userService.selectByPrimaryKey(userId);
            if (user != null) {
                ExceptionCast.cast(BaseErrorEnum.B12103);
            }
        }
    }

    private void checkLocationInfo(Geolocation geolocation) {
        if (!registerAreaVerify) {
            return;
        }
        String locationInfo = StringUtils.join(geolocation.getCountry(),
            geolocation.getProvince(), geolocation.getCity());

        List<String> areaList = JSONObject.parseArray(registerAreaList, String.class);
        if (areaList.stream().noneMatch(key -> locationInfo.contains(key))) {
            log.info("GPS不在允许注册地区,locationInfo:{}",locationInfo);
            ExceptionCast.cast(BaseErrorEnum.B12108);
        }
    }

    private void checkMobileNumberFormat(String mobileNumber) {
        MobileNumber mobileNumberObject = MobileNumber.buildFrom(mobileNumber);
        List<Map<String, String>> configList = paramCache.getComplexMultiList(APP_USER_REGISTER_COUNTRY_CODE_LIST);
        log.info("configList:{}", configList);

        if (!mobileNumberObject.validatePhoneNumber(configList)) {
            ExceptionCast.cast(BaseErrorEnum.B06701);
        }
    }

}
