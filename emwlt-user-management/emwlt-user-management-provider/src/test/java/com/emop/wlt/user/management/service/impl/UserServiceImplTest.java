package com.emop.wlt.user.management.service.impl;

import com.emop.wlt.user.entity.User;
import com.emop.wlt.user.service.MappingIndexService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mybatis.dynamic.sql.SqlTable;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userServiceImpl;
    @Mock
    private MappingIndexService mappingIndexService;

    @Test
    public void testSqlTable() {
        SqlTable result = userServiceImpl.sqlTable();
        Assert.assertNotNull(result);
    }

    @Test
    public void testSelectByPrimaryKey1() {
        Assert.assertThrows(Exception.class,
            ()-> userServiceImpl.selectByPrimaryKey("userId"));

    }

    @Test
    public void testSelectByPrimaryKey2() {
        User actualResp = userServiceImpl.selectByPrimaryKey(null);
        Assert.assertNull(actualResp);

    }

    @Test
    public void testSaveUserInfo() {
        Assert.assertThrows(Exception.class,
            ()-> userServiceImpl.saveUserInfo(new User()));
    }

    @Test
    public void testUpdateUserPwdByUserId() {
        Assert.assertThrows(Exception.class,
            ()-> userServiceImpl.updateUserPwdByUserId("userId", "userPwd", "salt"));
    }

    @Test
    public void testDeactivateUser() {
        when(mappingIndexService.deleteByPhone(any())).thenReturn(true);
        Assert.assertThrows(Exception.class,
            ()-> userServiceImpl.deactivateUser("phone", "userId"));
    }

    @Test
    public void testUpdateStatusByUserId() {
        Assert.assertThrows(Exception.class,
            ()-> userServiceImpl.updateStatusByUserId(new User()));
    }
}
