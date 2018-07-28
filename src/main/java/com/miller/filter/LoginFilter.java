package com.miller.filter;

import com.miller.common.RequestHolder;
import com.miller.model.SysUser;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by miller on 2018/7/28
 * @author Miller
 */
@Slf4j
public class LoginFilter implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        SysUser user = (SysUser) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect("signin.jsp");
        }
        RequestHolder.add(user);
        RequestHolder.add(req);
        filterChain.doFilter(servletRequest, servletResponse);
        return;
    }

    public void destroy() {

    }
}
