package com.miller.interceptors;

import com.miller.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by miller on 2018/7/22
 */
@Slf4j
public class HttpIntercept extends HandlerInterceptorAdapter {
    /**
     * 请求前
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURL().toString();
        Map parameterMap = request.getParameterMap();
        log.info("request start  url:{}, param:{}", url, JsonMapper.obj2String(parameterMap));
        return true;
    }

    /**
     * 正常请求结束后
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        String url = request.getRequestURL().toString();
//        Map parameterMap = request.getParameterMap();
//        log.info("request postHandle  url:{}, param:{}", url, JsonMapper.obj2String(parameterMap));
    }

    /**
     * 所有情况结束后(包括异常)
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String url = request.getRequestURL().toString();
        Map parameterMap = request.getParameterMap();
        log.info("request Completion  url:{}, param:{}", url, JsonMapper.obj2String(parameterMap));
    }
}
