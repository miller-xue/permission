package com.miller.enums.result;

import com.miller.enums.ResultEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by miller on 2018/8/4
 */
@Getter
@AllArgsConstructor
public enum AclModuleResult implements ResultEnum {
    ACL_MODULE_NAME_EXIST(9, "同一级别下存在相同名称的权限部门"),

    ACL_MODULE_NOT_EXIST(10,"权限模块不存在"),
    ACL_MODULE_HAS_CHILD(18, "当前模块下有子模块无法删除"),
    ACL_MODULE_HAS_ACL(19, "当前模块下有权限点无法删除"),
    PARENT_NOT_EXIST(5, "待更新的父部门不存在"),

    PARENT_NOT_CHILD(3,"父节点不能是当前部门的子节点"),

    PARENT_ID_NOT_EQUALS_ID(4,"当前节点的父节点ID不能是自己"),
    ;
    private int code;
    private String msg;
}
