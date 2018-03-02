package com.interfacetest.ucenter.qiantai;

import com.interfacetest.core.Http;
import com.interfacetest.core.Request;
import com.interfacetest.util.JsonHelper;
import org.apache.http.client.CookieStore;

import java.util.HashMap;
import java.util.Map;

import static com.interfacetest.ucenter.qiantai.PublicInterFace.login;

/**
 * Created by song on 2018/02/01.
 * 开放系统
 */
public class OpenSystem{

    /**
     * 开放系统刷新
     * @param sysCode 系统编码
     * @param cookieStore  登录用户的cookieStore
     * @return token
     */
    public static UserLoginInfo refreshtUser(String sysCode, CookieStore cookieStore) {
        UserLoginInfo userLoginInfo = new UserLoginInfo();
        Map<Object,Object> params = new HashMap<>();
        params.put("sysCode",sysCode);
        Request request = new Http().setHost(new UcenterHost().getUCHost()).setUrl("rzds/ucenter/refreshtUser.do").setCookieStore(cookieStore)
                .setParam(params).post();
        userLoginInfo.setCode(Integer.valueOf(JsonHelper.getValue(request.getBody(),"code").toString()));
        userLoginInfo.setToken(JsonHelper.getValue(JsonHelper.getValue(request.getBody(),"data").toString(),"token").toString());
        userLoginInfo.setCookieStore(cookieStore);
        return userLoginInfo;
    }

    /**
     * 开放平台用户退出
     * @param sysCode 系统编码
     * @param token 登录token
     * @param cookieStore 登录cookieStore
     * @return
     */
    public static int logoutUser(String sysCode, String token,CookieStore cookieStore) {
        Map<Object,Object> params = new HashMap<>();
        params.put("sysCode",sysCode);
        params.put("token",token);
        Request request = new Http().setHost(new UcenterHost().getUCHost()).setUrl("rzds/ucenter/logoutUser.do").setCookieStore(cookieStore)
                .setParam(params).post();
        return Integer.valueOf(JsonHelper.getValue(request.getBody(),"code").toString());
    }

    /**
     * 检验手机号是否可以关联
     * @param userPhone  用户手机号
     * @param sysCode  单位编码
     * @return  请求返回
     */
    public static int checkUserPhone(String userPhone,String sysCode) {
        Map<Object,Object> params = new HashMap<>();
        params.put("userPhone",userPhone);
        params.put("sysCode",sysCode);
        Request request = new Http().setUrl("ucds/ucenter/checkUserPhone.do")
                .setParam(params).post();
        return Integer.valueOf(JsonHelper.getValue(request.getBody(),"code").toString());
    }

    /**
     * 手机号不存在创建帐号并与登录用户关联接口
     * @param cookieStore  登录cookieStore
     * @param userPhone  用户手机号
     * @param sysCode  单位编码
     * @return  请求返回
     */
    public static String createAndLinkUser(CookieStore cookieStore,String userPhone,String sysCode ) {
        Map<Object,Object> params = new HashMap<>();
        params.put("userPhone",userPhone);
        params.put("sysCode",sysCode);
        Request request = new Http().setHost(new UcenterHost().getUCHost()).setUrl("rzds/ucenter/createAndLinkUser.do").setCookieStore(cookieStore)
                .setParam(params).post();
        return request.getBody();
    }

    /**
     * 手机号存在关联接口
     * 手机号存在，将改用户与登录用户进行关联
     * @param cookieStore 登录cookieStore
     * @param userPhone 用户手机号
     * @param userPassword 用户密码
     * @param sysCode 单位编码
     * @return 请求返回
     */
    public static int existAndLinkUser(CookieStore cookieStore,String userPhone,String userPassword,String sysCode) {
        Map<Object,Object> params = new HashMap<>();
        params.put("userPhone",userPhone);
        params.put("userPassword",userPassword);
        params.put("sysCode",sysCode);
        Request request = new Http().setHost(new UcenterHost().getUCHost()).setUrl("rzds/ucenter/existAndLinkUser.do").setCookieStore(cookieStore)
                .setParam(params).post();
        return Integer.valueOf(JsonHelper.getValue(request.getBody(),"code").toString());
    }
}
