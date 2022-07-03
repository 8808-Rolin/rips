package icu.rolin.ips.internetprintersystem.controller;


import icu.rolin.ips.internetprintersystem.configure.Constants;
import icu.rolin.ips.internetprintersystem.model.*;
import icu.rolin.ips.internetprintersystem.service.PrintService;
import icu.rolin.ips.internetprintersystem.util.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

// AJAX请求姐在此处
@RestController
@RequestMapping("/api")
public class AjaxController {
    private static Log logger = LogFactory.getLog(AjaxController.class);

    @Autowired
    private PrintService printService;


    /**
     * 用户通过该接口，获取对应图片
     * @param in 图片名称
     * @return 返回一张图片
     * @throws IOException 抛出异常
     */
    @RequestMapping(value = "/image/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getImage(@PathVariable("imageName") String in) throws IOException {
        File file = new File(Constants.savePath + "\\" + in);
        if (!file.exists()) {
            return null;
        }
        FileInputStream fis = new FileInputStream(file);
        byte[] bytes = new byte[fis.available()];
        fis.read(bytes, 0, fis.available());
        return bytes;
    }


    /**
     *  上传临时图片存放并返回名称
     * @param file 表单上传文件，名字file
     * @return 返回临时图片名字给前端，前端可以使用该名字获取图片
     */
    @RequestMapping(value = "/upload/temp-image.do", method = RequestMethod.POST)
    public ResponseVO tempImageUpload(@RequestParam("file") MultipartFile file) {
        if (Objects.isNull(file) || file.isEmpty()) {
            return new ResponseVO(400, "上传的文件为空");
        }

        try {
            byte[] bytes = file.getBytes();
            //要存入本地的地址放到path里面
            Path path = Paths.get(FileUtils.UPLOAD_FOLDER + "/");
            //如果没有files文件夹，则创建
            if (!Files.isWritable(path)) {
                Files.createDirectories(path);
            }
            String extension = FileUtils.getFileExtension(file);  //获取文件后缀
            String tempName = UUID.randomUUID().toString();
            String newName = String.valueOf(Calendar.getInstance().getTimeInMillis()) + "_" + tempName + extension;
            FileUtils.getFileByBytes(bytes, FileUtils.UPLOAD_FOLDER, newName);
            return new ResponseVO(0, newName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseVO(504, "上传失败");
    }


    /**
     *  接受打印图片的请求
     * @param up 接受一个上传对象，包含图片参数以及打印参数
     * @return 返回指定代码
     */
    @RequestMapping(value = "/print/img", method = RequestMethod.POST)
    public ResponseVO print(@RequestBody UpPrint up) {
        int res = printService.printImage(up);
        String message = "";
        switch (res){
            case 0:
                message = "加入打印队列完成！请等待打印机出荷";
                break;
            case 803:
                message = "程序出错，没有接收到任何路径";
                break;
            case 804:
                message = "获取临时图片失败，请联系管理员";
                break;
            case 805 :
            case 806 :
            case 807 :
                message = "获取打印机失败，请联系管理员";
                break;
            case 808 :
                message = "程序出错，请联系管理员";
                break;
            default:
                message = "unknown error";
        }
        return new ResponseVO(res,message);
    }


}
