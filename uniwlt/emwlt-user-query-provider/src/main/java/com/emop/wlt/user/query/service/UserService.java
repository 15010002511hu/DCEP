package com.emop.wlt.user.query.service;


import com.emop.wlt.user.entity.User;


public interface UserService {

    User selectByPrimaryKey(String userId);

}
