package com.emop.wlt.user.query.helper;

import com.emop.infocache.api.dto.DepartureDTO;
import com.emop.infocache.api.dto.HardWalletFreePayConfigDTO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BusinessParamHelper {

    public static List<DepartureDTO> buildDepartureParamDTO() {
        DepartureDTO departureDTO1 = new DepartureDTO();
        departureDTO1.setCode("+86");
        departureDTO1.setName("{\"zh-CN\":\"中国\", \"en-US\":\"China\",\"zh-HK\":\"中國\"}");
        departureDTO1.setFirst("{\"zh-CN\":\"Z\", \"en-US\":\"C\",\"zh-HK\":\"Z\"}");
        departureDTO1.setIsAllowRegist("1");
        departureDTO1.setIsAllowTransfer("1");
        departureDTO1.setCountryCode("CN");
        DepartureDTO departureDTO2 = new DepartureDTO();
        departureDTO2.setCode("+86");
        departureDTO2.setName("{\"zh-CN\":\"中国\", \"en-US\":\"China\",\"zh-HK\":\"中國\"}");
        departureDTO2.setFirst("{\"zh-CN\":\"#\", \"en-US\":\"#\",\"zh-HK\":\"#\"}");
        departureDTO2.setIsAllowRegist("1");
        departureDTO2.setIsAllowTransfer("1");
        departureDTO2.setCountryCode("CN");
        DepartureDTO departureDTO3 = new DepartureDTO();
        departureDTO3.setCode("+852");
        departureDTO3.setName("{\"zh-CN\":\"中国香港\", \"en-US\":\"Hong Kong, China \",\"zh-HK\":\"中國香港\"}");
        departureDTO3.setFirst("{\"zh-CN\":\"Z\", \"en-US\":\"H\",\"zh-HK\":\"Z\"}");
        departureDTO3.setIsAllowRegist("1");
        departureDTO3.setIsAllowTransfer("1");
        departureDTO3.setCountryCode("HK");
        DepartureDTO departureDTO4 = new DepartureDTO();
        departureDTO4.setCode("+852");
        departureDTO4.setName("{\"zh-CN\":\"中国香港\", \"en-US\":\"Hong Kong, China \",\"zh-HK\":\"中國香港\"}");
        departureDTO4.setFirst("{\"zh-CN\":\"#\", \"en-US\":\"#\",\"zh-HK\":\"#\"}");
        departureDTO4.setIsAllowRegist("1");
        departureDTO4.setIsAllowTransfer("1");
        departureDTO4.setCountryCode("HK");
        DepartureDTO departureDTO5 = new DepartureDTO();
        departureDTO5.setCode("+853");
        departureDTO5.setName("{\"zh-CN\":\"中国澳门\", \"en-US\":\"Macao, China\",\"zh-HK\":\"中國澳門\"}");
        departureDTO5.setFirst("{\"zh-CN\":\"Z\", \"en-US\":\"M\",\"zh-HK\":\"Z\"}");
        departureDTO5.setIsAllowRegist("1");
        departureDTO5.setIsAllowTransfer("1");
        departureDTO5.setCountryCode("MO");
        DepartureDTO departureDTO6 = new DepartureDTO();
        departureDTO6.setCode("+853");
        departureDTO6.setName("{\"zh-CN\":\"中国澳门\", \"en-US\":\"Macao, China\",\"zh-HK\":\"中國澳門\"}");
        departureDTO6.setFirst("{\"zh-CN\":\"#\", \"en-US\":\"#\",\"zh-HK\":\"#\"}");
        departureDTO6.setIsAllowRegist("1");
        departureDTO6.setIsAllowTransfer("1");
        departureDTO6.setCountryCode("MO");

        List<DepartureDTO> departureDTOList = new ArrayList<>();
        departureDTOList.add(departureDTO1);
        departureDTOList.add(departureDTO2);
        departureDTOList.add(departureDTO3);
        departureDTOList.add(departureDTO4);
        departureDTOList.add(departureDTO5);
        departureDTOList.add(departureDTO6);
        return departureDTOList;
    }

    public static List<Map<String,String>> buildCountryAndAreaList() {
        Map<String, String> map1 = new HashMap<>();
        map1.put("firstLetter", "{\"zh-CN\":\"#\", \"en-US\":\"#\",\"zh-HK\":\"#\"}");
        map1.put("code", "+853");
        map1.put("regionCode","MO");
        map1.put("traditionalChineseName", "中國澳門");
        map1.put("simplifiedChineseName", "中国澳门");
        map1.put("englishName", "Macao,China");
        map1.put("mobileNumberLength", "8");
        map1.put("mobileNumberRule", "^[6]\\d{7}$");
        Map<String, String> map2 = new HashMap<>();
        map2.put("firstLetter", "{\"zh-CN\":\"Z\", \"en-US\":\"M\",\"zh-HK\":\"Z\"}");
        map2.put("code", "+853");
        map2.put("regionCode","MO");
        map2.put("simplifiedChineseName", "中国澳门");
        map2.put("traditionalChineseName", "中國澳門");
        map2.put("englishName", "Macao,China");
        map2.put("mobileNumberLength", "8");
        map2.put("mobileNumberRule", "^[6]\\d{7}$");
        Map<String, String> map3 = new HashMap<>();
        map3.put("firstLetter", "{\"zh-CN\":\"#\", \"en-US\":\"#\",\"zh-HK\":\"#\"}");
        map3.put("code", "+86");
        map3.put("regionCode","CN");
        map3.put("simplifiedChineseName", "中国内地");
        map3.put("traditionalChineseName", "中國內地");
        map3.put("englishName", "Mainland,China");
        map3.put("mobileNumberLength", "11");
        map3.put("mobileNumberRule", "^[1][3-9]\\d{9}$");
        Map<String, String> map4 = new HashMap<>();
        map4.put("firstLetter", "{\"zh-CN\":\"Z\", \"en-US\":\"C\",\"zh-HK\":\"Z\"}");
        map4.put("code", "+86");
        map4.put("regionCode","CN");
        map4.put("simplifiedChineseName", "中国内地");
        map4.put("traditionalChineseName", "中國內地");
        map4.put("englishName", "Mainland,China");
        map4.put("mobileNumberLength", "11");
        map4.put("mobileNumberRule", "^[1][3-9]\\d{9}$");
        Map<String, String> map5 = new HashMap<>();
        map5.put("firstLetter", "{ \"zh-CN\":\"#\", \"en-US\":\"#\",\"zh-HK\":\"#\"}");
        map5.put("code", "+852");
        map5.put("regionCode","HK");
        map5.put("simplifiedChineseName", "中国香港");
        map5.put("traditionalChineseName", "中國香港");
        map5.put("englishName", "Hong Kong,China");
        map5.put("mobileNumberLength", "8");
        map5.put("mobileNumberRule", "^[4-9]\\d{7}$");
        Map<String, String> map6 = new HashMap<>();
        map6.put("firstLetter", "{\"zh-CN\":\"Z\", \"en-US\":\"H\",\"zh-HK\":\"Z\"}");
        map6.put("code", "+852");
        map6.put("regionCode","HK");
        map6.put("simplifiedChineseName", "中国香港");
        map6.put("traditionalChineseName", "中國香港");
        map6.put("englishName", "Hong Kong,China");
        map6.put("mobileNumberLength", "8");
        map6.put("mobileNumberRule", "^[4-9]\\d{7}$");

        List<Map<String,String>> countryAndAreaList = new ArrayList<>();
        countryAndAreaList.add(map1);
        countryAndAreaList.add(map2);
        countryAndAreaList.add(map3);
        countryAndAreaList.add(map4);
        countryAndAreaList.add(map5);
        countryAndAreaList.add(map6);
        return countryAndAreaList;
    }

    public static List<HardWalletFreePayConfigDTO> buildHardWalletFreePayConfigDTO() {
        HardWalletFreePayConfigDTO hardWalletFreePayConfigDTO1 = new HardWalletFreePayConfigDTO();
        hardWalletFreePayConfigDTO1.setHardWalletAccFreePayLimitOne("One");
        hardWalletFreePayConfigDTO1.setHardWalletAccFreePayLimitTwo("Two");
        hardWalletFreePayConfigDTO1.setHardWalletAccFreePayLimitDefault("Default");
        HardWalletFreePayConfigDTO hardWalletFreePayConfigDTO2 = new HardWalletFreePayConfigDTO();
        hardWalletFreePayConfigDTO2.setHardWalletAccFreePayLimitOne("1");
        hardWalletFreePayConfigDTO2.setHardWalletAccFreePayLimitTwo("2");
        hardWalletFreePayConfigDTO2.setHardWalletAccFreePayLimitDefault("默认");
        List<HardWalletFreePayConfigDTO> hardWalletFreePayConfigDTOList = new ArrayList<>();
        hardWalletFreePayConfigDTOList.add(hardWalletFreePayConfigDTO1);
        hardWalletFreePayConfigDTOList.add(hardWalletFreePayConfigDTO2);
        return hardWalletFreePayConfigDTOList;
    }

}
