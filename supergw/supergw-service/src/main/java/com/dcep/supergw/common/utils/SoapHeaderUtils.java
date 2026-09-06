package com.dcep.supergw.common.utils;

import com.dcep.common.encryption.DataEncryption;
import com.dcep.common.encryption.TransEncryption;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.infocache.manager.NacosConsume;
import org.springframework.stereotype.Component;

@Component
public class SoapHeaderUtils {

    private static final String DCEP_711_001_01 = "dcep.711.001.01";

    private static final String DCEP_302_001_01 = "dcep.302.001.01";

    public static void resetPbocDto(EnvelopeDTO<?> dto, String secretKey) {
        setPbocSignSn(dto);
        setNcrptnSn(dto);
        setDgtlEnvlp(dto, secretKey);
    }

    /**
     * 设置发送方
     */
    public static void setSender(EnvelopeDTO<?> dto) {
        dto.getSoapHeader().setSender(InfoCacheUtils.getPbocInf());
    }

    /**
     * 设置央行加签证书序号
     */
    public static void setPbocSignSn(EnvelopeDTO<?> dto) {
        dto.getSoapHeader().setSignSN(InfoCacheUtils.getPbocSignCertSeriNo(dto.getSoapHeader().getReceiver()));
    }

    /**
     * 设置加密证书序号，报文含有敏感字段则赋值为接收方公钥证书序列号，否则设为null；711报文采用dto中传的值
     */
    public static void setNcrptnSn(EnvelopeDTO<?> dto) {
        //711报文直接返回
        if (DCEP_711_001_01.equals(dto.getSoapHeader().getMsgTp())) {
            return;
        }
        //加密开关关闭或者dto不用加密，设置为null，返回
        if (!NacosConsume.getIsEncrypt() || (!(dto.body() instanceof DataEncryption) && !(dto
            .body() instanceof TransEncryption))) {
            dto.getSoapHeader().setNcrptnSN(null);
            return;
        }
        //302报文设置为发送方的证书编号
        if (DCEP_302_001_01.equals(dto.getSoapHeader().getMsgTp())) {
            dto.getSoapHeader().setNcrptnSN(InfoCacheUtils.getInstEncryptCertSeriNo(dto.getSoapHeader().getSender()));
            return;
        }
        //设置为接收方的证书编号
        dto.getSoapHeader().setNcrptnSN(InfoCacheUtils.getInstEncryptCertSeriNo(dto.getSoapHeader().getReceiver()));
//        String ncrptnSn = DCEP_711_001_01.equals(dto.getSoapHeader().getMsgTp()) ? dto.getSoapHeader().getNcrptnSN() :
//                (NacosConsume.getIsEncrypt() && dto.body() instanceof DataEncryption) ?
//                        InfoCacheUtils.getInstEncryptCertSeriNo(dto.getSoapHeader().getReceiver()) : null;
//        dto.getSoapHeader().setNcrptnSN(ncrptnSn);
    }

    /**
     * 设置数字信封，报文含有敏感字段则利用接收方加密证书序号、接收方机构号及对称密钥生成数字信封，否则赋值为null；711报文采用dto中传的值
     */
    public static void setDgtlEnvlp(EnvelopeDTO<?> dto, String secretKey) {
        // 内测环境证书采用央行机构号和央行加密证书序号生成，加签开关状态决定是内测环境还是其他环境
        if (DCEP_711_001_01.equals(dto.getSoapHeader().getMsgTp())) {
            return;
        }
        if (!NacosConsume.getIsEncrypt() || !(dto.body() instanceof DataEncryption)) {
            dto.getSoapHeader().setDgtlEnvlp(null);
            return;
        }
        String certId = "";
        //加签开关状态决定是内测环境还是其他环境
        if (!NacosConsume.getIsSign()) {
            certId = EncryptionToolUtils.getEncryptCertId(InfoCacheUtils.getPbocInf(),
                InfoCacheUtils.getInstEncryptCertSeriNo(InfoCacheUtils.getPbocInf()));
        } else if (DCEP_302_001_01.equals(dto.getSoapHeader().getMsgTp())) {
            certId = EncryptionToolUtils
                .getEncryptCertId(dto.getSoapHeader().getSender(), dto.getSoapHeader().getNcrptnSN());
        } else {
            certId = EncryptionToolUtils
                .getEncryptCertId(dto.getSoapHeader().getReceiver(), dto.getSoapHeader().getNcrptnSN());
        }
        dto.getSoapHeader().setDgtlEnvlp(EncryptionToolUtils.genDgtlEnvlp(secretKey, certId));
// 内测环境证书采用央行机构号和央行加密证书序号生成，加签开关状态决定是内测环境还是其他环境
//        String certId = NacosConsume.getIsSign() ? EncryptionToolUtils.getEncryptCertId(dto.getSoapHeader().getReceiver(), dto.getSoapHeader().getNcrptnSN()) :
//                EncryptionToolUtils.getEncryptCertId(InfoCacheUtils.getPbocInf(), InfoCacheUtils.getInstEncryptCertSeriNo(InfoCacheUtils.getPbocInf()));
//        String dgtlEnvlp = DCEP_711_001_01.equals(dto.getSoapHeader().getMsgTp()) ? dto.getSoapHeader().getDgtlEnvlp() :
//                (NacosConsume.getIsEncrypt() && dto.body() instanceof DataEncryption) ?
//                        EncryptionToolUtils.genDgtlEnvlp(secretKey, certId) : null;
//        dto.getSoapHeader().setDgtlEnvlp(dgtlEnvlp);
    }
}
