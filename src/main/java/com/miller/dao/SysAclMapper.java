package com.miller.dao;

import com.miller.common.PageQuery;
import com.miller.model.SysAcl;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysAclMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysAcl record);

    int insertSelective(SysAcl record);

    SysAcl selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysAcl record);

    int updateByPrimaryKey(SysAcl record);

    int countByNameAndAclModuleId(@Param("aclModuleId")int aclModuleId,
                                  @Param("name")String name,
                                  @Param("id")Integer id);

    /**
     * 跟据权限模块id查询数量
     * @param aclModuleId
     * @return
     */
    int countByAclModuleId(@Param("aclModuleId") int aclModuleId);

    /**
     * 根据权限模块id和分页对象,分页查询
     * @param aclModuleId
     * @param page
     * @return
     */
    List<SysAcl> getPageByAclModuleId(@Param("aclModuleId") int aclModuleId,
                                      @Param("page") PageQuery page);

    /**
     * 查询所有权限
     * @return
     */
    List<SysAcl> selectAll();

    /**
     * 根据id查询权限列表
     * @param idList
     * @return
     */
    List<SysAcl> selectByIdList(@Param("idList") List<Integer> idList);

    /**
     * 根据用户Id
     * 1.查询用户角色列表
     * 2.根据角色列表
     * 3.根据角色查询角色对应的权限idList
     * 4.查询出用户所拥有的权限List
     * @param userId
     * @return
     */
    List<SysAcl> selectAclListByUserId(@Param("userId") int userId);

    /**
     * 根据角色Id
     * 1.根据角色查询角色对应的权限idList
     * 2.查询出用户所拥有的权限List
     * @param roleId
     * @return
     */
    List<SysAcl> selectAclListByRoleId(@Param("roleId") int roleId);
}