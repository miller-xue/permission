package com.miller.service;

import com.miller.common.PageQuery;
import com.miller.common.PageResult;
import com.miller.model.SysUser;
import com.miller.param.UserParam;

/**
 * Created by miller on 2018/7/25
 */
public interface SysUserService {

    public void save(UserParam param);

    public void update(UserParam param);

    public SysUser findByKeyword(String keyword);

    public PageResult<SysUser> getPageByDeptId(int deptId, PageQuery pageQuery);
}
