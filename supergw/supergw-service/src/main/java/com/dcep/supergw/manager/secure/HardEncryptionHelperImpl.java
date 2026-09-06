package com.dcep.supergw.manager.secure;

import cn.com.platform.security.adapter.Algorithm;
import cn.com.platform.security.adapter.CipherKey;
import cn.com.platform.security.adapter.Session;
import cn.com.platform.security.adapter.exception.PlatformException;
import cn.com.platform.security.adapter.key.PSecretKey;
import cn.com.platform.security.adapter.key.PX509CertKey;
import cn.com.platform.security.adapter.key.format.KeyEncFormat;
import cn.com.platform.security.adapter.key.format.X509CertLabelFormat;
import cn.com.platform.security.adapter.result.RetDecryptEnvelopeParameter;
import cn.com.platform.security.adapter.result.RetEncryptEnvelopeParameter;
import cn.com.platform.security.adapter.result.RetGenerateSessionKeyParameter;
import cn.com.platform.security.adapter.result.RetSecretDecryptParameter;
import cn.com.platform.security.adapter.result.RetSecretEncryptParameter;
import cn.com.platform.security.adapter.result.RetUnwrapKeyParameter;
import cn.com.platform.security.adapter.result.RetWrapKeyParameter;
import com.dcep.common.encryption.TransEncryption;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.supergw.common.constant.Constant;
import com.dcep.supergw.common.enums.GwErrorEnum;
import com.dcep.supergw.common.exception.GwException;
import com.dcep.supergw.common.utils.InfoCacheUtils;
import com.dcep.supergw.common.utils.SoapHeaderUtils;
import com.dcep.supergw.sign.client.SignClient;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author maxinyu
 * @date 2023/6/9 10:18
 */
public class HardEncryptionHelperImpl extends AbstractEncryptionHelper {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private EnvelopeDTO<?> envelopeDTO;

    private byte[] sessionKey;

    private String orgCode;

    private Boolean isSetSoapHeader = false;

    private String envelopeStr;

    /**
     * 当且仅当字段不多于一个时候使用
     */
    @Override
    public String encrypt(String plainData) {
        if (StringUtils.isBlank(plainData)) {
            throw new GwException(GwErrorEnum.ENCRYPT_ERROR, "待加密字段为空");
        }
        isSetSoapHeader = true;
        if (sessionKey == null) {
            sessionKey = createKey();
            envelopeStr = createEnvelope();
        }
        try {
            Session kmsApi = SignClient.getSession();
            PSecretKey key = new PSecretKey(new KeyEncFormat(sessionKey));
            Algorithm algorithm = new Algorithm(Algorithm.SM4_ECB);
            RetSecretEncryptParameter ret1 = kmsApi.encrypt(envelopeDTO.getSoapHeader().getMsgSN(), key, algorithm,
                plainData.getBytes(Charset.forName(Constant.CHARTSET)));
            return StringUtils
                .toEncodedString(Base64.getEncoder().encode(ret1.getCiphertext()), Charset.forName(Constant.CHARTSET));
        } catch (PlatformException e) {
            throw new GwException(GwErrorEnum.ENCRYPT_ERROR, e);
        }
    }

    @Override
    public String decrypt(String encData) {
        if (StringUtils.isBlank(encData)) {
            throw new GwException(GwErrorEnum.DECRYPT_ERROR, "待解密字段为空");
        }
        try {
            if (sessionKey == null) {
                decryptEnvelope();
            }
            Session kmsApi = SignClient.getSession();
            PSecretKey key = new PSecretKey(new KeyEncFormat(sessionKey));
            Algorithm algorithm = new Algorithm(Algorithm.SM4_ECB);
            //base64 解码
            byte[] dataDecoderByte = Base64.getDecoder().decode(encData.getBytes(Charset.forName(Constant.CHARTSET)));
            RetSecretDecryptParameter ret1 = kmsApi
                .decrypt(envelopeDTO.getSoapHeader().getMsgSN(), key, algorithm, dataDecoderByte);
            return StringUtils.toEncodedString(ret1.getPlaintext(), Charset.forName(Constant.CHARTSET));
        } catch (PlatformException e) {
            throw new GwException(GwErrorEnum.DECRYPT_ERROR, e);
        }
    }

