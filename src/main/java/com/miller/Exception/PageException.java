package com.miller.Exception;

import com.miller.enums.ResultEnum;

/**
 * Created by miller on 2018/7/21
 */
public class PageException extends PermissionException {

    public PageException(Integer code) {
        super(code);
    }

    public PageException(String message, Integer code) {
        super(message, code);
    }

    public PageException(ResultEnum resultEnum) {
        super(resultEnum);
    }
}
