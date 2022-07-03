package icu.rolin.ips.internetprintersystem.util;

import icu.rolin.ips.internetprintersystem.service.PrintService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.print.Paper;

public class PrintUtil {
    private static Log logger = LogFactory.getLog(PrintService.class);

    /**
     * 收集整理web参数过来的打印机名字
     * 将其转换成具体打印机
     * @param webPrinterName
     * @return
     */
    public static String getPrinterNameFromParam(String webPrinterName){
        String printerName = "";
        switch (webPrinterName) {
            case "b185":
                printerName = "KONICA MINOLTA 185-8";
                break;
            default:
                logger.error("不存在此打印机 805");
        }
        return printerName;
    }


    /**
     * 得到纸张对象
     * @return 返回一个A4纸张对象
     */
    public static Paper getPaper() {
        Paper paper = new Paper();
        // 默认为A4纸张，对应像素宽和高分别为 595, 842
        int width = 595;
        int height = 842;
        // 设置边距，单位是像素，10mm边距，对应 28px
        int marginLeft = 10;
        int marginRight = 0;
        int marginTop = 10;
        int marginBottom = 0;
        paper.setSize(width, height);
        // 下面一行代码，解决了打印内容为空的问题
        paper.setImageableArea(marginLeft, marginRight, width - (marginLeft + marginRight), height - (marginTop + marginBottom));
        return paper;
    }

}
