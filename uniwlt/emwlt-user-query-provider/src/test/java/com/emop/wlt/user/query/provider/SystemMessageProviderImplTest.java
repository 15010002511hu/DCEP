package com.emop.wlt.user.query.provider;

import com.emop.wlt.user.pojo.SystemMessage;
import com.emop.wlt.user.query.service.SystemMessageQueryServie;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SystemMessageProviderImplTest {
    @Mock
    private SystemMessageQueryServie systemMessageServiceQuery;
    @InjectMocks
    private SystemMessageProviderImpl systemMessageProviderImpl;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSelectAll() {
        when(systemMessageServiceQuery.selectAll()).thenReturn(List.of(new SystemMessage("messageId", "msgsn", "content", "inst", LocalDateTime.of(2024, Month.OCTOBER, 24, 19, 10, 49), new Timestamp(0, 0, 0, 0, 0, 0, 0), LocalDateTime.of(2024, Month.OCTOBER, 24, 19, 10, 49), LocalDateTime.of(2024, Month.OCTOBER, 24, 19, 10, 49))));

        systemMessageProviderImpl.selectAll();
    }

    @Test
    public void testSelectByInstAndGtTimeStampAndGeValidTime() {
        when(systemMessageServiceQuery.selectByInstAndGtTimeStampAndGeValidTime(anyString(), any(), any())).thenReturn(List.of(new SystemMessage("messageId", "msgsn", "content", "inst", LocalDateTime.of(2024, Month.OCTOBER, 24, 19, 10, 49), new Timestamp(0, 0, 0, 0, 0, 0, 0), LocalDateTime.of(2024, Month.OCTOBER, 24, 19, 10, 49), LocalDateTime.of(2024, Month.OCTOBER, 24, 19, 10, 49))));

        systemMessageProviderImpl.selectByInstAndGtTimeStampAndGeValidTime("inst", new Timestamp(0, 0, 0, 0, 0, 0, 0), LocalDateTime.of(2024, Month.OCTOBER, 24, 19, 10, 49));
    }

}
