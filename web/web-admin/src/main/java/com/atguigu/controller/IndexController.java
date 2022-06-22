package com.atguigu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class IndexController {

    private static final String PAGE_FRAME = "frame/index";
    private static final String PAGE_MAIN = "frame/main";

    @RequestMapping("/")
    public String index(){
        return PAGE_FRAME;
    }

    @RequestMapping("/main")
    public String main(){
        return PAGE_MAIN;
    }
}
