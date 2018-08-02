package com.miller.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.miller.common.Result;
import com.miller.model.SysRole;
import com.miller.model.SysUser;
import com.miller.param.RoleParam;
import com.miller.service.SysRoleAclService;
import com.miller.service.SysRoleService;
import com.miller.service.SysRoleUserService;
import com.miller.service.SysUserService;
import com.miller.util.ResultUtil;
import com.miller.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by miller on 2018/7/29
 * 系统角色控制层
 *
 * @author Miller
 */
@Controller
@RequestMapping("/sys/role")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysRoleAclService sysRoleAclService;

    @Autowired
    private SysRoleUserService sysRoleUserService;


    @Autowired
    private SysUserService sysUserService;

    /**
     * 角色页面
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public String role() {
        return "role";
    }

    /**
     * 保存一个角色
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public Result save(RoleParam param) {
        sysRoleService.save(param);
        return ResultUtil.buildSuccess();
    }

    /**
     * 更新一个角色
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public Result update(RoleParam param) {
        sysRoleService.update(param);
        return ResultUtil.buildSuccess();
    }

    /**
     * 角色列表 JSON 返回
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Result<List<SysRole>> list() {
        return ResultUtil.buildSuccess(sysRoleService.getAll());
    }

    /**
     * 角色权限树
     *
     * @param roleId
     * @return
     */
    @RequestMapping(value = "/roleTree", method = RequestMethod.POST)
    @ResponseBody
    public Result roleTree(@RequestParam("roleId") int roleId) {
        return ResultUtil.buildSuccess(sysRoleService.roleTree(roleId));
    }

    /**
     * 修改权限
     *
     * @param roleId
     * @param aclIds
     * @return
     */
    @RequestMapping(value = "/changeAcls", method = RequestMethod.POST)
    @ResponseBody
    public Result changeAcls(@RequestParam("roleId") int roleId,
                             @RequestParam(value = "aclIds", required = false, defaultValue = "") String aclIds) {
        List<Integer> aclIdList = StringUtil.splitToListInt(aclIds);
        sysRoleAclService.changeRoleAcls(roleId, aclIdList);
        return ResultUtil.buildSuccess();
    }


    @RequestMapping("/users")
    @ResponseBody
    public Result users(@RequestParam("roleId") int roleId) {
        List<SysUser> selectedUserList = sysRoleUserService.getListByRoleId(roleId);
        List<SysUser> allUserList = sysUserService.getAll();

        List<SysUser> unselectedUserList = Lists.newArrayList();
        // 选中角色的id Set集合
        Set<Integer> selectedUserIdSet = selectedUserList.stream().map(sysUser -> sysUser.getId()).collect(Collectors.toSet());
        for (SysUser sysUser : allUserList) {
            if (sysUser.getStatus() == 1 && !selectedUserIdSet.contains(sysUser.getId())) {
                unselectedUserList.add(sysUser);
            }
        }
        Map<String, List<SysUser>> result = Maps.newHashMap();
        result.put("selected", selectedUserList);
        result.put("unselected", unselectedUserList);

        return ResultUtil.buildSuccess(result);
    }
}
