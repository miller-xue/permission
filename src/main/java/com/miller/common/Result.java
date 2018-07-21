package com.miller.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by miller on 2018/7/21
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T> {

    private boolean result;

    private String msg;

    private T data;

    public Result(boolean result) {
        this.result = result;
    }


    public static Result buildSuccess(Object data, String msg) {
        Result result = new Result(true);
        result.setData(data);
        result.setMsg(msg);
        return result;
    }

    public static Result buildSuccess(Object data) {
        return buildSuccess(data, null);
    }

    public static Result buildSuccess() {
        return buildSuccess(null, null);
    }

    public static Result buildFail(String msg) {
        Result result = new Result(false);
        result.setMsg(msg);
        return result;
    }
}
