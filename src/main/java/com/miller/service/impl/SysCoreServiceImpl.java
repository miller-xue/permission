package com.miller.service.impl;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.miller.common.RequestHolder;
import com.miller.dao.SysAclMapper;
import com.miller.dao.SysAclModuleMapper;
import com.miller.dto.AclDto;
import com.miller.dto.AclModuleLevelDto;
import com.miller.model.SysAcl;
import com.miller.service.SysCoreService;
import com.miller.util.TreeBuilder;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by miller on 2018/7/31
 * @author Miller
 */
@Service
public class SysCoreServiceImpl implements SysCoreService {

    @Resource
    private SysAclMapper sysAclMapper;

    @Resource
    private SysAclModuleMapper sysAclModuleMapper;

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

    @Override
    public List<AclModuleLevelDto> aclListToTree(List<AclDto> aclDtoList) {
        if (CollectionUtils.isEmpty(aclDtoList)) {
            return Lists.newArrayList();
        }
        List<AclModuleLevelDto> aclModuleList = AclModuleLevelDto.adaptList(sysAclModuleMapper.getAllAclModule());


        Multimap<Integer, AclDto> moduleIdAclMap = ArrayListMultimap.create();
        for (AclDto aclDto : aclDtoList) {
            if (aclDto.getStatus() == 1) {
                moduleIdAclMap.put(aclDto.getAclModuleId(), aclDto);
            }
        }
        bindAclListWithOrder(aclModuleList, moduleIdAclMap);

        return TreeBuilder.makeTreeList(aclModuleList, "id", "parentId");
    }

    /**
     * 权限模块绑定权限
     * @param aclModuleList
     * @param moduleIdAclMap
     */
    public void bindAclListWithOrder(List<AclModuleLevelDto> aclModuleList, Multimap<Integer, AclDto> moduleIdAclMap) {
        if(CollectionUtils.isEmpty(aclModuleList)){
            return;
        }
        for (AclModuleLevelDto aclModuleLevelDto : aclModuleList) {
            List<AclDto> aclDtoList = (List<AclDto>) moduleIdAclMap.get(aclModuleLevelDto.getId());
            if (CollectionUtils.isNotEmpty(aclDtoList)) {
                Collections.sort(aclDtoList, aclSqlCompartor);
                aclModuleLevelDto.setAclList(aclDtoList);
            }
        }
    }


    public Comparator<AclDto> aclSqlCompartor = new Comparator<AclDto>() {
        @Override
        public int compare(AclDto o1, AclDto o2) {
            return o1.getSeq() - o2.getSeq();
        }
    };
}
