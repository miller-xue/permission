package com.miller.dao;

import com.miller.model.SysRoleUser;
import com.miller.model.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysRoleUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysRoleUser record);

    int insertSelective(SysRoleUser record);

    SysRoleUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRoleUser record);

    int updateByPrimaryKey(SysRoleUser record);

    /**
     * 根据用户id查询角色Id列表
     *
     * @param userId
     * @return
     */
    List<Integer> selectRoleIdListByUserId(@Param("userId") int userId);


    /**
     * 根据角色id查询用户id列表
     * @param roleId
     * @return
     */
    List<Integer> selectUserIdListByRoleId(@Param("roleId") int roleId);

    /**
     * 根据id删除所有信息
     * @param roleId 角色id
     * @return 删除个数
     */
    int deleteByRoleId(@Param("roleId") int roleId);


    /**
     * 批量新增角色用户列表
     * @param roleUserList
     * @return
     */
    int batchInsert(@Param("roleUserList") List<SysRoleUser> roleUserList);

}