package com.emop.wlt.user.query.util;

import com.alibaba.fastjson.JSONObject;
import com.emop.wlt.user.query.model.vo.GroupconfigDB;
import com.emop.wlt.user.query.model.vo.UserConfig;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ConfigConstructorUtilsTest {

    @InjectMocks
    private ConfigConstructorUtils configConstructorUtils;

    private List<GroupconfigDB> groupConfigDBList;

    @Before
    public void setUp() {
        GroupconfigDB groupconfigDB1 = GroupconfigDB.builder()
            .groupId("0")
            .moduleKey("defaultConfig1")
            .functionKey("functionProtocolKey")
            .config("{\"config1\":\"1\"}")
            .version("1.0.0")
            .offlineWallet("iOS")
            .offlineWalletSwitch("0")
            .updateTime(LocalDateTime.now())
            .build();
        GroupconfigDB groupconfigDB2 = GroupconfigDB.builder()
            .groupId("0")
            .moduleKey("defaultConfig2")
            .functionKey("hardwareWalletBasedAccount")
            .config("{\"config2\":\"1\"}")
            .version("1.0.0")
            .offlineWallet("Android")
            .offlineWalletSwitch("1")
            .updateTime(LocalDateTime.now())
            .build();
        GroupconfigDB groupconfigDB3 = GroupconfigDB.builder()
            .groupId("0")
            .moduleKey("defaultConfig")
            .functionKey("commonConfig")
            .config("{\"instConfigVersion\":\"2.0\"}")
            .version("1.0.0")
            .offlineWallet("Android")
            .offlineWalletSwitch("0")
            .updateTime(LocalDateTime.now())
            .build();
        groupConfigDBList = new ArrayList<>();
        groupConfigDBList.add(groupconfigDB1);
        groupConfigDBList.add(groupconfigDB2);
        groupConfigDBList.add(groupconfigDB3);
    }

    @Test
    public void configConstructorNew() {
        UserConfig expectedUserConfig = UserConfig.builder()
            .version("1.0.0")
            .config(JSONObject.parseObject(""
                + "{\n"
                + "    \"functionProtocolKey\": {\n"
                + "        \"config1\": \"1\"\n"
                + "    },\n"
                + "    \"hardwareWalletBasedAccount\": {\n"
                + "        \"config2\": \"1\",\n"
                + "        \"offlineWallet\":\"0\",\n"
                + "        \"isDeviceAllowBasedAccountHardwareWallet\":\"0\"\n"
                + "    },\n"
                + "    \"commonConfig\": {\n"
                + "        \"instConfigVersion\": \"2.0\"\n"
                + "    }\n"
                + "}")).build();

        UserConfig actualUserConfig = configConstructorUtils.configConstructorNew(groupConfigDBList, "Samsung-5G");
        Assert.assertEquals(expectedUserConfig, actualUserConfig);
    }
}