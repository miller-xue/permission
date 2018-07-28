package com.miller.service;

import com.miller.Exception.ParamException;
import com.miller.dto.AclModuleLevelDto;
import com.miller.model.SysAclModule;
import com.miller.param.AclModuleParam;

import java.util.List;

/**
 * Created by miller on 2018/7/28
 */
public interface SysAclModuleService {

    /**
     * 保存一个权限模块
     * @param param
     * @throws ParamException 参数校验异常
     */
    void save(AclModuleParam param)throws ParamException;

    /**
     * 查询一个权限模块树
     *
     * @return
     */
    List<AclModuleLevelDto> aclModuleTree();

    /**
     * 更新一个权限模块
     * @param param
     * @throws ParamException
     */
    void update(AclModuleParam param) throws ParamException;


}
