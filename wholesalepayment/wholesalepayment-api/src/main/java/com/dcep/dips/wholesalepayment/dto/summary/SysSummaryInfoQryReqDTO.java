package com.dcep.dips.wholesalepayment.dto.summary;

import com.dcep.common.validator.Priority;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class SysSummaryInfoQryReqDTO implements Serializable {

    private static final long serialVersionUID = -5027283829400719447L;

    /**
     * 系统标识
     */
    @NotBlank(groups = Priority.Highest.class, message = "系统标识不能为空")
    @Length(min = 1, max = 4)
    private String sysId;

    /**
     * 结算日期
     */
    @NotBlank(groups = Priority.Highest.class, message = "结算日期不能为空")
    @Length(min = 8, max = 8)
    private String bookingDate;

}
