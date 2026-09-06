/**
 * DCEP.com.cn Inc. Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.supergw.common.utils;

import com.dcep.infocache.CertCache;
import com.dcep.infocache.MessagePermissionCache;
import com.dcep.infocache.OrgCache;
import com.dcep.infocache.api.dto.CertDTO;
import com.dcep.infocache.api.dto.OrgDTO;
import com.dcep.supergw.common.constant.Constant;
import com.dcep.supergw.common.enums.GwErrorEnum;
import com.dcep.supergw.common.exception.GwException;
import com.dubbo.ldc.ZoneClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author huyajun
 * @version $Id: InfoCacheUtils.java, v 0.1 2019年7月23日 上午11:15:43 huyajun Exp $
 */
@Slf4j
public class InfoCacheUtils {

    /**
     * 信息缓存： 1、获取央行加签验签证书 2、获取机构加签验签证书
     */

    /**
     * 统一网关 1、获取央行加密解密证书 2、获取机构加密解密证书
     */

    //private static final String LOGINED = "1";

    private static OrgCache orgCache = OrgCache.getInstance();

    private static CertCache certCache = CertCache.getInstance();

    private static MessagePermissionCache permissionCache = MessagePermissionCache.getInstance();

    private static final String ENV = ZoneClient.getInstance().getEnv() == null ?
        "" : ZoneClient.getInstance().getEnv().name();

    /**
     * 根据机构ID 获取机构号 多IDC情况下，根据机构号url逻辑发生了变化 首先根据当前ZONE和机构号获取url，获取不到，走单IDC的逻辑 单idc逻辑被封装到getInsUrl方法中
     */
    public static String getInstUrl(String instId, String msgType) {
        String url = "";
        Boolean isDirectLink = permissionCache.hasDirectLinkOrg(instId, msgType);
        try {
            if (!isDirectLink) {
                url = orgCache.getOrgIdcUrl(instId, ENV);
                if (StringUtils.isBlank(url)) {
                    url = getInsUrl(instId);
                }
            } else {
                url = getInsMsgTpUrl(instId);
            }
        } catch (Exception e) {
            url = getInsUrl(instId);
        }finally {
            log.info("机构号:{},报文编号:{},连接模式:{},EnvFlag:{},获取到的url为:{}", instId, msgType,
                isDirectLink ? "直连" : "间连", ENV, url);
        }
        return url;
    }

    public static String getOpenInstUrl(String instId) {
        return orgCache.getOpenDM(instId);
    }

    /**
     * 机构状态检查
     */
    public static boolean checkInst(String instId) {
        try {
            OrgDTO receiverInstInfo = orgCache.getFiInf(instId);
            if (receiverInstInfo == null) {
                return false;
            }
            //影响分支覆盖率  暂时注释掉  by-duz
            // 金融机构运行状态 1：已登录，2：恢复运行，3：故障，4:维护，5：已退出
            //String state = receiverInstInfo.getOrgState();
            //if (!StringUtils.equals(state, LOGINED)) {
            // 机构状态暂时不校验
            // return false;
            //}
            return true;
        } catch (Exception e) {
            throw new GwException(GwErrorEnum.MANAGER_RPC_ERROR, "信息缓存调用失败", e);
        }
    }

    /**
     * 获取央行到某机构inst的加签证书序号 如果目标机构为央行，缓存会返回空指针报错
     *
     * @return 机构的加签证书序号
     * @author duzhong
     */
    public static String getPbocSignCertSeriNo(String inst) {
        try {
            return certCache.getSignCert(getPbocInf(), inst).getSeriNo();
        } catch (Exception e) {
            log.error("获取机构{}加签证书序号出错", inst, e);
            throw new GwException(GwErrorEnum.REQUEST_PARAM_ILLEGAL, "获取机构加签证书序号出错", e);
        }
    }

    /**
     * 获取某机构inst的加密证书序号
     *
     * @return 机构的加密证书序号
     * @author duzhong
     */
    public static String getInstEncryptCertSeriNo(String inst) {
        try {
            return certCache.getEncryptCert(inst).getSeriNo();
        } catch (Exception e) {
            log.error("获取机构{}加密证书序号出错", inst, e);
            throw new GwException(GwErrorEnum.REQUEST_PARAM_ILLEGAL, "获取机构加密证书序号出错", e);
        }
    }

