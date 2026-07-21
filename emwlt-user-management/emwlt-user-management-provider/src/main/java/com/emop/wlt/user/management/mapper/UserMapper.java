package com.emop.wlt.user.management.mapper;

import com.emop.data.security.encrypt.mapper.BaseMapper;
import com.emop.wlt.user.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {

}