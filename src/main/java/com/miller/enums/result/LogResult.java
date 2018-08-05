package com.miller.enums.result;

import com.miller.enums.ResultEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by miller on 2018/8/5
 */
@Getter
@AllArgsConstructor
public enum  LogResult implements ResultEnum {
    INSERT_AND_DELETE_NOT_RECOVER(1,"新增和删除操作不能被还原")
    ;
    private int code;
    private String msg;
}
