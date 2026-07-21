package com.emop.wlt.user.management.model.request;

import com.emop.wlt.common.model.flow.FlowBaseReq;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 钱包升级预校验 请求报文
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Mapp31900101Req extends FlowBaseReq {

    /**
     * 钱包ID
     */
    @Length(max = 16)
    private String walletId;

    /**
     * 用户需要升级到的目标等级
     */
    @NotBlank
    private String targetLevel;

}
