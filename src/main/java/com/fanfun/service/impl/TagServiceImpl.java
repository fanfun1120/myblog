package com.fanfun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fanfun.domain.ResponseResult;
import com.fanfun.domain.vo.PageVo;
import com.fanfun.entity.Blog;
import com.fanfun.entity.BlogTags;
import com.fanfun.entity.Tag;
import com.fanfun.entity.Type;
import com.fanfun.mapper.TagMapper;
import com.fanfun.service.BlogService;
import com.fanfun.service.BlogTagsService;
import com.fanfun.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * (Tag)表服务实现类
 *
 * @author makejava
 * @since 2022-03-27 11:30:56
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Autowired
    private BlogTagsService blogTagsService;

    @Override
    public ResponseResult listTag() {
        List<Tag> tagList = list();
        tagList.stream().forEach(tag -> tag.setBlogs(blogTagsService.getBlogByTagId(tag.getId())));
        return ResponseResult.okResult(tagList);
    }

    @Override
    public ResponseResult listTag(Integer pageNum) {
        if (pageNum == null || pageNum == 0) {
            pageNum = 1;
        }
        Page page = new Page(pageNum, 6);
        List<Tag> records = page(page).getRecords();
        PageVo pageVo = new PageVo(records, page.getTotal(), page.getCurrent(), page.getPages());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public Tag getTagByName(String name) {
        LambdaQueryWrapper<Tag> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Tag::getName,name);
        return getOne(lambdaQueryWrapper);
    }

}
