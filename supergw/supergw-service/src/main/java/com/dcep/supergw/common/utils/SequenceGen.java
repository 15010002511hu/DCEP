package com.dcep.supergw.common.utils;

import com.dcep.common.utils.DcepDateUtils;
import java.text.DecimalFormat;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;

/**
 * @author nieyanping
 * @version SequenceGen.java, v 0.1, 2019/10/26 18:06
 * @description 生成自增序列
 */
public class SequenceGen {

    /**
     * 自增序号
     */
    private static int count = -1;

    /**
     * 上一次生成自增序列的时间
     */
    private static String lastDateStr;

    /**
     * 数字格式化
     */
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("00000000");

    static {
        if (StringUtils.isEmpty(lastDateStr)) {
            Date yesterday = new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24);

            lastDateStr = DcepDateUtils.formateDate(yesterday, "yyyyMMddHHmmssS");
        }
    }

    public static synchronized String genSequenceId() {
        String currentDateStr = DcepDateUtils.formateDate((new Date()), "yyyyMMddHHmmssS");

        if (currentDateStr.compareTo(lastDateStr) > 0) {
            count = -1;
        }

        String suffix = DECIMAL_FORMAT.format(++count);
        lastDateStr = currentDateStr;
        String uniqueId = currentDateStr + suffix;
        return uniqueId;
    }
}