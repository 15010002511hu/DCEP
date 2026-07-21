package com.emop.wlt.user.query.helper;

import com.alibaba.fastjson.JSONObject;
import com.emop.infocache.api.dto.ReleaseHistoryDTO;
import com.emop.wlt.user.query.model.vo.Protocol;

public class ProtocolHelper {

    public static ReleaseHistoryDTO buildProtocolData() {
        ReleaseHistoryDTO releaseHistoryDTO = new ReleaseHistoryDTO();
        releaseHistoryDTO.setBizModule("200");
        releaseHistoryDTO.setCurrentReleaseSnapshotId("cac2ac27a2b046ef822d7cd41c644b2b");
        releaseHistoryDTO.setGrayReleaseSnapshotId("e33731a700a644988915508a739bcac5");
        releaseHistoryDTO.setCurTime(null);
        releaseHistoryDTO.setGrayTime(null);
        releaseHistoryDTO.setCurrentContent("{\n"
            + "     \"en-US\": {\n"
            + "        \"appPrivacy_2024101415321928\": {\n"
            + "            \"popupContent\": \"APP User Service Agreement content\",\n"
            + "            \"popupNotifyType\": \"strong\",\n"
            + "            \"popupTitle\": \"APP User Service Agreement\",\n"
            + "            \"popupVersion\": \"1.0.0\",\n"
            + "            \"protocolNo\": \"2024101415321928\",\n"
            + "            \"title\": \"APP User Service Agreement\",\n"
            + "            \"uuid\": \"0d5869ae284d4133a9f551a77b23c10a\",\n"
            + "            \"url\": \"https://download-pet-s.cashyuan.com.cn:9103/emop-doms-public/f44c2022-ede5-4caf-9e5b-b5040a761010.html\"\n"
            + "        },\n"
            + "        \"orgCardBindCard_002000\": {\n"
            + "            \"popupContent\": \"\",\n"
            + "            \"innerPdfUrl\": \"https://download-pet-s.cashyuan.com.cn:9103/emop-doms-public/83e9aab4-e51f-4d3d-baf7-1f5d7a9cb28a.pdf\",\n"
            + "            \"popupNotifyType\": \"no\",\n"
            + "            \"popupTitle\": \"\",\n"
            + "            \"popupVersion\": \"\",\n"
            + "            \"protocolNo\": \"20241014150751383\",\n"
            + "            \"title\": \"Service Agreement for Binding Bank Account to Exchange e-MOP\",\n"
            + "            \"uuid\": \"15ca32c6386445a8a4d78a10d041c9f7\",\n"
            + "            \"url\": \"https://download-pet-s.cashyuan.com.cn:9103/emop-doms-public/c84fcb0a-3536-4791-9fbc-e19d221ad999.html\"\n"
            + "        }\n"
            + "    },\n"
            + "    \"zh-HK\": {\n"
            + "        \"appPrivacy_2024101415321928\": {\n"
            + "            \"popupContent\": \"APP用戶服務協議弹窗内容\",\n"
            + "            \"popupNotifyType\": \"strong\",\n"
            + "            \"popupTitle\": \"APP用戶服務協議弹窗标题\",\n"
            + "            \"popupVersion\": \"1.0.0\",\n"
            + "            \"protocolNo\": \"2024101415321928\",\n"
            + "            \"title\": \"APP用戶服務協議\",\n"
            + "            \"uuid\": \"0d5869ae284d4133a9f551a77b23c10a\",\n"
            + "            \"url\": \"https://download-pet-s.cashyuan.com.cn:9103/emop-doms-public/c8062cf9-7ad0-4273-b133-e3c9b3bde7bb.html\"\n"
            + "        },\n"
            + "        \"orgCardBindCard_002000\": {\n"
            + "            \"popupContent\": \"\",\n"
            + "            \"innerPdfUrl\": \"http://172.21.37.82:8090/download/attachments/82751197/%E6%96%B9%E6%AD%A3%E5%AD%97%E5%BA%93%E7%94%9F%E5%83%BB%E5%AD%97%E8%BE%93%E5%85%A5%E6%B3%95%E6%8A%80%E6%9C%AF%E4%BD%BF%E7%94%A8%E6%96%87%E6%A1%A3.pdf?api=v2\",\n"
            + "            \"popupNotifyType\": \"no\",\n"
            + "            \"popupTitle\": \"\",\n"
            + "            \"popupVersion\": \"\",\n"
            + "            \"protocolNo\": \"20241014150751383\",\n"
            + "            \"title\": \"方正字庫生僻字輸入法技術使用文檔\",\n"
            + "            \"uuid\": \"15ca32c6386445a8a4d78a10d041c9f7\",\n"
            + "            \"url\": \"https://download-pet-s.cashyuan.com.cn:9103/emop-doms-public/6304fe4e-d92c-4606-99a6-8124ce6cf200.html\"\n"
            + "        }\n"
            + "    },\n"
            + "    \"zh-CN\": {\n"
            + "        \"appPrivacy_2024101415321928\": {\n"
            + "            \"popupContent\": \"APP用户服务协议弹窗内容\",\n"
            + "            \"popupNotifyType\": \"strong\",\n"
            + "            \"popupTitle\": \"APP用户服务协议弹窗标题\",\n"
            + "            \"popupVersion\": \"1.0.0\",\n"
            + "            \"protocolNo\": \"2024101415321928\",\n"
            + "            \"title\": \"APP用户服务协议\",\n"
            + "            \"uuid\": \"0d5869ae284d4133a9f551a77b23c10a\",\n"
            + "            \"url\": \"https://download-pet-s.cashyuan.com.cn:9103/emop-doms-public/6b3c6b0c-becc-4883-bb35-cff3d5c0283d.html\"\n"
            + "        },\n"
            + "        \"orgCardBindCard_002000\": {\n"
            + "            \"popupContent\": \"\",\n"
            + "            \"innerPdfUrl\": \"https://download-pet-s.cashyuan.com.cn:9103/emop-doms-public/9a8eb8b9-425a-4261-8e83-1a75722a5253.pdf\",\n"
            + "            \"popupNotifyType\": \"no\",\n"
            + "            \"popupTitle\": \"\",\n"
            + "            \"popupVersion\": \"\",\n"
            + "            \"protocolNo\": \"20241014150751383\",\n"
            + "            \"title\": \"绑定银行账户兑换数字澳门元业务服务协议\",\n"
            + "            \"uuid\": \"15ca32c6386445a8a4d78a10d041c9f7\",\n"
            + "            \"url\": \"https://download-pet-s.cashyuan.com.cn:9103/emop-doms-public/0782e5fe-fa0e-4cb6-9439-be80535dd1d0.html\"\n"
            + "        }\n"
            + "    }\n"
            + "}");
        releaseHistoryDTO.setGrayContent("{\n"
            + "    \"en-US\": {\n"
            + "        \"appPrivacy_2024101415321928\": {\n"
            + "            \"popupContent\": \"APP User Service Agreement content\",\n"
            + "            \"popupNotifyType\": \"strong\",\n"
            + "            \"popupTitle\": \"APP User Service Agreement\",\n"
            + "            \"popupVersion\": \"1.0.0\",\n"
            + "            \"protocolNo\": \"2024101415321928\",\n"
            + "            \"title\": \"APP User Service Agreement\",\n"
            + "            \"uuid\": \"0d5869ae284d4133a9f551a77b23c10a\",\n"
            + "            \"url\": \"https://download-pet-s.cashyuan.com.cn:9103/emop-doms-public/f44c2022-ede5-4caf-9e5b-b5040a761010_gray.html\"\n"
            + "        },\n"
            + "        \"orgCardBindCard_002000\": {\n"
            + "            \"popupContent\": \"\",\n"
            + "            \"innerPdfUrl\": \"https://download-pet-s.cashyuan.com.cn:9103/emop-doms-public/83e9aab4-e51f-4d3d-baf7-1f5d7a9cb28a.pdf\",\n"
            + "            \"popupNotifyType\": \"no\",\n"
            + "            \"popupTitle\": \"\",\n"
            + "            \"popupVersion\": \"\",\n"
            + "            \"protocolNo\": \"20241014150751383\",\n"
            + "            \"title\": \"Service Agreement for Binding Bank Account to Exchange e-MOP\",\n"
            + "            \"uuid\": \"15ca32c6386445a8a4d78a10d041c9f7\",\n"
            + "            \"url\": \"https://download-pet-s.cashyuan.com.cn:9103/emop-doms-public/c84fcb0a-3536-4791-9fbc-e19d221ad999.html\"\n"
            + "        }\n"
            + "    },\n"
            + "    \"zh-HK\": {\n"
            + "        \"appPrivacy_2024101415321928\": {\n"
            + "            \"popupContent\": \"APP用戶服務協議弹窗内容\",\n"
            + "            \"popupNotifyType\": \"strong\",\n"
            + "            \"popupTitle\": \"APP用戶服務協議弹窗标题\",\n"
            + "            \"popupVersion\": \"1.0.0\",\n"
            + "            \"protocolNo\": \"2024101415321928\",\n"
            + "            \"title\": \"APP用戶服務協議\",\n"
            + "            \"uuid\": \"0d5869ae284d4133a9f551a77b23c10a\",\n"
            + "            \"url\": \"https://download-pet-s.cashyuan.com.cn:9103/emop-doms-public/c8062cf9-7ad0-4273-b133-e3c9b3bde7bb_gray.html\"\n"
            + "        },\n"
            + "        \"orgCardBindCard_002000\": {\n"
            + "            \"popupContent\": \"\",\n"
            + "            \"innerPdfUrl\": \"http://172.21.37.82:8090/download/attachments/82751197/%E6%96%B9%E6%AD%A3%E5%AD%97%E5%BA%93%E7%94%9F%E5%83%BB%E5%AD%97%E8%BE%93%E5%85%A5%E6%B3%95%E6%8A%80%E6%9C%AF%E4%BD%BF%E7%94%A8%E6%96%87%E6%A1%A3.pdf?api=v2\",\n"
            + "            \"popupNotifyType\": \"no\",\n"
            + "            \"popupTitle\": \"\",\n"
            + "            \"popupVersion\": \"\",\n"
            + "            \"protocolNo\": \"20241014150751383\",\n"
            + "            \"title\": \"方正字庫生僻字輸入法技術使用文檔\",\n"
            + "            \"uuid\": \"15ca32c6386445a8a4d78a10d041c9f7\",\n"
            + "            \"url\": \"https://download-pet-s.cashyuan.com.cn:9103/emop-doms-public/6304fe4e-d92c-4606-99a6-8124ce6cf200.html\"\n"
            + "        }\n"
            + "    },\n"
            + "    \"zh-CN\": {\n"
            + "        \"appPrivacy_2024101415321928\": {\n"
            + "            \"popupContent\": \"APP用户服务协议弹窗内容\",\n"
            + "            \"popupNotifyType\": \"strong\",\n"
            + "            \"popupTitle\": \"APP用户服务协议弹窗标题\",\n"
            + "            \"popupVersion\": \"1.0.0\",\n"
            + "            \"protocolNo\": \"2024101415321928\",\n"
            + "            \"title\": \"APP用户服务协议\",\n"
            + "            \"uuid\": \"0d5869ae284d4133a9f551a77b23c10a\",\n"
            + "            \"url\": \"https://download-pet-s.cashyuan.com.cn:9103/emop-doms-public/6b3c6b0c-becc-4883-bb35-cff3d5c0283d_gray.html\"\n"
            + "        },\n"
            + "        \"orgCardBindCard_002000\": {\n"
            + "            \"popupContent\": \"\",\n"
            + "            \"innerPdfUrl\": \"https://download-pet-s.cashyuan.com.cn:9103/emop-doms-public/9a8eb8b9-425a-4261-8e83-1a75722a5253.pdf\",\n"
            + "            \"popupNotifyType\": \"no\",\n"
            + "            \"popupTitle\": \"\",\n"
            + "            \"popupVersion\": \"\",\n"
            + "            \"protocolNo\": \"20241014150751383\",\n"
            + "            \"title\": \"绑定银行账户兑换数字澳门元业务服务协议\",\n"
            + "            \"uuid\": \"15ca32c6386445a8a4d78a10d041c9f7\",\n"
            + "            \"url\": \"https://download-pet-s.cashyuan.com.cn:9103/emop-doms-public/0782e5fe-fa0e-4cb6-9439-be80535dd1d0.html\"\n"
            + "        }\n"
            + "    }\n"
            + "}");

        return releaseHistoryDTO;
    }

