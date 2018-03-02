package com.interfacetest.ucenter.qiantai;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by song on 2018/1/23.
 */
public class UcenterHost {

//    private static String UCHost = "http://ucenter.test.faxuan.net/";
//    private static String UCMSHost = "http://ucms.test.faxuan.net/";
//
//    public static String getUCHost() {
//        return UCHost;
//    }
//    public static String getUCMSHost() {
//        return UCHost;
//    }

    /**
     * log4j打log
     */
    private Logger log = Logger.getLogger(this.getClass());

    /**
     * host对象
     */
    private String host;

    /**
     *
     * @return
     */
    public String getUCHost() {
            Properties prop = new Properties();
            try {
                prop.load(this.getClass().getClassLoader().getResourceAsStream("http.properties"));
            } catch (IOException e) {
                log.error("读取系统配置文件失败");
                e.printStackTrace();
            }

            host = prop.getProperty("ucenter").equals("") || prop.getProperty("ucenter") == null ? host : prop.getProperty("ucenter");
        return host;
    }
}
