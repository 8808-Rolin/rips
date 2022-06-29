package icu.rolin.ips.internetprintersystem;

import icu.rolin.ips.internetprintersystem.configure.Constants;
import icu.rolin.ips.internetprintersystem.util.SqliteUtil;
import jdk.jfr.BooleanFlag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class InternetPrinterSystemApplication {


    public static void main(String[] args) {
        File save = new File(Constants.savePath);
        File log = new File(Constants.logPath);
        if (!save.exists()) {
            save.mkdirs();
        }
        if (!log.exists()) {
            log.mkdirs();
        }
        SpringApplication.run(InternetPrinterSystemApplication.class, args);
    }

}
