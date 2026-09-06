/*
 * pbcdci.cn Inc.
 * Copyright © 2023 PBCDCI All Rights Reserved.
 */
package com.dcep.supergw.service.impl;

import com.crypto.CryptoAdapter;
import com.dcep.common.annotation.Gateway;
import com.dcep.common.enums.ProcessStatusEnum;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.Response;
import com.dcep.common.model.soap.GrpHdr;
import com.dcep.common.model.soap.OrgnlGrpHdr;
import com.dcep.common.model.soap.SoapBody;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.utils.DcepDateUtils;
import com.dcep.common.utils.MsgIdUtil;
import com.dcep.supergw.common.config.NacosConfigClient;
import com.dcep.supergw.common.utils.IndustryKeyUtils;
import com.dcep.supergw.dto.dc961.Dcep96100101DTO;
import com.dcep.supergw.dto.dc962.Dcep96200101DTO;
import com.dcep.supergw.dto.dc962.RspsnInf;
import com.dcep.supergw.service.IndustryKeyService;
import com.dcep.supergw.sign.client.SignClient;
import javax.xml.bind.DatatypeConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 行业密钥生成服务, 生成密钥用于行业终端与硬钱包之间双向身份认证
 *
 * @author laimincai
 * @date 2023/12/27
 */
@Slf4j
@Service("com.dcep.supergw.service.IndustryKeyService")
public class IndustryKeyServiceImpl implements IndustryKeyService {

    @Override
    public Response<EnvelopeDTO<GwDTO>> execute(EnvelopeDTO<GwDTO> request) {
        Dcep96100101DTO dcep961 = (Dcep96100101DTO) request.getSoapBody().getT();

        // 行业标识
        String industryId = dcep961.getIndstryInf().getIndstryId();
        // 行业标识扩展字段，默认0100，保持跟标准中的轨交类型编码一致
        String industryIdExt = StringUtils.defaultIfEmpty(dcep961.getIndstryInf().getIndstryXtndFld(), "0100");
        // 获取机构分散因子
        String instnFctrId = dcep961.getIndstryInf().getInstnFctrId();
        // 获取数字信封加密证书
        String ncrptnCertd = dcep961.getCertInf().getNcrptnCert();

        RspsnInf rspsnInf = new RspsnInf();
        try {
            //调用密码服务平台接口生成行业密钥
            byte[] evp = IndustryKeyUtils.create(request.getSoapHeader().getMsgSN(),
                (CryptoAdapter) SignClient.getSession(),
                NacosConfigClient.getInstance().getIndustryKeyLabel(),
                industryId, industryIdExt, instnFctrId, DatatypeConverter.parseBase64Binary(ncrptnCertd));
            rspsnInf.setRspsnSts(ProcessStatusEnum.SUCCESS.getCode());
            rspsnInf.setDgtlEnvlp(DatatypeConverter.printBase64Binary(evp));
        } catch (Exception e) {
            log.error("行业密钥生成失败: ", e);
            rspsnInf.setRspsnSts(ProcessStatusEnum.FAILED.getCode());
            rspsnInf.setRjctCd("R999"); //错误码写死R999
            rspsnInf.setRjctInf("行业密钥生成失败");
        }

        GrpHdr grpHdr = new GrpHdr(
            MsgIdUtil.genMsgIdByOriMsgId(dcep961.getGrpHdr().getMsgId(),
                Dcep96200101DTO.class.getAnnotation(Gateway.class).msgTp().substring(5, 8)),
            DcepDateUtils.getDcepDateStrNow(), request.getSoapHeader().getReceiver(),
            request.getSoapHeader().getSender(), null);

        OrgnlGrpHdr orgnlGrpHdr = new OrgnlGrpHdr(dcep961.getGrpHdr().getMsgId(),
            request.getSoapHeader().getSender(),
            request.getSoapHeader().getMsgTp());

        SoapHeader respHeader = new SoapHeader(request.getSoapHeader().getVer(),
            DcepDateUtils.getDcepDateStrNow(),
            Dcep96200101DTO.class.getAnnotation(Gateway.class).msgTp(),
            request.getSoapHeader().getMsgSN(),
            request.getSoapHeader().getReceiver(),
            request.getSoapHeader().getSender());

        EnvelopeDTO<GwDTO> response = new EnvelopeDTO<GwDTO>(respHeader, new SoapBody<>(
            new Dcep96200101DTO(grpHdr, orgnlGrpHdr, rspsnInf)));

        return new Response<>(response);
    }
}
