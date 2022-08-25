package com.fanfun.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fanfun.entity.User;


/**
 * (User)表服务接口
 *
 * @author makejava
 * @since 2022-03-27 10:50:29
 */
public interface UserService extends IService<User> {

    User checkUser(String username, String password);
}