    /**
     * 获取央行到某机构inst的加签昵称型如：00000000000000_4
     *
     * @author duzhong
     */
    public static String getPbocSignCertDnOrNickname(String inst) {
        try {
            return certCache.getSignCert(getPbocInf(), inst).getDnOrNickname();
        } catch (Exception e) {
            log.error("获取央行到机构{}加签昵称出错", inst, e);
            throw new GwException(GwErrorEnum.REQUEST_PARAM_ILLEGAL, "获取央行加签昵称出错，机构为：" + inst, e);
        }
    }

    /**
     * 获取央行机构号
     */
    public static String getPbocInf() {
        try {
            return orgCache.getPbocInf().getOrgCode();
        } catch (Exception e) {
            log.error("获取央行机构号出错", e);
            throw new GwException(GwErrorEnum.REQUEST_PARAM_ILLEGAL, "获取央行机构号出错", e);
        }
    }

    /**
     *
     */
    private static String getInsUrl(String insNo) {

        OrgDTO receiverInstInfo = orgCache.getFiInf(insNo);
        if (receiverInstInfo == null) {
            throw new GwException(GwErrorEnum.REQUEST_PARAM_ILLEGAL, "机构ID非法" + insNo);
        }
        // 获取机构域名
        return receiverInstInfo.getCompanyDomain();
    }

    private static String getInsMsgTpUrl(String insNo) {

        OrgDTO receiverInstInfo = orgCache.getFiInf(insNo);
        if (receiverInstInfo == null) {
            throw new GwException(GwErrorEnum.REQUEST_PARAM_ILLEGAL, "机构ID非法" + insNo);
        }
        // 获取机构域名
        return receiverInstInfo.getCompanyDomainWallet();
    }

    /**
     * 机构LEI码检查
     */
    public static boolean checkInstLEI(String instId, String instLEI) {
        try {
            String localInstLEI = getInstLEI(instId);
            if (StringUtils.isEmpty(localInstLEI) || StringUtils.isEmpty(instLEI) || instLEI.equals(localInstLEI)) {
                return true;
            }
            return false;
        } catch (GwException e) {
            throw e;
        } catch (Exception e) {
            throw new GwException(GwErrorEnum.REQUEST_PARAM_ILLEGAL, "核验机构LEI码失败", e);
        }
    }

    /**
     * 获取机构LEI码
     */
    public static String getInstLEI(String instId) {
        try {
            return orgCache.getFiInf(instId).getLeiCode();
        } catch (Exception e) {
            throw new GwException(GwErrorEnum.REQUEST_PARAM_ILLEGAL, "获取机构LEI码失败", e);
        }
    }

    public static Boolean isOrgUpdate(String receiver) {
        CertDTO certDTO = certCache.getEncryptCert(receiver);
        if (null == certDTO) {
            //查询不到机构证书信息
            throw new GwException(GwErrorEnum.REQUEST_PARAM_ILLEGAL, "获取机构加密证书序号出错");
        }
        return StringUtils.isNotBlank(certDTO.getAlgorithmType()) && !Constant.ORG_UPDATE_FLAG
            .equals(certDTO.getAlgorithmType());
    }

    public static String fetchAlgorithmType(String receiver) {
        try {
            return certCache.getEncryptCert(receiver).getAlgorithmType();
        } catch (GwException e) {
            throw e;
        } catch (Exception e) {
            throw new GwException(GwErrorEnum.REQUEST_PARAM_ILLEGAL, "获取算法类型错误", e);
        }
    }


    /**
     * 根据机构号判断是否有代理机构
     */
    public static Boolean isHasAgentOrg(String instNo) {
        OrgDTO instDto = orgCache.getFiInf(instNo);
        if (instDto == null) {
            return false;
        }
        String agentInst = instDto.getAgencyInstitutionCode();
        return StringUtils.isNotBlank(agentInst);
    }


    /**
     * 根据机构号判断是否有代理机构
     */
    public static String fetchAgentInst(String instNo) {
        return orgCache.getFiInf(instNo).getAgencyInstitutionCode();
    }
}
