package com.miller.controller;

import com.miller.common.PageQuery;
import com.miller.common.Result;
import com.miller.param.SearchLogParam;
import com.miller.service.SysLogService;
import com.miller.util.ResultUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by miller on 2018/8/5
 * @author Miller
 */
@Controller
@RequestMapping("/sys/log")
public class SysLogController {

    @Resource
    private SysLogService sysLogService;

    @RequestMapping("/log")
    public String page() {
        return "log";
    }

    @RequestMapping(value = "/page")
    @ResponseBody
    public Result searchPage(SearchLogParam param, PageQuery page) {
        return ResultUtil.buildSuccess(sysLogService.searchPageList(param, page));
    }

    @RequestMapping(value = "/recover")
    @ResponseBody
    public Result recover(@RequestParam("id") int logId) {
        sysLogService.recover(logId);
        return ResultUtil.buildSuccess();
    }
}
