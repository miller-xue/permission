package com.miller.Exception;

import com.miller.enums.ResultEnum;
import lombok.Getter;

/**
 * Created by miller on 2018/7/22
 * 参数校验未通过异常
 */
public class ParamException extends PermissionException {
    /**
     * 校验未通过错误信息
     */
    @Getter
    protected Object data;

    public ParamException(Integer code, Object data) {
        super(code);
        this.data = data;
    }

    public ParamException(String message, Integer code, Object data) {
        super(message, code);
        this.data = data;
    }

    public ParamException(ResultEnum resultEnum, Object data) {
        super(resultEnum);
        this.data = data;
    }
}
