package com.dcep.dips.wholesalepayment.dal.mapper;

import com.dcep.dips.wholesalepayment.dal.model.FundAdjustProdDO;
import com.dcep.dips.wholesalepayment.dal.model.SettlementProdDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FundAdjustProdMapper {
    int insert(FundAdjustProdDO fundAdjustProdDO);

    FundAdjustProdDO selectByPrimaryKey(FundAdjustProdDO fundAdjustProdDO);

    int updateByPrimaryKey(FundAdjustProdDO fundAdjustProdDO);

    /**
     * 结算钱包调整应答后，修改资金调整产品表业务状态
     *
     * @param fundAdjustProdDO 资金调整产品表数据对象
     * @return 影响的行数
     */
    int updateBizSts(@Param("orgnlFundDO") FundAdjustProdDO orgnlFundDO,
                     @Param("fundDO") FundAdjustProdDO fundDO);

    List<FundAdjustProdDO> selectByAdjustPtyId(@Param("ptyId") String ptyId,@Param("bookingDate") String bookingDate,@Param("bizSts") String bizSts);
}