    /**
     * 批量加密
     */
    @Override
    public List<String> encrypt(List<String> plainTexts) {
        //对cipherText中的null进行去重
        StringBuilder plainTextsSb = new StringBuilder();
        for (String s : plainTexts) {
            plainTextsSb.append(s).append("|");
        }
        log.debug("encrypt start , params is: {}", plainTextsSb.toString());
        List<String> formatPlainText = formatRequest(plainTexts);
        if (formatPlainText.isEmpty()) {
            //是否设置soaphead的flag
            return plainTexts;
        }
        isSetSoapHeader = true;
        StringBuilder formatPlainTextSb = new StringBuilder();
        for (String s : formatPlainText) {
            formatPlainTextSb.append(s).append("|");
        }
        log.debug("list encrypt start , format plain list is: {}", formatPlainTextSb.toString());
        try {
            Session kmsApi = SignClient.getSession();
            CipherKey cipherKey = new PX509CertKey(
                new X509CertLabelFormat(orgCode + "_" + InfoCacheUtils.getInstEncryptCertSeriNo(orgCode) + "_e"));
            Algorithm sm4Key = new Algorithm(Algorithm.SM4_KEY);
            Algorithm sm2Sm3Enc = new Algorithm(Algorithm.SM2_SM3_ENC);
            Algorithm sm4Ecb = new Algorithm(Algorithm.SM4_ECB);
            List<byte[]> plainDataBytes = new ArrayList<>();
            for (String plainText : formatPlainText) {
                plainDataBytes.add(plainText.getBytes(Charset.forName(Constant.CHARTSET)));
            }
            RetEncryptEnvelopeParameter parameter = kmsApi
                .encryptWithEnvelope(envelopeDTO.getSoapHeader().getMsgSN(), cipherKey, sm2Sm3Enc, sm4Key, sm4Ecb,
                    plainDataBytes);
            List<byte[]> cipherTexts = parameter.getCiphertexts();
            byte[] envelopeByte = parameter.getWkEvp();
            envelopeStr = Base64.getEncoder().encodeToString(envelopeByte);
            String algorithmType = InfoCacheUtils.fetchAlgorithmType(orgCode);
            envelopeStr = algorithmType + Constant.PIPELINE_SEPARATOR + envelopeStr;
            List<String> result = new ArrayList<>();
            for (byte[] cipherText : cipherTexts) {
                byte[] cipherEncoder = Base64.getEncoder().encode(cipherText);
                result.add(new String(cipherEncoder, Charset.forName(Constant.CHARTSET)));
            }
            StringBuilder resultSb = new StringBuilder();
            for (String s : result) {
                resultSb.append(s).append("|");
            }
            log.debug("list encrypt end , cipher list is: {}", resultSb.toString());
            List<String> formatResult = formatResult(result, plainTexts);
            StringBuilder formatResultSb = new StringBuilder();
            for (String s : formatResult) {
                formatResultSb.append(s).append("|");
            }
            log.debug("list encrypt end , format cipher list is: {}", formatResultSb.toString());
            return formatResult;
        } catch (PlatformException e) {
            throw new GwException(GwErrorEnum.ENCRYPT_ERROR, e);
        }
    }


