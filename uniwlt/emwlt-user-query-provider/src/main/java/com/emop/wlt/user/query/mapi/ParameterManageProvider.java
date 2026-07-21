package com.emop.wlt.user.query.mapi;

import com.alipay.gateway.adapterservice.annotation.OperationType;
import com.dubbo.ldc.ZoneRouter;
import com.emop.wlt.common.model.mapp.RequestModel;
import com.emop.wlt.common.model.mapp.ResponseModel;
import com.emop.wlt.user.query.model.request.Mapp09000101Req;
import com.emop.wlt.user.query.model.request.Mapp09100101Req;
import com.emop.wlt.user.query.model.request.Mapp09200101Req;
import com.emop.wlt.user.query.model.request.Mapp09300101Req;
import com.emop.wlt.user.query.model.request.Mapp09400101Req;
import com.emop.wlt.user.query.model.request.Mapp09600101Req;
import com.emop.wlt.user.query.model.request.Mapp20200101Req;
import com.emop.wlt.user.query.model.request.Mapp20300101Req;
import com.emop.wlt.user.query.model.response.Mapp09000101Resp;
import com.emop.wlt.user.query.model.response.Mapp09100101Resp;
import com.emop.wlt.user.query.model.response.Mapp09200101Resp;
import com.emop.wlt.user.query.model.response.Mapp09300101Resp;
import com.emop.wlt.user.query.model.response.Mapp09400101Resp;
import com.emop.wlt.user.query.model.response.Mapp20200101Resp;
import com.emop.wlt.user.query.model.response.Mapp20300101Resp;

@ZoneRouter(ZoneRouter.Type.CZ)
public interface ParameterManageProvider {

    /**
     * mapp.202.001.01 查询帮助分类列表
     * @param request
     * @return
     */
    @OperationType(value = "mapp.202.001.01", name = "查询帮助分类列表", desc = "mapp.202.001.01")
    ResponseModel<Mapp20200101Resp> queryHelpClassifyList(RequestModel<Mapp20200101Req> request);

    /**
     * mapp.203.001.01 查询问题详情
     * @param request
     * @return
     */
    @OperationType(value = "mapp.203.001.01", name = "查询问题详情", desc = "mapp.203.001.01")
    ResponseModel<Mapp20300101Resp> queryQuestionDetail(RequestModel<Mapp20300101Req> request);

    /**
     * 公管查询-业务自定义参数
     */
    @OperationType(value = "mapp.090.001.01", name = "公管查询-业务自定义参数", desc = "mapp.090.001.01")
    ResponseModel<Mapp09000101Resp> queryBusinessParam(RequestModel<Mapp09000101Req> request);

    /**
     * 公管查询-多语言参数和协议参数
     */
    @OperationType(value = "mapp.091.001.01", name = "公管查询-多语言参数和协议参数", desc = "mapp.091.001.01")
    ResponseModel<Mapp09100101Resp> queryLanguageAndProtocol(RequestModel<Mapp09100101Req> request);

    /**
     * 公管查询-通用业务参数
     */
    @OperationType(value = "mapp.092.001.01", name = "公管查询-通用业务参数", desc = "mapp.092.001.01")
    ResponseModel<Mapp09200101Resp> queryCommonParam(RequestModel<Mapp09200101Req> request);

    /**
     * 公管查询-云控参数
     */
    @OperationType(value = "mapp.093.001.01", name = "公管查询-云控参数", desc = "mapp.093.001.01")
    ResponseModel<Mapp09300101Resp> queryCloudControl(RequestModel<Mapp09300101Req> request);

    /**
     * 公管查询-银行卡信息参数
     */
    @OperationType(value = "mapp.094.001.01", name = "公管查询-银行卡信息参数", desc = "mapp.094.001.01")
    ResponseModel<Mapp09400101Resp> queryBankCardInfo(RequestModel<Mapp09400101Req> request);

    /**
     * 手机号格式校验
     */
    @OperationType(value = "mapp.096.001.01", name = "手机号格式校验", desc = "mapp.096.001.01")
    ResponseModel validateMobileNumber(RequestModel<Mapp09600101Req> request);

}
