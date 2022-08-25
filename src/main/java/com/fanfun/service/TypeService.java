package com.fanfun.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fanfun.domain.ResponseResult;
import com.fanfun.entity.Type;


/**
 * (Type)表服务接口
 *
 * @author makejava
 * @since 2022-03-27 11:30:31
 */
public interface TypeService extends IService<Type> {

    ResponseResult listType();
    ResponseResult listType(Integer pageNum);

    Type getTypeByName(String name);
}
