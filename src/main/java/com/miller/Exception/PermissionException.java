package com.miller.Exception;

import com.miller.enums.ResultEnum;
import lombok.Getter;

/**
 * Created by miller on 2018/7/21
 */

public class PermissionException extends RuntimeException {

    /**
     * 错误码,根据错误码枚举快速定位错误类型
     */
    @Getter
    protected Integer code;

    public PermissionException(Integer code) {
        this.code = code;
    }

    public PermissionException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public PermissionException(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
    }
}
