package com.emop.wlt.user.query.model.vo;

import lombok.Data;
import lombok.ToString;

/**
 * 问题汇总
 */

@Data
@ToString
public class QuestionSummary {

    /**
     * 帮助名称
     */
    private String questionName;

    /**
     * 问题编码
     */
    private String questionNo;

}
