package icu.rolin.ips.internetprintersystem.model;

public class PdfParam {
    private String pdfName;
    private Integer size;
    private Integer direction;
    private Integer zoom;
    private Integer num;
    private String printer;

    @Override
    public String toString() {
        return "PdfParam{" +
                "pdfName='" + pdfName + '\'' +
                ", size=" + size +
                ", direction=" + direction +
                ", zoom=" + zoom +
                ", num=" + num +
                ", printer='" + printer + '\'' +
                '}';
    }

    public String getPdfName() {
        return pdfName;
    }

    public void setPdfName(String pdfName) {
        this.pdfName = pdfName;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getDirection() {
        return direction;
    }

    public void setDirection(Integer direction) {
        this.direction = direction;
    }

    public Integer getZoom() {
        return zoom;
    }

    public void setZoom(Integer zoom) {
        this.zoom = zoom;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getPrinter() {
        return printer;
    }

    public void setPrinter(String printer) {
        this.printer = printer;
    }
}
