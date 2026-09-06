package com.dcep.dips.wholesalepayment.dto.acctrans;

import com.dcep.common.validator.Priority;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
public class ZeroOutRespDTO implements Serializable {
    private static final long serialVersionUID = -7312010782360145819L;

    /**
     * 交易流水号
     */
    @NotBlank(groups = Priority.Highest.class, message = "交易流水号，不能为空")
    @Length(min = 1, max = 35)
    private String transId;

    /**
     * 报文标识号
     */
    @NotBlank(groups = Priority.Highest.class, message = "报文标识号，不能为空")
    @Length(min = 1, max = 32)
    private String msgId;

    /**
     * 端到端标识号
     */
    @NotBlank(groups = Priority.Highest.class, message = "端到端标识号，不能为空")
    @Length(min = 1, max = 35)
    private String endToEndId;

    /**
     * 状态
     */
    @Pattern(regexp = "PR00|PR01", message = "PR00:处理成功，PR01:处理失败")
    @NotBlank(groups = Priority.Highest.class, message = "状态，不能为空")
    @Length(min = 4, max = 4)
    private String prcStatus;
}
