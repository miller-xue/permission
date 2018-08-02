package com.miller.service.impl;

import com.google.common.collect.Lists;
import com.miller.dao.SysRoleUserMapper;
import com.miller.dao.SysUserMapper;
import com.miller.model.SysUser;
import com.miller.service.SysRoleUserService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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

    public List<SysUser> getListByRoleId(int roleId) {
        List<Integer> userIdList = sysRoleUserMapper.selectUserIdListByRoleId(roleId);
        if (CollectionUtils.isEmpty(userIdList)) {
            return Lists.newArrayList();
        }
        return sysUserMapper.selectByIdList(userIdList);
    }
}
