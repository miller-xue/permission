package com.miller.Exception;

/**
 * Created by miller on 2018/7/21
 */
public class PermissionPageException extends PermissionException {

    public PermissionPageException(String code) {
        super(code);
    }

    public PermissionPageException(String message, String code) {
        super(message, code);
    }
}
