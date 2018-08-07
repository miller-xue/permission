package com.miller.dto;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by miller on 2018/8/7
 */
@Getter
@Setter
public class Menu {
    private Integer id;

    // 菜单展示的名称
    private String name;

    // 菜单点击跳转的链接
    private String url;

    // 下级菜单， 如果存在url里，就不存在下级菜单了
    private List<Menu> subList = Lists.newArrayList();

    public void addSubMenu(Menu menu) {
        if (menu != null) {
            subList.add(menu);
        }
    }
}
