package com.emop.wlt.user.management.mapi.business.impl;

import com.emop.common.enums.errorcode.BaseErrorEnum;
import com.emop.gateway.api.sdk.service.dto.GwPojoMsgDTO;
import com.emop.wlt.aspect.annotation.AppResponse;
import com.emop.wlt.common.enums.WalletLevelEnum;
import com.emop.wlt.common.enums.WalletStatusEnum;
import com.emop.wlt.common.model.flow.FlowRespInfo;
import com.emop.wlt.common.model.mapp.RequestModel;
import com.emop.wlt.common.model.mapp.ResponseModel;
import com.emop.wlt.common.util.InstNoUtils;
import com.emop.wlt.common.util.Safes;
import com.emop.wlt.flow.annotation.MappFlowJoin;
import com.emop.wlt.flow.context.condition.WalletLevelConditionEnum;
import com.emop.wlt.flow.context.util.FlowContextUtils;
import com.emop.wlt.gateway.manager.EmapGatewayManager;
import com.emop.wlt.model.emap.EmapDTOBuildHelper;
import com.emop.wlt.model.emap.emap321.Emap32100101ReqDTO;
import com.emop.wlt.user.management.assemble.WalletInfoAssembleFactory;
import com.emop.wlt.user.management.exception.ExceptionCast;
import com.emop.wlt.user.management.mapi.business.ReportAsLostProvider;
import com.emop.wlt.user.management.model.request.Mapp31300101Req;
import com.emop.wlt.user.management.model.request.Mapp31400101Req;
import com.emop.wlt.user.management.model.response.Mapp31300101Resp;
import com.emop.wlt.user.management.model.response.Mapp31400101Resp;
import com.emop.wlt.user.management.provider.UserProviderImpl;
import com.emop.wlt.user.management.service.MappingIndexManagementService;
import com.emop.wlt.user.wallet.api.UserWalletQueryProvider;
import com.emop.wlt.user.wallet.dto.WalletBasicInfoDTO;
import com.emop.wlt.user.wallet.dto.WalletInfoDTO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportAsLostProviderImpl implements ReportAsLostProvider {

    @DubboReference
    private UserWalletQueryProvider userWalletQueryProvider;
    @Autowired
    private MappingIndexManagementService mappingIndexManagementService;
    @Autowired
    private EmapGatewayManager emapGatewayManager;
    @Autowired
    private UserProviderImpl userProviderImpl;

    @MappFlowJoin(nodeCode = "MAPP313", groupNodeCode = "BNC313", verifySceneType = "VST36", flowVersion = "VST36_01")
    @AppResponse
    @Override
    public ResponseModel<Mapp31300101Resp> preCheck(RequestModel<Mapp31300101Req> request) {
        String mobileNumber = request.getMessageBody().getMobileNumber();
        // 判断是否注册
        String userId = mappingIndexManagementService.selectByPhone(mobileNumber);
        if (StringUtils.isBlank(userId)) {
            ExceptionCast.cast(BaseErrorEnum.B12301);
        }

        List<WalletInfoDTO> walletInfoDTOList = userWalletQueryProvider.queryUserWalletFromInst(userId);
        if (CollectionUtils.isEmpty(walletInfoDTOList)) {
            ExceptionCast.buzCast(BaseErrorEnum.B14104);
        }
        if (walletInfoDTOList.size() > 1) {
            ExceptionCast.buzCast(BaseErrorEnum.B14109);
        }

        WalletInfoDTO walletInfoDTO = Safes.first(walletInfoDTOList);
        WalletBasicInfoDTO walletBasicInfoDTO = walletInfoDTO.getWalletBasicInfo();
        if (WalletStatusEnum.LOSS.getCode().equals(walletBasicInfoDTO.getWalletStatus())) {
            ExceptionCast.buzCast(BaseErrorEnum.B06608);
        }
        if (!WalletStatusEnum.NORMAL.getCode().equals(walletBasicInfoDTO.getWalletStatus())) {
            ExceptionCast.buzCast(BaseErrorEnum.B06613);
        }

        FlowContextUtils.saveUserId(userId);
        FlowContextUtils.saveMobileNumber(mobileNumber);
        FlowContextUtils.saveWalletId(walletBasicInfoDTO.getWalletId());

        Mapp31300101Resp mapp31300101Resp = new Mapp31300101Resp();
        mapp31300101Resp.setWalletBasicInfo(WalletInfoAssembleFactory.assemble(walletBasicInfoDTO));
        mapp31300101Resp.setCustomerIdInfo(WalletInfoAssembleFactory.assemble(walletInfoDTO.getCustomerIdInfo()));
        FlowRespInfo flowRespInfo = new FlowRespInfo();
        mapp31300101Resp.setFlowRespInfo(flowRespInfo);
        flowRespInfo.setRouteCondition(
                WalletLevelEnum.BASIC_LEVEL.getCode().equals(walletBasicInfoDTO.getWalletLevel()) ?
                        WalletLevelConditionEnum.WL04.getCode() : WalletLevelConditionEnum.WL01_02_03.getCode());
        return ResponseModel.<Mapp31300101Resp>builder().messageBody(mapp31300101Resp).build();
    }

    @MappFlowJoin(nodeCode = "MAPP314", groupNodeCode = "BNC314", verifySceneType = "VST36", flowVersion = "VST36_01")
    @AppResponse
    @Override
    public ResponseModel<Mapp31400101Resp> reportAsLost(RequestModel<Mapp31400101Req> request) {
        String userId = FlowContextUtils.getUserId();
        String walletId = FlowContextUtils.getWalletId();
        String contextNo = FlowContextUtils.getInstContext();

        Emap32100101ReqDTO emap32100101ReqDTO = new Emap32100101ReqDTO();
        emap32100101ReqDTO.setWalletId(walletId);
        emap32100101ReqDTO.setContextNo(contextNo);

        GwPojoMsgDTO gwPojoMsgDto = EmapDTOBuildHelper.createGwPojoMsgDTO(
                emap32100101ReqDTO, userId, InstNoUtils.getInstNm(walletId));
        emapGatewayManager.send(gwPojoMsgDto);
        userProviderImpl.reportAsLost(userId);

        return ResponseModel.<Mapp31400101Resp>builder().messageBody(new Mapp31400101Resp()).build();
    }

}
