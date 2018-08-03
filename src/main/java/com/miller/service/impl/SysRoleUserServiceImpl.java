package com.miller.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.miller.common.RequestHolder;
import com.miller.dao.SysRoleUserMapper;
import com.miller.dao.SysUserMapper;
import com.miller.model.SysRoleUser;
import com.miller.model.SysUser;
import com.miller.service.SysRoleUserService;
import com.miller.util.IpUtil;
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

    @Override
    public List<SysUser> getListByRoleId(int roleId) {
        List<Integer> userIdList = sysRoleUserMapper.selectUserIdListByRoleId(roleId);
        if (CollectionUtils.isEmpty(userIdList)) {
            return Lists.newArrayList();
        }
        return sysUserMapper.selectByIdList(userIdList);
    }


    @Override
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

    }

    @Transactional
    public void updateRoleUsers(int roleId, List<Integer> userIdList) {
        sysRoleUserMapper.deleteByRoleId(roleId);
        if (CollectionUtils.isEmpty(userIdList)) {
            return;
        }
        // 拼装数据
        List<SysRoleUser> roleUserList = Lists.newArrayList();
        for (int userId : userIdList) {
            roleUserList.add(SysRoleUser.builder().roleId(roleId).userId(userId)
                    .operator(RequestHolder.getCurrentUser().getUsername())
                    .operatorIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()))
                    .operatorTime(new Date()).build());
        }
        //增加
        sysRoleUserMapper.batchInsert(roleUserList);

    }
}
