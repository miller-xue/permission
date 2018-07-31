package com.miller.service.impl;

import com.miller.common.RequestHolder;
import com.miller.dao.SysAclMapper;
import com.miller.model.SysAcl;
import com.miller.service.SysCoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by miller on 2018/7/31
 * @author Miller
 */
@Service
public class SysCoreServiceImpl implements SysCoreService {

    @Resource
    private SysAclMapper sysAclMapper;

    @Override
    public List<SysAcl> getCurrentUserAclList() {
        int userId = RequestHolder.getCurrentUser().getId();
        return getUserAclList(userId);
    }

    @Override
    public List<SysAcl> getRoleAclList(int roleId) {
        return sysAclMapper.selectAclListByRoleId(roleId);
    }

    @Override
    public List<SysAcl> getUserAclList(int userId) {
        if (isSuperAdmin()) {
            //1. 如果用户是超级管理员,取出来所有acl数据
            return sysAclMapper.selectAll();
        }
        return sysAclMapper.selectAclListByUserId(userId);
    }

    @Override
    public boolean isSuperAdmin() {
        return true;
    }
}
