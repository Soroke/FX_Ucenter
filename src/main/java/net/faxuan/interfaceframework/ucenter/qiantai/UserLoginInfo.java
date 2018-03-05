package net.faxuan.interfaceframework.ucenter.qiantai;

import org.apache.http.client.CookieStore;

/**
 * Created by song on 2017/11/27.
 */
public class UserLoginInfo {
    //返回状态编码
    private int code;
    //cookie信息
    private CookieStore cookieStore;
    //token信息
    private String token;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public CookieStore getCookieStore() {
        return cookieStore;
    }

    public void setCookieStore(CookieStore cookieStore) {
        this.cookieStore = cookieStore;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
