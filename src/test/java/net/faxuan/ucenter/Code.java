package net.faxuan.ucenter;

import static net.faxuan.interfaceframework.core.Http.*;

import com.alibaba.fastjson.JSON;
import net.faxuan.interfaceframework.core.UserLoginInfo;
import net.faxuan.root.BaseLogin;
import net.faxuan.root.sale.SaleLogin;
import org.testng.annotations.Test;


import java.io.*;
import java.net.URLDecoder;
import java.util.*;

/**
 * 注册
 * Created by song on 2018/3/8.
 */
public class Code {


    @Test(description = "注册，测试get")
    public void register() {
        get("http://ucms.test.faxuan.net/ucds/ucenter/registerUser.do","userAccount=lka8sd8513;userPassword=ceshi123;userPhone=18965302903;sysCode=KF01").body("code",200).body("msg","操作成功！");
    }

    @Test(description = "刷新，测试登录状态下的接口操作，测试post")
    public void refresht() {
        UserLoginInfo userLoginInfo = BaseLogin.signIn("A","ceshi123","KF01");
        post("http://ucenter.test.faxuan.net/rzds/ucenter/refreshtUser.do","sysCode=KF01").body("code",200).body("data.token",userLoginInfo.getToken());
        BaseLogin.signOut();
    }

    @Test
    public void saleLogin() {
        System.out.println(JSON.toJSON(SaleLogin.signInAdmin("admin;fxzx1234")).toString());
    }

    @Test
    public void t1() {
        for (int i=1;i<10;i++){
            for (int j=1;j<i+1;j++) {
                System.out.print(j + "x" + i + "=" + i*j + "\t");
            }
            System.out.println();
        }
    }

    @Test
    public void t2() {
        setHeader("Cookie","rid=a5sd56236fw52sd6f542a11236sdf5wa");
        System.out.print(get("http://salemh.t.faxuan.net").getHeaders());
    }


    @Test
    public void t3() {
        List<APIInfo> apiInfoList = new ArrayList<APIInfo>();

        String content = readToString("D:\\a.txt");
        String[] allApi = content.split("------------------------------------------------------------------");
        APIInfo apiInfo = null;
        for (String api:allApi) {
            if (api.equals("")) continue;

            String[] apiContent = api.split("\\n");
            apiInfo = new APIInfo();
            Boolean isCookies = false;
            int cookie = 0;
            for (String apiContentOneLine:apiContent) {

                if (apiContentOneLine.contains("POST") || apiContentOneLine.contains("GET")) {
                    String[] urlInfo = apiContentOneLine.split(" ");
                    try {
                        String[] up = urlInfo[1].split("\\?");
                        up[1].length();
                        String params = up[1].replaceAll("&",";");
                        apiInfo.setUrl(up[0]);
                        apiInfo.setParam(params);
                        break;
                    } catch (ArrayIndexOutOfBoundsException e) {
                        //System.out.print(e);
                        apiInfo.setUrl(urlInfo[1]);
                    }
                }
                if (apiContentOneLine.contains("Cookie")) {
                    cookie ++;
                    isCookies = true;
                    continue;
                }
                if (isCookies && cookie != 2) {
                    cookie ++;
                } else if (isCookies && cookie == 2) {
                    String params = apiContentOneLine.replaceAll("&",";");
                    apiInfo.setParam(params);
                    isCookies = false;
                    cookie = 0;
                }
            }
            if (!(apiInfo.getUrl().equals("") || apiInfo.getUrl() == "")) {
                apiInfoList.add(apiInfo);
                System.out.println(apiInfo);
            }

        }
    }

    class APIInfo {
        private String url;
        private String param;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getParam() {
            return param;
        }

        public void setParam(String param) {
            this.param = param;
        }

        public String toString() {
            return "url:" + url + "\t参数：" + param ;
        }
    }


    /**
     * 将fiddler导出文件进行转换，fiddler导出文件中文和特殊字符都进行了URL编码。
     * @param sourceFilePath 源文件
     * @param outFilePath 输出文件
     */
    public void codedConvert(String sourceFilePath,String outFilePath) {
        BufferedReader reader = null;
        BufferedWriter bufferedWriter = null;

        StringBuilder content = new StringBuilder();
        String codeing = "UTF-8";
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(sourceFilePath)));
            File file = new File(outFilePath);
            if (file.exists()) {
                file.delete();
            }
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFilePath, true),"UTF-8"));
            String lineStr;

            while ((lineStr = reader.readLine()) != null) {
                try {
                    String converted = URLDecoder.decode(lineStr,codeing);
                    content.append(converted).append("\n");
                } catch (IllegalArgumentException e) {
                    content.append(lineStr).append("\n");
                    continue;
                }
            }
            reader.close();
            System.out.println("转换完成已生成新文件。");
            bufferedWriter.write(content.toString());
            bufferedWriter.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    public String readToString(String fileName) {
        String encoding = "UTF-8";
        File file = new File(fileName);
        if (!file.exists()) {
            System.err.println("文件不存在");
            return "";
        }
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
//            return URLDecoder.decode(new String(filecontent, encoding),"UTF-8");
            return new String(filecontent, encoding);
        } catch (UnsupportedEncodingException e) {
            System.err.println("The OS does not support " + encoding);
            e.printStackTrace();
            return null;
        }
    }


//    public Boolean writeTOFile(String fileName,String content) {
//        BufferedWriter bufferedWriter = null;
//        try {
//            bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName, true),"UTF-8"));
//            bufferedWriter.write(content);
//            bufferedWriter.close();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return true;
//
//    }


}
