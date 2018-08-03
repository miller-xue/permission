package com.miller.service;

import com.miller.dto.AclDto;
import com.miller.dto.AclModuleLevelDto;
import com.miller.model.SysRole;
import com.miller.param.RoleParam;

import java.util.List;

/**
 * Created by miller on 2018/7/29
 * @author Miller
 */
public interface SysRoleService {

    /**
     * 保存一个角色
     * @param param
     */
    void save(RoleParam param);

    /**
     * 更新一个角色
     * @param param
     */
    void update(RoleParam param);

    /**
     * 获取所有角色
     *
     * @return
     */
    List<SysRole> getAll();

    /**
     * 根据角色获取权限模块树
     * @param roleId
     * @return
     */
    List roleTree(int roleId);


    /**
     * 根据用户id获取角色列表
     * @param userId 用户id
     * @return 角色列表
     */
    List<SysRole> getRoleListByUserId(int userId);

    /**
     * 查询有指定权限的角色列表
     * @param aclId 权限id
     * @return 拥有的用户列表
     */
    List<SysRole> getRoleListByAclId(int aclId);
}
