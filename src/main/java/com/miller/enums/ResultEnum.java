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

    DEPT_NAME_EXITS(-1, "同一级别下存在相同名称的部门")
    ;

    private Integer code;
    private String msg;
}
