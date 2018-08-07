package com.miller.service;

import com.miller.dto.AclModuleLevelDto;
import com.miller.dto.Menu;

import java.util.List;

/**
 * Created by miller on 2018/8/7
 */
public interface SysMenuService {

    List<Menu> changeTreeToMenu(List<AclModuleLevelDto> aclModuleLevelDtoList);
}
