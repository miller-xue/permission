package com.miller.controller;

import com.miller.model.SysUser;
import com.miller.service.SysUserService;
import com.miller.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by miller on 2018/7/26
 *
 * @author Miller
 */
@Controller
@RequestMapping
public class UserController {

    @Autowired
    private SysUserService userService;

    @RequestMapping(value = "/login")
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        SysUser sysUser = userService.findByKeyword(username);
        String errorMsg = "";
        String ret = request.getParameter("ret");

        if (StringUtils.isBlank(username)) {
            errorMsg = "用户名不能为空";
        } else if (StringUtils.isBlank(password)) {
            errorMsg = "密码不能为空";
        } else if (sysUser == null) {
            errorMsg = "查询不到指定用户";
        } else if (!sysUser.getPassword().equals(MD5Util.encrypt(password))) {
            errorMsg = "用户名或密码错误";
        } else if (sysUser.getStatus() != 1) {
            errorMsg = "用户已被冻结,请联系管理员";
        } else {
            request.getSession().setAttribute("user", sysUser);
            if (StringUtils.isNotBlank(ret)) {
                response.sendRedirect(ret);
            } else {
                response.sendRedirect("/admin/index");
                return;
            }
        }

        request.setAttribute("error", errorMsg);
        request.setAttribute("username", username);

        if (StringUtils.isNotBlank(ret)) {
            request.setAttribute("ret", ret);
        }
        request.getRequestDispatcher("signin.jsp").forward(request, response);
    }

    @RequestMapping(value = "/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getSession().invalidate();
        response.sendRedirect("signin.jsp") ;
    }
}
