package com.interfacetest.ucenter.qiantai;

import com.interfacetest.core.Http;
import com.interfacetest.core.Request;
import com.interfacetest.util.JsonHelper;
import org.apache.http.client.CookieStore;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by song on 2017/11/25.
 * 封闭系统
 */
public class SealSystem {

    /**
     * 封闭系统刷新
     * @param sysCode 系统编码
     * @param cookieStore 登录用户的cookieStore
     * @return token
     */
    public static UserLoginInfo FBRefresh(String sysCode, CookieStore cookieStore) {
        UserLoginInfo userLoginInfo = new UserLoginInfo();
        Map<Object,Object> params = new HashMap<>();
        params.put("sysCode",sysCode);
        Request request = new Http().setHost(new UcenterHost().getUCHost()).setUrl("rzds/ucenter/FBRefresh.do").setCookieStore(cookieStore)
                .setParam(params).post();
        userLoginInfo.setCode(Integer.valueOf(JsonHelper.getValue(request.getBody(),"code").toString()));
        userLoginInfo.setToken(JsonHelper.getValue(JsonHelper.getValue(request.getBody(),"data").toString(),"token").toString());
        userLoginInfo.setCookieStore(cookieStore);
        return userLoginInfo;
    }

    /**
     * 封闭系统用户退出
     * @param sysCode 系统编码
     * @param token 登录token
     * @param cookieStore 登录cookieStore
     * @return
     */
    public static int FBLogoutUser(String sysCode, String token,CookieStore cookieStore) {
        Map<Object,Object> params = new HashMap<>();
        params.put("sysCode",sysCode);
        params.put("token",token);
        Request request = new Http().setHost(new UcenterHost().getUCHost()).setUrl("rzds/ucenter/FBLogoutUser.do").setCookieStore(cookieStore)
                .setParam(params).post();
        return Integer.valueOf(JsonHelper.getValue(request.getBody(),"code").toString());
    }
}
