package com.miller.service.impl;

import com.google.common.collect.Lists;
import com.miller.dto.AclDto;
import com.miller.dto.AclModuleLevelDto;
import com.miller.dto.Menu;
import com.miller.service.SysMenuService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by miller on 2018/8/7
 * @author Miller
 */
@Service
public class SysMenuServiceImpl implements SysMenuService {


    @Override
    public List<Menu> changeTreeToMenu(List<AclModuleLevelDto> aclModuleLevelDtoList) {
        List<Menu> menuList = Lists.newArrayList();
        // 1.如果权限模块树空 返回空
        if (CollectionUtils.isEmpty(aclModuleLevelDtoList)) {
            return menuList;
        }
        // 处理菜单
        // 由于样式只适合最多两级，因此这里假设只有两层，尽管实现时层级是不限制多少级的
        for (AclModuleLevelDto firstLevel : aclModuleLevelDtoList) {
            Menu firstLevelMenu = new Menu();
            firstLevelMenu.setId(firstLevel.getId());
            firstLevelMenu.setName(firstLevel.getName());

            // 是否需要增加一级菜单
            boolean needAddFirstMenu = false;
            // 首先检查menu下是否已配置了有菜单的权限，如果有，就直接使用对应菜单的权限点的url作为点击后跳转的路径
            if (CollectionUtils.isNotEmpty(firstLevel.getAclList())) {
                for (AclDto aclDto : firstLevel.getAclList()) {
                    if (aclDto.getType() == 1 && StringUtils.isNotBlank(aclDto.getUrl())) {
                        firstLevelMenu.setUrl(aclDto.getUrl());
                        needAddFirstMenu = true;
                        break;
                    }
                }
            }
            // 目前这个菜单还没有点击跳转的url， 而且下面还有子模块时，继续尝试处理下一层级
            if (StringUtils.isBlank(firstLevelMenu.getUrl()) &&
                    CollectionUtils.isNotEmpty(firstLevel.getChildren())) {
                for (AclModuleLevelDto sendLevel : firstLevel.getChildren()) {
                    Menu secondLevelMenu = new Menu();
                    secondLevelMenu.setId(sendLevel.getId());
                    secondLevelMenu.setName(sendLevel.getName());
                    boolean needAddSecondMenu = false; // 是否需要增加二级菜单
                    if (CollectionUtils.isNotEmpty(sendLevel.getAclList())) {
                        for (AclDto aclDto : sendLevel.getAclList()) {
                            if (aclDto.getType() == 1 && StringUtils.isNotBlank(aclDto.getUrl())) {
                                secondLevelMenu.setUrl(aclDto.getUrl());
                                needAddFirstMenu = true;
                                needAddSecondMenu = true;
                                break;
                            }
                        }
                    }
                    if (needAddSecondMenu) {
                        firstLevelMenu.addSubMenu(secondLevelMenu);
                    }
                }
            }
            if (needAddFirstMenu) {
                menuList.add(firstLevelMenu);
            }
        }

        return menuList;
    }
}
