package com.emop.wlt.user.query.model.response;

import com.emop.wlt.user.query.model.vo.QuestionDetail;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * 查询问题详情 应答
 */

@Data
@Builder
@ToString
public class Mapp20300101Resp {

    /**
     * 问题详情列表
     */
    List<QuestionDetail> questionDetailList;
}
