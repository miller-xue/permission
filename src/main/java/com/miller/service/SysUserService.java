package com.miller.service;

import com.miller.common.PageQuery;
import com.miller.common.PageResult;
import com.miller.model.SysUser;
import com.miller.param.UserParam;

import java.util.List;

/**
 * Created by miller on 2018/7/25
 *
 * @author Miller
 */
public interface SysUserService {

    void save(UserParam param);

    void update(UserParam param);

    SysUser findByKeyword(String keyword);

    PageResult<SysUser> getPageByDeptId(int deptId, PageQuery pageQuery);

    /**
     * 获得所有用户
     *
     * @return
     */
    List<SysUser> getAll();
}
