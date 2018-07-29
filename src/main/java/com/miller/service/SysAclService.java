package com.miller.service;

import com.miller.common.PageQuery;
import com.miller.common.PageResult;
import com.miller.model.SysAcl;
import com.miller.param.AclParam;

/**
 * Created by miller on 2018/7/29
 *
 * @author Miller
 */
public interface SysAclService {

    /**
     * 保存一个权限点
     * @param param
     */
    void save(AclParam param);

    /**
     * 更新一个权限点
     * @param param
     */
    void update(AclParam param);

    /**
     * 根据权限模块点,和分页查询列表
     * @param aclModuleId
     * @param pageQuery
     * @return
     */
    PageResult<SysAcl> getPageByAclModuleId(int aclModuleId, PageQuery pageQuery);
}
