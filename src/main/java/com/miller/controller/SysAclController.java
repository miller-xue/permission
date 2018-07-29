package com.miller.controller;

import com.miller.common.PageQuery;
import com.miller.common.Result;
import com.miller.param.AclParam;
import com.miller.service.SysAclService;
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
 * @author Miller
 */
@Controller
@RequestMapping("/sys/acl")
@Slf4j
public class SysAclController {

    @Autowired
    private SysAclService aclService;


    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public Result save(AclParam param) {
        aclService.save(param);
        return ResultUtil.buildSuccess();
    }


    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public Result update(AclParam param) {
        aclService.update(param);
        return ResultUtil.buildSuccess();
    }

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    @ResponseBody
    public Result page(@RequestParam("aclModuleId") Integer aclModuleId, PageQuery pageQuery) {
        return ResultUtil.buildSuccess(aclService.getPageByAclModuleId(aclModuleId, pageQuery));
    }



}
