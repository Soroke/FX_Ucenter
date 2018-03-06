package net.faxuan.root;

import net.faxuan.interfaceframework.core.Response;
import net.faxuan.interfaceframework.core.TestCase;
import net.faxuan.interfaceframework.ucenter.qiantai.UserLoginInfo;
import net.faxuan.interfaceframework.util.JsonHelper;
import org.testng.Assert;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static net.faxuan.interfaceframework.core.HttpS.get;
import static net.faxuan.interfaceframework.core.HttpS.post;

/**
 * 用户登录退出
 * Created by song on 2018/3/5.
 */
public class BaseLogin {

    private static String userName;
    private static String password;
    private static int loginTypeCode;
    private static String loginSystemCode;
    private static String userFromSystemCode;

    public static UserLoginInfo signIn(String userName, String password, String systemCode) {
        return loginSystem(userName,password,1,systemCode,systemCode);
    }

    public static UserLoginInfo signIn(String userName,String password,int loginType,String systemCode) {
        return loginSystem(userName,password,loginType,systemCode,systemCode);
    }

    public static UserLoginInfo signIn(String userName,String password,int loginType,String loginSysCode,String userFromSysCode) {
        return loginSystem(userName,password,loginType,loginSysCode,userFromSysCode);
    }
    /**
     * 登录系统
     * @param user  用户userAccount、手机号、邮箱、QQ
     * @param passwd  用户密码
     * @param loginType  账号类型对应登录user；1对应userAccount、2对应手机号、3对应邮箱、4对应QQ
     * @param loginSysCode  用户登录系统的编码
     * @param userFromSysCode  用户所在系统的编码
     * @return  用户的登录token和cookie
     */
    private static UserLoginInfo loginSystem(String user,String passwd,int loginType,String loginSysCode,String userFromSysCode) {
        userName = user;
        password=passwd;
        loginTypeCode = loginType;
        loginSystemCode = loginSysCode;
        userFromSystemCode = userFromSysCode;

        //登录
        Map<Object,Object> params = new HashMap<>();
        params.put("loginCode",user);
        params.put("type",loginType);
        params.put("sysCode",loginSysCode);
        params.put("chooseSysCode",userFromSysCode);
        params.put("userPassword",passwd);
        Response response = get("http://ucms.test.faxuan.net/ucds/ucenter/checkLoginUserAccount.do",params);
        String userEncryptCode = JsonHelper.getValue(response.getBody(),"data.userEncryptCode").toString();
        String userAccount = JsonHelper.getValue(response.getBody(),"data.userAccount").toString();
        Assert.assertEquals(response.getRunCode(),200);
        //认证用户中心并return登录成功用户的token和cookie
        UserLoginInfo userLoginInfo = new UserLoginInfo();
        params.clear();
        params.put("userEncryptCode",userEncryptCode);
        params.put("sysCode",loginSysCode);
        params.put("chooseSysCode",userFromSysCode);
        params.put("userAccount",userAccount);
        Response response1 = post("http://ucenter.test.faxuan.net/rzds/ucenter/grantSystem.do",params);
        String token = JsonHelper.getValue(response1.getBody(),"data.token").toString();
        userLoginInfo.setToken(token);
        userLoginInfo.setCookieStore(response1.getCookies());
        userLoginInfo.setCode(Integer.valueOf(response1.getRunCode()));
        return userLoginInfo;
    }


}
