package com.dcep.dips.wholesalepayment.manager;

import com.dcep.clearingcenter.dto.settlement.HvpsReqDTO;
import com.dcep.clearingcenter.dto.settlement.HvpsRspDTO;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.dips.wholesalepayment.dal.model.FundAdjustProdDO;
import com.dcep.dips.wholesalepayment.dal.model.SystemStatusDO;
import com.dcep.dips.wholesalepayment.dto.acctrans.ZeroOutReqDTO;

/**
 * 系统状态表管理接口
 */
public interface SystemStatusManager {
    String selectCurSysDt();

    /**
     * 更新系统状态表
     * @param systemStatusDO
     * @return
     */
    int updateByPrimaryKey(SystemStatusDO systemStatusDO);

    /**
     * 按照系统编码查询系统状态表
     * @param sysCode
     * @return
     */
    SystemStatusDO selectByPrimaryKey(String sysCode);
}
