package com.emop.wlt.user.query.model.vo;

import java.util.List;
import lombok.Data;
import lombok.ToString;

/**
 * 帮助分类
 */

@Data
@ToString
public class HelpClassify {

    /**
     * 分类关键字
     */
    private String classifyKeyword;

    /**
     * 分类标题
     */
    private String classifyName;

    /**
     * 分类图片
     */
    private String classifyIcon;

    /**
     * 分类排序
     */
    private String classifySortId;

    /**
     * 问题汇总列表
     */
    private List<QuestionSummary> questionSummaryList;
}
