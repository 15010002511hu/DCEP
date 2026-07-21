package com.emop.wlt.user.management.service.impl;

import com.dubbo.dds.DDS;
import com.dubbo.dds.rule.SingleRule;
import com.emop.data.security.encrypt.service.impl.ServiceImpl;
import com.emop.wlt.common.enums.AppUserStatusEnum;
import com.emop.wlt.common.enums.LoginStatusEnum;
import com.emop.wlt.user.entity.MappingIndex;
import com.emop.wlt.user.entity.User;
import com.emop.wlt.user.management.constant.DatabaseConstant;
import com.emop.wlt.user.management.mapper.UserMapper;
import com.emop.wlt.user.management.mapper.sql.support.UserSqlSupport;
import com.emop.wlt.user.management.service.UserService;
import com.emop.wlt.user.service.MappingIndexService;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.util.Strings;
import org.mybatis.dynamic.sql.SqlTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private MappingIndexService mappingIndexService;

    @Override
    protected SqlTable sqlTable() {
        return UserSqlSupport.user;
    }

    @Override
    @DDS(value = DatabaseConstant.USER_MANAGEMENT_DB_KEY, rule = SingleRule.class)
    public User selectByPrimaryKey(String userId) {
        if (Strings.isBlank(userId)) {
            return null;
        }
        return super.selectOne(c -> c.where()
                .and(UserSqlSupport.userId, isEqualTo(userId))
        );
    }

    @Override
    @DDS(value = DatabaseConstant.USER_MANAGEMENT_DB_KEY, rule = SingleRule.class)
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public boolean saveUserInfo(User userLoginInfo) {
        LocalDateTime now = LocalDateTime.now();
        userLoginInfo.setCreateDatetime(now);
        userLoginInfo.setUpdateDatetime(now);
        return super.insertSelective(userLoginInfo) > NumberUtils.INTEGER_ZERO;
    }

    @DDS(value = DatabaseConstant.USER_MANAGEMENT_DB_KEY, rule = SingleRule.class)
    @Override
    public boolean updateUserPwdByUserId(String userId, String userPwd, String salt) {
        return super.update(c -> c.set(UserSqlSupport.pwd).equalTo(userPwd)
                .set(UserSqlSupport.pwdSalt).equalTo(salt)
                .set(UserSqlSupport.updateDatetime).equalTo(LocalDateTime.now())
                .where()
                .and(UserSqlSupport.userId, isEqualTo(userId))
        ) > NumberUtils.INTEGER_ZERO;
    }

    @Override
    @DDS(value = DatabaseConstant.USER_MANAGEMENT_DB_KEY, rule = SingleRule.class)
    @Transactional
    public void deactivateUser(String phone, String userId) {
        MappingIndex mappingIndex = new MappingIndex();
        mappingIndex.setMappingKey(phone);
        //删除索引表
        mappingIndexService.deleteByPhone(mappingIndex);
        //更新user表
        User userLoginInfo = new User();
        userLoginInfo.setUserId(userId);
        userLoginInfo.setLoginStatus(LoginStatusEnum.SIGNOUT.getValue());
        //注销状态
        userLoginInfo.setStatus(AppUserStatusEnum.LOGOFF.getValue());
        updateStatusByUserId(userLoginInfo);
    }

    @Override
    @DDS(value = DatabaseConstant.USER_MANAGEMENT_DB_KEY, rule = SingleRule.class)
    public boolean updateStatusByUserId(User userLoginInfo) {
        return super.update(c ->
                c.set(UserSqlSupport.loginStatus).equalToWhenPresent(userLoginInfo.getLoginStatus())
                        .set(UserSqlSupport.status).equalTo(userLoginInfo.getStatus())
                        .set(UserSqlSupport.updateDatetime).equalTo(LocalDateTime.now())
                        .where()
                        .and(UserSqlSupport.userId, isEqualTo(userLoginInfo.getUserId()))
        ) > NumberUtils.INTEGER_ZERO;
    }

}
