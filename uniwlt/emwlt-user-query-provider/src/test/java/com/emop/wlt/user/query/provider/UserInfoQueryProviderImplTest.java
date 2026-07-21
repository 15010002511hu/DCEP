package com.emop.wlt.user.query.provider;

import com.emop.wlt.user.entity.User;
import com.emop.wlt.user.query.dto.UserInfoDTO;
import com.emop.wlt.user.query.service.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.Month;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserInfoQueryProviderImplTest {

    @Mock
    private UserService userService;
    @InjectMocks
    private UserInfoQueryProviderImpl userInfoQueryProviderImpl;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSelectByUserId() {
        when(userService.selectByPrimaryKey(anyString())).thenReturn(new User());

        userInfoQueryProviderImpl.selectByUserId("userId");
    }

}
