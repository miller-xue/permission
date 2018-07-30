package com.miller.dao;

import com.miller.model.SysRoleAcl;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysRoleAclMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysRoleAcl record);

    int insertSelective(SysRoleAcl record);

    SysRoleAcl selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRoleAcl record);

    int updateByPrimaryKey(SysRoleAcl record);

    /**
     * 根据角色列表集合查询权限列表
     * @param roleIdList
     * @return
     */
    List<Integer> selectAclIdListByRoleIdList(@Param("roleIdList") List<Integer> roleIdList);
}