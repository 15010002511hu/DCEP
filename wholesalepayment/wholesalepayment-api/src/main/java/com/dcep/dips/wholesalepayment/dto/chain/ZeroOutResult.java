package com.dcep.dips.wholesalepayment.dto.chain;

import com.dcep.common.validator.Priority;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ZeroOutResult implements Serializable {

    private static final long serialVersionUID = 2779286386915023711L;

    /**
     * 清零机构编码
     */
    @NotBlank(groups = Priority.Highest.class, message = "清零机构编码不能为空")
    @Length(min = 1, max = 14)
    private String ptyId;
    /**
     * 系统标识
     */
    @NotBlank(groups = Priority.Highest.class, message = "系统标识不能为空")
    @Length(min = 1, max = 8)
    private String channelSys;
    /**
     * 当前系统标识 A:A账户 B:B账户
     */
    @Length(min = 1, max = 1)
    private String currentSystemFlag;
    /**
     * 完成清零金额
     */
    @NotBlank(groups = Priority.Highest.class, message = "完成清零金额不能为空")
    private BigDecimal finishZeroOutAmt;
    /**
     * 未完成清零金额
     */
    @NotBlank(groups = Priority.Highest.class, message = "未完成清零金额不能为空")
    private BigDecimal unFinishZeroOutAmt;
}
