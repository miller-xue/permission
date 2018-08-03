package com.miller.dao;

import com.miller.model.SysDept;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysDeptMapper {
    int deleteByPrimaryKey(@Param("id") Integer id);

    int insert(SysDept record);

    int insertSelective(SysDept record);

    SysDept selectByPrimaryKey(@Param("id") Integer id);

    int updateByPrimaryKeySelective(SysDept record);

    int updateByPrimaryKey(SysDept record);

    /**
     * 查询所有部门
     * @return
     */
    List<SysDept> getAllDept();

    /**
     * 查出当前level下所有子部门
     *
     * @param level
     * @return
     */
    List<SysDept> getChildDeptListByLevel(@Param("level") String level);

    /**
     * 批量修改level
     *
     * @param sysDeptList
     */
    void batchUpdateLevel(@Param("sysDeptList") List<SysDept> sysDeptList);

    /**
     * 根据名称和parentid 查询name使用的次数
     * @param parentId
     * @param name
     * @param id
     * @return
     */
    int countByNameAndParentId(@Param("parentId") Integer parentId,
                               @Param("name") String name,
                               @Param("id") Integer id);

    /**
     * 查询子部门数量
     * @param deptId 部门id
     * @return 数量
     */
    int countByParentId(@Param("deptId") int deptId);
}