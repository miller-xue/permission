package com.miller.service.impl;

import com.google.common.collect.Lists;
import com.miller.common.RequestHolder;
import com.miller.dao.SysAclMapper;
import com.miller.dao.SysRoleAclMapper;
import com.miller.dao.SysRoleMapper;
import com.miller.dao.SysRoleUserMapper;
import com.miller.model.SysAcl;
import com.miller.service.SysCoreService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by miller on 2018/7/30
 *
 * @author Miller
 */
@Deprecated
public class SysCoreServiceImplDemo implements SysCoreService {

    @Resource
    private SysAclMapper sysAclMapper;

    @Resource
    private SysRoleUserMapper sysRoleUserMapper;

    @Resource
    private SysRoleAclMapper sysRoleAclMapper;

    @Override
    public List<SysAcl> getCurrentUserAclList() {
        // 1.从RequestHolder中取出当前用户id
        int userId = RequestHolder.getCurrentUser().getId();
        return getUserAclList(userId);
    }

    @Override
    public List<SysAcl> getRoleAclList(int roleId) {
        // 查询角色拥有的权限
        List<Integer> aclIdList = sysRoleAclMapper.selectAclIdListByRoleIdList(Lists.<Integer>newArrayList(roleId));
        if (CollectionUtils.isEmpty(aclIdList)) {
            return Lists.newArrayList();
        }
        return sysAclMapper.selectByIdList(aclIdList);
    }

    @Override
    public List<SysAcl> getUserAclList(int userId) {
        if (isSuperAdmin()) {
            //1. 如果用户是超级管理员,取出来所有acl数据
            return sysAclMapper.selectAll();
        }
        // 查询当前用户分配的所有角色Id
        List<Integer> userRoleList = sysRoleUserMapper.selectRoleIdListByUserId(userId);
        if (CollectionUtils.isEmpty(userRoleList)) {
            return Lists.newArrayList();
        }
        List<Integer> userAclIdList = sysRoleAclMapper.selectAclIdListByRoleIdList(userRoleList);
        if (CollectionUtils.isEmpty(userAclIdList)) {
            return Lists.newArrayList();
        }
        return sysAclMapper.selectByIdList(userAclIdList);
    }

    @Override
    public boolean isSuperAdmin() {
        return true;
    }


}
