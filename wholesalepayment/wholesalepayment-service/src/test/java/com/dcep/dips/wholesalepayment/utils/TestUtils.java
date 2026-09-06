package com.dcep.dips.wholesalepayment.utils;

import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.utils.DcepDateUtils;
import com.dcep.dips.wholesalepayment.common.constants.Constant;
import com.dcep.dips.wholesalepayment.common.utils.IdUtils;
import com.dcep.dips.wholesalepayment.common.utils.TimeUtil;
import com.dcep.gateway.mcbdc.dto.soap.McbsSoapHeader;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class TestUtils {
    public static String getCurCreDtTm() {
        // 获取当前日期时间
        LocalDateTime now = LocalDateTime.now();
        // 定义日期时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        // 格式化日期时间
        return now.format(formatter);
    }

    public static String getCurDate() {
        // 获取当前日期
        LocalDate now = LocalDate.now();
        // 定义日期格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        // 格式化日期
        return now.format(formatter);
    }

    public static String getFullOrgId(String shortOrgId) {
        if (shortOrgId.equals("001")) { return "G4001011000013"; //央行
        } else if(shortOrgId.equals("002")) { return "C1010211000012"; //工商银行
        } else if(shortOrgId.equals("003")) { return "C1010311000014"; //农业银行
        } else if(shortOrgId.equals("004")) { return "C1010411000013"; //中国银行
        } else if(shortOrgId.equals("005")) { return "C1010511003703"; //建设银行
        } else if(shortOrgId.equals("006")) { return "Z2004944000010"; //腾讯
        } else if(shortOrgId.equals("007")) { return "Z2007933000010"; //蚂蚁金服
        } else{
            return "错误的机构顺序号";
        }
    }

    public static String getMsgId(String sender, String shortMsgTp) {
        return getCurDate() + sender + "1" + shortMsgTp + IdUtils.getRandomNum(14) + "00" + "0";
    }

    public static String replaceKeysWithValues(String strXml, Map<String, String> dataMap) {
        for (Map.Entry<String, String> entry : dataMap.entrySet()) {
            strXml = strXml.replace(entry.getKey(), entry.getValue());
        }
        return strXml;
    }

    public static <T> T createDcepDTO(String xmlFile, Map<String, String> dataMap, Class<T> clazz) {
        StringBuilder xmlBuilder = new StringBuilder();
        try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(xmlFile)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("File not found in classpath: " + xmlFile);
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    xmlBuilder.append(line).append("\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String strXml = xmlBuilder.toString();
        strXml = TestUtils.replaceKeysWithValues(strXml, dataMap);

        // 替换变量

        try {
            XmlMapper xmlMapper = new XmlMapper();
            T t = xmlMapper.readValue(strXml, clazz);
            System.out.println("dcepDTO=" + t);
            return t;
        } catch (Exception ingored) {
            System.out.println("exception message:" +  ingored.getMessage());
        }

        return null;
    }

    public static SoapHeader createSoapHeader(String msgTp, String msgId, String creDtTm, String shortSender, String shortReceiver) {
        SoapHeader soapHeader = new SoapHeader();
        soapHeader.setVer("01");
        soapHeader.setSndDtTm(creDtTm);
        soapHeader.setMsgTp(msgTp);
        soapHeader.setMsgSN(msgId + "0001");
        soapHeader.setSender(TestUtils.getFullOrgId(shortSender));
        soapHeader.setReceiver(TestUtils.getFullOrgId(shortReceiver));
        soapHeader.setSignSN("3");
        soapHeader.setNcrptnSN("3");
        soapHeader.setDgtlEnvlp("4");
        return soapHeader;
    }

    public static McbsSoapHeader generateMcbsSoapHeader(String msgTp) {
        McbsSoapHeader mcbsSoapHeader = new McbsSoapHeader();
        mcbsSoapHeader.setVer(Constant.MCBS_SOAPHEADER_VER);
        mcbsSoapHeader.setSndDtTm(TimeUtil.getMcbsCurrentTime(DcepDateUtils.ISO_DATETIME_PATTERN));
        mcbsSoapHeader.setMsgTp(msgTp);
        mcbsSoapHeader.setSenderLEI(Constant.LEI_MCBS);
        mcbsSoapHeader.setSenderCBMALEI(Constant.LEI_MCBS);
        mcbsSoapHeader.setReceiverLEI(Constant.LEI_PBOC);
        mcbsSoapHeader.setReceiverCBMALEI(Constant.LEI_PBOC);
        mcbsSoapHeader.setMessageDirection("S");
        return mcbsSoapHeader;
    }
}
