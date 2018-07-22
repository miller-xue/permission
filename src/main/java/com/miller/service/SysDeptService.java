package com.miller.service;

import com.miller.Exception.ParamException;
import com.miller.param.DeptParam;

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
}
