package icu.rolin.ips.internetprintersystem.model;

import java.util.ArrayList;

public class UpPrint {
    private ArrayList<ImageParam> ip;
    private PrintParam pp;

    public ArrayList<ImageParam> getIp() {
        return ip;
    }

    public void setIp(ArrayList<ImageParam> ip) {
        this.ip = ip;
    }

    public PrintParam getPp() {
        return pp;
    }

    public void setPp(PrintParam pp) {
        this.pp = pp;
    }

    @Override
    public String toString() {
        return "UpPrint{" +
                "ip=" + ip.toString() +
                ", pp=" + pp.toString() +
                '}';
    }
}

