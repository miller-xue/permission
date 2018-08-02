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

}
