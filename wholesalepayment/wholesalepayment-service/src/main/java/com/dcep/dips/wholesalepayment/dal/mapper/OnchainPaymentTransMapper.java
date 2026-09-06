package com.dcep.dips.wholesalepayment.dal.mapper;

import com.dcep.dips.wholesalepayment.dal.model.OnChainPaymentTransDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OnchainPaymentTransMapper {
    /**
     * 插入链上支付交易记录
     *
     * @param onchainPaymentTransDO DO对象
     * @return 插入成功的记录数
     */
    int insert(OnChainPaymentTransDO onchainPaymentTransDO);

    OnChainPaymentTransDO selectByPrimaryKey(String msgId);
}
