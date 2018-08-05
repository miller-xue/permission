package com.miller.controller;

import com.miller.common.Result;
import com.miller.param.AclModuleParam;
import com.miller.service.SysAclModuleService;
import com.miller.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by miller on 2018/7/28
 * 权限模块Controller
 * @author Miller
 */
@Controller
@RequestMapping("/sys/aclModule")
@Slf4j
public class SysAclModuleController {

    @Autowired
    private SysAclModuleService sysAclModuleService;


    @RequestMapping("/page")
    public String page() {
        return "acl";
    }

    /**
     * 权限模块树
     * @return 权限模块树
     */
    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    @ResponseBody
    public Result tree() {
        return ResultUtil.buildSuccess(sysAclModuleService.aclModuleTree());
    }


    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public Result save(AclModuleParam param) {
        sysAclModuleService.save(param);
        return ResultUtil.buildSuccess();
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public Result update(AclModuleParam param) {
        sysAclModuleService.update(param);
        return ResultUtil.buildSuccess();
    }

    @RequestMapping(value = "/delete")
    @ResponseBody
    public Result delete(@RequestParam("id") int id) {
        sysAclModuleService.delete(id);
        return ResultUtil.buildSuccess();
    }


}
