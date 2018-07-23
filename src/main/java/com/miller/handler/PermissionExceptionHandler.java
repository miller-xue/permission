package com.miller.handler;

import com.miller.Exception.JsonException;
import com.miller.Exception.PageException;
import com.miller.Exception.ParamException;
import com.miller.common.Result;
import com.miller.constant.SysConstans;
import com.miller.enums.ResultEnum;
import com.miller.util.ResultUtil;
import com.miller.util.SysUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by miller on 2018/7/21
 * @author Miller
 * 系统异常处理类
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
    @ExceptionHandler(value = JsonException.class)
    @ResponseBody
    public Result handlerPermissionJsonException(JsonException e, HttpServletRequest request) {
        log.error("json exception, url:" + request.getRequestURL().toString(), e);
        // 把错误信息和错误码返回调用者
        return ResultUtil.buildFail(e.getMessage(),e.getCode());
    }


    /**
     * 对page异常的统一处理
     *
     * @param e
     * @param model
     * @return
     */
    @ExceptionHandler(value = PageException.class)
    public String handlerPermissionPageException(PageException e, Model model, HttpServletRequest request) {
        model.addAttribute(SysConstans.EXCEPTION_MESSAGE_KEY, e.getMessage());
        model.addAttribute(SysConstans.EXCEPTION_CODE_KEY, e.getCode());
        log.error("page exception, url:" + request.getRequestURL().toString(), e);
        return "exception";
    }

    /**
     * 参数异常的处理
     *
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(value = ParamException.class)
    @ResponseBody
    public Result handleParamException(ParamException e) {
        return ResultUtil.buildFail(e.getData(), e.getMessage(), e.getCode());
    }

    /**
     *
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    public ModelAndView handleException(Exception e, HttpServletRequest request) {
        ModelAndView modelAndView = null;
        String viewName = "exception";
        if (isAjax(request)) {
            viewName = SysConstans.JSON_VIEW_NAME;
        }
        log.error("exception:{} ",e);
        return new ModelAndView(viewName, SysUtil.object2Map(ResultUtil.buildFail(ResultEnum.INNER_ERROR)));
    }


    private static boolean isAjax(HttpServletRequest request) {
        String xReq = request.getHeader("x-requested-with");
        if (xReq != null && !xReq.trim().equals("") && "XMLHttpRequest".equalsIgnoreCase(xReq)) {
            return true;
        }
        return false;
    }
}
