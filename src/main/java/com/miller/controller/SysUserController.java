package com.miller.controller;

import com.google.common.collect.Maps;
import com.miller.common.PageQuery;
import com.miller.common.PageResult;
import com.miller.common.Result;
import com.miller.param.UserParam;
import com.miller.service.SysRoleService;
import com.miller.service.SysUserService;
import com.miller.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

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

    @Autowired
    private SysRoleService roleService;

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public Result save(UserParam param) {
        userService.save(param);
        return ResultUtil.buildSuccess();
    }

    @RequestMapping(value = "/noAuth")
    public String noAuth() {
        return "noAuth";
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

    /**
     * 根据角色id查看角色拥有的权限
     *
     * @param userId 角色id
     * @return 角色拥有的权限
     */
    @RequestMapping("/acls")
    @ResponseBody
    public Result acls(@RequestParam("userId") int userId) {
        // TODO 查询权限应该是有权限列表的权限模块,没有的不查询
        Map<String, Object> map = Maps.newHashMap();
        map.put("acls", userService.userAclTree(userId));
        map.put("roles", roleService.getRoleListByUserId(userId));
        return ResultUtil.buildSuccess(map);
    }
}
