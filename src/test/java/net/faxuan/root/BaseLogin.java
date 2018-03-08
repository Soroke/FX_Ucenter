package net.faxuan.root;

import net.faxuan.interfaceframework.core.Response;
import net.faxuan.interfaceframework.core.UserLoginInfo;
import net.faxuan.interfaceframework.util.JsonHelper;

import java.util.HashMap;
import java.util.Map;

import static net.faxuan.interfaceframework.core.Http.get;
import static net.faxuan.interfaceframework.core.Http.post;

/**
 * 用户登录退出
 * Created by song on 2018/3/5.
 */
public class BaseLogin {

    private static String loginSystemCode;
    private static String TOKEN;

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
        loginSystemCode = loginSysCode;

        //登录
        Map<Object,Object> params = new HashMap<>();
        params.put("loginCode",user);
        params.put("type",loginType);
        params.put("sysCode",loginSysCode);
        params.put("chooseSysCode",userFromSysCode);
        params.put("userPassword",passwd);
        Response response = get("http://ucms.test.faxuan.net/ucds/ucenter/checkLoginUserAccount.do",params).body("code",200);
        String userEncryptCode = response.getValueFromBody("data.userEncryptCode").toString();
        String userAccount = response.getValueFromBody("data.userAccount").toString();
        //认证用户中心并return登录成功用户的token和cookie
        UserLoginInfo userLoginInfo = new UserLoginInfo();
        params.clear();
        params.put("userEncryptCode",userEncryptCode);
        params.put("sysCode",loginSysCode);
        params.put("chooseSysCode",userFromSysCode);
        params.put("userAccount",userAccount);
        Response response1 = get("http://ucenter.test.faxuan.net/rzds/ucenter/grantSystem.do",params);
        String token = JsonHelper.getValue(response1.getBody(),"data.token").toString();
        TOKEN = token;
        userLoginInfo.setToken(token);
        userLoginInfo.setCookieStore(response1.getCookies());
        userLoginInfo.setCode(Integer.valueOf(response1.getValueFromBody("code").toString()));
        return userLoginInfo;
    }

    /**
     * 退出当前登录用户
     * @return 是否退出成功
     */
    public static Boolean signOut(){
        Map<Object,Object> params = new HashMap<>();
        params.put("sysCode",loginSystemCode);
        params.put("token",TOKEN);
        try {
            get("http://ucenter.test.faxuan.net/rzds/ucenter/logoutUser.do",params).body("code",200);
            return true;
        } catch (AssertionError error) {
            try {
                get("http://ucenter.test.faxuan.net/rzds/ucenter/FBLogoutUser.do",params).body("code",200);
                return true;
            } catch (AssertionError ae) {
                return false;
            }
        }
    }

}
