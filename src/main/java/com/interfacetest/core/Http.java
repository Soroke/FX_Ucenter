package com.interfacetest.core;

import com.interfacetest.util.JsonHelper;
import com.sun.deploy.net.URLEncoder;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.*;

import static org.apache.commons.codec.Charsets.UTF_8;

/**
 * 提供发送get和post请求 并可设置header
 * Created by song on 2017/4/1.
 */
public class Http {
    /**
     * 测试环境host
     */
    private String host;

    /**
     * api接口的path
     */
    private String path;

    /**
     * 测试的url
     */
    private String url;

    /**
     * headers
     */
    private Map<Object,Object> headers = new HashMap<Object, Object>();

    /**
     * 参数
     */
    private Map<Object,Object> params = new HashMap<Object, Object>();

    /**
     * 编码默认为utf-8
     */
    private String encode = "utf-8";

    /**
     * log4j打log
     */
    private Logger log = Logger.getLogger(this.getClass());

    /**
     * 取得Request对象
     */
    private Request req = new Request();

    /**
     * 请求超时设置时长
     * 单位秒
     */
    private int timeOut = 15;


    /**
     * Cookies
     */
    CookieStore cookieStore = null;



    /**
     * 构造httprequest设置
     * 设置请求和传输超时时间
     */
    RequestConfig config = RequestConfig.custom().setConnectTimeout(timeOut * 1000).setConnectionRequestTimeout(timeOut * 1000).build();



    /**
     * 默认无参构造方法
     *
     * 读取系统配置文件取得默认host并赋值给当前host
     * 读取配置文件中的超时时长 如果没配置取默认值
     */
    public Http() {
        Properties prop = new Properties();
        try {
            prop.load(this.getClass().getClassLoader().getResourceAsStream("http.properties"));
        } catch (IOException e) {
            log.error("读取系统配置文件失败");
            e.printStackTrace();
        }

        host = prop.getProperty("host.qa").equals("") || prop.getProperty("host.qa") == null ? host : prop.getProperty("host.qa");
        timeOut = Integer.parseInt(prop.getProperty("host.requestTimeOut").equals("") ? String.valueOf(timeOut) : prop.getProperty("host.requestTimeOut"));
    }

    /**
     * 获取cookiesStore
     * @return
     */
    public CookieStore getCookieStore() {
        return cookieStore;
    }

    /**
     * 设置cookies
     * @param cookieStore
     */
    public Http setCookieStore(CookieStore cookieStore) {
        this.cookieStore = cookieStore;
        return this;
    }

    /**
     * 设置header
     * @param headers
     *      map
     * @return
     *      this
     */
    public Http setHeader(Map<Object,Object> headers) {
        this.headers = headers;
        req.setHeaders(headers);
        return this;
    }

    /**
     * 设置url
     * @param url
     *      path地址
     * @return
     *      this
     */
    public Http setUrl(String url){
        this.path = url;
        //req.setUrl(this.url);
        return this;
    }

    /**
     * 提供设置host功能，当用户设置host后就使用用户设置的host，否则使用配置文件中的host
     * @param host
     *      用户设置的host
     * @return
     *      this
     */
    public Http setHost(String host) {
        this.host = host;
        return this;
    }

    /**
     * 设置参数
     * @param params
     *      参数map
     * @return
     *      this
     */
    public Http setParam(Map<Object,Object> params) {
        this.params = params;
        req.setParams(params);
        return this;
    }

    /**
     * 设置header的方法
     * @param httpRequestBase
     *      http的对象例如 HttpGet、HttpPost
     */
    public void addHeaderToHttpRequest(HttpRequestBase httpRequestBase) {
        if(!headers.isEmpty()) {
            for(Map.Entry<Object, Object> entry : headers.entrySet()){
                System.err.println("key:" + entry.getKey().toString() + "\tvalue:" + entry.getValue().toString());
                httpRequestBase.addHeader(entry.getKey().toString(), entry.getValue().toString());
            }
        }
    }

