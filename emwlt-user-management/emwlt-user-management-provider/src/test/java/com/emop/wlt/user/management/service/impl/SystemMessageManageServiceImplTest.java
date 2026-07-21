package com.emop.wlt.user.management.service.impl;

import com.emop.wlt.user.pojo.SystemMessage;
import com.emop.wlt.user.service.SystemMessageService;
import java.time.LocalDateTime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SystemMessageManageServiceImplTest {

    @InjectMocks
    private SystemMessageManageServiceImpl systemMessageManageService;
    @Mock
    private SystemMessageService systemMessageService;

    @Test
    public void testSave() {
        when(systemMessageService.insert(any())).thenReturn(1);
        boolean result = systemMessageManageService.save(
            new SystemMessage("messageId", "msgsn", "content", "inst",
            LocalDateTime.now(), null, LocalDateTime.now(), LocalDateTime.now()));
        Assert.assertTrue(result);
    }
}
