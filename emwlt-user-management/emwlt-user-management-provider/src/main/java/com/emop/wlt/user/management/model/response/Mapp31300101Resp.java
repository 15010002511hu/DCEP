package com.emop.wlt.user.management.model.response;

import com.emop.wlt.common.model.flow.FlowBaseResp;
import com.emop.wlt.user.management.model.vo.CustomerIdInfoVO;
import com.emop.wlt.user.management.model.vo.WalletBasicInfoVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 钱包挂失预校验应答报文
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Mapp31300101Resp extends FlowBaseResp {

    /**
     * 钱包基础信息
     */
    private WalletBasicInfoVO walletBasicInfo;

    /**
     * 用户身份信息
     */
    private CustomerIdInfoVO customerIdInfo;

}
