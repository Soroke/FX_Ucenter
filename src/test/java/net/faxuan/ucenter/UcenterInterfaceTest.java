package net.faxuan.ucenter;

import static net.faxuan.interfaceframework.core.HttpS.*;

import net.faxuan.interfaceframework.core.Response;
import net.faxuan.interfaceframework.core.TestCase;
import net.faxuan.interfaceframework.util.ExcelUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by song on 2018/3/5.
 */
public class UcenterInterfaceTest extends TestCase{

    @Test(dataProvider = "getData")
    public void register(String result,String url,String params) {
        Response response = get(url,params);
        if (result.equals("注册成功")) {
            Assert.assertEquals(response.getRunCode(),200);
        } else Assert.assertEquals(response.getRunCode(),301);


    }
    @Test
    public void insertData() {
        ExcelUtil.importExcelData("D:/uc数据导入模板.xlsx","127.0.0.1","3306","uc","root","123123");
    }
}
