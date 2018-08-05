package com.miller.service.impl;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.miller.common.RequestHolder;
import com.miller.constant.CatchKeyConstants;
import com.miller.dao.SysAclMapper;
import com.miller.dao.SysAclModuleMapper;
import com.miller.dto.AclDto;
import com.miller.dto.AclModuleLevelDto;
import com.miller.model.SysAcl;
import com.miller.service.SysCatchService;
import com.miller.service.SysCoreService;
import com.miller.util.JsonMapper;
import com.miller.util.StringUtil;
import com.miller.util.TreeBuilder;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by miller on 2018/7/31
 * 系统核心服务层
 * @author Miller
 */
@Service
public class SysCoreServiceImpl implements SysCoreService {

    @Resource
    private SysAclMapper sysAclMapper;

    @Resource
    private SysAclModuleMapper sysAclModuleMapper;

    @Resource
    private SysCatchService catchService;

    @Override
    public List<SysAcl> getCurrentUserAclList() {
        // 1.从当前线程Holder中取出当前登陆用户
        int userId = RequestHolder.getCurrentUser().getId();
        return getUserAclList(userId);
    }

    @Override
    public List<SysAcl> getUserAclList(int userId) {
        //1. 如果用户是超级管理员,取出来所有acl数据
        if (isSuperAdmin()) {
            return sysAclMapper.selectAll();
        }
        // sql 内解决查询多个表问题
        return sysAclMapper.selectAclListByUserId(userId);
    }

    @Override
    public List<SysAcl> getRoleAclList(int roleId) {
        return sysAclMapper.selectAclListByRoleId(roleId);
    }


    @Override
    public boolean isSuperAdmin() {
        //TODO 自己写核心逻辑
        return true;
    }

    @Override
    public List<AclModuleLevelDto> aclListToTree(List<AclDto> aclDtoList) {
        // 1.判断权限是否为空, 权限为空权限树无意义
        if (CollectionUtils.isEmpty(aclDtoList)) {
            return Lists.newArrayList();
        }
        // 2.查询出所有权限模块列表
        List<AclModuleLevelDto> aclModuleList = AclModuleLevelDto.adaptList(sysAclModuleMapper.getAllAclModule());

        // 3.把权限列表 转换成 模块id ：{权限,权限....}结构
        Multimap<Integer, AclDto> moduleIdAclMap = ArrayListMultimap.create();
        for (AclDto aclDto : aclDtoList) {
            if (aclDto.getStatus() == 1) {
                moduleIdAclMap.put(aclDto.getAclModuleId(), aclDto);
            }
        }
        // 4.给权限模块绑定权限点
        bindAclListWithOrder(aclModuleList, moduleIdAclMap);
        // 拼装成树
        return TreeBuilder.makeTreeList(aclModuleList, "id", "parentId");
    }


    /**
     * 权限模块绑定权限
     *
     * @param aclModuleList
     * @param moduleIdAclMap
     */
    public void bindAclListWithOrder(List<AclModuleLevelDto> aclModuleList, Multimap<Integer, AclDto> moduleIdAclMap) {
        if (CollectionUtils.isEmpty(aclModuleList)) {
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


    /**
     * 扩展点 用户部门权限
     *
     * @param url
     * @return
     */
    @Override
    public boolean hasUrlAcl(String url) {
        // 超级管理员拥有所有权限
        if (isSuperAdmin()) {
            return true;
        }
        // 如果未空代表权限未曾管理这个url
        List<SysAcl> aclList = sysAclMapper.selectByUrl(url);
        if (CollectionUtils.isEmpty(aclList)) {
            return true;
        }
        // 当前用户权限列表（应该登陆时候放在session中）
        Set<Integer> userAclIdSet = getCurrentUserAclListFormCatch().stream().map(acl -> acl.getId()).collect(Collectors.toSet());

        // 规制：只要有一个权限点有权限,那么我们就认为有访问权限
        boolean hasValidAcl = false;
        for (SysAcl acl : aclList) {
            //判断一个用户是否具有某个权限点的访问权限
            if (acl == null || acl.getStatus() != 1) { // 权限点无效
                continue;
            }
            hasValidAcl = true;
            if (userAclIdSet.contains(acl.getId())) {
                return true;
            }
        }
        if (!hasValidAcl) {
            return true;
        }
        return false;
    }

    public List<SysAcl> getCurrentUserAclListFormCatch() {
        int userId = RequestHolder.getCurrentUser().getId();
        String cacheValue = catchService.getFromCache(CatchKeyConstants.USER_ACLS, String.valueOf(userId));
        if (StringUtils.isBlank(cacheValue)) {
            List<SysAcl> aclList = getCurrentUserAclList();
            if (CollectionUtils.isNotEmpty(aclList)) {
                catchService.saveCatche(JsonMapper.obj2String(aclList), 10000, CatchKeyConstants.USER_ACLS, String.valueOf(userId));
            }
            return aclList;
        }
        return JsonMapper.string2Obj(cacheValue, new TypeReference<List<SysAcl>>() {
        });
    }
}