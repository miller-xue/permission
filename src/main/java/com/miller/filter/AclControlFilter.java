package com.miller.filter;

import com.google.common.base.Splitter;
import com.google.common.collect.Sets;
import com.miller.common.RequestHolder;
import com.miller.common.Result;
import com.miller.enums.result.SysResult;
import com.miller.handler.PermissionExceptionHandler;
import com.miller.model.SysUser;
import com.miller.service.SysCoreService;
import com.miller.util.JsonMapper;
import com.miller.util.ResultUtil;
import com.miller.util.SpringContextUtil;
import com.miller.util.SysUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by miller on 2018/8/4
 *
 * @author Miller
 */
@Slf4j

public class AclControlFilter implements Filter {

    private static Set<String> exclusionUrlSet = Sets.newConcurrentHashSet();

    public static final String noAuthUrl = "/sys/user/noAuth";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String exclusionUrls = filterConfig.getInitParameter("exclusionUrls");
        // 字符串转List
        List<String> exclusionUrlList = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(exclusionUrls);
        //        转set
        exclusionUrlSet = Sets.newConcurrentHashSet(exclusionUrlList);
        exclusionUrlSet.add(noAuthUrl);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        // 1.请求URL
        String servletPath = request.getServletPath();
        // 2.请求参数Map
        Map requestMap = request.getParameterMap();

        // 3.白名单处理逻辑
        if (exclusionUrlSet.contains(servletPath)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        // 从当前线程中取出用户
        SysUser sysUser = RequestHolder.getCurrentUser();
        if (sysUser == null) {
            log.info("someone visit {}, but no login, paramter:{}", servletPath, JsonMapper.obj2String(requestMap));
            noAuth(request, response);
            return;
        }

        // TODO
        SysCoreService sysCoreService = SpringContextUtil.popBean(SysCoreService.class);
        if (!sysCoreService.hasUrlAcl(servletPath)) {
            log.info("{} visit {}, but no login, paramter:{}", JsonMapper.obj2String(sysUser), servletPath, JsonMapper.obj2String(requestMap));
            noAuth(request, response);
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
        return;
    }

    private void noAuth(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String servletPath = request.getServletPath();
        // 判断是json还是页面请求
        if (SysUtil.isAjax(request)) {
            Result result = ResultUtil.buildFail(SysResult.NO_AUTH);
            response.setHeader("Content-Type", "application/json");
            response.getWriter().print(JsonMapper.obj2String(result));
        }else {
            clientRedirect(noAuthUrl, response);
        }
    }

    private void clientRedirect(String url, HttpServletResponse response) throws IOException {
        response.setHeader("Content-Type", "text/html");
        response.getWriter().print("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n"
                + "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" + "<head>\n" + "<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\"/>\n"
                + "<title>跳转中...</title>\n" + "</head>\n" + "<body>\n" + "跳转中，请稍候...\n" + "<script type=\"text/javascript\">//<![CDATA[\n"
                + "window.location.href='" + url + "?ret='+encodeURIComponent(window.location.href);\n" + "//]]></script>\n" + "</body>\n" + "</html>\n");

    }

    @Override
    public void destroy() {

    }
}
