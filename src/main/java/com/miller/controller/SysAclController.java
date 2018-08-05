package com.miller.controller;

import com.google.common.collect.Maps;
import com.miller.common.PageQuery;
import com.miller.common.Result;
import com.miller.param.AclParam;
import com.miller.service.SysAclService;
import com.miller.service.SysRoleService;
import com.miller.service.SysUserService;
import com.miller.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by miller on 2018/7/28
 * 权限Controller
 * @author Miller
 */
@Controller
@RequestMapping("/sys/acl")
@Slf4j
public class SysAclController {

    @Autowired
    private SysAclService aclService;

    @Autowired
    private SysRoleService roleService;

    @Autowired
    private SysUserService sysUserService;


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

    /**
     * 权限点分页json查询
     * @param aclModuleId
     * @param pageQuery
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    @ResponseBody
    public Result page(@RequestParam("aclModuleId") Integer aclModuleId, PageQuery pageQuery) {
        return ResultUtil.buildSuccess(aclService.getPageByAclModuleId(aclModuleId, pageQuery));
    }


    /**
     * 查看拥有指定权限的角色和用户列表
     *
     * @param aclId 权限id
     * @return 角色拥有的权限
     */
    @RequestMapping("/acls")
    @ResponseBody
    public Result acls(@RequestParam("aclId") int aclId) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("roles", roleService.getRoleListByAclId(aclId));
        map.put("users", sysUserService.getListByAclId(aclId));
        return ResultUtil.buildSuccess(map);
    }


}
