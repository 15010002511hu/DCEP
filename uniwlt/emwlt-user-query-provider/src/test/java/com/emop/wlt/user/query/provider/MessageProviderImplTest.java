package com.emop.wlt.user.query.provider;

import com.emop.common.model.Response;
import com.emop.gateway.dto.emap.EmapDTO;
import com.emop.gateway.dto.emap.RequestModel;
import com.emop.gateway.dto.emap.ResponseModel;
import com.emop.wlt.user.query.service.impl.MessageService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MessageProviderImplTest {

    @Mock
    private MessageService messageService;
    @InjectMocks
    private MessageProviderImpl messageProviderImpl;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testExecute() {
        Response<ResponseModel<EmapDTO>> response = messageProviderImpl.execute(new RequestModel<EmapDTO>());
        Assert.assertEquals("PR00", response.getResult().getProcessResult().getProcessStatus());
        Assert.assertEquals("成功", response.getResult().getProcessResult().getProcessInformation());
    }

}
