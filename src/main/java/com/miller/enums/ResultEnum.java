package com.miller.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by miller on 2018/7/22
 * @author Miller
 * 系统错误信息枚举
 */
@AllArgsConstructor
@Getter
public enum ResultEnum {


    INNER_ERROR(-1, "系统错误"),

    SUCCESS(0, "成功"),

    PARAM_ERROR(1, "参数不正确"),

    DEPT_NAME_EXITS(2, "同一级别下存在相同名称的部门"),

    DEPT_PARENT_NOT_EXIT(5, "待更新的父部门不存在"),

    DEPT_PARENT_NOT_CHILD(3,"父节点不能是当前部门的子节点"),

    DEPT_PARENT_ID_NOT_EQUALS_ID(4,"当前节点的父节点ID不能是自己"),


    USER_TELEPHONE_EXIST(6, "电话已被占用"),
    USER_EMAIL_EXIST(7,"邮箱已被占用"),

    USER_NOT_EXIST(8,"待更新的用户不存在")
    ;

    private Integer code;
    private String msg;
}
