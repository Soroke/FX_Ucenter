package net.faxuan.interfaceframework.core;

import com.alibaba.fastjson.JSON;
import net.faxuan.interfaceframework.util.JsonHelper;
import net.faxuan.interfaceframework.util.Validatable;
import org.apache.http.client.CookieStore;

import java.util.Map;

/**
 * Created by song on 2018/3/1.
 */
public class Response extends Validatable{

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
    private ResponseType responseType;

    /**
     * 请求状态码
     */
    private int statusCode;

    /**
     * 运行返回状态码
     * @param headers
     */
    private int runCode;



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


    public void setResponseType(ResponseType responseType) {
        this.responseType = responseType;
    }

    public ResponseType getResponseType() {
        return responseType;
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

    public int getRunCode() {
        return runCode;
    }

    public int setRunCode() {
        if (!(body.equals("") || body == null)) {
            runCode = new Integer(JsonHelper.getValue(body,"code").toString());
        }
        return -1;
    }
    @Override
    public String toString() {
        return JSON.toJSON(this).toString();
    }


}
