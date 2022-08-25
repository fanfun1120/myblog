package com.fanfun.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fanfun.NotFoundException;
import com.fanfun.domain.ResponseResult;
import com.fanfun.domain.vo.PageVo;
import com.fanfun.entity.Blog;
import com.fanfun.entity.BlogTags;
import com.fanfun.entity.Tag;
import com.fanfun.mapper.BlogMapper;
import com.fanfun.mapper.UserMapper;
import com.fanfun.service.BlogService;
import com.fanfun.service.BlogTagsService;
import com.fanfun.service.TypeService;
import com.fanfun.service.UserService;
import com.fanfun.util.MarkdownUtils;
import com.fanfun.util.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * (Blog)表服务实现类
 *
 * @author makejava
 * @since 2022-03-26 21:01:44
 */
@Service("blogService")
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {

    @Autowired
    private BlogService blogService;

    @Autowired
    private UserService userService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private BlogTagsService blogTagsService;

    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult getBlogs(Integer pageNum) {
        if (pageNum == null || pageNum == 0) {
            pageNum = 1;
        }
        String cache_Key = "getIndexPage:" + pageNum;
        String cache_json = redisCache.getCacheObject(cache_Key);
        PageVo pageVo = null;
        if (cache_json != null) {
            return ResponseResult.okResult(JSON.parseObject(cache_json, PageVo.class));
        } else {
            //查询条件
            LambdaQueryWrapper<Blog> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(Blog::getPublished, 1);
            lambdaQueryWrapper.orderByDesc(Blog::getCreateTime);
            Page page = new Page(pageNum, 6);
            blogService.page(page, lambdaQueryWrapper);
            List<Blog> pageRecords = page.getRecords();
            pageRecords.stream().forEach(blog -> blog.setUser(userService.getById(blog.getUserId())));
            pageRecords.stream().forEach(blog -> blog.setType(typeService.getById(blog.getTypeId())));
            pageVo = new PageVo(pageRecords, page.getTotal(), page.getCurrent(), page.getPages());
            String json = JSON.toJSONString(pageVo);
            redisCache.setCacheObject(cache_Key, json,3, TimeUnit.DAYS);
            return ResponseResult.okResult(pageVo);
        }
    }

    @Override
    public ResponseResult getAllBlogs(Integer pageNum) {
        if (pageNum == null || pageNum == 0) {
            pageNum = 1;
        }
        //查询条件
        LambdaQueryWrapper<Blog> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByDesc(Blog::getCreateTime);
        Page page = new Page(pageNum, 6);
        blogService.page(page, lambdaQueryWrapper);
        List<Blog> pageRecords = page.getRecords();
        pageRecords.stream().forEach(blog -> blog.setUser(userService.getById(blog.getUserId())));
        pageRecords.stream().forEach(blog -> blog.setType(typeService.getById(blog.getTypeId())));
        PageVo pageVo = new PageVo(pageRecords, page.getTotal(), page.getCurrent(), page.getPages());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getBlogs(Integer pageNum, Long typeId) {
        if (pageNum == null || pageNum == 0) {
            pageNum = 1;
        }
        //查询条件
        LambdaQueryWrapper<Blog> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Blog::getPublished, 1);
        lambdaQueryWrapper.eq(typeId != -1, Blog::getTypeId, typeId);
        lambdaQueryWrapper.orderByDesc(Blog::getCreateTime);
        Page page = new Page(pageNum, 6);
        blogService.page(page, lambdaQueryWrapper);
        List<Blog> pageRecords = page.getRecords();
        pageRecords.stream().forEach(blog -> blog.setUser(userService.getById(blog.getUserId())));
        pageRecords.stream().forEach(blog -> blog.setType(typeService.getById(blog.getTypeId())));
        PageVo pageVo = new PageVo(pageRecords, page.getTotal(), page.getCurrent(), page.getPages());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult listRecommendBlogTop(Integer i) {
        LambdaQueryWrapper<Blog> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Blog::getRecommend, 1);
        lambdaQueryWrapper.eq(Blog::getPublished, 1);
        lambdaQueryWrapper.orderByDesc(Blog::getCreateTime);
        lambdaQueryWrapper.last("limit " + i);
        List<Blog> blogList = list(lambdaQueryWrapper);
        return ResponseResult.okResult(blogList);
    }

    @Override
    public ResponseResult getAndConvertBlog(Long id) {
        Blog blog = getById(id);
        blog.setUser(userService.getById(blog.getUserId()));
        blog.setType(typeService.getById(blog.getTypeId()));
        List<Tag> tagList = blogTagsService.getTagByBlogId(id);
        blog.setTags(tagList);
        if (blog == null) {
            throw new NotFoundException("博客不存在");
        }
        blog.setContent(MarkdownUtils.markdownToHtmlExtensions(blog.getContent()));
        updateViews(id);

        return ResponseResult.okResult(blog);
    }

    @Transactional
    @Override
    public void updateViews(Long id) {
        Blog blog = getById(id);
        blog.setViews(blog.getViews() + 1);
        saveOrUpdate(blog);
    }

    @Override
    public ResponseResult listBlog(String query, Integer pageNum) {
        if (pageNum == null || pageNum == 0) {
            pageNum = 1;
        }
        //查询条件
        LambdaQueryWrapper<Blog> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Blog::getPublished, 1);
        lambdaQueryWrapper.orderByDesc(Blog::getCreateTime);
        lambdaQueryWrapper.like(StringUtils.isNotBlank(query), Blog::getTitle, query);
        Page page = new Page(pageNum, 6);
        blogService.page(page, lambdaQueryWrapper);
        List<Blog> pageRecords = page.getRecords();
        pageRecords.stream().forEach(blog -> blog.setUser(userService.getById(blog.getUserId())));
        pageRecords.stream().forEach(blog -> blog.setType(typeService.getById(blog.getTypeId())));
        PageVo pageVo = new PageVo(pageRecords, page.getTotal(), page.getCurrent(), page.getPages());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult countBlog() {
        return ResponseResult.okResult(blogService.list().size());
    }

    @Override
    public ResponseResult archiveBlog() {
        Map<String, List<Blog>> map = new HashMap<>();
        List<Blog> blogList = blogService.list();
        List<String> years = new ArrayList<>();
        blogList.stream().forEach(blog -> {
            Calendar c = Calendar.getInstance();
            c.setTime(blog.getCreateTime());
            years.add(Integer.toString(c.get(Calendar.YEAR)));
        });
        years.stream().forEach(year -> {
            map.put(year, blogService.findByYear(year));
        });
        return ResponseResult.okResult(map);
    }

    @Override
    public List<Blog> findByYear(String year) {
        LambdaQueryWrapper<Blog> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(Blog::getCreateTime, year);
        lambdaQueryWrapper.last("limit 6");
        return blogService.list(lambdaQueryWrapper);
    }

    @Override
    public ResponseResult deleteBlog(Long id) {
        LambdaQueryWrapper<BlogTags> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        QueryWrapper<BlogTags> queryWrapper = new QueryWrapper<>();
        lambdaQueryWrapper.eq(BlogTags::getBlogsId, id);
        blogTagsService.remove(lambdaQueryWrapper);
        blogService.removeById(id);
        return ResponseResult.okResult(200, "操作成功");
    }

}
