package com.dcep.supergw.common.utils;

import com.dcep.encryptsuit.EncryptSuit;
import com.dcep.encryptsuit.SM4Key;
import com.dcep.kmsapi.exception.KmsException;
import com.dcep.kmsapi.intf.KmsApi;
import com.dcep.supergw.Aplication;
import com.dcep.supergw.common.exception.GwException;
import com.dcep.supergw.sign.client.SignClient;
import java.security.NoSuchAlgorithmException;
import mockit.Mock;
import mockit.MockUp;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author nieyanping
 * @version EncryptionToolUtilsTest.java, v 0.1, 2020/1/10 9:21
 * @description TODO
 */
@SpringBootTest(classes = Aplication.class)
@RunWith(JMockit.class)
public class EncryptionToolUtilsTest {

    @Test
    public void test_getEncryptCertId_succ() {
        EncryptionToolUtils.getEncryptCertId("C0000000000000", "4");
    }

    @Test
    public void test_genSecretKey_succ() throws Exception {
        EncryptionToolUtils.genSecretKey();
    }

    @Test
    public void test_genSecretKey_fail() {
        new MockUp<EncryptSuit>(EncryptSuit.class) {
            @Mock
            public SM4Key genSM4Key() throws NoSuchAlgorithmException {
                throw new NoSuchAlgorithmException();
            }
        };
        try {
            EncryptionToolUtils.genSecretKey();
        } catch (GwException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_genDgtlEnvlp_success() throws Exception {
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

        EncryptionToolUtils.genDgtlEnvlp("66D251A71E45B6E0C0BCE05680357A84", "");
    }

    @Test(expected = GwException.class)
    public void test_validateUtils_genDgtlEnvlp_fail() {
        MockUp<KmsApi> api = new MockUp<KmsApi>(KmsApi.class) {
            @Mock
            public String genDgtlEnvlp(String arg0, String arg1, String arg2) throws KmsException {
                throw new KmsException();
            }
        };

        new MockUp<SignClient>(SignClient.class) {
            @Mock
            public KmsApi getInstance() {
                return api.getMockInstance();
            }
        };

        EncryptionToolUtils.genDgtlEnvlp("66D251A71E45B6E0C0BCE05680357A84", "");
    }

    @Test
    public void test_decryptDgtEnvlp_succ() throws Exception {

        MockUp<KmsApi> api = new MockUp<KmsApi>(KmsApi.class) {
            @Mock
            public String decDgtlEnvlp(String arg0, String arg1, String arg2) throws KmsException {
                return "mock_KmsApi_decDgtlEnvlp";
            }
        };

        new MockUp<SignClient>(SignClient.class) {
            @Mock
            public KmsApi getInstance() {
                return api.getMockInstance();
            }
        };

        EncryptionToolUtils
            .decryptDgtEnvlp("MIGLAiBhSNigLjJNCQniFx7v7OAldv3Pu5lggQmEzNmzNLQ9RAIgEr7LkONScMS9S+PgethCTlQ7YeVjxAO" +
                    "7Ar995wnGzGkEIFvPnXpiJHMCf8imHCmmyWpxkz7bkJKbD+GWysAzIkJaBCNy0AUyPsmp+R3XguDJ36REuVFxyjmXPc1YLDdv+oo+ecjIpg==",
                "C0000000000000_4_e");
    }

    @Test(expected = GwException.class)
    public void test_decryptDgtEnvlp_fail() {
        MockUp<KmsApi> api = new MockUp<KmsApi>(KmsApi.class) {
            @Mock
            public String decDgtlEnvlp(String arg0, String arg1, String arg2) throws KmsException {
                throw new KmsException();
            }
        };

        new MockUp<SignClient>(SignClient.class) {
            @Mock
            public KmsApi getInstance() {
                return api.getMockInstance();
            }
        };

        EncryptionToolUtils.decryptDgtEnvlp("12345", "C0000000000000_4_e");
    }
}