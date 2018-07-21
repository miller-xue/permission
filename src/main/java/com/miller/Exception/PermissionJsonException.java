package com.miller.Exception;

/**
 * Created by miller on 2018/7/21
 */
public class PermissionJsonException extends PermissionException {

    public PermissionJsonException(String code) {
        super(code);
    }

    public PermissionJsonException(String message, String code) {
        super(message, code);
    }
}
