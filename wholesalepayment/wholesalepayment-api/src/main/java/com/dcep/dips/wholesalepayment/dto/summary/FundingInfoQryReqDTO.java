package com.dcep.dips.wholesalepayment.dto.summary;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class FundingInfoQryReqDTO {
    @NotBlank(message = "机构编码不能为空")
    @Size(max = 14, message = "机构编码长度不能超过14位")
    private String ptyId;

    @NotBlank(message = "结算日期不能为空")
    @Size(max = 8, message = "结算日期长度不能超过35位")
    private String bookingDate;
}
