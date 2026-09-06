package com.dcep.supergw.common.utils;

import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.utils.DcepDigestLogUtils;
import com.dcep.infocache.OrgCache;
import com.dcep.supergw.common.constant.Constant;
import com.dubbo.ldc.ZoneClient;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author nieyanping
 * @version HttpRpcLogUtils.java, v 0.1, 2019/12/6 14:23
 * @description 记录调用网关的http请求
 */
public class HttpRpcLogUtils {

    private static Logger logger = LoggerFactory.getLogger("digest");

    private static OrgCache orgCache = OrgCache.getInstance();

    private static String ZONE_NAME = ZoneClient.getInstance().getZoneName();

    private final static String APP_NAME = EnviromentUtils.get("spring.application.name");

    private final static String INVOCATION = "com.dcep.supergw.controller.MainController.doExecute";

    private final static String LOCAL_IP;

    static {
        LOCAL_IP = HttpAddressUtils.LOCAL_IP;
    }

    public static void writeRpcLog(HttpServletRequest request, SoapHeader soapHeader, String resultCode) {
        try {
            long begTime = (long) request.getAttribute("beginTime");

            long elapse = System.currentTimeMillis() - begTime;

            String sender = soapHeader.getSender();
            String receiver = soapHeader.getReceiver();
            String msgSn = soapHeader.getMsgSN();
            String msgType = soapHeader.getMsgTp();

            String clientIp = HttpAddressUtils.clientIpAddr(request);

            //机构的六位编码
            String clientAppName =
                orgCache.getFiInf(sender) != null ? orgCache.getFiInf(sender).getOrgInnerCode() : null;
            String otherInfo = "";
            if (StringUtils.isEmpty(ZONE_NAME)) {
                ZONE_NAME = "";
            }
            if (StringUtils.isEmpty(resultCode) || resultCode.startsWith("PR00")) {
                resultCode = "0";
            } else if (resultCode.length() >= 5) {
                resultCode = resultCode.substring(resultCode.length() - 5);
            }

            String digest = DcepDigestLogUtils
                .createDigestLog(Constant.SYSTEM, ZONE_NAME, APP_NAME, INVOCATION, msgSn, msgType,
                    sender, receiver, clientIp, LOCAL_IP, clientAppName, elapse, resultCode, otherInfo);

            logger.info(digest);
        } catch (Throwable e) {
            logger.error("Write to rpc log error: ", e);
        }

    }
}