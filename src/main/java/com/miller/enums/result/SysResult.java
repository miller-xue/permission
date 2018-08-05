package com.miller.enums.result;

import com.miller.enums.ResultEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by miller on 2018/8/4
 * @author Miller
 * 系统返回枚举
 */
@Getter
@AllArgsConstructor
public enum  SysResult implements ResultEnum {

    NO_AUTH(-2, "无权限访问,如需要访问,请联系管理员"),
    INNER_ERROR(-1, "系统错误"),
    SUCCESS(0, "成功"),
    PARAM_ERROR(1, "参数不正确"),
    ;
    private int code;
    private String msg;
}
