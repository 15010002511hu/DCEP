package com.dcep.dips.wholesalepayment.dto.common;

/**
 *
 * 常量类
 * @author chenkai
 * @version $Id: Constant.java, v 0.1 2019年8月26日 下午7:06:52 sunxiaofeng Exp $
 */
public class Common {

    public static final int    TIME_OUT = -1;
    
    public static final String MIN_ORDER_EXPIRY_TIME = "5";
    
    public static final String MAX_ORDER_EXPIRY_TIME = "300";
    
    /**业务类型：红包发放**/
    public static final String PURP_RED_PACKET = "C225";
    
    /**业务类型：货币桥-贷记**/
    public static final String MBRIDGE_PURP_CDTR = "C226";
    
    /**业务类型：货币桥-借记**/
    public static final String MBRIDGE_PURP_DBTR = "D226";
    
    /**业务类型：退款兑回**/
    public static final String PURP_CONREF_RECON = "C203";
    
    /**业务类型：兑回**/
    public static final String PURP_RECON = "C201";
    
    /**业务类型：消费-借记**/
    public static final String PURP_CON_DBTR = "D203";

    /**新业务类型：红包发放**/
    public static final String PURP_RED_PACKET_NEW = "222";

    /**新业务类型：兑回**/
    public static final String PURP_RECON_NEW = "201";

    /**新业务类型：消费**/
    public static final String PURP_CONSUMER_NEW = "100";

    /**业务类型：货币桥**/
    public static final String MBRIDGE_PURP_NEW = "223";
    
    /**业务类型：阿联酋货币桥**/
    public static final String GCSC_MBRIDGE_PURP = "720";

    public static final String PRESS_END_T = "T";

    /**新业务种类：兑回**/
    public static final String PURP_RECON_TP1 = "20100001";
    public static final String PURP_RECON_TP2 = "20100002";
    public static final String PURP_RECON_TP3 = "20100003";
    public static final String PURP_RECON_TP4 = "20100004";

    /**新业务种类：退款兑回**/
    public static final String PURP_RED_RECON_TP5 = "20100005";
    public static final String PURP_RED_RECON_TP6 = "20100006";
    public static final String PURP_RED_RECON_TP7 = "20100007";
    public static final String PURP_RED_RECON_TP8 = "20100008";

}
