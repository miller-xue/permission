package com.miller.controller;

import com.miller.model.SysUser;
import com.miller.service.SysUserService;
import com.miller.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by miller on 2018/7/26
 */
@Controller
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private SysUserService userService;

    @RequestMapping(value = "/login")
    public String login(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String errorMsg = "";
        String ret = request.getParameter("ret");
        if (StringUtils.isBlank(username)) {
            errorMsg = "用户名不能为空";
        }
        if (StringUtils.isBlank(password)) {
            errorMsg = "密码不能为空";
        }

        SysUser sysUser = userService.findByKeyword(username);
        if (sysUser == null) {
            errorMsg = "查询不到指定用户";
        }
        if (!sysUser.getPassword().equals(MD5Util.encrypt(password))) {
            errorMsg = "用户名或密码错误";
        }


    }

}
