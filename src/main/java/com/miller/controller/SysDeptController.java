package com.miller.controller;

import com.miller.common.Result;
import com.miller.param.DeptParam;
import com.miller.service.SysDeptService;
import com.miller.service.SysTreeService;
import com.miller.util.ResultUtil;
import com.miller.util.TreeBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by miller on 2018/7/22
 *
 * @author Miller
 */
@Controller
@RequestMapping("/sys/dept")
@Slf4j
public class SysDeptController {

    @Autowired
    private SysDeptService deptService;


    @RequestMapping("/page")
    public String page() {
        return "dept";
    }

    /**
     * 保存一个部门
     *
     * @param param 部门参数对象
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public Result saveDept(DeptParam param) {
        deptService.save(param);
        return ResultUtil.buildSuccess();
    }

    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    @ResponseBody
    public Result tree() {
        return ResultUtil.buildSuccess(deptService.deptTree());
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public Result updateDept(DeptParam deptParam) {
        deptService.update(deptParam);
        return ResultUtil.buildSuccess();
    }
}
