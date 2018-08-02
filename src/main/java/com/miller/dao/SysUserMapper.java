package com.miller.dao;

import com.miller.common.PageQuery;
import com.miller.model.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    /**
     * 根据关键字查询用户
     *
     * @param keyword
     * @return
     */
    SysUser findByKeyword(@Param("keyword") String keyword);

    /**
     * 根据邮箱和id查询出现的次
     * @param mail
     * @param id
     * @return
     */
    int countByMail(@Param("mail") String mail,@Param("id") Integer id);

    /**
     * 确定电话的唯一性
     *
     * @param telephone
     * @param id
     * @return
     */
    int countByTelephone(@Param("telephone") String telephone, @Param("id") Integer id);

    /**
     * 根据部门id查询下所属用户列表
     * @param deptId
     * @return
     */
    int countByDeptId(@Param("deptId") int deptId);

    /**
     * 根据部门id分页查询用户列表
     * @param deptId
     * @param page
     * @return
     */
    List<SysUser> getPageByDeptId(@Param("deptId") int deptId,
                                  @Param("page") PageQuery page);

    /**
     * 根据idlist查询用户列表
     * @param idList
     * @return
     */
    List<SysUser> selectByIdList(@Param("idList") List<Integer> idList);

    /**
     * 根据角色id查询用户列表
     *
     * @param roleId
     * @return
     */
    List<SysUser> selectByRoleId(@Param("roleId") int roleId);

    /**
     * 查询所有用户
     * @return
     */
    List<SysUser> selectAll();
}