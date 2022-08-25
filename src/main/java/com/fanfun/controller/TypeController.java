package com.fanfun.controller;

import com.fanfun.service.BlogService;
import com.fanfun.service.TagService;
import com.fanfun.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.util.annotation.Nullable;

@Controller
public class TypeController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @GetMapping("/types/{id}")
    public String types(@PathVariable Long id, Model model, @RequestParam("page") @Nullable Integer pageNum) {
        model.addAttribute("page", blogService.getBlogs(pageNum, id));
        model.addAttribute("types", typeService.listType());
        model.addAttribute("activeTypeId", id);
        return "types";
    }


}
