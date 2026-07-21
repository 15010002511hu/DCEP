package com.emop.wlt.user.management.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BusinessParamHelper {
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
}
