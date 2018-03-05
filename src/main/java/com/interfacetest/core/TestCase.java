package com.interfacetest.core;

import com.interfacetest.ucenter.qiantai.UserLoginInfo;
import com.interfacetest.util.JsonHelper;
import com.interfacetest.util.Mysql;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static com.interfacetest.core.HttpS.*;

/**
 * Created by song on 2018/3/2.
 */
@SuppressWarnings("unused")
public class TestCase {
    protected Log log = LogFactory.getLog(this.getClass());
    private Mysql sql = new Mysql();
    /**
     * 初始化mysql配置信息
     */
    @Parameters({"mysql_url", "mysql_user", "mysql_pwd"})
    @BeforeClass(alwaysRun = true)
    protected void mysqlInit(String url, String user, String pwd) {
System.out.println(url + user + pwd);
        sql.setUrl(url);
        sql.setUserName(user);
        sql.setPassWord(pwd);
    }

    @AfterClass(alwaysRun = true)
    public void closeMysqlConnect() {
        sql.deconnSQL();
    }

    /**
     * 使用testNG.xml里的数据库配置连接数据库 并
     * 用当前执行的测试类  完整包名+类名 作为用例名称为条件去数据库查找 测试用例数据
     *  如需要其他用例名称的数据等特殊情况请子类重写此方法
     * @return mysql.getJDBCData(String caseName)返回的Object[][]对象数组
     */
    @DataProvider
    public Object[][] getData(){
        return sql.getJDBCData(this.getClass().getName());
    }

//    @DataProvider
//    public Object[][] getData1(){
//        return sql.getdd(this.getClass().getName(),"niha");
//    }


    public static UserLoginInfo login(String userName,String password,String systemCode) {
        return loginSystem(userName,password,1,systemCode,systemCode);
    }

    public static UserLoginInfo login(String userName,String password,int loginType,String systemCode) {
        return loginSystem(userName,password,loginType,systemCode,systemCode);
    }

    public static UserLoginInfo login(String userName,String password,String systemCode,int loginType,String loginSysCode,String userFromSysCode) {
        return loginSystem(userName,password,loginType,loginSysCode,userFromSysCode);
    }
    /**
     *
     * @param user  用户userAccount、手机号、邮箱、QQ
     * @param passwd  用户密码
     * @param loginType  账号类型对应登录user；1对应userAccount、2对应手机号、3对应邮箱、4对应QQ
     * @param loginSysCode  用户登录系统的编码
     * @param userFromSysCode  用户所在系统的编码
     * @return  用户的登录token和cookie
     */
    private static UserLoginInfo loginSystem(String user,String passwd,int loginType,String loginSysCode,String userFromSysCode) {

        Properties prop = new Properties();
        try {
            prop.load(TestCase.class.getClassLoader().getResourceAsStream("http.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String loginURL = prop.getProperty("loginURL");
        String grantURL = prop.getProperty("grantURL");

//System.err.println("-=-=-=" + loginURL + "-=-=-=-=-=-=-=" + grantURL + "-=-=-=-=-=-=-");
        //登录
        Map<Object,Object> params = new HashMap<>();
        params.put("loginCode",user);
        params.put("type",loginType);
        params.put("sysCode",loginSysCode);
        params.put("chooseSysCode",userFromSysCode);
        params.put("userPassword",passwd);
        Response response = get(loginURL,params);
        String userEncryptCode = JsonHelper.getValue(response.getBody(),"data.userEncryptCode").toString();
        String userAccount = JsonHelper.getValue(response.getBody(),"data.userAccount").toString();
        Assert.assertEquals(response.getRunCode(),"200");
        //认证用户中心并return登录成功用户的token和cookie
        UserLoginInfo userLoginInfo = new UserLoginInfo();
        params.clear();
        params.put("userEncryptCode",userEncryptCode);
        params.put("sysCode",loginSysCode);
        params.put("chooseSysCode",userFromSysCode);
        params.put("userAccount",userAccount);
        Response response1 = post(grantURL,params);
        String token = JsonHelper.getValue(response1.getBody(),"data.token").toString();
        userLoginInfo.setToken(token);
        userLoginInfo.setCookieStore(response1.getCookies());
        userLoginInfo.setCode(Integer.valueOf(response1.getRunCode()));
        return userLoginInfo;
    }
}
