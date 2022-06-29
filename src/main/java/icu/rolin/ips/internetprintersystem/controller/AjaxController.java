package icu.rolin.ips.internetprintersystem.controller;


import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import icu.rolin.ips.internetprintersystem.configure.Constants;
import icu.rolin.ips.internetprintersystem.model.*;
import icu.rolin.ips.internetprintersystem.util.FileUtils;
import icu.rolin.ips.internetprintersystem.util.IPUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.ResolutionSyntax;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.PrinterResolution;
import javax.servlet.http.HttpServletRequest;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping("/api")
public class AjaxController {
    private static Log logger = LogFactory.getLog(AjaxController.class);

    @RequestMapping(value = "/image-print.do", method = RequestMethod.POST)
    public ResponseVO imagePrint(HttpServletRequest httpServletRequest,
                                 @RequestPart("images") MultipartFile[] images,
                                 @RequestBody PrintPO printPO) {
        String ip = IPUtil.getIpAddr(httpServletRequest);
        System.out.println("当前获取到的用户IP为：" + ip);
        System.out.println(printPO.toString());

        return null;
    }

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


    @RequestMapping(value = "/print", method = RequestMethod.POST)
    public ResponseVO print(@RequestBody UpPrint up) {
        File tempDir = new File(Constants.savePath + "\\temp");
        ArrayList<String> imagePaths = new ArrayList<>();
        synchronized (this) {
            if (!tempDir.exists()) tempDir.mkdirs();
        }

        for (ImageParam ip : up.getIp()) {
            String imagePath = Constants.savePath + "\\" + ip.getImage_name();
            List<String> paths = Splitter.on(".").splitToList(imagePath);
            String resultPath =
                    Joiner.on(File.separator).join(Arrays.asList(tempDir, UUID.randomUUID() + ".jpg"));
            FileUtils.GenerateImage(ip.getImage_base64(), resultPath);
            imagePaths.add(resultPath);
        }
        // 开打 先验证路径可行性
        if (imagePaths.size() <= 0) {
            logger.error("???拿不到任何路径 803");
            return new ResponseVO(-803, "程序出错！");
        }
        for (String ii : imagePaths) {
            if (!new File(ii).exists()) {
                logger.error("临时图片获取失败 804");
                return new ResponseVO(-804, "程序出错！");
            }
        }
        // 这下才能真正的开整！
        InputStream fis = null;
        String printerName = "";
        switch (up.getPp().getPrintName()) {
            case "b185":
                printerName = "KONICA MINOLTA 185-8";
                break;
            default:
                logger.error("不存在此打印机 805");
                return new ResponseVO(-805, "不存在此打印机！");
        }
        try {
            // 设置打印格式
            DocFlavor flavor = DocFlavor.INPUT_STREAM.JPEG;
            // 设置打印参数
            PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
            PrinterResolution pr = new PrinterResolution(600, 600, ResolutionSyntax.DPI);

            // 慢慢完善参数选项
            aset.add(MediaSizeName.ISO_A4);
            aset.add(new Copies(up.getPp().getNum())); //份数

            // 定位打印服务(Print Server)
            PrintService printService = null;
            if (printerName != null) {
                //获得本台电脑连接的所有打印机
                PrintService[] printServices = PrinterJob.lookupPrintServices();
                if (printServices == null || printServices.length == 0) {
                    logger.error("打印失败，未找到可用打印机，请检查。");
                    return new ResponseVO(-806, "打印失败，未找到可用打印机，请检查。");
                }
                //匹配指定打印机
                for (int i = 0; i < printServices.length; i++) {
                    System.out.println(printServices[i].getName());
                    if (printServices[i].getName().contains(printerName)) {
                        printService = printServices[i];
                        break;
                    }
                }
                if (printService == null) {
                    logger.error("打印失败，未找到名称为" + printerName + "的打印机，请检查。");
                    return new ResponseVO(-807, "打印失败，未找到对应打印机，请检查。");
                }
            }

            // 构造打印流
            for (String p : imagePaths) {
                System.out.println(p);
                fis = new FileInputStream(new File(p)); // 构造待打印的文件流
                Doc doc = new SimpleDoc(fis, flavor, null);
                DocPrintJob job = printService.createPrintJob(); // 创建打印作业
                job.print(doc, aset);
                logger.info("加入打印队列完毕");
            }
        } catch (Exception e1) {
            e1.printStackTrace();
            return new ResponseVO(-808, "打印失败,出大错！出错详情：" + e1.getMessage());
        }

        return new ResponseVO(0, "打印完毕！");
    }
}
