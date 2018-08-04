package com.miller.enums.result;

import com.miller.enums.ResultEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by miller on 2018/8/4
 */
@Getter
@AllArgsConstructor
public enum AclResult implements ResultEnum {
    ACL_NAME_EXIST(11, "当前权限模块下存在相同名称的权限点"),
    ACL_NOT_EXIST(12,"权限点不存在"),
    ;
    private int code;
    private String msg;
}
