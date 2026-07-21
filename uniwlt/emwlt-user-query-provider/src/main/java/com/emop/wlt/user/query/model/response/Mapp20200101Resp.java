package com.emop.wlt.user.query.model.response;

import com.emop.wlt.user.query.model.vo.HelpClassify;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * 查询帮助分类列表 应答
 */

@Data
@Builder
@ToString
public class Mapp20200101Resp {

    /**
     * 帮助分类列表
     */
    private List<HelpClassify> helpClassifyList;
}
