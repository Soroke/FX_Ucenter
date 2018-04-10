package net.faxuan.interfaceframework.core;

import com.alibaba.fastjson.JSON;
import net.faxuan.interfaceframework.util.JsonHelper;
import org.apache.http.client.CookieStore;
import org.apache.log4j.Logger;
import org.testng.Assert;

import java.awt.*;
import java.util.Map;

/**
 * Created by song on 2018/3/1.
 */
public class Response{

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
     * log4j打log
     */
    private Logger log = Logger.getLogger(this.getClass());


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
        String[] bodys = body.split("\\n");
        try {
            bodys[1].equals("");
            this.body = bodys[1];
        } catch (IndexOutOfBoundsException ioobe) {
            this.body = body;
        }
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


    /**
     * 验证返回body中json的指定值
     * @param position 在json中的位置
     * @param contrastive 对比数据
     */
    public Response body(String position,Object contrastive) {
        Object object = null;
        if (contrastive instanceof String) {
            object=JsonHelper.getValue(body,position).toString();
            Assert.assertEquals(object,contrastive);
        } else if (contrastive instanceof Integer) {
            object=Integer.valueOf(JsonHelper.getValue(body,position).toString());
            Assert.assertEquals(object,contrastive);
        } else if (contrastive instanceof Double) {
            object=Double.valueOf(JsonHelper.getValue(body,position).toString());
            Assert.assertEquals(object,contrastive);
        } else if (contrastive instanceof Float) {
            object=Float.valueOf(JsonHelper.getValue(body,position).toString());
            Assert.assertEquals(object,contrastive);
        } else{
            object=JsonHelper.getValue(body,position);
            Assert.assertEquals(object,contrastive);
        }
//log.info("获取返回json中" + position + "的值为：" + object + "\t对比值为:" + contrastive);
        return this;
    }

    /**
     * 解析验证json返回中的多个值
     * @param validationItem
     * @return 本身
     */
    public Response body(String validationItem) {
        System.out.println(validationItem);
        String[] key = validationItem.split("=");
        if (key[0].equals("body")) {
            bodyValue(validationItem);
        } else {
            String[] options = validationItem.split(";");
            for (String option:options) {
                String[] contrast = option.split("=");
                String obj = JsonHelper.getValue(body,contrast[0]).toString();
                Assert.assertEquals(obj,contrast[1]);
                log.info("获取返回json中" + contrast[0] + "的值为：" + obj + "\t对比值为:" + contrast[1]);
            }
        }
        return this;
    }

    /**
     * 直接验证body等于某个值
     * @param bodyValue
     * @return 本身
     */
    public Response bodyValue(String bodyValue) {
        String[] bb = bodyValue.split("=");
        Assert.assertEquals(body.trim(),bb[1]);
        return this;
    }

    /**
     * 根据传入参数获取指定的值
     * @param position
     * @return
     */
    public Object getValueFromBody(String position) {
        return JsonHelper.getValue(body,position);
    }

}
