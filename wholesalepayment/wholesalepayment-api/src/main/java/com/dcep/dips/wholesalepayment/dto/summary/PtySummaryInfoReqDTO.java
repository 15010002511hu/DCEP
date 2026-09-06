package com.dcep.dips.wholesalepayment.dto.summary;

import com.dcep.common.validator.Priority;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class PtySummaryInfoReqDTO implements Serializable {

    private static final long serialVersionUID = -8111926465142783348L;

    /**
     * 14位机构编码
     */
    @NotBlank(groups = Priority.Highest.class, message = "机构编码不能为空")
    @Length(min = 1, max = 14)
    private String ptyId;

    /**
     * 结算日期
     */
    @NotBlank(groups = Priority.Highest.class, message = "结算日期不能为空")
    @Length(min = 8, max = 8)
    private String bookingDate;
}
