package icu.rolin.ips.internetprintersystem.model;

import java.io.Serializable;

public class PrintPO{
    private Integer paperSize;
    private Integer paperType;
    private Integer printDensity;
    private Integer printNum;
    private String printerName;

    public Integer getPaperSize() {
        return paperSize;
    }

    public void setPaperSize(Integer paperSize) {
        this.paperSize = paperSize;
    }

    public Integer getPaperType() {
        return paperType;
    }

    public void setPaperType(Integer paperType) {
        this.paperType = paperType;
    }

    public Integer getPrintDensity() {
        return printDensity;
    }

    public void setPrintDensity(Integer printDensity) {
        this.printDensity = printDensity;
    }

    public Integer getPrintNum() {
        return printNum;
    }

    public void setPrintNum(Integer printNum) {
        this.printNum = printNum;
    }

    public String getPrinterName() {
        return printerName;
    }

    public void setPrinterName(String printerName) {
        this.printerName = printerName;
    }

    @Override
    public String toString() {
        return "PrintPO{" +
                "paperSize=" + paperSize +
                ", paperType=" + paperType +
                ", printDensity=" + printDensity +
                ", printNum=" + printNum +
                ", printerName='" + printerName + '\'' +
                '}';
    }
}
