package com.miller.controller;

import com.miller.Exception.ParamException;
import com.miller.util.SpringContextUtil;
import com.miller.common.Result;
import com.miller.dao.SysAclMapper;
import com.miller.model.SysAcl;
import com.miller.param.TestVo;
import com.miller.util.BeanValidator;
import com.miller.util.JsonMapper;
import com.miller.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by miller on 2018/7/21
 */
@RequestMapping(value = "/test")
@Controller
@Slf4j
public class TestController {

    @RequestMapping
//    @ResponseBody
    public Result test() throws Exception {
        throw new Exception("21");
//        return Result.buildSuccess("hello permission");
    }


    @RequestMapping(value = "/validate")
    @ResponseBody
    public Result validate(TestVo testVo) throws ParamException {
        log.info("validate");
        BeanValidator.check(testVo);
        SysAclMapper sysAclMapper = SpringContextUtil.popBean(SysAclMapper.class);
        SysAcl sysAcl = sysAclMapper.selectByPrimaryKey(1);
        log.info("result:{}", JsonMapper.obj2String(sysAcl));
        return ResultUtil.buildSuccess();

    }
}
