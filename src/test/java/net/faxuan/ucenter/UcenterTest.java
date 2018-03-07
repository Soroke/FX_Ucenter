package net.faxuan.ucenter;

import net.faxuan.interfaceframework.core.Response;
import net.faxuan.interfaceframework.core.TestCase;
import net.faxuan.root.BaseLogin;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import static net.faxuan.interfaceframework.core.HttpS.*;

/**
 * Created by song on 2018/3/8.
 */
public class UcenterTest extends TestCase{

    private Logger log = Logger.getLogger(this.getClass());

    @Test(dataProvider = "getData")
        public void currency(String description,String precondition,int result,String url,String params) {
        log.info(description);

        try{
            String[] loginfo = precondition.split(",");
            switch (loginfo.length) {
                case 3:
                    BaseLogin.signIn(loginfo[0],loginfo[1],loginfo[2]);
                    break;
                case 4:
                    BaseLogin.signIn(loginfo[0],loginfo[1],Integer.valueOf(loginfo[2]),loginfo[3]);
                    break;
                case 5:
                    BaseLogin.signIn(loginfo[0],loginfo[1],Integer.valueOf(loginfo[2]),loginfo[3],loginfo[4]);
                    break;
                default:
                    System.err.println("登录信息有误！");
            }
            Response response = get(url,params);
            BaseLogin.signOut();
            Assert.assertEquals(result,response.getRunCode());
        } catch (NullPointerException e) {
            Response response = get(url,params);
            Assert.assertEquals(result,response.getRunCode());
        }
    }
}
