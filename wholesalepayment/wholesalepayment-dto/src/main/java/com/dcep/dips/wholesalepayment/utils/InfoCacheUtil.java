/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.utils;

import com.dcep.common.enums.ErrorEnum;
import com.dcep.common.exception.DcepException;
import com.dcep.dips.common.constant.CommonConstant;
import com.dcep.infocache.OrgCache;
import com.dcep.infocache.OwnershipCache;
import com.dcep.infocache.api.dto.OrgDTO;
import com.dcep.infocache.api.dto.OwnershipParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 信息缓存工具类
 * 
 * @author chenkai
 * @version $Id: InfoCacheUtil.java, v 0.1 2019年10月16日 上午11:15:43 chenkai Exp $
 */
@Slf4j
public class InfoCacheUtil {

	private static OrgCache orgCache = OrgCache.getInstance();

	private static OwnershipCache ownershipCache = OwnershipCache.getInstance();

	/**
	 * 获取机构在大额中直参行号(清算行号)
	 *
	 * @param instNo 运营中心的直参机构号
	 * @return 大额中直参行号(清算行号)
	 */
	public static String getHvpsClrBkNo(String instNo) {
		OrgDTO orgDTO = orgCache.getFiInf(instNo);
		if (null == orgDTO) {
			throw new DcepException(ErrorEnum.UNKNOWN_EXCEPTION.getCode(), "机构信息不存在，机构号为：" + instNo);
		}

		if (StringUtils.isBlank(orgDTO.getAgentClearingBankNumber())) {
			throw new DcepException(ErrorEnum.UNKNOWN_EXCEPTION.getCode(), "机构的大额直参行号为空，机构号为：" + instNo);
		}
		return orgDTO.getAgentClearingBankNumber();
	}

	/**
	 * 获取机构在大额中运营中心下面的间参行号
	 *
	 * @param instNo 运营中心直参机构号
	 * @return 大额间参行号
	 */
	public static String getHvpsBkNo(String instNo) {
		OrgDTO orgDTO = orgCache.getFiInf(instNo);
		if (null == orgDTO) {
			throw new DcepException(ErrorEnum.UNKNOWN_EXCEPTION.getCode(), "机构信息不存在，机构号为：" + instNo);
		}

		if (StringUtils.isBlank(orgDTO.getIndirectBankNumber())) {
			throw new DcepException(ErrorEnum.UNKNOWN_EXCEPTION.getCode(), "机构在大额中运营中心下面的间参行号为空，机构号为：" + instNo);
		}
		return orgDTO.getIndirectBankNumber();
	}

	/**
	 * 大额来账报文，通过大额的直参行号（清算行号），获取运营中心机构号
	 *
	 * @param hvpsClrBkNo 大额的直参行号
	 * @return 运营中心机构号
	 */
	public static String getInstNoForHvpsClrBkNo(String hvpsClrBkNo) {
		List<OrgDTO> orgDTOs = orgCache.listOrgInfs();
		if (CollectionUtils.isEmpty(orgDTOs)) {
			throw new DcepException(ErrorEnum.UNKNOWN_EXCEPTION.getCode(), "运营中心机构信息表为空");
		}

		for (OrgDTO orgDTO : orgDTOs) {
			if (hvpsClrBkNo.equals(orgDTO.getAgentClearingBankNumber())) {
				return orgDTO.getOrgCode();
			}
		}

		throw new DcepException(ErrorEnum.UNKNOWN_EXCEPTION.getCode(),
				"通过大额直参行号反查运营中心下的机构号，查无记录，大额直参行号为：" + hvpsClrBkNo);
	}

	/**
	 * 大额来账报文，通过大额的间参行号，获取运营中心机构号
	 *
	 * @param hvpsBkNo 大额的间参行号
	 * @return 运营中心机构号
	 */
	public static String getInstNoForHvpsBkNo(String hvpsBkNo) {
		List<OrgDTO> orgDTOs = orgCache.listOrgInfs();
		if (CollectionUtils.isEmpty(orgDTOs)) {
			throw new DcepException(ErrorEnum.UNKNOWN_EXCEPTION.getCode(), "运营中心机构信息表为空");
		}

		for (OrgDTO orgDTO : orgDTOs) {
			if (hvpsBkNo.equals(orgDTO.getIndirectBankNumber())) {
				return orgDTO.getOrgCode();
			}
		}

		throw new DcepException(ErrorEnum.UNKNOWN_EXCEPTION.getCode(),
				"通过大额间参行号反查运营中心机构号，查无记录，大额间参行号为：" + hvpsBkNo);
	}

	/**
	 * 获取运营中心在大额的直参行号
	 *
	 * @return 运营中心在大额的直参行号
	 */
	public static String getPbocHvpsClrBkNo() {
		try {
			String bankNumber = orgCache.getPbocInf().getBankNumber();
			if (StringUtils.isEmpty(bankNumber)) {
				throw new DcepException(ErrorEnum.UNKNOWN_EXCEPTION.getCode(), "运营中心机构信息中未配置大额直参行号");
			}
			return bankNumber;
		} catch (Exception e) {
			log.error("获取运营中心在大额的直参行号出错", e);
			throw new DcepException(ErrorEnum.UNKNOWN_EXCEPTION.getCode(), "获取运营中心在大额的直参行号出错", e);
		}
	}

