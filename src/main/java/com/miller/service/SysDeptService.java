package com.miller.service;

import com.miller.Exception.ParamException;
import com.miller.dto.DeptLevelDto;
import com.miller.param.DeptParam;

import java.util.List;

/**
 * Created by miller on 2018/7/22
 * 部门服务层接口
 * @author Miller
 */
public interface SysDeptService {

    /**
     * 保存一个部门
     * @param param
     * @throws ParamException 参数校验异常
     */
    void save(DeptParam param)throws ParamException;

    /**
     * 查询一个部门树
     *
     * @return
     */

    List<DeptLevelDto> deptTree();

    /**
     * 更新一个部门,如何父部门
     * @param param
     * @throws ParamException
     */
    void update(DeptParam param) throws ParamException;

    /**
     * 删除一个部门
     * @param deptId
     */
    void delete(int deptId);
}
