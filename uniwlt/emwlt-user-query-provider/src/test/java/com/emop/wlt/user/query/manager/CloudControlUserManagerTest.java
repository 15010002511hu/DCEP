package com.emop.wlt.user.query.manager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CloudControlUserManagerTest {

    private CloudControlUserManager cloudControlUserManager = new CloudControlUserManager();

    @Test
    public void testCheckUserIsGray() {
        cloudControlUserManager.checkUserIsGray("userPhone");
    }

    @Test
    public void testCheckUserIsGrayBoolean() {
        cloudControlUserManager.checkUserIsGrayBoolean("userPhone");
    }

    @Test
    public void testIsQRCodePayWhite() {
        cloudControlUserManager.isQRCodePayWhite("userPhone");
    }

}