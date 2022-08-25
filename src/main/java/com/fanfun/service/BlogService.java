package com.fanfun.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fanfun.domain.ResponseResult;
import com.fanfun.entity.Blog;

import java.util.List;


/**
 * (Blog)表服务接口
 *
 * @author makejava
 * @since 2022-03-26 21:01:44
 */
public interface BlogService extends IService<Blog> {

    ResponseResult listRecommendBlogTop(Integer i);
    ResponseResult getBlogs(Integer pageNum);
    ResponseResult getAllBlogs(Integer pageNum);
    ResponseResult getBlogs(Integer pageNum,Long typeId);
    ResponseResult getAndConvertBlog(Long id);
    void updateViews(Long id);
    ResponseResult listBlog(String query, Integer pageNum);

    ResponseResult countBlog();

    ResponseResult archiveBlog();

    List<Blog> findByYear(String year);

    ResponseResult deleteBlog(Long id);
}
