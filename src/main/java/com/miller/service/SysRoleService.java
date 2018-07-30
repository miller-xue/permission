package com.miller.service;

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
}
