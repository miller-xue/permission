package com.miller.controller;

import com.miller.common.Result;
import com.miller.model.SysRole;
import com.miller.param.RoleParam;
import com.miller.service.SysRoleService;
import com.miller.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by miller on 2018/7/29
 * @author Miller
 */
@Controller
@RequestMapping("/sys/role")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @RequestMapping(value = "/page",method = RequestMethod.GET)
    public String role() {
        return "role";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public Result save(RoleParam param) {
        sysRoleService.save(param);
        return ResultUtil.buildSuccess();
    }


    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public Result update(RoleParam param) {
        sysRoleService.update(param);
        return ResultUtil.buildSuccess();
    }
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Result<List<SysRole>> list() {
        return ResultUtil.buildSuccess(sysRoleService.getAll());
    }
}
