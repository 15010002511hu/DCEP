package com.emop.wlt.user.query.helper;

import com.alibaba.fastjson.JSONObject;
import com.emop.infocache.api.dto.FiTechParamAttachMasterDTO;
import com.emop.infocache.api.dto.OrgDTO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BankCardInfoHelper {

    public static List<FiTechParamAttachMasterDTO> buildBankCardInfo() {
        FiTechParamAttachMasterDTO fiTechParamAttachMasterDTO1 = new FiTechParamAttachMasterDTO();
        fiTechParamAttachMasterDTO1.setOrgCode("002000");
        fiTechParamAttachMasterDTO1.setBankFirstLetter("B");
        fiTechParamAttachMasterDTO1.setBankName("中国银行");
        fiTechParamAttachMasterDTO1.setPullAppId("pullAppId1");
        fiTechParamAttachMasterDTO1.setColorLogo("colorLogo1");
        fiTechParamAttachMasterDTO1.setWhiteLogo("whiteLogo1");
        fiTechParamAttachMasterDTO1.setDarkLogo("darkLogo1");
        fiTechParamAttachMasterDTO1.setNewStartColor("newStartColor1");
        fiTechParamAttachMasterDTO1.setNewEndColor("newEndColor1");
        fiTechParamAttachMasterDTO1.setNewBackgroundColor("newBackgroundColor1");
        fiTechParamAttachMasterDTO1.setCardTypes("2");
        fiTechParamAttachMasterDTO1.setWalletAutoTopup("1");
        fiTechParamAttachMasterDTO1.setCardAutoTopup("1");

        FiTechParamAttachMasterDTO fiTechParamAttachMasterDTO2 = new FiTechParamAttachMasterDTO();
        fiTechParamAttachMasterDTO2.setOrgCode("003000");
        fiTechParamAttachMasterDTO2.setBankFirstLetter("H");
        fiTechParamAttachMasterDTO2.setBankName("汇丰银行");
        fiTechParamAttachMasterDTO2.setPullAppId("pullAppId2");
        fiTechParamAttachMasterDTO2.setColorLogo("colorLogo2");
        fiTechParamAttachMasterDTO2.setWhiteLogo("whiteLogo2");
        fiTechParamAttachMasterDTO2.setDarkLogo("darkLogo2");
        fiTechParamAttachMasterDTO2.setNewStartColor("newStartColor2");
        fiTechParamAttachMasterDTO2.setNewEndColor("newEndColor2");
        fiTechParamAttachMasterDTO2.setNewBackgroundColor("newBackgroundColor2");
        fiTechParamAttachMasterDTO2.setCardTypes("2");
        fiTechParamAttachMasterDTO2.setWalletAutoTopup("1");
        fiTechParamAttachMasterDTO2.setCardAutoTopup("1");

        FiTechParamAttachMasterDTO fiTechParamAttachMasterDTO3 = new FiTechParamAttachMasterDTO();
        fiTechParamAttachMasterDTO3.setOrgCode("005000");
        fiTechParamAttachMasterDTO3.setBankFirstLetter("");
        fiTechParamAttachMasterDTO3.setBankName("花旗银行");
        fiTechParamAttachMasterDTO3.setPullAppId("pullAppId3");
        fiTechParamAttachMasterDTO3.setColorLogo("colorLogo3");
        fiTechParamAttachMasterDTO3.setWhiteLogo("whiteLogo3");
        fiTechParamAttachMasterDTO3.setDarkLogo("darkLogo3");
        fiTechParamAttachMasterDTO3.setNewStartColor("newStartColor3");
        fiTechParamAttachMasterDTO3.setNewEndColor("newEndColor3");
        fiTechParamAttachMasterDTO3.setNewBackgroundColor("newBackgroundColor3");
        fiTechParamAttachMasterDTO3.setCardTypes("2");
        fiTechParamAttachMasterDTO3.setWalletAutoTopup("1");
        fiTechParamAttachMasterDTO3.setCardAutoTopup("1");

        List<FiTechParamAttachMasterDTO> fiTechParamAttachMasterDTOList = new ArrayList<>();
        fiTechParamAttachMasterDTOList.add(fiTechParamAttachMasterDTO1);
        fiTechParamAttachMasterDTOList.add(fiTechParamAttachMasterDTO2);
        fiTechParamAttachMasterDTOList.add(fiTechParamAttachMasterDTO3);
        return fiTechParamAttachMasterDTOList;
    }

    public static Map<String, OrgDTO> buildFiInf() {
        OrgDTO orgDTO1 = new OrgDTO();
        orgDTO1.setOrgCode("002000");
        orgDTO1.setComplexShortName("中國銀行");
        orgDTO1.setFiEnglishShortName("BOC");
        OrgDTO orgDTO2 = new OrgDTO();
        orgDTO2.setOrgCode("003000");
        orgDTO2.setComplexShortName("匯豐銀行");
        orgDTO2.setFiEnglishShortName("HSBC");
        OrgDTO orgDTO3 = new OrgDTO();
        orgDTO3.setOrgCode("005000");
        orgDTO3.setComplexShortName("花旗銀行");
        orgDTO3.setFiEnglishShortName("");
        Map<String, OrgDTO> fiInf = new HashMap<>();
        fiInf.put("002000", orgDTO1);
        fiInf.put("003000", orgDTO2);
        fiInf.put("005000", orgDTO3);
        return fiInf;
    }

    public static JSONObject buildExpectedBindCardDetailBanksInfoZHHK() {
        return JSONObject.parseObject(""
            + "{\n"
            + "    \"002000\": {\n"
            + "        \"ShortName\": \"中國銀行\",\n"
            + "        \"NameIndex\": \"B\",\n"
            + "        \"BindCardAppID\": \"pullAppId1\",\n"
            + "        \"ColorLogo\": \"colorLogo1\",\n"
            + "        \"WhiteLogo\": \"whiteLogo1\",\n"
            + "        \"DarkLogo\": \"darkLogo1\",\n"
            + "        \"Color1\": \"newStartColor1\",\n"
            + "        \"Color2\": \"newEndColor1\",\n"
            + "        \"Color3\": \"newBackgroundColor1\",\n"
            + "        \"CardType\": \"1\",\n"
            + "        \"EnableWalletAutoTopup\": \"1\",\n"
            + "        \"EnableCardAutoTopup\": \"1\"\n"
            + "    },\n"
            + "    \"003000\": {\n"
            + "        \"ShortName\": \"匯豐銀行\",\n"
            + "        \"NameIndex\": \"H\",\n"
            + "        \"BindCardAppID\": \"pullAppId2\",\n"
            + "        \"ColorLogo\": \"colorLogo2\",\n"
            + "        \"WhiteLogo\": \"whiteLogo2\",\n"
            + "        \"DarkLogo\": \"darkLogo2\",\n"
            + "        \"Color1\": \"newStartColor2\",\n"
            + "        \"Color2\": \"newEndColor2\",\n"
            + "        \"Color3\": \"newBackgroundColor2\",\n"
            + "        \"CardType\": \"1\",\n"
            + "        \"EnableWalletAutoTopup\": \"1\",\n"
            + "        \"EnableCardAutoTopup\": \"1\"\n"
            + "    },\n"
            + "    \"005000\": {\n"
            + "        \"ShortName\": \"花旗銀行\",\n"
            + "        \"NameIndex\": \"A\",\n"
            + "        \"BindCardAppID\": \"pullAppId3\",\n"
            + "        \"ColorLogo\": \"colorLogo3\",\n"
            + "        \"WhiteLogo\": \"whiteLogo3\",\n"
            + "        \"DarkLogo\": \"darkLogo3\",\n"
            + "        \"Color1\": \"newStartColor3\",\n"
            + "        \"Color2\": \"newEndColor3\",\n"
            + "        \"Color3\": \"newBackgroundColor3\",\n"
            + "        \"CardType\": \"1\",\n"
            + "        \"EnableWalletAutoTopup\": \"1\",\n"
            + "        \"EnableCardAutoTopup\": \"1\"\n"
            + "    }\n"
            + "}");
    }

    public static JSONObject buildExceptedBindCardDetailBanksInfoZHCN() {
        return JSONObject.parseObject(""
            + "{\n"
            + "    \"002000\": {\n"
            + "        \"ShortName\": \"中国银行\",\n"
            + "        \"NameIndex\": \"B\",\n"
            + "        \"BindCardAppID\": \"pullAppId1\",\n"
            + "        \"ColorLogo\": \"colorLogo1\",\n"
            + "        \"WhiteLogo\": \"whiteLogo1\",\n"
            + "        \"DarkLogo\": \"darkLogo1\",\n"
            + "        \"Color1\": \"newStartColor1\",\n"
            + "        \"Color2\": \"newEndColor1\",\n"
            + "        \"Color3\": \"newBackgroundColor1\",\n"
            + "        \"CardType\": \"1\",\n"
            + "        \"EnableWalletAutoTopup\": \"1\",\n"
            + "        \"EnableCardAutoTopup\": \"1\"\n"
            + "    },\n"
            + "    \"003000\": {\n"
            + "        \"ShortName\": \"汇丰银行\",\n"
            + "        \"NameIndex\": \"H\",\n"
            + "        \"BindCardAppID\": \"pullAppId2\",\n"
            + "        \"ColorLogo\": \"colorLogo2\",\n"
            + "        \"WhiteLogo\": \"whiteLogo2\",\n"
            + "        \"DarkLogo\": \"darkLogo2\",\n"
            + "        \"Color1\": \"newStartColor2\",\n"
            + "        \"Color2\": \"newEndColor2\",\n"
            + "        \"Color3\": \"newBackgroundColor2\",\n"
            + "        \"CardType\": \"1\",\n"
            + "        \"EnableWalletAutoTopup\": \"1\",\n"
            + "        \"EnableCardAutoTopup\": \"1\"\n"
            + "    },\n"
            + "    \"005000\": {\n"
            + "        \"ShortName\": \"花旗银行\",\n"
            + "        \"NameIndex\": \"A\",\n"
            + "        \"BindCardAppID\": \"pullAppId3\",\n"
            + "        \"ColorLogo\": \"colorLogo3\",\n"
            + "        \"WhiteLogo\": \"whiteLogo3\",\n"
            + "        \"DarkLogo\": \"darkLogo3\",\n"
            + "        \"Color1\": \"newStartColor3\",\n"
            + "        \"Color2\": \"newEndColor3\",\n"
            + "        \"Color3\": \"newBackgroundColor3\",\n"
            + "        \"CardType\": \"1\",\n"
            + "        \"EnableWalletAutoTopup\": \"1\",\n"
            + "        \"EnableCardAutoTopup\": \"1\"\n"
            + "    }\n"
            + "}");
    }

    public static JSONObject buildExceptedBindCardDetailBanksInfoENUS() {
        return JSONObject.parseObject(""
            + "{\n"
            + "    \"002000\": {\n"
            + "        \"ShortName\": \"BOC\",\n"
            + "        \"NameIndex\": \"B\",\n"
            + "        \"BindCardAppID\": \"pullAppId1\",\n"
            + "        \"ColorLogo\": \"colorLogo1\",\n"
            + "        \"WhiteLogo\": \"whiteLogo1\",\n"
            + "        \"DarkLogo\": \"darkLogo1\",\n"
            + "        \"Color1\": \"newStartColor1\",\n"
            + "        \"Color2\": \"newEndColor1\",\n"
            + "        \"Color3\": \"newBackgroundColor1\",\n"
            + "        \"CardType\": \"1\",\n"
            + "        \"EnableWalletAutoTopup\": \"1\",\n"
            + "        \"EnableCardAutoTopup\": \"1\"\n"
            + "    },\n"
            + "    \"003000\": {\n"
            + "        \"ShortName\": \"HSBC\",\n"
            + "        \"NameIndex\": \"H\",\n"
            + "        \"BindCardAppID\": \"pullAppId2\",\n"
            + "        \"ColorLogo\": \"colorLogo2\",\n"
            + "        \"WhiteLogo\": \"whiteLogo2\",\n"
            + "        \"DarkLogo\": \"darkLogo2\",\n"
            + "        \"Color1\": \"newStartColor2\",\n"
            + "        \"Color2\": \"newEndColor2\",\n"
            + "        \"Color3\": \"newBackgroundColor2\",\n"
            + "        \"CardType\": \"1\",\n"
            + "        \"EnableWalletAutoTopup\": \"1\",\n"
            + "        \"EnableCardAutoTopup\": \"1\"\n"
            + "    },\n"
            + "    \"005000\": {\n"
            + "        \"ShortName\": \"\",\n"
            + "        \"NameIndex\": \"A\",\n"
            + "        \"BindCardAppID\": \"pullAppId3\",\n"
            + "        \"ColorLogo\": \"colorLogo3\",\n"
            + "        \"WhiteLogo\": \"whiteLogo3\",\n"
            + "        \"DarkLogo\": \"darkLogo3\",\n"
            + "        \"Color1\": \"newStartColor3\",\n"
            + "        \"Color2\": \"newEndColor3\",\n"
            + "        \"Color3\": \"newBackgroundColor3\",\n"
            + "        \"CardType\": \"1\",\n"
            + "        \"EnableWalletAutoTopup\": \"1\",\n"
            + "        \"EnableCardAutoTopup\": \"1\"\n"
            + "    }\n"
            + "}");
    }

}
