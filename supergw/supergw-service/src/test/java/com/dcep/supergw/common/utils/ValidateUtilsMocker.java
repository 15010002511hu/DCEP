package com.dcep.supergw.common.utils;

import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.kmsapi.exception.KmsException;
import com.dcep.kmsapi.intf.KmsApi;
import com.dcep.supergw.sign.client.SignClient;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import mockit.Mock;
import mockit.MockUp;

@Slf4j
public class ValidateUtilsMocker {

    public static void mock() {
        new MockUp<ValidateUtils>(ValidateUtils.class) {
            @Mock
            public void validateMsg(SoapHeader header, String content, String signStr) {
                log.info("execute mock ValidateUtils.validateMsg(SoapHeader header, String content, String signStr)");
            }

            @Mock
            public <T> void validate(T obj) {
                log.info("execute mock ValidateUtils.validate(T obj)");
            }

            @Mock
            public void decrypt(EnvelopeDTO dto) {
                log.info("execute mock ValidateUtils.decrypt(EnvelopeDTO dto)");
            }

            @Mock
            public void encrypt(EnvelopeDTO dto) {
                log.info("execute mock ValidateUtils.encrypt(EnvelopeDTO dto)");
            }

            public String sign(String signSrc, String certId) {
                log.info("execute mock ValidateUtils.sign(String signSrc, String certId)");
                return "unit test sign";
            }
        };

        new MockUp<XmlUtils>(XmlUtils.class) {
            @Mock
            public <T extends GwDTO> void check(EnvelopeDTO<T> envelopeDTO) {
                log.info("execute mock XmlUtils.check(EnvelopeDTO dto)");
            }
        };

        new MockUp<InfoCacheUtils>(InfoCacheUtils.class) {
            @Mock
            public String getPbocInf() {
                return "00000000000000";
            }

            @Mock
            public String getPbocCert() {
                return "4";
            }

            @Mock
            public String getCertDnOrNickname() {
                return "00000000000000_4_s";
            }

            @Mock
            public String getPbocCertDnOrNickname() {
                return "00000000000000_4_s";
            }
        };

        new MockUp<GwMsgUtils>(GwMsgUtils.class) {
            @Mock
            public void writerXmlToInst(HttpServletResponse resp, String xml) {
                log.info("mock");
            }
        };

        MockUp<KmsApi> api = new MockUp<KmsApi>(KmsApi.class) {
            @Mock
            public String genDgtlEnvlp(String arg0, String arg1, String arg2) throws KmsException {
                return "mock_KmsApi_genDgtlEnvlp";
            }
        };

        new MockUp<SignClient>(SignClient.class) {
            @Mock
            public KmsApi getInstance() {
                return api.getMockInstance();
            }
        };
    }

}
