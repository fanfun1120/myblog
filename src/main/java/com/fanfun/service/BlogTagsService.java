package com.fanfun.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fanfun.domain.ResponseResult;
import com.fanfun.entity.Blog;
import com.fanfun.entity.BlogTags;
import com.fanfun.entity.Tag;

import java.util.List;


/**
 * (BlogTags)表服务接口
 *
 * @author makejava
 * @since 2022-03-30 22:34:50
 */
public interface BlogTagsService extends IService<BlogTags> {
    ResponseResult getBlogByTagId(Long id,Integer pageNum);

    List<Blog> getBlogByTagId(Long id);
    List<Tag> getTagByBlogId(Long id);
}
