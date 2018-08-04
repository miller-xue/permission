package com.miller.enums.result;

import com.miller.enums.ResultEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by miller on 2018/8/4
 */
@Getter
@AllArgsConstructor
public enum UserResult implements ResultEnum {
    USER_TELEPHONE_EXIST(6, "电话已被占用"),
    USER_EMAIL_EXIST(7,"邮箱已被占用"),

    USER_NOT_EXIST(8,"待更新的用户不存在"),

    ;
    private int code;
    private String msg;
}
