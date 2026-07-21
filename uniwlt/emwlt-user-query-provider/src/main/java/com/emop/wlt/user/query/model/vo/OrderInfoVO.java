package com.emop.wlt.user.query.model.vo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class OrderInfoVO {

    /**
     * 订单号
     */
    private String orderNumber;

    /**
     * 订单金额
     */
    private String transactionAmount;

}
