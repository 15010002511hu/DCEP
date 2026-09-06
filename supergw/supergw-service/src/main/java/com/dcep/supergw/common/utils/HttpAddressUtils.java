package com.dcep.supergw.common.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : maxinyu
 * @version : HttpAddressUtils.java v 0.1 2020-12-30
 * @description :
 */
public class HttpAddressUtils {

    private static Logger log = LoggerFactory.getLogger(HttpAddressUtils.class);

    private static final int IP_LENGTH = 15;

    private static final String UNKNOWN = "unknown";

    public static final String LOCAL_IP = localIpAddr();

    public static String clientIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && ip.length() > IP_LENGTH) {
            String[] ips = ip.split(",");
            for (String strIp : ips) {
                if (!(UNKNOWN.equalsIgnoreCase(strIp))) {
                    ip = strIp;
                    break;
                }
            }
            return ip;
        }

        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }

    public static String localIpAddr() {
        //本地ip，若未配置外网ip则返回
        String localIp = null;
        //外网ip
        String netIp = null;
        //是否找到外网ip
        boolean finded = false;
        Enumeration<NetworkInterface> networkInterfaces;
        try {
            networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements() && !finded) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    if (!inetAddress.isSiteLocalAddress() && !inetAddress.isLoopbackAddress()
                        && inetAddress.getHostAddress().indexOf(":") == -1) {
                        netIp = inetAddress.getHostAddress();
                        finded = true;
                        break;
                    } else if (inetAddress.isSiteLocalAddress() && !inetAddress.isLoopbackAddress()
                        && inetAddress.getHostAddress().indexOf(":") == -1) {
                        localIp = inetAddress.getHostAddress();
                    }
                }
            }
            if (!StringUtils.isEmpty(netIp)) {
                return netIp;
            }
            return localIp;
        } catch (SocketException e) {
            log.error("localIpAddr.failed", e);
        }
        return localIp;
    }
}
