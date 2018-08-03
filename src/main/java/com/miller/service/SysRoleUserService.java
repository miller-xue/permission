package com.miller.service;

import com.miller.model.SysUser;

import java.util.List;

/**
 * Created by miller on 2018/8/2
 *
 * @author Miller
 */
public interface SysRoleUserService {

    /**
     * 根据角色id查询出所有管理的用户
     * @param roleId
     * @return
     */
    List<SysUser> getListByRoleId(int roleId);


    /**
     * 修改角色关联的用户列表
     * @param roleId 角色id
     * @param userIdList 角色关联的用户列表
     */
    void changeRoleUsers(int roleId, List<Integer> userIdList);

}
