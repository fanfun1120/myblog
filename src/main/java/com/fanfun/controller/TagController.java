package com.fanfun.controller;

import com.fanfun.entity.Tag;
import com.fanfun.service.BlogService;
import com.fanfun.service.BlogTagsService;
import com.fanfun.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.util.annotation.Nullable;

import java.util.Comparator;
import java.util.List;

@Controller
public class TagController {

    @Autowired
    private TagService tagService;

    @Autowired
    private BlogTagsService blogTagsService;

    @GetMapping("/tags/{id}")
    public String tags(@RequestParam("page") @Nullable Integer pageNum,
                       @PathVariable Long id, Model model) {
        List<Tag> tags = (List<Tag>) tagService.listTag().getData();
        tags.sort(new Comparator<Tag>() {
            @Override
            public int compare(Tag o1, Tag o2) {
                return o2.getBlogs().size() - o1.getBlogs().size();
            }
        });
        if (id == -1) {
            id = tags.get(0).getId();
        }
        model.addAttribute("tags", tags);
        model.addAttribute("page", blogTagsService.getBlogByTagId(id,pageNum));
        model.addAttribute("activeTagId", id);
        return "tags";
    }

}