    public static Protocol buildExpectedProtocolDataOfficial() {
        String content = "{\n"
            + "    \"appPrivacy\": {\n"
            + "        \"popupContent\": \"APP用戶服務協議弹窗内容\",\n"
            + "        \"popupNotifyType\": \"strong\",\n"
            + "        \"popupTitle\": \"APP用戶服務協議弹窗标题\",\n"
            + "        \"popupVersion\": \"1.0.0\",\n"
            + "        \"protocolNo\": \"2024101415321928\",\n"
            + "        \"title\": \"APP用戶服務協議\",\n"
            + "        \"uuid\": \"0d5869ae284d4133a9f551a77b23c10a\",\n"
            + "        \"url\": \"https://download-pet-s.cashyuan.com.cn:9103/emop-doms-public/c8062cf9-7ad0-4273-b133-e3c9b3bde7bb.html\"\n"
            + "    },\n"
            + "    \"orgCardBindCard_002000\": {\n"
            + "        \"popupContent\": \"\",\n"
            + "        \"innerPdfUrl\": \"http://172.21.37.82:8090/download/attachments/82751197/%E6%96%B9%E6%AD%A3%E5%AD%97%E5%BA%93%E7%94%9F%E5%83%BB%E5%AD%97%E8%BE%93%E5%85%A5%E6%B3%95%E6%8A%80%E6%9C%AF%E4%BD%BF%E7%94%A8%E6%96%87%E6%A1%A3.pdf?api=v2\",\n"
            + "        \"popupNotifyType\": \"no\",\n"
            + "        \"popupTitle\": \"\",\n"
            + "        \"popupVersion\": \"\",\n"
            + "        \"protocolNo\": \"20241014150751383\",\n"
            + "        \"title\": \"方正字庫生僻字輸入法技術使用文檔\",\n"
            + "        \"uuid\": \"15ca32c6386445a8a4d78a10d041c9f7\",\n"
            + "        \"url\": \"https://download-pet-s.cashyuan.com.cn:9103/emop-doms-public/6304fe4e-d92c-4606-99a6-8124ce6cf200.html\"\n"
            + "    }\n"
            + "}";

        JSONObject protocolContent = JSONObject.parseObject(content);
        JSONObject protocolMap = new JSONObject();
        for (String key : protocolContent.keySet()) {
            protocolMap.put(key, protocolContent.get(key));
        }

        return Protocol.builder()
            .releaseId("cac2ac27a2b046ef822d7cd41c644b2b")
            .updateTime("")
            .content(protocolMap).build();
    }

