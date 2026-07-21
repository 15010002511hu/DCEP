package com.emop.wlt.user.management.provider;

import com.emop.wlt.user.management.service.SystemMessageManageService;
import com.emop.wlt.user.pojo.SystemMessage;
import java.time.LocalDateTime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SystemMessageManageProviderImplTest {

    @InjectMocks
    private SystemMessageManageProviderImpl systemMessageManageProvider;
    @Mock
    private SystemMessageManageService systemMessageManageService;

    @Test
    public void testSave() {
        when(systemMessageManageService.save(any())).thenReturn(true);

        boolean result = systemMessageManageProvider.save(
            new SystemMessage("messageId", "msgsn", "content", "inst",
            LocalDateTime.now(), null, LocalDateTime.now(), LocalDateTime.now()));
        Assert.assertTrue(result);
    }
}
