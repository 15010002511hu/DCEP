package com.emop.wlt.user.management.manager;

import com.emop.wlt.user.entity.User;
import com.emop.wlt.user.management.service.MappingIndexManagementService;
import com.emop.wlt.user.management.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserManager {

    @Autowired
    private MappingIndexManagementService mappingIndexManagementService;

    @Autowired
    private UserService userService;

    public User queryUserByPhone(String phone) {
        String userId = mappingIndexManagementService.selectByPhone(phone);
        if (StringUtils.isBlank(userId)) {
            log.info("用户未注册{}", phone);
            return null;
        }
        User user = userService.selectByPrimaryKey(userId);
        if (user == null) {
            log.info("无user信息{}", phone);
            return null;
        }
        return user;
    }

}
