package net.faxuan.sale;

import net.faxuan.interfaceframework.util.Mysql;
import net.faxuan.root.sale.SaleLogin;
import net.faxuan.root.sale.SaleUserInfo;
import org.apache.commons.io.FileUtils;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by song on 2018/4/17.
 */
public class Init {
    //mysql对象
    Mysql mysql = new Mysql();
    //当前系统时间
    Date date = new Date();
    String dateNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    String dateaNumber = new SimpleDateFormat("yyyyMMddHHmmss").format(date);
    //rid固定
    String rid = "a5sd56236fw52sd6f542a11236sdf5wa";
    //登录对象
    SaleUserInfo saleUserInfo = new SaleUserInfo();


    @BeforeClass
    public void before() {
        saleUserInfo = SaleLogin.signInAdmin("songrenkun;888888");
        //初始化数据库链接信息
        mysql.setUrl("jdbc:mysql://150.138.148.222:63306/sales?useSSL=true&amp;characterEncoding=UTF-8");
        mysql.setUserName("root");
        mysql.setPassWord("pf_testDB.0168");
        mysql.connSQL();
    }

    @AfterClass
    public void after() {
        mysql.deconnSQL();
    }

    /**
     * 获取SQL查询出数据的数量
     * @param sql
     * @return
     */
    public int getDBInfoCount(String sql) {
        ResultSet resultSet = mysql.selectSQL(sql);
        int count = 0;
        try {
            while (resultSet.next()) {
                count ++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * 获取url请求的文件
     * @param url url地址
     * @param root 文件目录
     * @param fileName 文件名
     * @return
     */
    public boolean exportURLFile(String url,String root,String fileName) {
        try {
            FileUtils.copyURLToFile(new URL(urlEnCode(url)), new File(root, fileName));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 将URL进行编码
     * @param url
     * @return
     */
    public String urlEnCode(String url) {
        String[] urls = url.split("\\?");
        String host = urls[0];
        List<String> params = new ArrayList<>();
        String[] par = urls[1].split("&");
        for (String param:par) {
            String[] p = param.split("=");
            String np = null;
            try {
                np = p[0] + "=" + URLEncoder.encode(p[1],"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            params.add(np);
        }
        String encodeParams = "";
        for (int i=0; i<params.size();i++) {
            if (i != (params.size()-1)) {
                encodeParams = encodeParams + params.get(i) + "&";
            } else {
                encodeParams += params.get(i);
            }
        }
        return host + "?" + encodeParams;
    }


    /**
     * 获取类所在文件夹
     * @param cl
     * @return
     */
    public String getDirPath(Class cl) {
        return new File(cl.getResource("").getPath()).getPath();
    }


}
