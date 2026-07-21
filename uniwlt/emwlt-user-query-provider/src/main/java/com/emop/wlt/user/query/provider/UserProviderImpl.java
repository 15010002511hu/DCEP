package com.emop.wlt.user.query.provider;


import com.emop.wlt.user.entity.User;
import com.emop.wlt.user.query.api.UserProvider;
import com.emop.wlt.user.query.dto.vo.UserVO;
import com.emop.wlt.user.query.service.MappingIndexManagementService;
import com.emop.wlt.user.query.service.UserService;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author fanjianyu
 * @title
 * @description UserProviderImpl
 * @date 2021/8/6
 */
@DubboService
public class UserProviderImpl implements UserProvider {

    @Autowired
    private UserService userService;

    @Autowired
    private MappingIndexManagementService mappingIndexManagementService;

    @Override
    public UserVO selectByPrimaryKey(String userId) {
        User user = userService.selectByPrimaryKey(userId);
        if (Objects.isNull(user)) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    public UserVO selectByLandAls(String landAls) {
        String userId = mappingIndexManagementService.selectByPhone(landAls);
        if (StringUtils.isBlank(userId)) {
            return null;
        }
        return selectByPrimaryKey(userId);
    }

    @Override
    public String selectUserIdByPhone(String phone) {
        return mappingIndexManagementService.selectByPhone(phone);
    }
}
