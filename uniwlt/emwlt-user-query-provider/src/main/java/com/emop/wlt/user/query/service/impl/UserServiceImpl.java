package com.emop.wlt.user.query.service.impl;

import com.dubbo.dds.DDS;
import com.dubbo.dds.rule.SingleRule;
import com.emop.data.security.encrypt.service.impl.ServiceImpl;
import com.emop.wlt.user.entity.User;
import com.emop.wlt.user.query.constant.DatabaseConstant;
import com.emop.wlt.user.query.mapper.UserMapper;
import com.emop.wlt.user.query.mapper.sql.support.UserSqlSupport;
import com.emop.wlt.user.query.service.UserService;
import org.mybatis.dynamic.sql.SqlTable;
import org.springframework.stereotype.Service;

import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    protected SqlTable sqlTable() {
        return UserSqlSupport.user;
    }

    @Override
    @DDS(value = DatabaseConstant.USER_QUERY_DATABASE, rule = SingleRule.class)
    public User selectByPrimaryKey(String userId) {
        return super.selectOne(c -> c.where()
                .and(UserSqlSupport.userId, isEqualTo(userId))
        );
    }

}
