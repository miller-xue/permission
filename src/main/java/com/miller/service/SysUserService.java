package com.miller.service;

import com.miller.common.PageQuery;
import com.miller.common.PageResult;
import com.miller.dto.AclModuleLevelDto;
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

    /**
     * 根据keword查询是否用户存在
     * @param keyword 手机 or mail
     * @return user对象
     */
    SysUser findByKeyword(String keyword);

    PageResult<SysUser> getPageByDeptId(int deptId, PageQuery pageQuery);

    /**
     * 获得所有用户
     *
     * @return
     */
    List<SysUser> getAll();

    /**
     * 根据用户id 查询用户权限树
     * @param userId
     * @return
     */
    List<AclModuleLevelDto> userAclTree(int userId);


    /**
     * 根据权限id 查询用过该权限的用户列表
     * @param aclId 权限id
     * @return 拥有该权限的用户列表
     */
    List<SysUser> getListByAclId(int aclId);
}