    /**
     * 批量解密
     */
    @Override
    public List<String> decrypt(List<String> cipherTexts) {
        //对cipherText中的null进行去重
        StringBuilder cipherTextsSb = new StringBuilder();
        for (String s : cipherTexts) {
            cipherTextsSb.append(s).append("|");
        }
        log.debug("decrypt start , params is: {}", cipherTextsSb.toString());
        List<String> formatCipherText = formatRequest(cipherTexts);
        if (formatCipherText.isEmpty()) {
            //是否设置soaphead的flag
            return cipherTexts;
        }
        StringBuilder formatCipherTextSb = new StringBuilder();
        for (String s : formatCipherText) {
            formatCipherTextSb.append(s).append("|");
        }
        log.debug("list decrypt start , format cipher list is: {}", formatCipherTextSb.toString());
        try {

            Session kmsApi = SignClient.getSession();
            CipherKey key = new PX509CertKey(
                new X509CertLabelFormat(formatCipherKey(orgCode, envelopeDTO.getSoapHeader().getNcrptnSN())));
            Algorithm wkAlgorithm = new Algorithm(Algorithm.SM2_SM3_ENC);
            Algorithm encAlgorithm = new Algorithm(Algorithm.SM4_ECB);
            //对数字信封进行截取
            String dgtEnvelop = StringUtils
                .split(envelopeDTO.getSoapHeader().getDgtlEnvlp(), Constant.PIPELINE_SEPARATOR)[1];
            //对数字信封进行解码
            byte[] envelopeByte = Base64.getDecoder().decode(dgtEnvelop);
            List<byte[]> encDataBytes = new ArrayList<>();
            for (String cipherText : formatCipherText) {
                //base64解码
                byte[] dataDecoderByte = Base64.getDecoder()
                    .decode(cipherText.getBytes(Charset.forName(Constant.CHARTSET)));
                encDataBytes.add(dataDecoderByte);
            }
            RetDecryptEnvelopeParameter parameter = kmsApi
                .decryptWithEnvelope(envelopeDTO.getSoapHeader().getMsgSN(), key, wkAlgorithm, envelopeByte,
                    encAlgorithm, encDataBytes);
            List<byte[]> plainTexts = parameter.getPlaintexts();
            List<String> result = new ArrayList<>();
            for (byte[] plainText : plainTexts) {
                result.add(new String(plainText, Charset.forName(Constant.CHARTSET)));
            }
            StringBuilder resultSb = new StringBuilder();
            for (String s : result) {
                resultSb.append(s).append("|");
            }
            log.debug("list decrypt end , plain list is: {}", resultSb.toString());
            List<String> formatResult = formatResult(result, cipherTexts);
            StringBuilder formatResultSb = new StringBuilder();
            for (String s : formatResult) {
                formatResultSb.append(s).append("|");
            }
            log.debug("list encrypt end , format plain list is: {}", formatResultSb.toString());
            return formatResult;
        } catch (PlatformException e) {
            throw new GwException(GwErrorEnum.DECRYPT_ERROR, e);
        }
    }

    /**
     * 加密之前
     */
    @Override
    public void encryptInit(EnvelopeDTO<?> dto) {
        this.envelopeDTO = dto;
        //
        if (Constant.DCEP_302_001_01.equals(dto.getSoapHeader().getMsgTp())) {
            orgCode = dto.getSoapHeader().getSender();
        } else {
            orgCode = dto.getSoapHeader().getReceiver();
        }
    }

    /**
     * 加密之后
     */
    @Override
    public void encryptFinish(EnvelopeDTO<?> dto) {
        if (!isSetSoapHeader) {
            log.info("orgCode {} , msgTp is {} ,but have no cipher information", dto.getSoapHeader().getSender(),
                dto.getSoapHeader().getMsgTp());
            /**没有执行加密动作,报文体没有敏感信息**/
            //设置数字soapheader数字信封为空
            dto.getSoapHeader().setDgtlEnvlp(null);
            if (!(dto.body() instanceof TransEncryption)) {
                /**613即是加解密也是转解密，证书序号都是用soapheader的ncrptnSN**/
                //若报文不涉及转加密，设置证书序号ncrptnSN为空
                dto.getSoapHeader().setNcrptnSN(null);
            } else {
                SoapHeaderUtils.setNcrptnSn(dto);
            }
            return;
        }
        SoapHeaderUtils.setNcrptnSn(dto);
        envelopeDTO.getSoapHeader().setDgtlEnvlp(envelopeStr);
    }

