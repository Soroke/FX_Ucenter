package net.faxuan.interfaceframework.core;

import com.alibaba.fastjson.JSON;
import org.apache.http.client.CookieStore;

import java.util.Map;

/**
 * Created by song on 2018/02/01.
 */
public class Request {

    /**
     * headers
     */
    private Map<Object,Object> headers;

    /**
     * Cookies
     */
    private CookieStore cookies;

    /**
     * 返回结果
     */
    private String body;

    /**
     * 参数params
     */
    private Map<Object,Object> params;

    /**
     * 测试url
     */
    private String url;

    /**
     * 运行时间
     */
    private Long runTime;

    /**
     * 请求方式
     */
    private RequestType requestType;

    /**
     * 请求状态码
     */
    private int statusCode;




    public void setHeaders(Map<Object,Object> headers) {
        this.headers = headers;
    }


    public Map<Object,Object> getHeaders() {
        return headers;
    }


    public void setParams(Map<Object,Object> params) {
        this.params = params;
    }


    public Map<Object,Object> getParams() {
        return params;
    }


    public void setUrl(String url) {
        this.url = url;
    }


    public String getUrl() {
        return url;
    }


    public void setRunTime(Long runtime) {
        this.runTime = runtime;
    }


    public Long getRunTime() {
        return runTime;
    }


    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
    public CookieStore getCookies() {
        return cookies;
    }

    public void setCookies(CookieStore cookies) {
        this.cookies = cookies;
    }
    @Override
    public String toString() {
        return JSON.toJSON(this).toString();
    }
}
