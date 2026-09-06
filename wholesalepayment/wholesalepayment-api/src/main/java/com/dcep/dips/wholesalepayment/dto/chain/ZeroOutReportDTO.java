package com.dcep.dips.wholesalepayment.dto.chain;

import com.dcep.common.validator.Priority;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.List;

@Data
public class ZeroOutReportDTO implements Serializable {

    private static final long serialVersionUID = -6529098497037844347L;

    /**
     * 交易标识号
     */
    @NotBlank(groups = Priority.Highest.class, message = "交易标识号不能为空")
    @Length(min = 16, max = 32)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "交易标识号禁止中文")
    private String msgId;

    /**
     * 原清零通知交易标识号
     */
    @NotBlank(groups = Priority.Highest.class, message = "原交易标识号不能为空")
    @Length(min = 1, max = 32)
    private String orgnlMsgId;

    /**
     * 清零结果列表
     */
    @NotBlank(groups = Priority.Highest.class, message = "清零结果列表不能为空")
    private List<ZeroOutResult> zeroOutResultlList;
}
