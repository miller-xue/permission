package com.miller.common;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by miller on 2018/7/28
 * @author Miller
 */
@Getter
@Setter
@ToString
@Builder
public class PageResult<T> {

    private List<T> data = new ArrayList<T>();

    private int total = 0;
}
