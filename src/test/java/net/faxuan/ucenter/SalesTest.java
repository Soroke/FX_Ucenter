package net.faxuan.ucenter;

import net.faxuan.interfaceframework.core.TestCase;
import net.faxuan.root.sale.SaleLogin;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import static net.faxuan.interfaceframework.core.Http.get;

/**
 * Created by song on 2018/3/28.
 */
public class SalesTest extends TestCase{


    private Logger log = Logger.getLogger(this.getClass());

    @Test(dataProvider = "getData")
    public void currency(String description,String precondition,String validation,String url,String params) {
        //打印测试描述log
        log.info(description);
        //检查预置条件是否为空
        if (isNull(precondition)) {
            //为空直接调用接口请求并验证返回
            get(url,params).body(validation);
        } else {
            /**
             * 不为空先使用预置条件中的登录信息登录
             * 然后调用接口请求并验证返回
             * 最后退出登录用户
             */
            SaleLogin.signInAdmin(precondition);
            get(url,params).body(validation);
        }
    }
}
