package com.emop.wlt.user.query.model.vo;

import lombok.Data;
import lombok.ToString;

/**
 * 问题详情
 */

@Data
@ToString
public class QuestionDetail {

    /**
     * 问题编码
     */
    private String questionNo;

    /**
     * 问题
     */
    private String question;

    /**
     * 答案
     */
    private String answer;
}
