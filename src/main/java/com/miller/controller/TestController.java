package com.miller.controller;

import com.miller.Exception.PermissionPageException;
import com.miller.common.Result;
import com.miller.param.TestVo;
import com.miller.util.BeanValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

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
    public Result validate(TestVo testVo) {
        log.info("validate");
        try {
            Map<String, String> validate = BeanValidator.validateObject(testVo);
            if (validate != null && validate.keySet().size() > 0) {
                for (Map.Entry<String, String> entry : validate.entrySet()) {
                    log.info("{}=>{}", entry.getKey(), entry.getValue());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.buildSuccess();

    }
}
