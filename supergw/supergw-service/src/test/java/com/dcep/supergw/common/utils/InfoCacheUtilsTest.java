package com.dcep.supergw.common.utils;

import com.dcep.infocache.CertCache;
import com.dcep.infocache.OrgCache;
import com.dcep.supergw.Aplication;
import com.dcep.supergw.common.exception.GwException;
import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author : maxinyu
 * @version : InfoCacheUtilsTest.java v 0.1 2019-11-12
 * @description :
 */
@SpringBootTest(classes = Aplication.class)
@RunWith(JMockit.class)
public class InfoCacheUtilsTest {

    @Mocked
    OrgCache orgCache;

    @Mocked
    CertCache certCache;

    /**
     * 错误的机构号
     */
    @Test(expected = GwException.class)
    public void TestgetInstUrl1() {
        new Expectations() {
            {
                orgCache.getFiInf("C1010411000000");
                result = null;
            }
        };
        String instid = "C1010411000000";
        System.out.println(InfoCacheUtils.getInstUrl(instid,""));
    }

    /**
     * 正确的机构号
     */
    @Test
    public void TestgetInstUrl2() {
        String instid = "C1010411000013";
        System.out.println(InfoCacheUtils.getInstUrl(instid,""));
    }

    /**
     * 错误的机构号
     */
    @Test
    public void testCheckInst1() {
        String instid = "C1010411000000";
        System.out.println(InfoCacheUtils.checkInst(instid));
    }

    /**
     * 正确的机构号
     */
    @Test
    public void testCheckInst2() {
        String instid = "C1010411000013";
        System.out.println(InfoCacheUtils.checkInst(instid));
    }

    /**
     * 机构状态检查 state 不为 1
     */
    @Test
    public void testCheckInst3() {
        new Expectations() {
            {
                orgCache.getFiInf("C1010411000013").getOrgState();
                result = 2;
            }
        };
        String instid = "C1010411000013";
        System.out.println(InfoCacheUtils.checkInst(instid));
    }

    /**
     * 获取机构中心 错误的机构号
     */
    @Test(expected = GwException.class)
    public void testGetInstCert1() {
        new Expectations() {
            {
                certCache.getEncryptCert("C1010411000000");
                result = null;
            }
        };
        String instid = "C1010411000000";
        System.out.println(InfoCacheUtils.getInstEncryptCertSeriNo(instid));
    }

    /**
     * 获取机构中心 正确的机构号
     */
    @Test
    public void testGetInstCert2() {
        String instid = "C1010411000013";
        System.out.println(InfoCacheUtils.getInstEncryptCertSeriNo(instid));
    }


    /**
     * 获取NickName 错误的机构号
     */
    @Test(expected = GwException.class)
    public void testGetCertDnOrNickname1() {
        new Expectations() {
            {
                certCache.getEncryptCert("C1010411000000");
                result = null;
            }
        };
        String instid = "C1010411000000";
        System.out.println(InfoCacheUtils.getPbocSignCertDnOrNickname(instid));
    }

    /**
     * 获取NickName 正确的机构号
     */
    @Test
    public void testGetCertDnOrNickname2() {

        new Expectations() {
            {
                certCache.getSignCert(anyString, anyString).getDnOrNickname();
                result = "00000000000000_4";
            }
        };
        System.out.println(InfoCacheUtils.getPbocSignCertDnOrNickname("abc"));
    }

    /**
     * 获取央行机构号
     */
    @Test
    public void testGetPbocInf() {
        System.out.println(InfoCacheUtils.getPbocInf());
    }

    /**
     * 测试获取央行证书序号
     */
    @Test
    public void testGetPbocCertDnOrNickname() {
        new Expectations() {
            {
                orgCache.getPbocInf().getOrgCode();
                result = "Pbo";
                certCache.getSignCert("Pbo", anyString).getDnOrNickname();
                result = "abc";
            }
        };
        System.out.println(InfoCacheUtils.getPbocSignCertDnOrNickname("abc"));
    }

}
