package com.emop.wlt.user.query.helper;


import com.emop.infocache.api.dto.help.HelpClassifyDetailDTO;
import com.emop.infocache.api.dto.help.HelpClassifyLangDTO;
import com.emop.infocache.api.dto.help.HelpDetailDTO;
import com.emop.infocache.api.dto.help.HelpDetailLangDTO;
import com.emop.infocache.api.dto.help.HelpQuestSortDTO;
import com.emop.infocache.api.dto.help.HelpVersionDTO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HelpQuestionHelper {

    public static HelpClassifyDetailDTO buildHelpClassifyDetail1() {
        HelpClassifyLangDTO helpClassifyLangDTO1 = new HelpClassifyLangDTO();
        helpClassifyLangDTO1.setKeyword("硬钱包US");
        helpClassifyLangDTO1.setLanguageType("en-US");
        helpClassifyLangDTO1.setClassifyName("硬钱包US");
        HelpClassifyLangDTO helpClassifyLangDT02 = new HelpClassifyLangDTO();
        helpClassifyLangDT02.setKeyword("硬钱包PT");
        helpClassifyLangDT02.setLanguageType("pt-PT");
        helpClassifyLangDT02.setClassifyName("硬钱包PT");
        HelpClassifyLangDTO helpClassifyLangDT03 = new HelpClassifyLangDTO();
        helpClassifyLangDT03.setKeyword("硬钱包HK");
        helpClassifyLangDT03.setLanguageType("zh-HK");
        helpClassifyLangDT03.setClassifyName("硬钱包HK");
        HelpClassifyLangDTO helpClassifyLangDTO4 = new HelpClassifyLangDTO();
        helpClassifyLangDTO4.setKeyword("硬钱包CN");
        helpClassifyLangDTO4.setLanguageType("zh-CN");
        helpClassifyLangDTO4.setClassifyName("硬钱包CN");
        List<HelpClassifyLangDTO> langs = new ArrayList<>();
        langs.add(helpClassifyLangDTO1);
        langs.add(helpClassifyLangDT02);
        langs.add(helpClassifyLangDT03);
        langs.add(helpClassifyLangDTO4);
        HelpQuestSortDTO helpQuestSortDTO = new HelpQuestSortDTO();
        helpQuestSortDTO.setQuestionNo("QU0005202410143607");
        helpQuestSortDTO.setSortId(1);
        List<HelpQuestSortDTO> questionNos = new ArrayList<>();
        questionNos.add(helpQuestSortDTO);
        HelpClassifyDetailDTO helpClassifyDetailDTO = new HelpClassifyDetailDTO();
        helpClassifyDetailDTO.setClassifyNo("CC0005202410145482");
        helpClassifyDetailDTO.setClassifyType("1");
        helpClassifyDetailDTO.setIcon("https");
        helpClassifyDetailDTO.setEffectiveStatus("0");
        helpClassifyDetailDTO.setLangs(langs);
        helpClassifyDetailDTO.setQuestions(questionNos);
        return helpClassifyDetailDTO;
    }

    public static HelpClassifyDetailDTO buildHelpClassifyDetail2() {
        HelpClassifyLangDTO helpClassifyLangDTO1 = new HelpClassifyLangDTO();
        helpClassifyLangDTO1.setKeyword("常见问题US");
        helpClassifyLangDTO1.setLanguageType("en-US");
        helpClassifyLangDTO1.setClassifyName("常见问题US");
        HelpClassifyLangDTO helpClassifyLangDTO2 = new HelpClassifyLangDTO();
        helpClassifyLangDTO2.setKeyword("常见问题PT");
        helpClassifyLangDTO2.setLanguageType("pt-PT");
        helpClassifyLangDTO2.setClassifyName("常见问题PT");
        HelpClassifyLangDTO helpClassifyLangDTO3 = new HelpClassifyLangDTO();
        helpClassifyLangDTO3.setKeyword("常见问题HK");
        helpClassifyLangDTO3.setLanguageType("zh-HK");
        helpClassifyLangDTO3.setClassifyName("常见问题HK");
        HelpClassifyLangDTO helpClassifyLangDTO4 = new HelpClassifyLangDTO();
        helpClassifyLangDTO4.setKeyword("常见问题CN");
        helpClassifyLangDTO4.setLanguageType("zh-CN");
        helpClassifyLangDTO4.setClassifyName("常见问题CN");
        List<HelpClassifyLangDTO> langs = new ArrayList<>();
        langs.add(helpClassifyLangDTO1);
        langs.add(helpClassifyLangDTO2);
        langs.add(helpClassifyLangDTO3);
        langs.add(helpClassifyLangDTO4);
        HelpQuestSortDTO helpQuestSortDTO = new HelpQuestSortDTO();
        helpQuestSortDTO.setQuestionNo("QU0005202410140245");
        helpQuestSortDTO.setSortId(1);
        List<HelpQuestSortDTO> questionNos = new ArrayList<>();
        questionNos.add(helpQuestSortDTO);
        HelpClassifyDetailDTO helpClassifyDetailDTO = new HelpClassifyDetailDTO();
        helpClassifyDetailDTO.setClassifyNo("CC0005202410147401");
        helpClassifyDetailDTO.setClassifyType("1");
        helpClassifyDetailDTO.setIcon("https");
        helpClassifyDetailDTO.setEffectiveStatus("0");
        helpClassifyDetailDTO.setLangs(langs);
        helpClassifyDetailDTO.setQuestions(questionNos);
        return helpClassifyDetailDTO;
    }

    public static HelpDetailDTO buildHelpDetail1(){
        Map<String, String> questionMap = new HashMap<>();
        Map<String, String> answerMap = new HashMap<>();
        questionMap.put("en-US", "What shall I do when I cannot sign up with mobile number?");
        questionMap.put("pt-PT", "What shall I do when I cannot sign up with mobile number?");
        questionMap.put("zh-HK", "手機號註冊不了數字澳門元怎麼辦？");
        questionMap.put("zh-CN", "手机号注册不了数字澳门元怎么办？");
        answerMap.put("en-US", "Please try to ......");
        answerMap.put("pt-PT", "Please try to ......");
        answerMap.put("zh-HK", "請嘗試......");
        answerMap.put("zh-CN", "请尝试 ......");
        HelpDetailDTO helpDetailDTO = buildHelpDetailHelper(questionMap, answerMap);
        helpDetailDTO.setQuestionNo("QU0005202410140245");
        return helpDetailDTO;
    }

    public static HelpDetailDTO buildHelpDetail2(){
        Map<String, String> questionMap = new HashMap<>();
        Map<String, String> answerMap = new HashMap<>();
        questionMap.put("en-US", "Why can't I link my card?");
        questionMap.put("pt-PT", "Why can't I link my card?");
        questionMap.put("zh-HK", "為什麼我的銀行卡沒法綁定？");
        questionMap.put("zh-CN", "为什么我的银行卡没法绑定？");
        answerMap.put("en-US", "Please make sure ......");
        answerMap.put("pt-PT", "Please make sure ......");
        answerMap.put("zh-HK", "請確保......");
        answerMap.put("zh-CN", "请确保......");
        HelpDetailDTO helpDetailDTO = buildHelpDetailHelper(questionMap, answerMap);
        helpDetailDTO.setQuestionNo("QU0005202410143607");
        return helpDetailDTO;
    }

    private static HelpDetailDTO buildHelpDetailHelper(Map<String, String> questionMap, Map<String, String> answerMap) {
        HelpDetailLangDTO helpDetailLangDTO1 = new HelpDetailLangDTO();
        helpDetailLangDTO1.setLanguageType("en-US");
        helpDetailLangDTO1.setQuestion(questionMap.get("en-US"));
        helpDetailLangDTO1.setAnswer(answerMap.get("en-US"));
        List<HelpDetailLangDTO> helpDetailLangDTOList1 = new ArrayList<>();
        helpDetailLangDTOList1.add(helpDetailLangDTO1);
        HelpDetailLangDTO helpDetailLangDTO2 = new HelpDetailLangDTO();
        helpDetailLangDTO2.setLanguageType("pt-PT");
        helpDetailLangDTO2.setQuestion(questionMap.get("pt-PT"));
        helpDetailLangDTO2.setAnswer(answerMap.get("pt-PT"));
        List<HelpDetailLangDTO> helpDetailLangDTOList2 = new ArrayList<>();
        helpDetailLangDTOList2.add(helpDetailLangDTO2);
        HelpDetailLangDTO helpDetailLangDTO3 = new HelpDetailLangDTO();
        helpDetailLangDTO3.setLanguageType("zh-HK");
        helpDetailLangDTO3.setQuestion(questionMap.get("zh-HK"));
        helpDetailLangDTO3.setAnswer(answerMap.get("zh-HK"));
        List<HelpDetailLangDTO> helpDetailLangDTOList3 = new ArrayList<>();
        helpDetailLangDTOList3.add(helpDetailLangDTO3);
        HelpDetailLangDTO helpDetailLangDTO4 = new HelpDetailLangDTO();
        helpDetailLangDTO4.setLanguageType("zh-CN");
        helpDetailLangDTO4.setQuestion(questionMap.get("zh-CN"));
        helpDetailLangDTO4.setAnswer(answerMap.get("zh-CN"));
        List<HelpDetailLangDTO> helpDetailLangDTOList4 = new ArrayList<>();
        helpDetailLangDTOList4.add(helpDetailLangDTO4);

        HelpVersionDTO helpVersionDTO1 = new HelpVersionDTO();
        helpVersionDTO1.setLangs(helpDetailLangDTOList1);
        helpVersionDTO1.setAndroidMax("9.0");
        helpVersionDTO1.setAndroidMin("1.0");
        helpVersionDTO1.setIosMax("9.0");
        helpVersionDTO1.setIosMin("1.0");
        HelpVersionDTO helpVersionDTO2 = new HelpVersionDTO();
        helpVersionDTO2.setLangs(helpDetailLangDTOList2);
        helpVersionDTO2.setAndroidMax("9.0");
        helpVersionDTO2.setAndroidMin("1.0");
        helpVersionDTO2.setIosMax("9.0");
        helpVersionDTO2.setIosMin("1.0");
        HelpVersionDTO helpVersionDTO3 = new HelpVersionDTO();
        helpVersionDTO3.setLangs(helpDetailLangDTOList3);
        helpVersionDTO3.setAndroidMax("9.0");
        helpVersionDTO3.setAndroidMin("1.0");
        helpVersionDTO3.setIosMax("9.0");
        helpVersionDTO3.setIosMin("1.0");
        HelpVersionDTO helpVersionDTO4 = new HelpVersionDTO();
        helpVersionDTO4.setLangs(helpDetailLangDTOList4);
        helpVersionDTO4.setAndroidMax("9.0");
        helpVersionDTO4.setAndroidMin("1.0");
        helpVersionDTO4.setIosMax("9.0");
        helpVersionDTO4.setIosMin("1.0");

        List<HelpVersionDTO> versions = new ArrayList<>();
        versions.add(helpVersionDTO1);
        versions.add(helpVersionDTO2);
        versions.add(helpVersionDTO3);
        versions.add(helpVersionDTO4);
        HelpDetailDTO helpDetailDTO = new HelpDetailDTO();
        helpDetailDTO.setEffectiveStatus("0");
        helpDetailDTO.setVersions(versions);
        return helpDetailDTO;
    }

    public static List<HelpClassifyDetailDTO> buildClassifyDetailDTOList() {
        //构建帮助详分类
        HelpClassifyDetailDTO helpClassifyDetailDTO1 = HelpQuestionHelper.buildHelpClassifyDetail1();
        HelpClassifyDetailDTO helpClassifyDetailDTO2 = HelpQuestionHelper.buildHelpClassifyDetail2();

        //构建帮助分类列表
        List<HelpClassifyDetailDTO> helpClassifyDetailDTOList = new ArrayList<>();
        helpClassifyDetailDTOList.add(helpClassifyDetailDTO1);
        helpClassifyDetailDTOList.add(helpClassifyDetailDTO2);
        return helpClassifyDetailDTOList;
    }

}
