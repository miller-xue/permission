package com.miller.enums.result;

import com.miller.enums.ResultEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by miller on 2018/8/4
 */
@Getter
@AllArgsConstructor
public enum DeptResult implements ResultEnum {

    DEPT_NAME_EXIST(2, "同一级别下存在相同名称的部门"),
    DEPT_NOT_EXIST(15, "部门不存在"),
    DEPT_HAS_CHILD(16, "当前部门下有子部门,无法删除"),
    DEPT_HAS_USER(17, "当前部门下有用户,无法删除"),

    PARENT_NOT_EXIST(5, "待更新的父部门不存在"),

    PARENT_NOT_CHILD(3,"父节点不能是当前部门的子节点"),

    PARENT_ID_NOT_EQUALS_ID(4,"当前节点的父节点ID不能是自己"),
    ;
    private int code;
    private String msg;
}
