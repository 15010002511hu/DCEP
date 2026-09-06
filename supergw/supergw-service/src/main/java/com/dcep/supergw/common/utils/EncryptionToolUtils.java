package com.dcep.supergw.common.utils;

import com.dcep.common.model.soap.SoapHeader;
import com.dcep.encryptsuit.EncryptSuit;
import com.dcep.supergw.common.constant.Constant;
import com.dcep.supergw.common.enums.GwErrorEnum;
import com.dcep.supergw.common.exception.GwException;
import com.dcep.supergw.sign.client.SignClient;
import java.security.NoSuchAlgorithmException;
import org.slf4j.MDC;

/**
 * @author nieyanping
 * @version EncryptionToolUtils.java, v 0.1, 2020/1/9 11:37
 * @description 加解密工具类
 */
public class EncryptionToolUtils {

    /**
     * 组装加密certId（证书ID）
     *
     * @param fiCode 机构编号
     * @param ncrptnSn 机构证书序号
     */
    public static String getEncryptCertId(String fiCode, String ncrptnSn) {
        StringBuffer certId = new StringBuffer();
        certId.append(fiCode).append(Constant.CERT_SEPARATOR).append(ncrptnSn);
        return certId.toString();
    }

    /**
     * @return java.lang.String
     * @description 调用加密机算法生成对称密钥
     * @date 2019/12/10 9:41
     */
    public static String genSecretKey() {
        try {
            return EncryptSuit.genSM4Key().getSzHexKey();
        } catch (NoSuchAlgorithmException e) {
            throw new GwException(GwErrorEnum.UNKNOWN_EXCEPTION, "加密机生成对称密钥失败", e);
        }
    }

    public static String genSecretKey(SoapHeader header) {
        String msgTp371 = "dcep.371.001.01";
        if (msgTp371.equals(header.getMsgTp())) {
            return decryptDgtEnvlp(header.getDgtlEnvlp(),
                getEncryptCertId(InfoCacheUtils.getPbocInf(), header.getNcrptnSN()));
        } else {
            return genSecretKey();
        }
    }

    /**
     * @return java.lang.String
     * @description 利用接收方公钥和对称密钥生成数字信封
     * @date 2019/12/10 9:43
     */
    public static String genDgtlEnvlp(String secretKey, String certId) {
        try {
            return SignClient.getInstance().genDgtlEnvlp(certId, secretKey, MDC.get("msgSn"));
        } catch (Exception e) {
            throw new GwException(GwErrorEnum.ENVLP_ENCRYPT_ERROR,
                "数字信封生成失败：" + e.getMessage(), e);
        }
    }

    public static String decryptDgtEnvlp(String dgtlEnvlp, String certId) {
        try {
            return SignClient.getInstance().decDgtlEnvlp(certId, dgtlEnvlp, MDC.get("msgSn"));
        } catch (Exception e) {
            throw new GwException(GwErrorEnum.ENVLP_DECRYPT_ERROR,
                "数字信封解密失败：" + e.getMessage(), e);
        }
    }

}