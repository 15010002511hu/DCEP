package com.dcep.dips.wholesalepayment.common.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NetWorkUtil {
    private  static   String localHostIp = null;//本机ip不同机器不同，非常量
    /**
     *  获取本机ip地址 ,地址在环境初始化时加载一次 ，方法用作打印日志无风险，若做其他功能，建议加用户或证书认证。
     * 网段为非Loopback且sitelocal类型
     * @return 例如 192.168.30.243
     */
    public static String getHostAddress() {

        return localHostIp;
    }

    @PostConstruct
    public void initlocalHostIp() {
        try {
            localHostIp = InetAddress.getLocalHost().getHostAddress();
            log.info("init local host ip = {} ",localHostIp);
        } catch (UnknownHostException e) {
            log.error("init local host Exceptioin {}",e);
        }
    }

}
