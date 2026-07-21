package com.emop.wlt.user.query.helper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.emop.doms.cloudcontrol.query.dto.CloudControlUserInfoRespDTO;
import com.emop.wlt.user.query.model.vo.UserConfig;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CloudControlHelper {

    public static List<CloudControlUserInfoRespDTO> buildCloudControlConfigData() {
        CloudControlUserInfoRespDTO cloudControlUserInfoRespDTO1 = new CloudControlUserInfoRespDTO();
        cloudControlUserInfoRespDTO1.setGroupCode("0");
        cloudControlUserInfoRespDTO1.setModuleProtocolKey("defaultConfig1");
        cloudControlUserInfoRespDTO1.setFunctionProtocolKey("functionProtocolKey1");
        cloudControlUserInfoRespDTO1.setConfig("{\"config1\":\"1\"}");
        cloudControlUserInfoRespDTO1.setVersion("1.0.0");
        cloudControlUserInfoRespDTO1.setDeviceTypes("iOS");
        cloudControlUserInfoRespDTO1.setDeviceTypeSwitch("0");
        cloudControlUserInfoRespDTO1.setUpdateTime(new Date());

        CloudControlUserInfoRespDTO cloudControlUserInfoRespDTO2 = new CloudControlUserInfoRespDTO();
        cloudControlUserInfoRespDTO2.setGroupCode("0");
        cloudControlUserInfoRespDTO2.setModuleProtocolKey("defaultConfig2");
        cloudControlUserInfoRespDTO2.setFunctionProtocolKey("functionProtocolKey2");
        cloudControlUserInfoRespDTO2.setConfig("{\"config2\":\"1\"}");
        cloudControlUserInfoRespDTO2.setVersion("1.0.0");
        cloudControlUserInfoRespDTO2.setDeviceTypes("Android");
        cloudControlUserInfoRespDTO2.setDeviceTypeSwitch("0");

        CloudControlUserInfoRespDTO cloudControlUserInfoRespDTO3 = new CloudControlUserInfoRespDTO();
        cloudControlUserInfoRespDTO3.setGroupCode("0");
        cloudControlUserInfoRespDTO3.setModuleProtocolKey("defaultConfig");
        cloudControlUserInfoRespDTO3.setFunctionProtocolKey("commonConfig");
        cloudControlUserInfoRespDTO3.setConfig("{\"instConfigVersion\":\"2.0\"}");
        cloudControlUserInfoRespDTO3.setVersion("1.0.0");
        cloudControlUserInfoRespDTO3.setDeviceTypes("Android");
        cloudControlUserInfoRespDTO3.setDeviceTypeSwitch("0");

        List<CloudControlUserInfoRespDTO> cloudControlUserInfoRespDTOList = new ArrayList<>();
        cloudControlUserInfoRespDTOList.add(cloudControlUserInfoRespDTO1);
        cloudControlUserInfoRespDTOList.add(cloudControlUserInfoRespDTO2);
        cloudControlUserInfoRespDTOList.add(cloudControlUserInfoRespDTO3);
        return cloudControlUserInfoRespDTOList;
    }

    public static UserConfig buildUserConfig() {
        JSON config = JSONObject.parseObject(""
            + "{\n"
            + "    \"functionProtocolKey1\": {\n"
            + "        \"config1\": \"1\"\n"
            + "    },\n"
            + "    \"functionProtocolKey2\": {\n"
            + "        \"config2\": \"1\"\n"
            + "    },\n"
            + "    \"commonConfig\": {\n"
            + "        \"instConfigVersion\": \"1.0\"\n"
            + "    }\n"
            + "}");
        return UserConfig.builder().version("").config(config).build();
    }
}
