package icu.rolin.ips.internetprintersystem.service;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import icu.rolin.ips.internetprintersystem.configure.Constants;
import icu.rolin.ips.internetprintersystem.controller.AjaxController;
import icu.rolin.ips.internetprintersystem.model.ImageParam;
import icu.rolin.ips.internetprintersystem.model.ResponseVO;
import icu.rolin.ips.internetprintersystem.model.UpPrint;
import icu.rolin.ips.internetprintersystem.util.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.ResolutionSyntax;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.PrinterResolution;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * 负责打印的业务请求
 */
@Service
public class PrintService {
    private static Log logger = LogFactory.getLog(PrintService.class);

    /**
     * 返回数值对应状态如下：
     * 803：没有任何路径可用     804：获取临时图片失败
     * 805：不存在对应打印机（前端问题）  806：未找到可用打印机（后端问题） 807：匹配打印机失败（后端问题）
     * 808：异常捕获
     *
     * @param up 打印指定
     * @return 返回一个数值，表示业务执行状态
     */
    public int printImage(UpPrint up) {
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
            return 803;
        }
        for (String ii : imagePaths) {
            if (!new File(ii).exists()) {
                logger.error("临时图片获取失败 804");
                return 804;
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
                return 805;
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
            javax.print.PrintService printService = null;
            if (printerName != null) {
                //获得本台电脑连接的所有打印机
                javax.print.PrintService[] printServices = PrinterJob.lookupPrintServices();
                if (printServices == null || printServices.length == 0) {
                    logger.error("打印失败，未找到可用打印机，请检查。");
                    return 806;
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
                    return 807;
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
            return 808;
        }

        return 0;
    }
}
