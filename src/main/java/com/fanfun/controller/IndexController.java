package com.fanfun.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fanfun.domain.ResponseResult;
import com.fanfun.entity.User;
import com.fanfun.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.util.annotation.Nullable;

@Controller
public class IndexController {


    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    @GetMapping("/")
    public String getAllData(Model model, @RequestParam("page") @Nullable Integer pageNum) {
        model.addAttribute("page", blogService.getBlogs(pageNum));
        model.addAttribute("types", typeService.listType());
        model.addAttribute("tags", tagService.listTag());
        model.addAttribute("recommendBlogs", blogService.listRecommendBlogTop(4));
        return "index";
    }

    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Long id,Model model){
        model.addAttribute("blog", blogService.getAndConvertBlog(id));
        return "blog";
    }

    @PostMapping("/search")
    public String search(@RequestParam String query, Model model,@RequestParam("page") @Nullable Integer pageNum) {
        model.addAttribute("page", blogService.listBlog(query, pageNum));
        model.addAttribute("query", query);
        return "search";
    }

}
