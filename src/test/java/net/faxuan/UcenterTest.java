package net.faxuan;

import net.faxuan.interfaceframework.core.Http;
import net.faxuan.interfaceframework.core.Response;
import net.faxuan.interfaceframework.core.TestCase;
import net.faxuan.root.BaseLogin;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static net.faxuan.interfaceframework.core.HttpS.*;

/**
 * Created by song on 2018/1/23.
 */
public class UcenterTest extends TestCase{

    @Test
    public void te() {
        System.err.println(this.getClass().getMethods()[0].getName());

        new Http().setHost("https://www.baidu.com/").get();

        Map<Object,Object> a = new HashMap<Object,Object>();
        a.put("userAccount","admin");
        a.put("userPassword","123abc");
        a.put("rid","9dab1020112cab387230ca7545a1ee41");
        a.put("login","0");
        a.put("key","42");
        new Http().setHost("https://fzbd.t.faxuan.net/fzss/back/userBackService!doAdminLogin.do").setParam(a).get();
        //Response response = get("https://fzbd.t.faxuan.net/fzss/back/userBackService!doAdminLogin.do",a);

//        a.put("loginCode","9876543210090");
//        a.put("type","1");
//        a.put("sysCode","FX001");
//        a.put("chooseSysCode","FX001");
//        a.put("userPassword","ceshi123");
////        new Http().setHost("https://fzbd.t.faxuan.net/fzss/back/userBackService!doAdminLogin.do").setParam(a).get();
//        Response response = get("http://ucms.test.faxuan.net/ucds/ucenter/checkLoginUserAccount.do",a);
//        System.err.print(JsonHelper.getValueN(response.getBody(),"userEncryptCode"));
    }

    @DataProvider
    public Object[][] dataProvider(){
        return new Object[][]{
                {"小李"},
                {"小赵"},
                {"小明"},
                {"小周"}
        };
    }
    @Test(dataProvider = "dataProvider")
    public void testcase(String name){
        System.out.println(name);
    }

    @Test(dataProvider = "getData")
    public void lala(String result,String url,String data) {
        Response response = get(url,data);
        //Assert.assertEquals(JsonHelper.getValue(response.getBody(),"code").toString(),result);
        //System.err.print(CurrentLineInfo.getMethodName());
    }



    @Test(dataProvider = "getData")
    public void sss(String jg,String url,String data) {
        BaseLogin.signIn("9876543210090","ceshi123","FX001");
        post(url,data);
    }

}
