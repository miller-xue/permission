package com.miller.controller;

import com.miller.service.SysRoleAclService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by miller on 2018/7/29
 * @author Miller
 */
@Controller
@RequestMapping("/sys/roleAcl")
public class SysRoleAclController {

    @Autowired
    private SysRoleAclService sysRoleAclService;
}
