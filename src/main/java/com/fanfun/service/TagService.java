package com.fanfun.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fanfun.domain.ResponseResult;
import com.fanfun.entity.Tag;

import java.util.List;


/**
 * (Tag)表服务接口
 *
 * @author makejava
 * @since 2022-03-27 11:30:56
 */
public interface TagService extends IService<Tag> {

    ResponseResult listTag();
    ResponseResult listTag(Integer pageNum);

    Tag getTagByName(String name);
}
