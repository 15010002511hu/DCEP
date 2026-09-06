package com.dcep.dips.wholesalepayment.dto.chain;

import com.dcep.common.validator.Priority;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class ZeroOutFinishReqDTO implements Serializable {

    private static final long serialVersionUID = -5094496236458024586L;

    /**
     * 系统当前日期
     */
    @NotBlank(groups = Priority.Highest.class, message = "系统当前日期不能为空")
    @Length(min = 8, max = 8)
    private String systemDate;

    /**
     * 清零系统标识
     */
    @NotBlank(groups = Priority.Highest.class, message = "清零系统标识不能为空")
    @Length(min = 1, max = 8)
    private String systemId;
}