    public static Protocol buildExpectedProtocolDataGray() {
        String content = "{\n"
            + "    \"appPrivacy\": {\n"
            + "        \"popupContent\": \"APP用戶服務協議弹窗内容\",\n"
            + "        \"popupNotifyType\": \"strong\",\n"
            + "        \"popupTitle\": \"APP用戶服務協議弹窗标题\",\n"
            + "        \"popupVersion\": \"1.0.0\",\n"
            + "        \"protocolNo\": \"2024101415321928\",\n"
            + "        \"title\": \"APP用戶服務協議\",\n"
            + "        \"uuid\": \"0d5869ae284d4133a9f551a77b23c10a\",\n"
            + "        \"url\": \"https://download-pet-s.cashyuan.com.cn:9103/emop-doms-public/c8062cf9-7ad0-4273-b133-e3c9b3bde7bb_gray.html\"\n"
            + "    },\n"
            + "    \"orgCardBindCard_002000\": {\n"
            + "        \"popupContent\": \"\",\n"
            + "        \"innerPdfUrl\": \"http://172.21.37.82:8090/download/attachments/82751197/%E6%96%B9%E6%AD%A3%E5%AD%97%E5%BA%93%E7%94%9F%E5%83%BB%E5%AD%97%E8%BE%93%E5%85%A5%E6%B3%95%E6%8A%80%E6%9C%AF%E4%BD%BF%E7%94%A8%E6%96%87%E6%A1%A3.pdf?api=v2\",\n"
            + "        \"popupNotifyType\": \"no\",\n"
            + "        \"popupTitle\": \"\",\n"
            + "        \"popupVersion\": \"\",\n"
            + "        \"protocolNo\": \"20241014150751383\",\n"
            + "        \"title\": \"方正字庫生僻字輸入法技術使用文檔\",\n"
            + "        \"uuid\": \"15ca32c6386445a8a4d78a10d041c9f7\",\n"
            + "        \"url\": \"https://download-pet-s.cashyuan.com.cn:9103/emop-doms-public/6304fe4e-d92c-4606-99a6-8124ce6cf200.html\"\n"
            + "    }\n"
            + "}";

        JSONObject protocolContent = JSONObject.parseObject(content);
        JSONObject protocolMap = new JSONObject();
        for (String key : protocolContent.keySet()) {
            protocolMap.put(key, protocolContent.get(key));
        }

        return Protocol.builder()
            .releaseId("e33731a700a644988915508a739bcac5")
            .updateTime("")
            .content(protocolMap).build();
    }

}
