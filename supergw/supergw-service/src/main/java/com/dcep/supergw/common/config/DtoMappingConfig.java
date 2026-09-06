package com.dcep.supergw.common.config;

import com.dcep.common.annotation.Gateway;
import com.dcep.common.exception.DcepException;
import com.dcep.common.utils.JarScanUtils;
import com.dcep.supergw.common.utils.EnviromentUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author : maxinyu
 * @version : DtoMappingConfig.java v 0.1 2019-08-27
 * @description : dto路径映射
 */

@Slf4j
public class DtoMappingConfig {

    private static String[] PACKAGE_NAMES = EnviromentUtils.get("dto.packages", String[].class, null);

    /**
     * 私有化构造方法
     */
    private DtoMappingConfig() {
    }

    /**
     * 存放dto与dto类型的对应关系
     */
    static Map<String, Class<?>> cache;

    static {
        cache = new HashMap<>();
        try {
            getClzFroPkgs(PACKAGE_NAMES).stream()
                .filter(clz -> clz.isAnnotationPresent(Gateway.class))
                .forEach(clz -> cache.put(clz.getDeclaredAnnotation(Gateway.class).msgTp(), clz));
        } catch (DcepException e) {
            log.error("dto扫描初始化异常:{}", e);
        } catch (Throwable throwable) {
            log.error("dto扫描初始化异常:{}", throwable);
        }

    }

    public static List<Class<?>> getClzFroPkgs(String[] pkgs) {
        List<Class<?>> clazzs = new ArrayList<>();
        for (String pkg : pkgs) {
            clazzs.addAll(JarScanUtils.getClzFromPkg(pkg));
        }
        return clazzs;
    }

    public static void loadDTO() {
        for (String key : cache.keySet()) {
            log.info("dto--->>>>>" + key + ":" + cache.get(key).getName());
        }
    }

    /**
     * @param msgTp 报文类型
     * @return 返回对应的class 类
     */
    public static Class<?> getClzByMsgTp(String msgTp) {
        return !StringUtils.isEmpty(msgTp) ? cache.get(msgTp) : null;
    }

    /**
     * @param msgTp 报文类型
     * @return 返回class名称
     */
    public static String getClzNameByMsgTp(String msgTp) {
        Class<?> clz = getClzByMsgTp(msgTp);
        return clz != null ? clz.getName() : "";
    }

}
