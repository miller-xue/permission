package com.miller.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by miller on 2018/7/27
 * 跳转首页Controller
 * @author Miller
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @RequestMapping("/index")
    public String index() {
        return "admin";
    }
}
