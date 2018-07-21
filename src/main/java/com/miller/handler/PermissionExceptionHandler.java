package com.miller.handler;

import com.miller.Exception.PermissionJsonException;
import com.miller.Exception.PermissionPageException;
import com.miller.common.Result;
import com.miller.constant.SysConstans;
import com.miller.util.Object2Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by miller on 2018/7/21
 */
@ControllerAdvice
@Slf4j
public class PermissionExceptionHandler {

    /**
     * 对json异常的统一处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = PermissionJsonException.class)
    @ResponseBody
    public Result handlerPermissionJsonException(PermissionJsonException e, HttpServletRequest request) {
        log.error("json exception, url:" + request.getRequestURL().toString(), e);
        return Result.buildFail(e.getMessage());
    }


    /**
     * 对page异常的统一处理
     *
     * @param e
     * @param model
     * @return
     */
    @ExceptionHandler(value = PermissionPageException.class)
    public String handlerPermissionPageException(PermissionPageException e, Model model, HttpServletRequest request) {
        model.addAttribute(SysConstans.EXCEPTION_MESSAGE_KEY, e.getMessage());
        log.error("page exception, url:" + request.getRequestURL().toString(), e);
        return "exception";
    }


    @ExceptionHandler(value = Exception.class)
    public ModelAndView handleException(Exception e, HttpServletRequest request) {
        ModelAndView modelAndView = null;
        String viewName = "exception";
        if (isAjax(request)) {
            viewName = SysConstans.JSON_VIEW_NAME;
        }
        return new ModelAndView(viewName, Object2Map.object2Map(Result.buildFail(e.getMessage())));
    }


    private static boolean isAjax(HttpServletRequest request) {
        String xReq = request.getHeader("x-requested-with");
        if (xReq != null && !xReq.trim().equals("") && "XMLHttpRequest".equalsIgnoreCase(xReq)) {
            return true;
        }
        return false;
    }
}
