package com.emop.wlt.user.query.provider;

import com.emop.wlt.user.entity.CidIndex;
import com.emop.wlt.user.query.service.CidIndexManagementService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CidIndexQueryProviderImplTest {
    @Mock
    private CidIndexManagementService cidIndexManagementService;
    @InjectMocks
    private CidIndexQueryProviderImpl cidIndexQueryProviderImpl;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testQueryCidByIdInfo() {
        when(cidIndexManagementService.getCidByIdentityInfo(anyString(), anyString())).thenReturn(new CidIndex());

        cidIndexQueryProviderImpl.queryCidByIdInfo("identityType", "identityNumber");
    }

}
