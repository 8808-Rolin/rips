package icu.rolin.ips.internetprintersystem.controller;


import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class RouteController {

    // 首页
    @RequestMapping({"/","index","index.html"})
    public String index(){
        return "index";
    }

    // 选择页面，如果是PC端则直接重定向至打印页面
    @RequestMapping( "/start")
    public String start(Device device){
        if(device.isMobile()){
            return "mobile_page/choose_print";
        }else{
            return "redirect:/print/image";
        }
    }


    @RequestMapping({"/print","/print/image"})
    public String print(Device device){
        if(device.isMobile()){
            return "mobile_page/m-printing";
        }else{
            return "page/printing";
        }
    }

    @RequestMapping("/log/changelog")
    public String update(Device device){
        if(device.isMobile()){
            return "mobile_page/m-update-log";
        }else{
            return "page/update-log";
        }
    }

    @RequestMapping("/print/pdf")
    public String pring_pdf(Device device){
        if(device.isMobile()){
            return "mobile_page/printing_pdf";
        }else{
            return "page/printing_pdf";
        }
    }


}
