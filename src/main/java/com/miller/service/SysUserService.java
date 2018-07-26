package com.miller.service;

import com.miller.model.SysUser;
import com.miller.param.UserParam;

/**
 * Created by miller on 2018/7/25
 */
public interface SysUserService {

    public void save(UserParam param);

    public void update(UserParam param);

    public SysUser findByKeyword(String keyword);
}
