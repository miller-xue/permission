package com.miller.Exception;

/**
 * Created by miller on 2018/7/21
 */
public class PermissionException extends RuntimeException {

    /**
     * 错误码,根据错误码枚举快速定位错误类型
     */
    protected String code;

    public PermissionException(String code) {
        this.code = code;
    }

    public PermissionException(String message, String code) {
        super(message);
        this.code = code;
    }
}
