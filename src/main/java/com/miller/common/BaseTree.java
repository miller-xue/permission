package com.miller.common;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by miller on 2018/7/22
 */
public class BaseTree<T> {
    @Getter
    @Setter
    protected List<T> children = Lists.newArrayList();
}
