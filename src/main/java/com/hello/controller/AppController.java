package com.hello.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by 浦云飞 on 2016/12/9.
 */
@Controller
public class AppController {
    @RequestMapping("/")
    public String index(Model model) {
        return "index";
    }

    @RequestMapping("/guest")
    public String guest(Model model) {
        return "guest";
    }
}
