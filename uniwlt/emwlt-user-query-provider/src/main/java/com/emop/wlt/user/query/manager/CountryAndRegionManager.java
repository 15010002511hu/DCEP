package com.emop.wlt.user.query.manager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.emop.common.enums.errorcode.BaseErrorEnum;
import com.emop.infocache.ParamCache;
import com.emop.wlt.user.query.exception.ExceptionCast;
import com.emop.wlt.user.query.model.vo.CountryAndRegionParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.emop.wlt.common.constant.LanguageTypeConstant.ENUS;
import static com.emop.wlt.common.constant.LanguageTypeConstant.PTPT;
import static com.emop.wlt.common.constant.LanguageTypeConstant.ZHCN;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Service
@Slf4j
public class CountryAndRegionManager {

    @NacosValue(value = "${uniwltApp.countryParameter.top:#}", autoRefreshed = true)
    private String top;
    @Autowired
    private ParamCache paramCache;

    private static final String BIZ_TYPE_REGISTER = "BT00";
    private static final String BIZ_TYPE_TRANSFER = "BT01";
    private static final String ALLOW_TRANSFER = "1";
    private static final String ALLOW_REGISTER = "1";
    private static final String MAINLAND = "+86";
    private static final String HONGKONG = "+852";
    private static final String MACAO = "+853";
    private static final String APP_USER_REGISTER_COUNTRY_CODE_LIST = "1001AppUserRegisterCountryCodeList";
    private static final String APP_USER_TRANSFER_COUNTRY_CODE_LIST = "1001AppTransferCountryCodeList";
    private static final String ZH_CN_NAME = "simplifiedChineseName";
    private static final String ZH_HK_NAME = "traditionalChineseName";
    private static final String EN_US_NAME = "englishName";
    private static final String PT_PT_NAME = "portugueseName";
    private static final String FIRST_LETTER = "firstLetter";
    private static final String CODE = "code";
    private static final String REGION_CODE = "regionCode";
    private static final String MOBILE_NUMBER_LENGTH = "mobileNumberLength";
    private static final String TRANSFER_MOBILE_NUMBER_LENGTH = "transferMobileNumberLength";
    private static final String REGISTER_MOBILE_NUMBER_LENGTH = "registerMobileNumberLength";

    public String queryMobileNumberLengthRule() {
        List<Map<String, String>> transferList =  paramCache.getComplexMultiList(APP_USER_TRANSFER_COUNTRY_CODE_LIST);
        log.debug("transferList:{}", transferList);
        List<Map<String, String>> registerList =  paramCache.getComplexMultiList(APP_USER_REGISTER_COUNTRY_CODE_LIST);
        log.debug("registerList:{}", registerList);

        Map<String, Map<String, String>> mobileNumberLengthRule = new HashMap<>();
        mobileNumberLengthRule.put(TRANSFER_MOBILE_NUMBER_LENGTH,
            transferList.stream().collect(toMap(item->item.get(CODE),item->item.getOrDefault(MOBILE_NUMBER_LENGTH, ""),(key1, key2)->key2)));
        mobileNumberLengthRule.put(REGISTER_MOBILE_NUMBER_LENGTH,
            registerList.stream().collect(toMap(item->item.get(CODE),item->item.getOrDefault(MOBILE_NUMBER_LENGTH, ""),(key1, key2)->key2)));
        log.debug("mobileNumberLengthRule:{}",mobileNumberLengthRule);

        return JSON.toJSONString(mobileNumberLengthRule);
    }

    public String queryCountryAndRegionCode(String language, String bizType) {
        if (StringUtils.isBlank(language) || StringUtils.isBlank(bizType)) {
            ExceptionCast.cast(BaseErrorEnum.S02021);
        }

        TreeMap<String, List<CountryAndRegionParam>> result = switch (bizType) {
            case BIZ_TYPE_TRANSFER -> getAllowTransferList(language);
            case BIZ_TYPE_REGISTER -> getAllowRegisterList(language);
            default -> null;
        };

        if (Objects.isNull(result)) {
            return null;
        }

        for (Map.Entry<String, List<CountryAndRegionParam>> entry : result.entrySet()) {
            List<CountryAndRegionParam> second = result.get(entry.getKey());
            result.put(entry.getKey(), second.stream().sorted(Comparator.comparing(CountryAndRegionParam::getName))
                .collect(toList()));
        }
        //热门国家/地区的特殊排序
        List<CountryAndRegionParam> special = result.get(top);
        log.debug("special:{}", special);
        List<CountryAndRegionParam> tops = new ArrayList<>();
        CountryAndRegionParam mainland = null;
        CountryAndRegionParam hongkong = null;
        CountryAndRegionParam macao = null;
        Iterator<CountryAndRegionParam> iterator = special.iterator();
        while (iterator.hasNext()) {
            CountryAndRegionParam cdp = iterator.next();
            if (MAINLAND.equals(cdp.getCode())) {
                mainland = cdp;
                iterator.remove();
            }
            if (HONGKONG.equals(cdp.getCode())) {
                hongkong = cdp;
                iterator.remove();
            }
            if (MACAO.equals(cdp.getCode())) {
                macao = cdp;
                iterator.remove();
            }
        }

        if (macao != null) {
            tops.add(macao);
        }
        if (mainland != null) {
            tops.add(mainland);
        }
        if (hongkong != null) {
            tops.add(hongkong);
        }

        result.clear();
        result.put(top, tops);
        return JSON.toJSONString(result);
    }

