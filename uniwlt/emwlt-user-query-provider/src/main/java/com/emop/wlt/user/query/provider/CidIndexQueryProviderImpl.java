package com.emop.wlt.user.query.provider;

import com.emop.wlt.user.entity.CidIndex;
import com.emop.wlt.user.query.api.CidIndexQueryProvider;
import com.emop.wlt.user.query.service.CidIndexManagementService;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService
@Slf4j
public class CidIndexQueryProviderImpl implements CidIndexQueryProvider {

    @Autowired
    private CidIndexManagementService cidIndexManagementService;

    @Override
    public String queryCidByIdInfo(String identityType, String identityNumber) {
        CidIndex cidIndex = cidIndexManagementService.getCidByIdentityInfo(identityType, identityNumber);
        return Optional.ofNullable(cidIndex).map(item->item.getCid()).orElse(null);
    }
}
