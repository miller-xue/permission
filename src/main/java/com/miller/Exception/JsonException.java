package com.miller.Exception;

import com.miller.enums.ResultEnum;

/**
 * Created by miller on 2018/7/21
 */
public class JsonException extends PermissionException {

    public JsonException(Integer code) {
        super(code);
    }

    public JsonException(String message, Integer code) {
        super(message, code);
    }

    public JsonException(ResultEnum resultEnum) {
        super(resultEnum);
    }
}
