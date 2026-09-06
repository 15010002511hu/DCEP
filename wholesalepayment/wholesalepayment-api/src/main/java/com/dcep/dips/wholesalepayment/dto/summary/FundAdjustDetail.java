package com.dcep.dips.wholesalepayment.dto.summary;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;
@Data
public class FundAdjustDetail {
    @NotBlank(message = "汇总笔数不能为空")
    private Long countNumber;

    private List<FundAdjustInfDTO> datas;
}
