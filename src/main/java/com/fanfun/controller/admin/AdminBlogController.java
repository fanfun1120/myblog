package com.fanfun.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fanfun.entity.Blog;
import com.fanfun.entity.BlogTags;
import com.fanfun.entity.User;
import com.fanfun.service.*;
import com.fanfun.util.RedisCache;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import reactor.util.annotation.Nullable;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminBlogController {
    private static final String INPUT = "admin/blogs-input";
    private static final String LIST = "admin/blogs";
    private static final String REDIRECT_LIST = "redirect:/admin/blogs";

    @Autowired
    private BlogTagsService blogTagsService;
    @Autowired
    private UserService userService;
    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;
    @Autowired
    private RedisCache redisCache;

    @GetMapping("/cache")
    public String cache() {
        String pattern = "getIndexPage:*";
        for (String key : redisCache.keys(pattern)) {
            redisCache.deleteObject(key);
        }
        return "redirect:/admin";
    }

    @GetMapping("/blogs")
    public String blogs(@RequestParam("page") @Nullable Integer pageNum, Model model) {
        model.addAttribute("types", typeService.listType());
        model.addAttribute("page", blogService.getAllBlogs(pageNum));
        return LIST;
    }

    @GetMapping("/blogs/input")
    public String input(Model model) {
        setTypeAndTag(model);
        model.addAttribute("blog", new Blog());
        return INPUT;
    }

    private void setTypeAndTag(Model model) {
        model.addAttribute("types", typeService.listType());
        model.addAttribute("tags", tagService.listTag());
    }

    @GetMapping("/blogs/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {
        setTypeAndTag(model);
        Blog blog = blogService.getById(id);
        blog.setFlag("");
        blog.setUser(userService.getById(blog.getUserId()));
        blog.setType(typeService.getById(blog.getTypeId()));
        blog.setTags(blogTagsService.getTagByBlogId(blog.getId()));
        blog.init();
        model.addAttribute("blog", blog);
        return INPUT;
    }

    @GetMapping("/blogs/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("message", "删除成功");
        return REDIRECT_LIST;
    }

    @PostMapping("/blogs")
    public String post(Blog blog, RedirectAttributes attributes, HttpSession session) {
        long blogId = UUID.randomUUID().toString().replace("-", "").hashCode();
        long id = blogId < 0 ? -blogId : blogId;
        Date date = new Date();
        blog.setUserId(1l);
        if (blog.getViews() == null) {
            blog.setViews(0);
        }
        blog.setTypeId(blog.getType().getId());
        blog.setCommentabled("0");
        blog.setUpdateTime(date);
        blog.setFlag(blog.getFlag().replace(",", ""));
        if (blog.getRecommend() == null || blog.getRecommend().equals("false")) {
            blog.setRecommend("0");
        } else {
            blog.setRecommend("1");
        }
        String tagIds = blog.getTagIds();
        String[] ids = tagIds.split(",");
        LambdaQueryWrapper<BlogTags> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 意味着新加入
        if (blog.getId() == null) {
            blog.setId(id);
            blog.setCreateTime(date);
            lambdaQueryWrapper.eq(BlogTags::getBlogsId, id);
            blogTagsService.remove(lambdaQueryWrapper);
            for (int i = 0; i < ids.length; i++) {
                blogTagsService.save(new BlogTags(id, Long.parseLong(ids[i])));
            }
        } else {
            // 更新
            Blog dataBaseBlog = blogService.getById(blog.getId());
            blog.setCreateTime(dataBaseBlog.getCreateTime());
            lambdaQueryWrapper.eq(BlogTags::getBlogsId, blog.getId());
            blogTagsService.remove(lambdaQueryWrapper);
            for (int i = 0; i < ids.length; i++) {
                blogTagsService.save(new BlogTags(blog.getId(), Long.parseLong(ids[i])));
            }

        }
        Boolean b = blogService.saveOrUpdate(blog);


        if (b) {
            attributes.addFlashAttribute("message", "操作成功");
        } else {
            attributes.addFlashAttribute("message", "操作失败");
        }
        return REDIRECT_LIST;
    }

}
