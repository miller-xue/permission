package com.miller.common;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;

/**
 * Created by miller on 2018/7/28
 */

public class PageQuery {

    @Getter
    @Setter
    @Min(value = 1,message = "当前页码不合法")
    private int pageNo = 1;

    @Getter
    @Setter
    @Min(value = 1,message = "每页展示的数量不合法")
    private int pageSize = 1;

    @Setter
    private int offset;

    public int getOffset() {
        return (pageNo - 1) * pageSize ;
    }
}
