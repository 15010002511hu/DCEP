package com.dcep.dips.wholesalepayment.common.utils;

import com.dcep.dips.common.constant.CommonConstant;

import java.util.Random;

public class IdUtils {
    private final static String RESERVE = "00000";
    private final static String CONTROL_VAL = "00";

    /**
     * 随机生成transId
     * transId=8位年月日+4位系统编号+5位保留代码+12位流水号+2位控制位+1位环境标识位
     *
     * @param bizDt 8位业务日期
     * @param sysCd 4位系统编号
     * @param reserve 5位保留代码
     * @param sequence 12位流水号
     * @param controlVal 2位控制位
     * @param envVal 1位环境标识位
     * @return 交易流水号
     */
    public static String randomTransId(String bizDt, String sysCd, String reserve, String controlVal, String envVal) {
        return bizDt + sysCd + reserve + getRandomNum(12) + controlVal + envVal;
    }

    /**
     * 随机生成transId
     *
     * @param bizDt 当前系统日期
     * @return 交易流水号
     */
    public static String randomTransIdWithBizDt(String bizDt) {
        return randomTransId(bizDt, CommonConstant.SysCode.WHOLESALE, RESERVE, CONTROL_VAL, CommonUtil.getEnvVal());
    }

    /**
     * 随机生成长度为length的数字
     *
     * @param length 序号长度
     * @return 随机序号
     */
    public static String getRandomNum(int length) {
        String val = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            val += String.valueOf(random.nextInt(10));
        }
        return val;
    }
}