    /**
     * 发送get请求
     *
     * @return
     *  Request对象
     *  包含：
     *      请求类型
     *      url
     *      返回结果
     *      状态码
     *      响应时间
     */
    public Request get() {
        if (cookieStore == null)  cookieStore = new BasicCookieStore();
        HttpClient httpClient =  HttpClientBuilder.create().setDefaultRequestConfig(config).setDefaultCookieStore(cookieStore).build();
        url = host + path;
        String baseUrl = url;
        //用于计算接口请求响应时间
        Long startTime = 0L;
        long runTime = 0L;
        log.info("------------------------开始请求------------------------");
        log.info("接口类型：get");
        log.info("接口URL为：" + url);
        /**
         * create by song
         * 如果有参数
         * 设置get请求参数 并和url进行拼接为完整的请求地址
         * 设置请求参数的编码 格式为utf-8
         */
        if(!params.isEmpty()) {
            String param = "";
            for (Map.Entry<Object,Object> entry : params.entrySet()) {
                if(!param.equals("")){
                    param = param + "&";
                }
                try{
                    param += URLEncoder.encode(entry.getKey().toString(),encode)+"="+URLEncoder.encode(entry.getValue().toString(),encode);
                } catch (IOException e) {
                    log.error("get请求参数设置utf-8编码时出错");
                    e.printStackTrace();
                }

                //打印请求参数信息
                log.info("参数：\"" +entry.getKey() + "\":\"" + entry.getValue() + "\"");
            }
            url = url + "?" + param;
        }
        HttpGet httpGet = null;
        HttpResponse response = null;
        int count = 0;
        /**
         * 接口测试不通过时，暂停1s继续请求；
         * 如果三次都不通过再判定测试失败
         */
        while(count < 3) {
            httpGet = new HttpGet(url);
            this.addHeaderToHttpRequest(httpGet);
            //请求开始时间
            startTime = new Date().getTime();
            try {
                response = httpClient.execute(httpGet);
                req.setBody(EntityUtils.toString(response.getEntity(), encode));
                runTime = new Date().getTime() - startTime;
            } catch(IOException ioe) {
                log.error("get请求发送时出错！");
                ioe.printStackTrace();
                return null;
            } catch (NullPointerException npe) {
                log.error("没有设置URL参数！");
                npe.printStackTrace();
                return null;
            }
            if (response.getStatusLine().getStatusCode() != 200) {
                count ++;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (count == 3) {
                    log.error("接口:" + baseUrl + "请求返回结果为失败；已重试3次，接口测试失败。");
                } else {
                    log.error("接口:" + baseUrl + "请求返回结果为失败；开始第" + count + "次重试");
                }
            } else {
                count = 5;
            }
        }
        log.info("接口响应时间为：" + runTime + "ms");
        log.info("------------------------请求结束------------------------");
        req.setUrl(url);
        req.setRunTime(runTime);
        req.setRequestType(RequestType.GET);
        req.setCookies(cookieStore);
        //保存header
        Header[] headers = response.getAllHeaders();
        Map<Object,Object> hashMap = new HashMap<Object,Object>();
        for (Header header:headers) {
            hashMap.put(header.getName(),header.getValue());
        }
        req.setHeaders(hashMap);
        try {
            req.setBody(EntityUtils.toString(response.getEntity(), encode));
        } catch (IOException e) {
            e.printStackTrace();
        }
        req.setStatusCode(response.getStatusLine().getStatusCode());
        log.info(req);
        return req;
    }

    /**
     * 发送post请求
     *
     * @return
     *  Request对象
     *  包含：
     *      请求类型
     *      url
     *      参数
     *      返回结果
     *      状态码
     *      响应时间
     */
    public Request post() {
        if (cookieStore == null)  cookieStore = new BasicCookieStore();
        HttpClient httpClient =  HttpClientBuilder.create().setDefaultRequestConfig(config).setDefaultCookieStore(cookieStore).build();
        url = host + path;
        //用于计算接口请求响应时间
        Long startTime = 0L;
        long runTime = 0L;
        log.info("------------------------开始请求------------------------");
        log.info("接口类型：post");
        log.info("接口URL为：" + url);
        HttpPost httpPost = new HttpPost(url);
        this.addHeaderToHttpRequest(httpPost);
        /**
         * 检查参数是否为空
         * 如果存在参数循环添加
         */
        if(!params.isEmpty()) {
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            for (Map.Entry<Object,Object> entry : params.entrySet()) {
                log.info("参数：\"" +entry.getKey() + "\":\"" + entry.getValue() + "\"");
                param.add(new BasicNameValuePair(entry.getKey().toString(), entry.getValue().toString()));
            }
            HttpEntity entity = new UrlEncodedFormEntity(param,UTF_8);
            httpPost.setEntity(entity);
        }
        int count = 0;
        HttpResponse response = null;
        /**
         * 接口测试不通过时，暂停1s继续请求；
         * 如果三次都不通过再判定测试失败
         */
        while(count < 3 ) {
            //计算请求开始时间
            startTime = new Date().getTime();
            try {
                response = httpClient.execute(httpPost);
                req.setBody(EntityUtils.toString(response.getEntity(), encode));
                runTime = new Date().getTime() - startTime;
            } catch(IOException ioe) {
                log.error("post请求发送时出错");
                ioe.printStackTrace();
                return null;
            }catch (NullPointerException npe) {
                log.error("没有设置URL参数！");
                npe.printStackTrace();
                return null;
            }
            if ( response.getStatusLine().getStatusCode() != 200 ) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                count ++;
                if (count == 3) {
                    log.error("接口:" + url + "请求返回结果为失败；已重试3次，接口测试失败。");
                } else {
                    log.error("接口:" + url + "请求返回结果为失败；开始第" + count + "次重试");
                }
            } else {
                count = 5;
            }
        }

        log.info("接口响应时间为：" + runTime + "ms");
        log.info("------------------------请求结束------------------------");
        req.setUrl(url);
        req.setRunTime(runTime);
        req.setRequestType(RequestType.POST);
        req.setCookies(cookieStore);
        //保存header
        Header[] headers = response.getAllHeaders();
        Map<Object,Object> hashMap= new HashMap<Object,Object>();
        for (Header header:headers) {
            hashMap.put(header.getName(),header.getValue());
        }
        req.setHeaders(hashMap);
        req.setStatusCode(response.getStatusLine().getStatusCode());
        log.info(req);

        return req;
    }

}
