package icu.rolin.ips.internetprintersystem.model;

import java.util.ArrayList;

public class SQLiteData {
    private String ip;
    private ArrayList<String> file_path;
    private Integer printerCount;
    private String printerName;
    private Long timestamp;

    public SQLiteData(String ip, ArrayList<String> file_path, Integer printerCount, String printerName) {
        this.ip = ip;
        this.file_path = file_path;
        this.printerCount = printerCount;
        this.printerName = printerName;
        timestamp = System.currentTimeMillis();
    }

    public SQLiteData() {
    }


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public ArrayList<String> getFile_path() {
        return file_path;
    }

    public void setFile_path(ArrayList<String> file_path) {
        this.file_path = file_path;
    }

    public Integer getPrinterCount() {
        return printerCount;
    }

    public void setPrinterCount(Integer printerCount) {
        this.printerCount = printerCount;
    }

    public String getPrinterName() {
        return printerName;
    }

    public void setPrinterName(String printerName) {
        this.printerName = printerName;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
