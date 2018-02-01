package com.interfacetest.ucenter.qiantai;

import com.interfacetest.core.Http;
import com.interfacetest.core.Request;
import com.interfacetest.util.JsonHelper;
import org.apache.http.client.CookieStore;
import org.testng.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by song on 2017/11/25.
 * 公共接口
 */
public class PublicInterFace {

    /**
     * 用户注册
     * @param userAccount 用户名
     * @param userPassword 密码
     * @param userPhone 手机号
     * @param sysCode 系统编码
     */
    public static int registerUser(String userAccount,String userPassword,String userPhone,String sysCode) {
        Map<Object,Object> params = new HashMap<>();
        params.put("userAccount",userAccount);
        params.put("userPassword",userPassword);
        params.put("userPhone",userPhone);
        params.put("sysCode",sysCode);

        Request request = new Http()
                .setUrl("ucds/ucenter/registerUser.do").setParam(params).post();
        return Integer.valueOf(JsonHelper.getValue(request.getBody(),"code").toString());
    }

    /**
     * 用户登录自身所在系统，登录用户名为用户账号
     * @param user  用户的UserAccount
     * @param passwd  用户密码
     * @param sysCode 单位编码
     * @return  用户的登录token和cookie
     */
    public static UserLoginInfo login(String user, String passwd, String sysCode) {
        Map<Object,Object> params = new HashMap<>();
        params.put("loginCode",user);
        params.put("type","1");
        params.put("sysCode",sysCode);
        params.put("chooseSysCode",sysCode);
        params.put("userPassword",passwd);
        Request request = new Http()
                .setUrl("ucds/ucenter/checkLoginUserAccount.do").setParam(params).post();

        String userEncryptCode = JsonHelper.getValue(JsonHelper.getValue(request.getBody(),"data").toString(),"userEncryptCode").toString();
        String userAccount = JsonHelper.getValue(JsonHelper.getValue(request.getBody(),"data").toString(),"userAccount").toString();
        Assert.assertEquals(JsonHelper.getValue(request.getBody(),"code"),"200");
        //认证用户中心并return登录成功用户的token和cookie
        return grant(userEncryptCode,sysCode,sysCode,userAccount);
    }

    /**
     * 用户使用账户登录 其他关联系统
     * @param user  用户的UserAccount
     * @param passwd  用户的密码
     * @param loginSysCode  登录系统的编码
     * @param userFromSysCode  用户所在系统的编码
     * @return
     */
    public static UserLoginInfo login(String user,String passwd,String loginSysCode,String userFromSysCode) {
        //登录
        Map<Object,Object> params = new HashMap<>();
        params.put("loginCode",user);
        params.put("type",1);
        params.put("sysCode",loginSysCode);
        params.put("chooseSysCode",userFromSysCode);
        params.put("userPassword",passwd);
        Request request = new Http()
                .setUrl("ucds/ucenter/checkLoginUserAccount.do").setParam(params).post();
        String userEncryptCode = JsonHelper.getValue(JsonHelper.getValue(request.getBody(),"data").toString(),"userEncryptCode").toString();
        String userAccount = JsonHelper.getValue(JsonHelper.getValue(request.getBody(),"data").toString(),"userAccount").toString();
        Assert.assertEquals(JsonHelper.getValue(request.getBody(),"code"),"200");
        //认证用户中心并return登录成功用户的token和cookie
        return grant(userEncryptCode,loginSysCode,userFromSysCode,userAccount);
    }

