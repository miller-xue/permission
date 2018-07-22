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

    private Integer code;

    public Result(boolean result) {
        this.result = result;
    }

}
