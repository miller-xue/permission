package com.miller.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.miller.common.RequestHolder;
import com.miller.constant.LogType;
import com.miller.dao.SysLogMapper;
import com.miller.dao.SysRoleAclMapper;
import com.miller.model.SysLogWithBLOBs;
import com.miller.model.SysRoleAcl;
import com.miller.service.SysLogService;
import com.miller.service.SysRoleAclService;
import com.miller.util.IpUtil;
import com.miller.util.JsonMapper;
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

    @Resource
    private SysLogMapper sysLogMapper;

    @Override
    @Transactional
    public void changeRoleAcls(int roleId, List<Integer> aclIdList) {
        // 1.查询当前角色拥有的权限id列表
        List<Integer> originAclIdList = sysRoleAclMapper.selectAclIdListByRoleIdList(Lists.newArrayList(roleId));

        // 2.判断数据库权限和新增的权限是否相等
        if (aclIdList.size() == originAclIdList.size()) {
            // 拥有的权限点列表
            Set<Integer> originAclIdSet = Sets.newHashSet(originAclIdList);
            // 修改后的权限点列表
            Set<Integer> aclIdSet = Sets.newHashSet(aclIdList);
            // removeAll操作
            originAclIdSet.removeAll(aclIdSet);
            if (CollectionUtils.isEmpty(originAclIdSet)) {
                return;
            }
        }
        updateRoleAcls(roleId, aclIdList);
        saveRoleAclLog(roleId, originAclIdList, aclIdList);
    }


    private void updateRoleAcls(int roleId, List<Integer> aclIdList) {
        // 1.删除角色的所有权限
        sysRoleAclMapper.deleteByRoleId(roleId);
        // 2.权限id 为空 删除所有权限
        if (CollectionUtils.isEmpty(aclIdList)) {
            return;
        }
        List<SysRoleAcl> roleAclList = Lists.newArrayList();
        for (Integer aclId : aclIdList  ) {
            roleAclList.add(SysRoleAcl.builder().aclId(aclId).roleId(roleId)
                    .operator(RequestHolder.getCurrentUser().getUsername())
                    .operateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()))
                    .operateTime(new Date()).build());
        }
        sysRoleAclMapper.batchInsert(roleAclList);
    }

    private void saveRoleAclLog(int roleId, List<Integer> before, List<Integer> after) {
        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
        sysLog.setType(LogType.TYPE_ROLE_ACL);
        // 新增没有after
        sysLog.setTargetId(roleId);
        sysLog.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLog.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        sysLog.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysLog.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysLog.setOperateTime(new Date());
        sysLog.setStatus(1);
        sysLogMapper.insert(sysLog);
    }
}
