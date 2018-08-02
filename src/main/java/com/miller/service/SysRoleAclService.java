package com.miller.service;

import java.util.List;

/**
 * Created by miller on 2018/7/29
 * @author Miller
 */
public interface SysRoleAclService {

    /**
     * 修改角色的权限列表
     * @param roleId
     * @param aclIdList
     */
    void changeRoleAcls(int roleId, List<Integer> aclIdList);
}
