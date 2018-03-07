package net.faxuan.ucenter;

import static net.faxuan.interfaceframework.core.HttpS.*;

import net.faxuan.interfaceframework.core.HttpS;
import net.faxuan.interfaceframework.core.Response;
import net.faxuan.interfaceframework.core.TestCase;
import net.faxuan.interfaceframework.ucenter.qiantai.UserLoginInfo;
import net.faxuan.root.BaseLogin;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * 注册
 * Created by song on 2018/3/5.
 */
public class Register extends TestCase{

    private Logger log = Logger.getLogger(this.getClass());


    @Test(dataProvider = "getData")
    public void register(String result,String url,String params) {
        log.info(result);
        Response response = get(url,params);
        if (result.equals("验证注册功能可以正常注册")) {
            Assert.assertEquals(response.getRunCode(),200);
        } else Assert.assertEquals(response.getRunCode(),301);
    }

    @Test(dataProvider = "getData")
    public void deleteUser(String result,String url,String params) {
        log.info(result);
        Response response = get(url,params);
        Assert.assertEquals(response.getStatusCode(),200);
    }

    @Test(dataProvider = "getData")
    public void resetUserPassword(String result,String url,String params) {
        log.info(result);
        UserLoginInfo userLoginInfo = BaseLogin.signIn("Song01","ceshi123","KF01");
        HttpS httpS = new HttpS();
        httpS.setCookieStore(userLoginInfo.getCookieStore());
        Response response = httpS.get(url,params);
        Assert.assertEquals(response.getStatusCode(),200);
    }

    @Test(dataProvider = "getData")
    public void refreshtUser(String result,String url,String params) {
        log.info(result);
        BaseLogin.signIn("Song01","ceshi123","KF01");
        Response response = get(url,params);
        BaseLogin.signOut();
        Assert.assertEquals(response.getStatusCode(),200);
    }
}
