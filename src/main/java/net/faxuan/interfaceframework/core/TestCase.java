package net.faxuan.interfaceframework.core;

import net.faxuan.interfaceframework.util.Mysql;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;

import java.lang.reflect.Method;

/**
 * Created by song on 2018/3/2.
 */
@SuppressWarnings("unused")
public class TestCase {
    protected Log log = LogFactory.getLog(this.getClass());
    private Mysql sql = new Mysql();
    /**
     * 初始化mysql配置信息
     */
    @Parameters({"mysql_url", "mysql_user", "mysql_pwd"})
    @BeforeClass(alwaysRun = true)
    protected void mysqlInit(String url, String user, String pwd) {
System.out.println(url + user + pwd);
        sql.setUrl(url);
        sql.setUserName(user);
        sql.setPassWord(pwd);
    }

    @AfterClass(alwaysRun = true)
    public void closeMysqlConnect() {
        sql.deconnSQL();
    }

    /**
     * 使用testNG.xml里的数据库配置连接数据库 并
     * 用当前执行的测试类  完整包名+类名 作为用例名称为条件去数据库查找 测试用例数据
     *  如需要其他用例名称的数据等特殊情况请子类重写此方法
     * @return mysql.getJDBCData(String caseName)返回的Object[][]对象数组
     */
    @DataProvider
    public Object[][] getData(Method method){
//System.err.println(this.getClass().getName() + "-----" + method.getName());
        return sql.getData(this.getClass().getName(),method.getName());
    }

}
