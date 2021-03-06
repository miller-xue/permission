package com.miller.service;

import com.miller.dto.AclDto;
import com.miller.dto.AclModuleLevelDto;
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

    /**
     * 是不是超级管理员
     * @return
     */
    boolean isSuperAdmin();


    /**
     * 拼接AclModule acl 拼接成树
     * @param aclDtoList
     * @return
     */
    List<AclModuleLevelDto> aclListToTree(List<AclDto> aclDtoList);

    /**
     * 判断这个url是否有权限
     * @param url
     * @return
     */
    boolean hasUrlAcl(String url);
}
