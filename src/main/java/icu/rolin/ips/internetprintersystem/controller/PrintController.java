package icu.rolin.ips.internetprintersystem.controller;


import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class PrintController {

    @RequestMapping("/")
    public String index(){
        return "index";
    }

    @RequestMapping( "/start")
    public String start(Device device){
        if(device.isMobile()){
            return "choose_print";
        }else{
            return "redirect:/print";
        }
    }


    @RequestMapping("/print")
    public String print(Device device){
        if(device.isMobile()){
            return "m-printing";
        }else{
            return "printing";
        }
    }

    @RequestMapping("/changelog")
    public String update(Device device){
        if(device.isMobile()){
            return "m-update-log";
        }else{
            return "update-log";
        }
    }
}
