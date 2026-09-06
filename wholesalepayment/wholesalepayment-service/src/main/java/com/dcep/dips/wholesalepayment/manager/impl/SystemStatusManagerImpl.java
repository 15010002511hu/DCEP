package com.dcep.dips.wholesalepayment.manager.impl;

import com.alibaba.nacos.common.utils.StringUtils;
import com.dcep.common.exception.DcepException;
import com.dcep.dips.common.constant.CommonConstant;
import com.dcep.dips.wholesalepayment.dal.mapper.SystemStatusDOMapper;
import com.dcep.dips.wholesalepayment.dal.model.SystemStatusDO;
import com.dcep.dips.wholesalepayment.enums.ErrorEnum;
import com.dcep.dips.wholesalepayment.manager.SystemStatusManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
public class SystemStatusManagerImpl implements SystemStatusManager {

    @Autowired
    private SystemStatusDOMapper systemStatusMapper;

    @Override
    public String selectCurSysDt() {
        String curSysDt = systemStatusMapper.selectCurSysDt(CommonConstant.SysCode.WHOLESALE);
        if (StringUtils.isEmpty(curSysDt)) {
            log.error("系统状态表记录不存在，获取系统工作日期失败，sysCd={}", CommonConstant.SysCode.WHOLESALE);
            throw new DcepException(ErrorEnum.BUSI_DATA_NOTEXCEED.getCode(), "获取当前系统工作日期失败");
        }
        return curSysDt;
    }
    /**
     * 更新系统状态表
     * @param systemStatusDO
     * @return
     */
    @Override
    public int updateByPrimaryKey(SystemStatusDO systemStatusDO) {
        return systemStatusMapper.updateByPrimaryKey(systemStatusDO);
    }

    /**
     * 按照系统编码查询系统状态表
     *
     * @param sysCode
     * @return
     */
    @Override
    public SystemStatusDO selectByPrimaryKey(String sysCode) {
        return systemStatusMapper.selectByPrimaryKey(sysCode);
    }
}
