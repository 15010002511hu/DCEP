package com.dcep.dips.wholesalepayment.dto.chain;


import com.dcep.common.validator.Priority;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Setter
@Getter
@ToString
public class OnChainResultDTO implements Serializable {
    /**
     * 交易标识号
     */
    @NotBlank(groups = Priority.Highest.class, message = "交易标识号不能为空")
    @Length(min = 16, max = 32)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "交易标识号禁止中文")
    private String id;
    /**
     * 交易状态（0-成功 ）
     */
    private Integer status;
    /**
     * 业务类型
     */
    private Integer type;
    /**
     * 转出机构id
     */
    @NotBlank(groups = Priority.Highest.class, message = "转出机构ID不能为空")
    @Length(min = 1, max = 14)
    private String fromOperator;
    /**
     * 转入机构id
     */
    @NotBlank(groups = Priority.Highest.class, message = "转入机构ID不能为空")
    @Length(min = 1, max = 14)
    private String toOperator;
    /**
     * 备注
     */
    @Length(max = 300,message = "备注信息超过长度限制")
    private String desc;
    /**
     * 请求业务信息
     */
    @NotBlank(groups = Priority.Highest.class, message = "请求业务信息不能为空")
    @Length(max = 65535,message = "请求业务信息超过长度限制")
    private String encData;
    /**
     * 使用系统标志
     */
    @Length(max = 1,message = "使用系统标志超过长度限制")
    @Pattern(regexp = "A||B")
    private String useCurrentSystemFlag;



}
