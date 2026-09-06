/*
 * pbcdci.cn Inc.
 * Copyright © 2025 PBCDCI All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.api;

import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.Response;
import com.dubbo.ldc.ZoneRouter;

@ZoneRouter
public interface PaymentService {

    Response<EnvelopeDTO<GwDTO>> dbtrSettle(EnvelopeDTO<GwDTO> in);

    Response<EnvelopeDTO<GwDTO>> cdtrSettle(EnvelopeDTO<GwDTO> in);

    Response<GwDTO> orderConfirm(EnvelopeDTO<GwDTO> in);

    Response<EnvelopeDTO<GwDTO>> resultReport(EnvelopeDTO<GwDTO> in);
}
