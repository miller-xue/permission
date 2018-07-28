package com.miller.controller;

import com.miller.common.PageQuery;
import com.miller.common.PageResult;
import com.miller.common.Result;
import com.miller.param.UserParam;
import com.miller.service.SysUserService;
import com.miller.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by miller on 2018/7/25
 *
 * @author Miller
 */
@RequestMapping("/sys/user")
@Controller
public class SysUserController {

    @Autowired
    private SysUserService userService;

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public Result save(UserParam param) {
        userService.save(param);
        return ResultUtil.buildSuccess();
    }


    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public Result update(UserParam param) {
        userService.update(param);
        return ResultUtil.buildSuccess();
    }

    @RequestMapping(value = "/page")
    @ResponseBody
    public Result<PageResult> page(@RequestParam("deptId") Integer deptId,
                                   PageQuery pageQuery) {
        return ResultUtil.buildSuccess(userService.getPageByDeptId(deptId, pageQuery));
    }
}
