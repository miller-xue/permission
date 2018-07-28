package com.miller.dao;

import com.miller.model.SysAclModule;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysAclModuleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysAclModule record);

    int insertSelective(SysAclModule record);

    SysAclModule selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysAclModule record);

    int updateByPrimaryKey(SysAclModule record);

    /**
     * 根据名称和父节点id查询name出现次数判断唯一
     * @param parentId
     * @param name
     * @param id
     * @return
     */
    int countByNameAndParentId(@Param("parentId") Integer parentId,
                                     @Param("name")String name,
                                     @Param("id")Integer id);

    /**
     * 获得所有的aclModule对象
     * @return
     */
    List<SysAclModule> getAllAclModule();


    /**
     * 获取当前level下所有子权限模块
     *
     * @param level
     * @return
     */
    List<SysAclModule> getChildAclModuleListByLevel(@Param("level") String level);

    /**
     * 批量更新level值
     * @param list
     */
    void batchUpdateLevel(@Param("list") List<SysAclModule> list);
}