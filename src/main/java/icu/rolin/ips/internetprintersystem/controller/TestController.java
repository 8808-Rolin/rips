package icu.rolin.ips.internetprintersystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/hello")
public class TestController {

    @RequestMapping(value = "/test")
    public String test(){
        int res = 1 / 0;
        return "test";
    }
}