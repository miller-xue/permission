package com.miller.service.impl;

import com.miller.dao.SysRoleAclMapper;
import com.miller.service.SysRoleAclService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by miller on 2018/7/29
 */
@Service
public class SysRoleAclServiceImpl implements SysRoleAclService {

    @Resource
    private SysRoleAclMapper sysRoleAclMapper;

}
