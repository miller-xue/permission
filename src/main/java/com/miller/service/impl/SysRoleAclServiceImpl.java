package com.miller.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.miller.common.RequestHolder;
import com.miller.dao.SysRoleAclMapper;
import com.miller.model.SysRoleAcl;
import com.miller.service.SysRoleAclService;
import com.miller.util.IpUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by miller on 2018/7/29
 * @author Miller
 */
@Service
public class  SysRoleAclServiceImpl implements SysRoleAclService {

    @Resource
    private SysRoleAclMapper sysRoleAclMapper;

    @Override
    public void changeRoleAcls(int roleId, List<Integer> aclIdList) {
        List<Integer> originAclIdList = sysRoleAclMapper.selectAclIdListByRoleIdList(Lists.newArrayList(roleId));
        // 判断数据库权限和新增的权限是否相等
        if (aclIdList.size() == originAclIdList.size()) {
            Set<Integer> originAclIdSet = Sets.newHashSet(originAclIdList);
            Set<Integer> aclIdSet = Sets.newHashSet(aclIdList);
            originAclIdSet.removeAll(aclIdList);
            if (CollectionUtils.isEmpty(originAclIdSet)) {
                return;
            }
        }
        updateRoleAcls(roleId, aclIdList);
    }

    @Transactional
    public void updateRoleAcls(int roleId, List<Integer> aclIdList) {
        sysRoleAclMapper.deleteByRoleId(roleId);
        // 权限id 为空 删除所有权限
        if (CollectionUtils.isEmpty(aclIdList)) {
            return;
        }
        List<SysRoleAcl> roleAclList = Lists.newArrayList();
        for (Integer aclId : aclIdList  ) {
            roleAclList.add(SysRoleAcl.builder().aclId(aclId).roleId(roleId)
                    .operator(RequestHolder.getCurrentUser().getUsername())
                    .operatorIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()))
                    .operatorTime(new Date()).build());
        }
        sysRoleAclMapper.batchInsert(roleAclList);
    }
}