    /**
     * 解密之前
     */
    @Override
    public void decryptInit(EnvelopeDTO<?> dto) {
        envelopeDTO = dto;
        orgCode = InfoCacheUtils.getPbocInf();
    }

    /**
     * 解密之后
     */
    @Override
    public void decryptFinish(EnvelopeDTO<?> dto) {
        //do nothing
    }

    private void decryptEnvelope() {
        Session kmsApi = SignClient.getSession();
        PX509CertKey unWrapKey = new PX509CertKey(
            new X509CertLabelFormat(formatCipherKey(orgCode, envelopeDTO.getSoapHeader().getNcrptnSN())));
        Algorithm unWrapAlgorithm = new Algorithm(Algorithm.SM2_SM3_ENC);
        Algorithm keyAlgorithm1 = new Algorithm(Algorithm.SM4_KEY);
        //对数字信封进行截取
        String dgtEnvelop = StringUtils
            .split(envelopeDTO.getSoapHeader().getDgtlEnvlp(), Constant.PIPELINE_SEPARATOR)[1];
        //对数字信封进行解码
        byte[] envelopeByte = Base64.getDecoder().decode(dgtEnvelop);
        try {
            RetUnwrapKeyParameter ret3 = kmsApi
                .unwrapKey(envelopeDTO.getSoapHeader().getMsgSN(), unWrapKey, unWrapAlgorithm, envelopeByte,
                    keyAlgorithm1);
            sessionKey = ret3.getwkCtx();
        } catch (PlatformException e) {
            throw new GwException(GwErrorEnum.ENVLP_DECRYPT_ERROR, e);
        }
    }

    private byte[] createKey() {
        //生成会话密钥
        Session kmsApi = SignClient.getSession();
        Algorithm keyAlgorithm = new Algorithm(Algorithm.SM4_KEY);
        try {
            RetGenerateSessionKeyParameter ret = kmsApi
                .generateSessionKey(envelopeDTO.getSoapHeader().getMsgSN(), keyAlgorithm);
            return ret.getWk();
        } catch (PlatformException e) {
            throw new GwException(GwErrorEnum.ENVLP_ENCRYPT_ERROR, e);
        }
    }

    private String createEnvelope() {
        String algorithmType;
        if (Constant.DCEP_302_001_01.equals(envelopeDTO.getSoapHeader().getMsgTp())) {
            algorithmType = InfoCacheUtils.fetchAlgorithmType(envelopeDTO.getSoapHeader().getSender());
        } else {
            algorithmType = InfoCacheUtils.fetchAlgorithmType(envelopeDTO.getSoapHeader().getReceiver());
        }
        //对会话密钥进行加密
        Session kmsApi = SignClient.getSession();
        PX509CertKey pkey = new PX509CertKey(
            new X509CertLabelFormat(formatCipherKey(orgCode, InfoCacheUtils.getInstEncryptCertSeriNo(orgCode))));
        Algorithm wrapAlgorithm = new Algorithm(Algorithm.SM2_SM3_ENC);
        PSecretKey key = new PSecretKey(new KeyEncFormat(sessionKey));
        String dgtl = "";
        try {
            RetWrapKeyParameter retWarpKey = kmsApi
                .wrapKey(envelopeDTO.getSoapHeader().getMsgSN(), pkey, wrapAlgorithm, key);
            dgtl = StringUtils.toEncodedString(Base64.getEncoder().encode(retWarpKey.getKeyEvp()),
                Charset.forName(Constant.CHARTSET));

        } catch (PlatformException e) {
            throw new GwException(GwErrorEnum.ENVLP_ENCRYPT_ERROR, e);
        }
        String dgtlEnvlp = algorithmType + Constant.PIPELINE_SEPARATOR + dgtl;
        return dgtlEnvlp;
    }
}
