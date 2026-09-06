package com.dcep.dips.wholesalepayment.manager;


import com.dcep.dips.common.dto.dc417.Dcep41700101DTO;
import com.dcep.dips.common.dto.dc418.Dcep41800101DTO;

public interface ClearingDtlQueryManager {

	/**
	 * 机构向中心发送的417报文，查询中心交易明细，中心向机构返回418报文
	 *
	 * @param req
	 * @return
	 */
	Dcep41800101DTO queryClearingDtlFromOrg(Dcep41700101DTO req);
}
