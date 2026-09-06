/*
 * pbcdci.cn Inc.
 * Copyright © 2023 PBCDCI All Rights Reserved.
 */
package com.dcep.supergw.service;

import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.Response;

public interface IndustryKeyService {

    Response<EnvelopeDTO<GwDTO>> execute(EnvelopeDTO<GwDTO> request);
}