    private TreeMap<String, List<CountryAndRegionParam>> getAllowRegisterList(String language) {
        List<Map<String, String>> allowRegisterList =  paramCache.getComplexMultiList(APP_USER_REGISTER_COUNTRY_CODE_LIST);
        log.debug("allowRegisterList:{}", allowRegisterList);

        String nameKey = switch (language) {
            case ZHCN -> ZH_CN_NAME;
            case ENUS -> EN_US_NAME;
            case PTPT -> PT_PT_NAME;
            default -> ZH_HK_NAME;
        };

        TreeMap<String, List<CountryAndRegionParam>> result = new TreeMap<>();
        for(Map<String, String> map : allowRegisterList) {
            if (StringUtils.isNotBlank(map.get(FIRST_LETTER))
                && StringUtils.isNotBlank(map.get(CODE))
                && StringUtils.isNotBlank(map.get(nameKey))) {
                String firstLetter = JSONObject.parseObject(map.get(FIRST_LETTER)).getString(language);
                if (StringUtils.isNotBlank(firstLetter)) {
                    CountryAndRegionParam countryAndRegionParam = new CountryAndRegionParam();
                    countryAndRegionParam.setIsAllowRegister(ALLOW_REGISTER);
                    countryAndRegionParam.setCode(map.get(CODE));
                    countryAndRegionParam.setCountryCode(map.get(REGION_CODE));
                    countryAndRegionParam.setFirstLetter(firstLetter);
                    countryAndRegionParam.setName(map.get(nameKey));

                    List<CountryAndRegionParam> second = result.get(firstLetter);
                    if (second != null && second.size() > 0) {
                        second.add(countryAndRegionParam);
                    } else {
                        second = new ArrayList<>();
                        second.add(countryAndRegionParam);
                        result.put(firstLetter, second);
                    }
                }
            }
        }

        return result;
    }

    private TreeMap<String, List<CountryAndRegionParam>> getAllowTransferList(String language) {
        List<Map<String, String>> allowTransferList =  paramCache.getComplexMultiList(APP_USER_TRANSFER_COUNTRY_CODE_LIST);
        log.debug("allowTransferList:{}", allowTransferList);

        String nameKey = switch (language) {
            case ZHCN -> ZH_CN_NAME;
            case ENUS -> EN_US_NAME;
            case PTPT -> PT_PT_NAME;
            default -> ZH_HK_NAME;
        };

        TreeMap<String, List<CountryAndRegionParam>> result = new TreeMap<>();
        for(Map<String, String> map : allowTransferList) {
            if (StringUtils.isNotBlank(map.get(FIRST_LETTER)) && StringUtils.isNotBlank(map.get(CODE))) {
                String firstLetter = JSONObject.parseObject(map.get(FIRST_LETTER)).getString(language);
                if (StringUtils.isNotBlank(firstLetter)) {
                    CountryAndRegionParam countryAndRegionParam = new CountryAndRegionParam();
                    countryAndRegionParam.setIsAllowTransfer(ALLOW_TRANSFER);
                    countryAndRegionParam.setCode(map.get(CODE));
                    countryAndRegionParam.setCountryCode(map.get(REGION_CODE));
                    countryAndRegionParam.setFirstLetter(firstLetter);
                    countryAndRegionParam.setName(map.get(nameKey));

                    List<CountryAndRegionParam> second = result.get(firstLetter);
                    if (second != null && second.size() > 0) {
                        second.add(countryAndRegionParam);
                    } else {
                        second = new ArrayList<>();
                        second.add(countryAndRegionParam);
                        result.put(firstLetter, second);
                    }
                }
            }
        }

        return result;
    }
}
