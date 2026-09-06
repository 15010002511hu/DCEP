package com.dcep.dips.wholesalepayment.dto.chain;


import com.dcep.common.validator.Priority;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.math.BigDecimal;

@Setter
@Getter
@ToString
public class OnChainAdjustRespDTO implements Serializable {

    /**
     * 结算日期
     */
    @NotNull(groups = Priority.Highest.class, message = "结算日期不能为空")
    @Length(min = 8, max = 8,message = "结算日期长度超过限制")
    private String settlementDate;
    /**
     * 业务状态
     */
    @NotNull(groups = Priority.Highest.class, message = "业务状态不能为空")
    @Length(min = 1, max = 4,message = "业务状态长度超过限制")
    private String bizStatus;
    /**
     * 业务处理码
     */
    @Length(min = 1, max = 16,message = "业务处理码长度超过限制")
    private String bizProcessCode;
    /**
     * 业务处理信息
     */
    @Length(min = 1, max = 315,message = "业务处理信息长度超过限制")
    private String bizProcessInfo;

}
