package com.dcep.dips.wholesalepayment.common.utils;

import com.dcep.common.exception.DcepException;
import com.dcep.common.utils.DcepDateUtils;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.util.Date;

/**
 * 分区ID截取
 * 
 * @author sunxiaofeng
 * @version $Id: PartitionUtil.java, v 0.1 2019年8月27日 下午4:35:29 sunxiaofeng Exp
 *          $
 */
@Slf4j
public class PartitionUtil {

    public static String monthAndDay(Date date) {
        return DcepDateUtils.formateDate(date, "MMdd");
    }

    public static String minute(Date date) {
        return DcepDateUtils.formateDate(date, "mm");
    }

    public static String ctrlStsTableSurface(Date date) {
        return (Integer.parseInt(DcepDateUtils.formateDate(date, "HH")) % 2) == 0 ? "b" : "a";
    }

    // 截取msgId前8位日期值作为分区ID
    public static Date msgCreateTime(String msgId) {
        try {
            String str = msgId.trim();
            return DcepDateUtils.parseDate(str.substring(0, 4) + "-" + str.substring(4, 6) + "-" + str.substring(6, 8),
                    DcepDateUtils.ISO_DATE_PATTERN);
        } catch (ParseException e) {
            log.error("msgCreateTime parse date exception:{}", e);
            throw new DcepException("","");//TODO
        }
    }

    // 截取transId前8位日期值作为分区ID
    public static Date partitionTime(String transId) {
        try {
            String str = transId.trim();
            return DcepDateUtils.parseDate(str.substring(0, 8), DcepDateUtils.DATE_PATTERN);
        } catch (ParseException e) {
            log.error("partitionTime parse date exception:{}", e.getMessage(), e);
            throw new DcepException("","");
        }
    }
}
