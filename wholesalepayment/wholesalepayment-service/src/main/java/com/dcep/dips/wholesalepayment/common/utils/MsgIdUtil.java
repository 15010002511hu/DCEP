package com.dcep.dips.wholesalepayment.common.utils;

import com.dcep.dips.wholesalepayment.common.constants.Constant;
import com.dcep.dips.wholesalepayment.dto.ClearingDTO;
import com.dcep.dips.wholesalepayment.dto.mcbs.MbridgeReqDTO;
import com.dcep.dips.wholesalepayment.enums.McbsClrTpEnum;
import com.dcep.dips.wholesalepayment.utils.InfoCacheUtil;
import com.dcep.common.enums.CrossInstEnum;
import com.dcep.common.utils.DcepDateUtils;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Random;

/**
 * MsgId生成工具类
 * 
 * @author cxf
 * 
 */
@Slf4j
public class MsgIdUtil {

    /**
     * 固定发起方向
     */
    private final static String ISSUE = "ISUE";
    private final static String REDEEM = "REDT";
    private final static String CROSS_CHAIN_PAY = "CCPY";
    private final static Random random = new SecureRandom();
    //4位预留位,暂定0000
    private final static String extendInfo = "0000";
    private static final int LONG_LENGTH = 8;
    /**
     * 随机生成msgId msgId生成=8位年月日+3位机构号+1位跨行标识+3位报文编号+14位随机数字+2位控制位+1位环境标识位
     */
    public static String genDcepMsgId(ClearingDTO clearingDTO, String shardingMsgId) {
        String orgId = InfoCacheUtil.getOrgInnerCode(Constant.MCBS);
        String msgTp = clearingDTO.clrMsgTp().substring(5, 8);
        String envVal = CommonUtil.getEnvVal();
        String shardingId = shardingMsgId.substring(29, 31);
        return clearingDTO.clrBatId().substring(1, 9) + orgId + CrossInstEnum.OUT_INST.getCode() + msgTp
                + getRandomNum(14) + shardingId + envVal;
    }


    /**
     * 随机生成mcbsMsgId msgId生成=10位年月日时 +4位预留位+2位业务类型+13位摘要+2位数据分片位+1位环境位
     * 14位时间(年月日时分秒):由2023-08-21T09:10:21 转换为 20230821091021
     */
    public static String genMcbsMsgId(MbridgeReqDTO mbridgeDTO) {
        String envFlag = CommonUtil.getEnvVal();
        // 2位业务类型
        String clrTp = mbridgeDTO.getClrTp() != null ? getClrTp(mbridgeDTO.getClrTp()) : McbsClrTpEnum.OTHER.getCode();
        return mbridgeDTO.getSndDtTm().substring(0, 13).replace("-", "").replace("T", "")
                + extendInfo + clrTp + getAHA256Str(mbridgeDTO)
                + mbridgeDTO.getShardingMsgId().substring(29, 31) + envFlag;
    }

    // 按照货币桥金融基础设施规则生成
    public static String genQueryMcbsMsgId(String shardingId){
        String envFlag = CommonUtil.getEnvVal();
        return TimeUtil.getMcbsCurrentTime(DcepDateUtils.DATETIME_PATTERN).substring(0, 10)
                + extendInfo + McbsClrTpEnum.OTHER.getCode() + getRandomNum(13) + shardingId + envFlag; 
    }
    
    // 生成13位摘要信息
    private static String getAHA256Str(MbridgeReqDTO mbridgeDTO) {
        String encodeStr = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(mbridgeDTO.toString().getBytes(StandardCharsets.UTF_8));
            // from hash to long
            ByteBuffer buffer = ByteBuffer.allocate(LONG_LENGTH);
            buffer.put(hash, 0, LONG_LENGTH);
            buffer.flip();
            buffer.put(0, (byte) (buffer.get(0) & 0x7F));
            encodeStr = Long.toString(buffer.getLong());
        } catch (Exception e) {
            log.error("getAHA256Str process error, Exception{}", e);
        }

        if (null != encodeStr && encodeStr.length() >= 13) {
            return encodeStr.substring(0, 13);
        } else if (null != encodeStr && encodeStr.length() < 13) {
            return getRandomNum(13 - encodeStr.length()) + encodeStr;
        } else {
            return mbridgeDTO.getShardingMsgId().substring(16, 29);
        }
    }
    /**
     * 获取业务类型编码
     *
     * @param clrTp
     * @return
     */
    private static String getClrTp(String clrTp) {
        if (ISSUE.equals(clrTp)) {
            return McbsClrTpEnum.ISSUE.getCode();
        } else if (REDEEM.equals(clrTp)) {
            return McbsClrTpEnum.REDEEM.getCode();
        } else if (CROSS_CHAIN_PAY.equals(clrTp)) {
            return McbsClrTpEnum.CROSS_CHAIN_PAY.getCode();
        } else {
            return McbsClrTpEnum.OTHER.getCode();
        }
    }

    /**
     * 随机生成长度为length的数字
     * 
     * @param length
     * @return
     */
    public static String getRandomNum(int length) {
        String val = "";
        for (int i = 0; i < length; i++) {
            val += String.valueOf(random.nextInt(10));
        }
        return val;
    }

}
