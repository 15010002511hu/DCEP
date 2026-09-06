package com.dcep.supergw.common.utils;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.dcep.supergw.common.enums.GwErrorEnum;
import com.dcep.supergw.common.exception.GwException;
import java.util.List;

/**
 * @author maxinyu
 * @date 2024/2/2 15:57
 */
public class FlowUtils {

    /**
     * 申请资源,申请不到资源就抛出限流异常
     */
    public static void flow(String entryName) {
        Entry entry = null;
        try {
            entry = SphU.entry(entryName, EntryType.IN);
        } catch (BlockException e) {
            throw new GwException(GwErrorEnum.LIMITING_ERROR);
        } finally {
            if (entry != null) {
                entry.exit();
            }
        }
    }

    /**
     * 资源名称格式化
     */
    public static String resourceNameFormat(List<String> names) {
        String format = String.join("-", names);
        return format;
    }
}
