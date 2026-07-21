package com.emop.wlt.user.query.util;

import com.alibaba.fastjson.JSONObject;
import com.emop.wlt.user.query.constant.ControlConstant;
import com.emop.wlt.user.query.model.vo.GroupconfigDB;
import com.emop.wlt.user.query.model.vo.UserConfig;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ConfigConstructorUtils {

    public static final String OFFLINE_WALLET = "offlineWallet";
    public static final String HARDWARE_WALLET_BASED_ACCOUNT = "hardwareWalletBasedAccount";
    public static final String IS_DEVICE_ALLOW_BASED_ACCOUNT_HARDWARE_WALLET = "isDeviceAllowBasedAccountHardwareWallet";
    public static final String SWITCH_ON = "1";
    public static final String SWITCH_OFF = "0";

    /**
     * 新架构云控，优化json处理逻辑，筛掉无用层级
     *
     * @param groupConfigDBList
     * @return
     */
    public UserConfig configConstructorNew(List<GroupconfigDB> groupConfigDBList, String deviceModel) {
        log.debug("用户的配置:{}", groupConfigDBList);
        //done 构造配置
        String version = "";

        LocalDateTime initDateTime = LocalDateTime.parse(ControlConstant.INIT_DATETIME);
        JSONObject jsonObject = new JSONObject();
        for (GroupconfigDB groupconfigDB : groupConfigDBList) {
            //done 检查版本，多标签选最新更新时间版本

            if (initDateTime.isBefore(groupconfigDB.getUpdateTime())) {
                version = groupconfigDB.getVersion();
            }
            //构造json-config
            String functionKey = groupconfigDB.getFunctionKey();
            //云控返回config字段不会为空，不做判空
            JSONObject jsonObjectConfig = JSONObject.parseObject(groupconfigDB.getConfig());
            //云控返回该机构下可支持硬钱包的设备型号，判断前端给的设备型号是否在内
            String offlineWalletSwitch = checkDeviceModel(groupconfigDB.getOfflineWallet(),
                groupconfigDB.getOfflineWalletSwitch(), deviceModel);
            //非机构时，传硬钱包开关
            if (StringUtils.isNotBlank(offlineWalletSwitch)) {
                jsonObjectConfig.put(OFFLINE_WALLET, offlineWalletSwitch);
            }
            //无电支付开关
            if (Objects.equals(HARDWARE_WALLET_BASED_ACCOUNT, functionKey)) {
                jsonObjectConfig.put(IS_DEVICE_ALLOW_BASED_ACCOUNT_HARDWARE_WALLET,
                    checkDeviceModel(groupconfigDB.getOfflineWallet(), deviceModel));
            }
            jsonObject.put(functionKey, jsonObjectConfig);
        }
        log.debug("jsonObject:{}", jsonObject);
        return UserConfig.builder()
            .version(version)
            .config(jsonObject.isEmpty() ? null : jsonObject)
            .build();
    }

    /**
     * 判断当前设备机型对应机构是否支持硬钱包
     *
     * @param supportDeviceModel
     * @param deviceModelSwitch
     * @param thisDeviceModel
     * @return
     */
    private String checkDeviceModel(String supportDeviceModel, String deviceModelSwitch, String thisDeviceModel) {
        try {
            //硬件型号开关为空或为关，则认为非机构
            if (StringUtils.isBlank(deviceModelSwitch) || SWITCH_OFF.equals(deviceModelSwitch)) {
                return null;
            }
            //当前设备型号为空，默认硬钱包开关关闭
            if (StringUtils.isBlank(thisDeviceModel)) {
                return SWITCH_OFF;
            }
            supportDeviceModel = (supportDeviceModel == null ? "" : supportDeviceModel);
            List<String> deviceModels = new ArrayList<>();
            Collections.addAll(deviceModels, supportDeviceModel.split(","));
            for (String device : deviceModels) {
                if (device.equalsIgnoreCase(thisDeviceModel)) {
                    return SWITCH_ON;
                }
            }
            return SWITCH_OFF;
        } catch (Exception e) {
            log.error("判断当前设备机型对应机构是否支持硬钱包异常, e=", e);
            return SWITCH_OFF;
        }
    }

    /**
     * 判断当前设备机型是否支持无电支付
     *
     * @param supportDeviceModel
     * @param thisDeviceModel
     * @return
     */
    private String checkDeviceModel(String supportDeviceModel, String thisDeviceModel) {
        try {
            //当前设备型号为空，默认开关关闭
            if (StringUtils.isBlank(thisDeviceModel)) {
                return SWITCH_OFF;
            }
            supportDeviceModel = (supportDeviceModel == null ? "" : supportDeviceModel);
            List<String> deviceModels = new ArrayList<>();
            Collections.addAll(deviceModels, supportDeviceModel.split(","));
            for (String device : deviceModels) {
                if (device.equalsIgnoreCase(thisDeviceModel)) {
                    return SWITCH_ON;
                }
            }
            return SWITCH_OFF;
        } catch (Exception e) {
            log.error("判断当前设备机型是否支持无电支付, e=", e);
            return SWITCH_OFF;
        }
    }

}
