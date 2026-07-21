package com.emop.wlt.user.management.model.response;

import com.emop.wlt.common.model.flow.FlowBaseResp;
import com.emop.wlt.user.management.model.vo.WalletBasicInfoVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 钱包挂失应答报文
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Mapp31400101Resp extends FlowBaseResp {

    /**
     * 钱包基础信息
     */
    private WalletBasicInfoVO walletBasicInfo;

}