	/**
	 * 获取运营中心DCEP渠道钱包ID
	 *
	 * @return 运营中心的钱包ID
	 */
	public static String getPbocDcepWlltId() {
		try {
			OwnershipParam ownershipParam = ownershipCache.getParamByKey(CommonConstant.WLLT_ID_ASSETS_ACS);
			if (ownershipParam == null || StringUtils.isEmpty(ownershipParam.getParmVal())) {
				throw new DcepException(ErrorEnum.UNKNOWN_EXCEPTION.getCode(), "获取运营中心DCEP渠道系统钱包ID失败");
			}
			return ownershipParam.getParmVal();
		} catch (Exception e) {
			log.error("获取运营中心DCEP渠道系统钱包ID失败", e);
			throw new DcepException(ErrorEnum.UNKNOWN_EXCEPTION.getCode(), "获取运营中心DCEP渠道系统钱包ID失败", e);
		}
	}

	/**
	 * 获取机构的托管机构，如果不存在托管机构，则返回null
	 * 
	 * @return 托管机构号
	 */
	public static String getCustodianInstNo(String instNo) {
		OrgDTO orgDTO = orgCache.getFiInf(instNo);
		if (null == orgDTO) {
			throw new DcepException(ErrorEnum.UNKNOWN_EXCEPTION.getCode(), "获取机构归属的托管机构失败，机构信息不存在，机构号：" + instNo);
		}

		if (StringUtils.isEmpty(orgDTO.getCustodianBankCode())) {
			return instNo;
		}

		return orgDTO.getCustodianBankCode();
	}

	/**
	 * 获取公管的公共参数
	 *
	 * @return 参数对象
	 */
	public static OwnershipParam getPublicParam(String key) {
		try {
			OwnershipParam ownershipParam = ownershipCache.getParamByKey(key);
			if (ownershipParam == null) {
				throw new DcepException(ErrorEnum.UNKNOWN_EXCEPTION.getCode(), "获取公共参数为空，key=" + key);
			}
			return ownershipParam;
		} catch (Exception e) {
			log.error("获取公共参数异常，key={}", key, e);
			throw new DcepException(ErrorEnum.UNKNOWN_EXCEPTION.getCode(), "获取公共参数异常，key=" + key, e);
		}
	}

	/**
	 * 获取央行机构号
	 *
	 * @return 机构号
	 */
	public static String getPbocInf() {
		try {
			return orgCache.getPbocInf().getOrgCode();
		} catch (Exception e) {
			log.error("获取央行机构号出错", e);
			throw new DcepException(ErrorEnum.UNKNOWN_EXCEPTION.getCode(), "获取央行机构号出错", e);
		}
	}

	/**
	 * 获取央行内部机构号
	 *
	 * @return 央行机构号
	 */
	public static String getPbocInnerCode() {
		try {
			return orgCache.getPbocInf().getOrgInnerCode().substring(0, 3);
		} catch (Exception e) {
			log.error("获取央行内部机构号出错", e);
			throw new DcepException(ErrorEnum.UNKNOWN_EXCEPTION.getCode(), "获取央行内部机构号出错", e);
		}
	}

	/**
	 * 检查机构状态 ST00：设置故障 ST01：恢复运行 ST02：已登录 ST03：已退出 ST04: 设置维护
	 * 
	 * @param instNo 参与机构号
	 * @return 机构状态
	 */
	public static boolean checkInstState(String instNo) {
		String orgState = orgCache.getFiInf(instNo).getOrgState();
		log.debug("checkInstState, instNo:{}, orgState:{}", instNo, orgState);
		return orgState.matches("ST01||ST02||LOT00||LOT04");
	}

	/**
	 * 检查系统状态
	 *
	 * @return 是否正常
	 */
	public static boolean checkSysState() {
		return true;
	}

	/**
	 * 获取机构内部编码
	 *
	 * @param instNo 参与机构号
	 * @return 机构内部编码
	 */
	public static String getOrgInnerCode(String instNo) {
		try {
			return orgCache.getFiInf(instNo).getOrgInnerCode().substring(0, 3);
		} catch (Exception e) {
			log.error("获取机构内部编码", e);
			throw new DcepException(ErrorEnum.UNKNOWN_EXCEPTION.getCode(), "获取机构内部编码出错，机构号为" + instNo);
		}
	}

	/**
	 * 获取机构LEI编码
	 * @param instNo 参与机构号
	 * @return 机构LEI编码
	 */
	public static String getLeiCode(String instNo) {
		try {
			OrgDTO orgDTO = orgCache.getFiInf(instNo);
			if (null == orgDTO || (StringUtils.isBlank(orgDTO.getLeiCode()))) {
				return null;
			}
			return orgDTO.getLeiCode();
		} catch (Exception e) {
			log.error("获取机构LEI码出错", e);
			throw new DcepException(ErrorEnum.UNKNOWN_EXCEPTION.getCode(), "获取机构LEI码出错，机构号为" + instNo);
		}
	}

	/**
	 * -此方法用于检查某个机构是否是合作银行。
	 *
	 * 3：直连合作银行 4：间连合作银行
	 */
	public static boolean checkInstType(String instNo) {
		String orgType = orgCache.getFiInf(instNo).getOrgType();
		log.debug("checkInstType, instNo:{}, orgType:{}", instNo, orgType);
		return orgType.matches("3|4");
	}
}
