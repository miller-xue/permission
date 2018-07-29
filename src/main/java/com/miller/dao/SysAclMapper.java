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
}