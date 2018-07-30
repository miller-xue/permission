package com.miller.service;

import com.miller.model.SysAcl;

import java.util.List;

/**
 * Created by miller on 2018/7/30
 * 系统核心服务层
 * @author Miller
 */
public interface SysCoreService {

    /**
     * 获取当前用户的权限列表
     * @return
     */
    List<SysAcl> getCurrentUserAclList();

    /**
     * 获取当前角色的权限列表
     * @param roleId
     * @return
     */
    List<SysAcl> getRoleAclList(int roleId);

    /**
     * 获取当前用户的权限列表
     * @param userId
     * @return
     */
    List<SysAcl> getUserAclList(int userId);

    boolean isSuperAdmin();
}
