package com.miller.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.miller.common.RequestHolder;
import com.miller.constant.LogType;
import com.miller.dao.SysLogMapper;
import com.miller.dao.SysRoleUserMapper;
import com.miller.dao.SysUserMapper;
import com.miller.model.SysLog;
import com.miller.model.SysLogWithBLOBs;
import com.miller.model.SysRoleUser;
import com.miller.model.SysUser;
import com.miller.service.SysLogService;
import com.miller.service.SysRoleUserService;
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
 * Created by miller on 2018/8/2
 *
 * @author Miller
 */
@Service
public class SysRoleUserServiceImpl implements SysRoleUserService {

    @Resource
    private SysRoleUserMapper sysRoleUserMapper;

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private SysLogMapper sysLogMapper;

    @Override
    public List<SysUser> getListByRoleId(int roleId) {
        // 根据角色id 查询所有 角色对应的用户
        // 1.查询出角色关联的用户idlist
        List<Integer> userIdList = sysRoleUserMapper.selectUserIdListByRoleId(roleId);
        if (CollectionUtils.isEmpty(userIdList)) {
            return Lists.newArrayList();
        }
        // 2，查询用户列表
        return sysUserMapper.selectByIdList(userIdList);
    }


    @Override
    @Transactional
    public void changeRoleUsers(int roleId, List<Integer> userIdList) {
        // 已存在的用户id列表
        List<Integer> originUserIdList = sysRoleUserMapper.selectUserIdListByRoleId(roleId);
        // 判断是否与已存在的列表相等
        if (userIdList.size() == originUserIdList.size()) {
            Set<Integer> originUserIdSet = Sets.newHashSet(originUserIdList);
            Set<Integer> userIdSet = Sets.newHashSet(userIdList);
            originUserIdSet.removeAll(userIdSet);
            // 为空 则未修改
            if (originUserIdSet.isEmpty()) {
                return;
            }
        }
        updateRoleUsers(roleId, userIdList);
        saveRoleUserLog(roleId, originUserIdList, userIdList);
    }


    private void updateRoleUsers(int roleId, List<Integer> userIdList) {
        sysRoleUserMapper.deleteByRoleId(roleId);
        if (CollectionUtils.isEmpty(userIdList)) {
            return;
        }
        // 拼装数据
        List<SysRoleUser> roleUserList = Lists.newArrayList();
        for (int userId : userIdList) {
            roleUserList.add(SysRoleUser.builder().roleId(roleId).userId(userId)
                    .operator(RequestHolder.getCurrentUser().getUsername())
                    .operateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()))
                    .operateTime(new Date()).build());
        }
        //增加
        sysRoleUserMapper.batchInsert(roleUserList);

    }

    public void saveRoleUserLog(int roleId, List<Integer> before, List<Integer> after) {
        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
        sysLog.setType(LogType.TYPE_ROLE_USER);
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
