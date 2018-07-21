package com.miller.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by miller on 2018/7/21
 */
@Controller
@RequestMapping("/test")
@Slf4j
public class TestController {

    @RequestMapping
    @ResponseBody
    public String hello() {
        log.info("hello");
        return "helloWorld";
    }
}
