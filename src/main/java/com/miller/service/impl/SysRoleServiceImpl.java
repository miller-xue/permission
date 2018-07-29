package com.miller.service.impl;

import com.miller.Exception.ParamException;
import com.miller.common.RequestHolder;
import com.miller.dao.SysRoleMapper;
import com.miller.enums.ResultEnum;
import com.miller.model.SysRole;
import com.miller.param.RoleParam;
import com.miller.service.SysRoleService;
import com.miller.util.BeanValidator;
import com.miller.util.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by miller on 2018/7/29
 * @author Miller
 */
@Service
@Slf4j
public class SysRoleServiceImpl implements SysRoleService {

    @Resource
    private SysRoleMapper sysRoleMapper;

    public void save(RoleParam param) {
        BeanValidator.check(param);
        SysRole role = param2Role(param);
        if (checkExist(role.getName(), role.getId())) {
            throw new ParamException(ResultEnum.ROLE_NAME_EXIST);
        }
        sysRoleMapper.insertSelective(role);
    }

    public void update(RoleParam param) {
        BeanValidator.check(param);
        SysRole before = sysRoleMapper.selectByPrimaryKey(param.getId());
        if (before == null) {
            throw new ParamException(ResultEnum.ROLE_NOT_EXIST);
        }
        SysRole after = param2Role(param);
        // 如果名称不相等,判断名称是否重复
        if (!before.getName().equals(after.getName())) {
            if (checkExist(after.getName(), after.getId())) {
                throw new ParamException(ResultEnum.ROLE_NAME_EXIST);
            }
        }
        sysRoleMapper.updateByPrimaryKeySelective(after);
    }

    public List<SysRole> getAll() {
        return sysRoleMapper.selectAll();
    }


    /**
     * 判断名称是否重复
     * @param name
     * @param id
     * @return
     */
    private boolean checkExist(String name, Integer id) {
        return sysRoleMapper.countByName(name, id) > 0;
    }

    public SysRole param2Role(RoleParam param) {
        SysRole sysRole = new SysRole();
        BeanUtils.copyProperties(param, sysRole);
        sysRole.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysRole.setOperatorIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysRole.setOperatorTime(new Date());
        return sysRole;
    }
}
