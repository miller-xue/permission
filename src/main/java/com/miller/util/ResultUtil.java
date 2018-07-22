package com.miller.util;

import com.miller.common.Result;
import com.miller.enums.ResultEnum;

/**
 * Created by miller on 2018/7/22
 */
public class ResultUtil {
    private ResultUtil() {

    }


    public static Result buildSuccess() {
        return buildSuccess(null, ResultEnum.SUCCESS.getMsg(), ResultEnum.SUCCESS.getCode());
    }

    public static Result buildSuccess(Object data, String msg, Integer code) {
        Result result = new Result(true);
        result.setData(data);
        result.setMsg(msg);
        result.setCode(code);
        return result;
    }

    public static Result buildSuccess(Object data) {
        return buildSuccess(data, ResultEnum.SUCCESS.getMsg(), ResultEnum.SUCCESS.getCode());
    }


    public static Result buildFail(Object data,String msg,Integer code) {
        Result result = new Result(false);
        result.setData(data);
        result.setMsg(msg);
        result.setCode(code);
        return result;
    }

    public static Result buildFail(String msg,Integer code) {
        return buildFail(null, msg, code);
    }

    public static Result buildFail(ResultEnum resultEnum) {
        return buildFail(resultEnum.getMsg(), resultEnum.getCode());
    }
}
