package com.miller.dao;

import com.miller.model.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysRoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysRole record);

    int insertSelective(SysRole record);

    SysRole selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRole record);

    int updateByPrimaryKey(SysRole record);

    /**
     * 查询名称出现的次数
     * @param name
     * @param id
     * @return
     */
    int countByName(@Param("name") String name,
                    @Param("id") Integer id);

    /**
     * 查询所有角色
     * @return
     */
    List<SysRole> selectAll();

    /**
     * 根据用户Id 查询角色列表
     * @param userId
     * @return
     */
    List<SysRole> selectListByUserId(@Param("userId") int userId);

    /**
     * 根据权限id 查询拥有该权限的角色
     * @param aclId 权限id
     * @return 角色列表
     */
    List<SysRole> selectListByAclId(@Param("aclId") int aclId);
}