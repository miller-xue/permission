package com.miller.enums.result;

import com.miller.enums.ResultEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by miller on 2018/8/4
 */
@Getter
@AllArgsConstructor
public enum RoleResult implements ResultEnum {
    ROLE_NAME_EXIST(13, "角色名重复"),
    ROLE_NOT_EXIST(14, "待更新的角色不存在"),
    ;
    private int code;
    private String msg;
}
