package com.dcep.supergw.manager.secure;

import cn.com.platform.security.adapter.Algorithm;
import cn.com.platform.security.adapter.CipherKey;
import cn.com.platform.security.adapter.Session;
import cn.com.platform.security.adapter.exception.PlatformException;
import cn.com.platform.security.adapter.key.PX509CertKey;
import cn.com.platform.security.adapter.key.format.X509CertLabelFormat;
import cn.com.platform.security.adapter.result.RetTransformEncParameter;
import com.dcep.common.exception.DcepException;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.supergw.common.constant.Constant;
import com.dcep.supergw.common.enums.GwErrorEnum;
import com.dcep.supergw.common.exception.GwException;
import com.dcep.supergw.common.utils.InfoCacheUtils;
import com.dcep.supergw.common.utils.SoapHeaderUtils;
import com.dcep.supergw.sign.client.SignClient;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 * @author maxinyu
 * @date 2023/6/9 19:08
 */
public class HeaderTransferEncryptionHelperImpl extends AbstractEncryptionHelper {

    EnvelopeDTO<?> dto;

    String dgtl = "";

    @Override
    public String encrypt(String str) {
        try {
            String dgtlEnvlp = dto.getSoapHeader().getDgtlEnvlp();
            if (StringUtils.isBlank(dgtlEnvlp)) {
                throw new GwException(GwErrorEnum.ENCRYPT_ERROR.getCode(), "dgtlEnvlp is " + dgtlEnvlp);
            }
            String[] dgtlArray = StringUtils.split(dgtlEnvlp, Constant.PIPELINE_SEPARATOR);
            //发送方接受方都升级完毕
            Session kmsApi = SignClient.getSession();
            CipherKey srcCertKey = new PX509CertKey(new X509CertLabelFormat(
                formatCipherKey(InfoCacheUtils.getPbocInf(), dto.getSoapHeader().getNcrptnSN())));
            Algorithm srcAlgorithm = new Algorithm(Algorithm.SM2_SM3_ENC);
            CipherKey desCertKey = new PX509CertKey(new X509CertLabelFormat(
                formatCipherKey(dto.getSoapHeader().getReceiver(),
                    InfoCacheUtils.getInstEncryptCertSeriNo(dto.getSoapHeader().getReceiver()))));
            Algorithm desAlgorithm = new Algorithm(Algorithm.SM2_SM3_ENC);
            //数字信封解码
            byte[] decodeByte = Base64.getDecoder().decode(dgtlArray[1]);
            RetTransformEncParameter parameter = kmsApi
                .transformEnc(dto.getSoapHeader().getMsgSN(), decodeByte, srcCertKey, srcAlgorithm, desCertKey,
                    desAlgorithm);
            byte[] ctx = parameter.getCtx();
            String dtglResult = InfoCacheUtils.fetchAlgorithmType(
                dto.getSoapHeader().getReceiver())
                + Constant.PIPELINE_SEPARATOR
                + StringUtils
                .toEncodedString(Base64.getEncoder().encode(ctx), Charset.forName(Constant.CHARTSET));
            dgtl = dtglResult;
            return dtglResult;

        } catch (PlatformException e) {
            throw new DcepException("转加密异常", e);
        } catch (Exception e) {
            throw new DcepException("转加密发生未知异常", e);
        }

    }

    @Override
    public String decrypt(String encData) {
        throw new DcepException("此接口方法不可用");
    }

    @Override
    public List<String> encrypt(List<String> plainData) {
        throw new DcepException("此接口方法不可用");
    }

    @Override
    public List<String> decrypt(List<String> encData) {
        throw new DcepException("此接口方法不可用");
    }


    @Override
    public void encryptInit(EnvelopeDTO<?> dto) {
        this.dto = dto;
    }

    @Override
    public void encryptFinish(EnvelopeDTO<?> dto) {
        //设置加解密证书
        SoapHeaderUtils.setNcrptnSn(dto);
        dto.getSoapHeader().setDgtlEnvlp(dgtl);
    }

    @Override
    public void decryptInit(EnvelopeDTO<?> dto) {
        //do nothing
    }

    @Override
    public void decryptFinish(EnvelopeDTO<?> dto) {
        //do nothing
    }


}
