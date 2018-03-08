package net.faxuan.ucenter;

import static net.faxuan.interfaceframework.core.Http.*;

import net.faxuan.interfaceframework.core.UserLoginInfo;
import net.faxuan.root.BaseLogin;
import org.testng.annotations.Test;

/**
 * 注册
 * Created by song on 2018/3/8.
 */
public class Code {

    @Test(description = "注册，测试get")
    public void register() {
        get("http://ucms.test.faxuan.net/ucds/ucenter/registerUser.do","userAccount=lkassd8513;userPassword=ceshi123;userPhone=18860702903;sysCode=KF01").body("code",200).body("msg","操作成功！");
    }

    @Test(description = "刷新，测试登录状态下的接口操作，测试post")
    public void refresht() {
        UserLoginInfo userLoginInfo = BaseLogin.signIn("A","ceshi123","KF01");
        post("http://ucenter.test.faxuan.net/rzds/ucenter/refreshtUser.do","sysCode=KF01").body("code",200).body("data.token",userLoginInfo.getToken());
        BaseLogin.signOut();
    }
}
