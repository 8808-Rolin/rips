package icu.rolin.ips.internetprintersystem.model;

import java.io.Serializable;

public class PrintParam implements Serializable {
    private Integer size;
    private Integer type;
    private Integer density;
    private Integer num;
    private String printName;

    @Override
    public String toString() {
        return "PrintParam{" +
                "size=" + size +
                ", type=" + type +
                ", density=" + density +
                ", num=" + num +
                ", printName='" + printName + '\'' +
                '}';
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getDensity() {
        return density;
    }

    public void setDensity(Integer density) {
        this.density = density;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getPrintName() {
        return printName;
    }

    public void setPrintName(String printName) {
        this.printName = printName;
    }
}