    /**
     * 用户登录自身系统且用户类型为其他
     * @param user 用户userAccount、手机号、邮箱、QQ
     * @param passwd  用户密码
     * @param loginType 账号类型对应登录user；1对应userAccount、2对应手机号、3对应邮箱、4对应QQ
     * @param sysCode  用户所在单位的编码
     * @return  用户的登录token和cookie
     */
    public static UserLoginInfo login(String user,String passwd,int loginType,String sysCode) {
        Map<Object,Object> params = new HashMap<>();
        params.put("loginCode",user);
        params.put("type",loginType);
        params.put("sysCode",sysCode);
        params.put("chooseSysCode",sysCode);
        params.put("userPassword",passwd);
        Request request = new Http()
                .setUrl("ucds/ucenter/checkLoginUserAccount.do").setParam(params).post();
        String userAccount = JsonHelper.getValue(JsonHelper.getValue(request.getBody(),"data").toString(),"userAccount").toString();
        String userEncryptCode = JsonHelper.getValue(JsonHelper.getValue(request.getBody(),"data").toString(),"userEncryptCode").toString();
        Assert.assertEquals(JsonHelper.getValue(request.getBody(),"code"),"200");
        //认证用户中心并return登录成功用户的token和cookie
        return grant(userEncryptCode,sysCode,sysCode,userAccount);
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
    public static UserLoginInfo login(String user,String passwd,int loginType,String loginSysCode,String userFromSysCode) {
        //登录
        Map<Object,Object> params = new HashMap<>();
        params.put("loginCode",user);
        params.put("type",loginType);
        params.put("sysCode",loginSysCode);
        params.put("chooseSysCode",userFromSysCode);
        params.put("userPassword",passwd);
        Request request = new Http()
                .setUrl("ucds/ucenter/checkLoginUserAccount.do").setParam(params).post();
        String userEncryptCode = JsonHelper.getValue(JsonHelper.getValue(request.getBody(),"data").toString(),"userEncryptCode").toString();
        String userAccount = JsonHelper.getValue(JsonHelper.getValue(request.getBody(),"data").toString(),"userAccount").toString();
        Assert.assertEquals(JsonHelper.getValue(request.getBody(),"code"),"200");
        //认证用户中心并return登录成功用户的token和cookie
        return grant(userEncryptCode,loginSysCode,userFromSysCode,userAccount);
    }

    /**
     * 用户认证
     * @param userEncryptCode 用户登录后返回的加密串
     * @param loginSysCode  用户登录系统的编码
     * @param userFromSysCode  用户所在系统的编码
     * @param user  登录所用的用户名
     * @return  用户的登录token和cookie
     */
    private static UserLoginInfo grant(String userEncryptCode,String loginSysCode,String userFromSysCode,String user) {
        UserLoginInfo userLoginInfo = new UserLoginInfo();
        Map<Object,Object> params = new HashMap<>();
        params.put("userEncryptCode",userEncryptCode);
        params.put("sysCode",loginSysCode);
        params.put("chooseSysCode",userFromSysCode);
        params.put("userAccount",user);
        Request request = new Http()
                .setHost(new UcenterHost().getUCHost()).setUrl("rzds/ucenter/grantSystem.do").setParam(params).post();
        String token = JsonHelper.getValue(JsonHelper.getValue(request.getBody(),"data").toString(),"token").toString();
        userLoginInfo.setToken(token);
        userLoginInfo.setCookieStore(request.getCookies());
        userLoginInfo.setCode(Integer.valueOf(JsonHelper.getValue(request.getBody(),"code").toString()));
        return userLoginInfo;
    }

    /**
     * 重复性校验接口1
     * @param loginCode  登录编码；可以是手机号、QQ、邮箱、用户名对应loginType
     * @param sysCode  用户所在系统编码
     * @param loginType 1对应userAccount、2对应手机号、3对应邮箱、4对应QQ
     */
    public static String repeatCheck(String loginCode,String sysCode,int loginType) {
        Map<Object,Object> params = new HashMap<>();
        params.put("loginCode",loginCode);
        params.put("sysCode",sysCode);
        params.put("type",loginType);

        Request request = new Http()
                .setUrl("ucds/ucenter/repeatCheck.do").setParam(params).post();
        return request.getBody();
    }

    /**
     * 重复性校验接口2
     * @param loginCode 登录编码；可以是手机号、QQ、邮箱对应loginType
     * @param userAccount 用户账号
     * @param sysCode 用户所在系统编码
     * @param loginType 1对应userAccount、2对应手机号、3对应邮箱、4对应QQ
     * @return
     */
    public static int repeatCheckUserAccountAndLoginCode(String loginCode,String userAccount,String sysCode,int loginType) {
        Map<Object,Object> params = new HashMap<>();
        params.put("loginCode",loginCode);
        params.put("userAccount",userAccount);
        params.put("sysCode",sysCode);
        params.put("type",loginType);

        Request request = new Http()
                .setUrl("ucds/ucenter/repeatCheckUserAccountAndLoginCode.do").setParam(params).post();
        return Integer.valueOf(JsonHelper.getValue(request.getBody(),"code").toString());
    }

    /**
     * 系统令牌校验
     * @param token token
     * @param sysCode 系统编码
     * @return
     */
    public static int getTokenCheck(String token,String sysCode) {
        Map<Object,Object> params = new HashMap<>();
        params.put("token",token);
        params.put("sysCode",sysCode);

        Request request = new Http()
                .setUrl("ucds/ucenter/getTokenCheck.do").setParam(params).post();
        return Integer.valueOf(JsonHelper.getValue(request.getBody(),"code").toString());
    }

    /**
     * 绑定接口
     * @param userAccount  用户名
     * @param bindCode  绑定code 手机号,邮箱,qq
     * @param bindType  绑定类型 2手机号 3邮箱 4qq
     * @param sysCode  系统编码
     * @return  请求返回
     */
    public static int bindUser(String userAccount,String bindCode,int bindType,String sysCode) {
        Map<Object,Object> params = new HashMap<>();
        params.put("userAccount",userAccount);
        params.put("bindCode",bindCode);
        params.put("bindType",bindType);
        params.put("sysCode",sysCode);
        Request request = new Http().setUrl("ucds/ucenter/bindUser.do")
                .setParam(params).post();
        return Integer.valueOf(JsonHelper.getValue(request.getBody(),"code").toString());
    }

    /**
     * 解绑接口
     * @param userAccount  用户名
     * @param bindCode  绑定code 手机号,邮箱,qq
     * @param bindType  绑定类型 2手机号 3邮箱 4qq
     * @param sysCode  系统编码
     * @return  请求返回
     */
    public static int unbindUser(String userAccount,String bindCode,int bindType,String sysCode) {
        Map<Object,Object> params = new HashMap<>();
        params.put("userAccount",userAccount);
        params.put("unbindCode",bindCode);
        params.put("unbindType",bindType);
        params.put("sysCode",sysCode);
        Request request = new Http().setUrl("ucds/ucenter/unbindUser.do")
                .setParam(params).post();
        return Integer.valueOf(JsonHelper.getValue(request.getBody(),"code").toString());
    }

    /**
     * 查询来源系统下拉列表接口
     * @param sysCode 单位编码
     * @return 接口返回
     */
    public static String getSystemSourceList(String sysCode) {
        Map<Object,Object> params = new HashMap<>();
        params.put("sysCode",sysCode);
        Request request = new Http().setUrl("ucds/ucenter/getSystemSourceList.do")
                .setParam(params).post();
        return request.getBody();
    }

    /**
     * 账号是已否存在关联校验接口
     * @param userLoginInfo 登录用户的cookieStore和token
     * @param userAccount 登录账号
     * @param loginUsersysCode 登录用户单位编码
     * @return 接口返回
     */
    public static int existLink(UserLoginInfo userLoginInfo,String userAccount,String loginUsersysCode) {
        CookieStore cookieStore = userLoginInfo.getCookieStore();
        String token = userLoginInfo.getToken();
        Map<Object,Object> params = new HashMap<>();
        params.put("userAccount",userAccount);
        params.put("sysCode",loginUsersysCode);
        params.put("token",token);
        Request request = new Http().setUrl("ucds/ucenter/existLink.do").setCookieStore(cookieStore)
                .setParam(params).post();
        return Integer.valueOf(JsonHelper.getValue(request.getBody(),"code").toString());
    }

    /**
     * 关联账号提交接口
     * @param cookieStore 登录用户cookieStore
     * @param loginUserFromSysCode 登录用户单位编码
     * @param linkUserAccount 关联用户账号
     * @param linkUserAccountSysCode  关联用户单位编码
     * @param linkPassword 关联用户密码
     * @return  接口返回
     */
    public static String realtionLinkUserAccount(CookieStore cookieStore,String loginUserFromSysCode,String linkUserAccount,String linkUserAccountSysCode,String linkPassword) {
        Map<Object,Object> params = new HashMap<>();
        params.put("sysCode",loginUserFromSysCode);
        params.put("linkUserAccount",linkUserAccount);
        params.put("linkUserAccountSysCode",linkUserAccountSysCode);
        params.put("linkPassword",linkPassword);
        Request request = new Http().setHost(new UcenterHost().getUCHost()).setUrl("rzds/ucenter/realtionLinkUserAccount.do").setCookieStore(cookieStore)
                .setParam(params).post();
        return request.getBody();
    }

    /**
     * 获取关联账号接口
     * @param loginUserCookieStoreAndToken 登录用户的cookieStore和token
     * @param loginUsersysCode 用户所在单位编码
     * @return 接口返回信息
     */
    public static String findUserRealtion(Map<Object,Object> loginUserCookieStoreAndToken,String loginUsersysCode) {
        Map<Object,Object> params = new HashMap<>();
        CookieStore cookieStore = (CookieStore)loginUserCookieStoreAndToken.get("cookieStore");
        String token = loginUserCookieStoreAndToken.get("token").toString();
        params.put("sysCode",loginUsersysCode);
        params.put("token",token);

        Request request = new Http().setUrl("ucds/ucenter/findUserRealtion.do").setCookieStore(cookieStore)
                .setParam(params).post();
        return request.getBody();
    }

    /**
     * 取消关联提交接口
     * @param loginUserCookieStoreAndToken 登录用户的cookieStore和token
     * @param loginUsersysCode 用户所在单位编码
     * @param loginUserAccount 登录用户用户名
     * @return
     */
    public static String cancelLinkUserAccount(Map<Object,Object> loginUserCookieStoreAndToken,String loginUsersysCode,String loginUserAccount) {
        CookieStore cookieStore = (CookieStore)loginUserCookieStoreAndToken.get("cookieStore");
        String token = loginUserCookieStoreAndToken.get("token").toString();
        Map<Object,Object> params = new HashMap<>();
        params.put("sysCode",loginUsersysCode);
        params.put("userAccount",loginUserAccount);
        params.put("token",token);

        Request request = new Http().setHost(new UcenterHost().getUCHost()).setUrl("rzds/ucenter/cancelLinkUserAccount.do").setCookieStore(cookieStore)
                .setParam(params).post();
        return request.getBody();
    }

    /**
     * 用户账户验证接口
     * @param loginCode 根据type值填写
     * @param sysCode 系统编码
     * @param type 2 手机号    3 邮箱
     * @return
     */
    public static int validationUserAccount(String loginCode,String sysCode,int type) {
        Map<Object,Object> params = new HashMap<>();
        params.put("loginCode",loginCode);
        params.put("sysCode",sysCode);
        params.put("type",type);

        Request request = new Http().setUrl("ucds/ucenter/validationUserAccount.do")
                .setParam(params).post();
        return Integer.valueOf(JsonHelper.getValue(request.getBody(),"code").toString());
    }


    /**
     * 找回密码接口
     * @param userAccount 用户名
     * @param sysCode 单位编码
     * @param newPassword 密码
     * @return 接口返回
     */
    public static int updateUserPassword(String userAccount,String sysCode,String newPassword) {
        Map<Object,Object> params = new HashMap<>();
        params.put("userAccount",userAccount);
        params.put("sysCode",sysCode);
        params.put("userPassword",newPassword);

        Request request = new Http().setUrl("ucds/ucenter/updateUserPassword.do")
                .setParam(params).post();
        return Integer.valueOf(JsonHelper.getValue(request.getBody(),"code").toString());
    }

    /**
     * 修改密码接口
     * @param userLoginInfo 登录用户的cookieStore和token
     * @param userPassword 用户名
     * @param sysCode 单位编码
     * @param newPassword 密码
     * @return 接口返回
     */
    public static int updateLoginUserPassword(UserLoginInfo userLoginInfo,String userPassword,String sysCode,String newPassword) {
        CookieStore cookieStore = userLoginInfo.getCookieStore();
        String token = userLoginInfo.getToken();
        Map<Object,Object> params = new HashMap<>();
        params.put("userPassword",userPassword);
        params.put("sysCode",sysCode);
        params.put("userPassword",newPassword);
        params.put("token",token);

        Request request = new Http().setUrl("ucds/ucenter/updateLoginUserPassword.do").setCookieStore(cookieStore)
                .setParam(params).post();
        return Integer.valueOf(JsonHelper.getValue(request.getBody(),"code").toString());
    }

}
