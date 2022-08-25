package com.fanfun.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fanfun.domain.ResponseResult;
import com.fanfun.domain.vo.PageVo;
import com.fanfun.entity.Blog;
import com.fanfun.entity.Type;
import com.fanfun.mapper.TypeMapper;
import com.fanfun.service.BlogService;
import com.fanfun.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * (Type)表服务实现类
 *
 * @author makejava
 * @since 2022-03-27 11:30:31
 */
@Service("typeService")
public class TypeServiceImpl extends ServiceImpl<TypeMapper, Type> implements TypeService {

    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;

//    list(new LambdaQueryWrapper<Blog>().eq(Blog::getTypeId,type.getId())))

    @Override
    public ResponseResult listType() {
        List<Type> typeList = list();
        typeList.stream().forEach(type -> type.setBlogs(blogService.list(new LambdaQueryWrapper<Blog>().eq(Blog::getTypeId,type.getId()))));
        return ResponseResult.okResult(typeList);
    }

    @Override
    public ResponseResult listType(Integer pageNum) {
        if (pageNum == null || pageNum == 0) {
            pageNum = 1;
        }
        Page page = new Page(pageNum, 6);
        List<Type> records = typeService.page(page).getRecords();
        PageVo pageVo = new PageVo(records, page.getTotal(), page.getCurrent(), page.getPages());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public Type getTypeByName(String name) {
        LambdaQueryWrapper<Type> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Type::getName,name);
        return getOne(lambdaQueryWrapper);
    }
}
