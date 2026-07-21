package com.emop.wlt.user.management.provider;


import com.emop.wlt.user.management.api.SystemMessageManageProvider;
import com.emop.wlt.user.management.service.SystemMessageManageService;
import com.emop.wlt.user.pojo.SystemMessage;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author fanjianyu
 * @title
 * @description SystemMessageManageProviderImpl
 * @date 2022/3/31
 */
@DubboService
public class SystemMessageManageProviderImpl implements SystemMessageManageProvider {

    @Autowired
    private SystemMessageManageService systemMessageManageService;

    @Override
    public boolean save(SystemMessage systemMessage) {
        return systemMessageManageService.save(systemMessage);
    }
}
