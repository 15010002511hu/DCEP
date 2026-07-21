package com.emop.wlt.user.management.service.impl;

import com.emop.wlt.user.entity.MappingIndex;
import com.emop.wlt.user.entity.User;
import com.emop.wlt.user.management.service.UserService;
import com.emop.wlt.user.service.MappingIndexService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MappingIndexManagementServiceImplTest {

    @InjectMocks
    private MappingIndexManagementServiceImpl mappingIndexManagementService;
    @Mock
    private MappingIndexService mappingIndexService;
    @Mock
    private UserService userService;

    @Test
    public void testSelectByPhone1() {
        MappingIndex mappingIndex = new MappingIndex();
        mappingIndex.setMappingValue("123456");
        when(mappingIndexService.selectByPhone(any())).thenReturn(mappingIndex);
        String result = mappingIndexManagementService.selectByPhone("phone");
        Assert.assertEquals("123456", result);
    }

    @Test
    public void testSelectByPhone2() {
        when(mappingIndexService.selectByPhone(any())).thenReturn(null);
        String result = mappingIndexManagementService.selectByPhone("phone");
        Assert.assertNull(result);
    }

    @Test
    public void testSelectByPhone3() {
        String result = mappingIndexManagementService.selectByPhone(null);
        Assert.assertNull(result);
    }

    @Test
    public void testSaveUserLogic() {
        when(mappingIndexService.saveLandals(any())).thenReturn(true);
        when(userService.saveUserInfo(any())).thenReturn(true);
        mappingIndexManagementService.saveUserLogic(new MappingIndex(), new User());
    }
}
