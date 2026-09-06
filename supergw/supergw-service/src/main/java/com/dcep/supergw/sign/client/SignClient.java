package com.dcep.supergw.sign.client;

import cn.com.platform.security.adapter.Session;
import com.crypto.CryptoAdapter;
import com.dcep.kmsapi.impl.DcepKms;
import com.dcep.kmsapi.intf.KmsApi;
import java.io.ByteArrayInputStream;
import java.util.Properties;

public class SignClient {

    private static DcepKms client;
    private static Session session;

    private SignClient() {
    }

    public static void refresh(String props) throws Exception {
        //Properties对象加载roperties
        Properties svsProp = new Properties();
        svsProp.load(new ByteArrayInputStream(props.getBytes()));

        //新建签名服务API对象，并用Properties初始化
        if (client == null) {
            client = new DcepKms(svsProp);
        } else { //更新签名服务API对象配置信息
            client.updateConfig(svsProp);
        }

        if (session == null) {
            session = new CryptoAdapter(svsProp);
        } else {
            session.updateConfig(svsProp);
        }
    }

    public static KmsApi getInstance() {
        return client;
    }

    public static Session getSession() {
        return session;
    }
}
