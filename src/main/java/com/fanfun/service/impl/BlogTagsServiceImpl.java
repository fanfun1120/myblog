package com.fanfun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fanfun.domain.ResponseResult;
import com.fanfun.domain.vo.PageVo;
import com.fanfun.entity.Blog;
import com.fanfun.entity.BlogTags;
import com.fanfun.entity.Tag;
import com.fanfun.mapper.BlogTagsMapper;
import com.fanfun.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * (BlogTags)表服务实现类
 *
 * @author makejava
 * @since 2022-03-30 22:34:50
 */
@Service("blogTagsService")
public class BlogTagsServiceImpl extends ServiceImpl<BlogTagsMapper, BlogTags> implements BlogTagsService {

    @Autowired
    private BlogService blogService;

    @Autowired
    private TagService tagService;

    @Autowired
    private UserService userService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private BlogTagsService blogTagsService;

    @Override
    public ResponseResult getBlogByTagId(Long id, Integer pageNum) {
        if (pageNum == null || pageNum == 0) {
            pageNum = 1;
        }
        List<Blog> blogList = new ArrayList<>();
        Page page = new Page(pageNum, 6);
        Page tagPage = page(page, new LambdaQueryWrapper<BlogTags>().eq(id != -1, BlogTags::getTagsId, id));
        List<BlogTags> tagPageRecords = tagPage.getRecords();
        tagPageRecords.stream().forEach(blogId -> {
            blogList.add(blogService.getById(blogId.getBlogsId()));
        });
        blogList.stream().forEach(blog -> blog.setUser(userService.getById(blog.getUserId())));
        blogList.stream().forEach(blog -> blog.setType(typeService.getById(blog.getTypeId())));
        blogList.stream().forEach(blog -> blog.setTags(blogTagsService.getTagByBlogId(blog.getId())));
        PageVo pageVo = new PageVo(blogList, page.getTotal(), page.getCurrent(), page.getPages());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public List<Blog> getBlogByTagId(Long id) {
        List<Blog> blogList = new ArrayList<>();
        List<BlogTags> blogIdList = list(new LambdaQueryWrapper<BlogTags>().eq(BlogTags::getTagsId, id));
        blogIdList.stream().forEach(blogId -> {
            blogList.add(blogService.getById(blogId.getBlogsId()));
        });
        return blogList;
    }

    @Override
    public List<Tag> getTagByBlogId(Long id) {
        List<Tag> tagList = new ArrayList<>();
        List<BlogTags> tagIdList = list(new LambdaQueryWrapper<BlogTags>().eq(BlogTags::getBlogsId, id));
        tagIdList.stream().forEach(tagId -> {
            tagList.add(tagService.getById(tagId.getTagsId()));
        });
        return tagList;
    }


}
