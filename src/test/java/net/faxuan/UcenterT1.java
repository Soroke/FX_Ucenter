package net.faxuan;

import com.interfacetest.ucenter.houtai.UCenterInterFace;
import com.interfacetest.ucenter.qiantai.OpenSystem;
import com.interfacetest.ucenter.qiantai.PublicInterFace;
import com.interfacetest.ucenter.qiantai.SealSystem;
import com.interfacetest.ucenter.qiantai.UserLoginInfo;
import com.interfacetest.util.GetDate;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by song on 2017/11/27.
 */
public class UcenterT1 {
    String time = GetDate.getDate();
    private String FBUSER = "FBAuto" + time;
    private String KFUSER = "KFAuto" + time;
    private String USERPHONE = "188" + time;
    private String USERPASSWORD = "ceshi123";
//    private String FBXTCODE = "wh001";
//    private String KFXTCODE = "wh002";
    private String FBXTCODE = "FBXT01";
    private String KFXTCODE = "KFXT01";

    /**
     * 新建用户
     * 封闭系统和开放系统各新建一个
     */
    @Test(priority = 1)
    public void reregister() {
//        int count = 0;
//        String name = "yptSoroke";
//        String phone = "183036109";
//        for(int i=0;i<30;i++) {
//            count++;
//            name += count;
//            if (count <= 10) {
//                phone = phone + 0 + count;
//            } else phone +=count;
//            Assert.assertEquals(PublicInterFace.registerUser(name,USERPASSWORD,phone,"FX003"),200);
//            name = "yptSoroke";
//            phone = "183029109";
//        }
        Assert.assertEquals(PublicInterFace.registerUser(FBUSER,USERPASSWORD,USERPHONE,FBXTCODE),200);
        Assert.assertEquals(PublicInterFace.registerUser(KFUSER,USERPASSWORD,USERPHONE,KFXTCODE),200);
    }

    /**
     * 解绑两个新建用户的手机号
     */
    @Test(priority = 2)
    public void unbindUserPhone() {
        Assert.assertEquals(PublicInterFace.unbindUser(FBUSER,USERPHONE,2,FBXTCODE),200);
        Assert.assertEquals(PublicInterFace.unbindUser(KFUSER,USERPHONE,2,KFXTCODE),200);
    }

    /**
     * 为两个用户绑定手机号
     */
    @Test(priority = 3)
    public void bindUserPhone() {
        Assert.assertEquals(PublicInterFace.bindUser(FBUSER,USERPHONE,2,FBXTCODE),200);
        Assert.assertEquals(PublicInterFace.bindUser(KFUSER,USERPHONE,2,KFXTCODE),200);
    }

    /**
     * 检查手机号在开放系统中是否存在
     * 应该返回code：321，手机号存在且未关联
     */
    @Test(priority = 4)
    public void checkUserPhone() {
        Assert.assertEquals(OpenSystem.checkUserPhone(USERPHONE,KFXTCODE),321);
    }

    /**
     * 分别检查新建两个用户是否存在关联
     */
    @Test(priority = 5)
    public void existLink() {
        //检查封闭系统账号是否存在关联
        UserLoginInfo userLoginInfo = PublicInterFace.login(FBUSER,USERPASSWORD,FBXTCODE);
        Assert.assertEquals(userLoginInfo.getCode(),200);
        Assert.assertEquals(PublicInterFace.existLink(userLoginInfo,FBUSER,FBXTCODE),200);
        //检查开放系统账号是否存在关联
        userLoginInfo = PublicInterFace.login(KFUSER,USERPASSWORD,KFXTCODE);
        Assert.assertEquals(userLoginInfo.getCode(),200);
        Assert.assertEquals(PublicInterFace.existLink(userLoginInfo,KFUSER,KFXTCODE),200);
    }
    /**
     * 用户手机号存在然后关联用户
     * 封闭系统用户和开放系统用户进行关联
     */
    @Test(priority = 6)
    public void OpenSystemPhoneExisLinkUser() {
        UserLoginInfo userLoginInfo = PublicInterFace.login(FBUSER,USERPASSWORD,FBXTCODE);
        Assert.assertEquals(userLoginInfo.getCode(),200);
        Assert.assertEquals(OpenSystem.existAndLinkUser(userLoginInfo.getCookieStore(),USERPHONE,USERPASSWORD,KFXTCODE),200);
    }

    /**
     * 分别找回封闭系统和开放系统两个用户的密码
     * 然后登录验证是否找回成功
     */
    @Test(priority = 7)
    public void findPassword() {
        //封闭系统用户账户并找回密码
        Assert.assertEquals(PublicInterFace.validationUserAccount(USERPHONE,FBXTCODE,2),200);
        Assert.assertEquals(PublicInterFace.updateUserPassword(FBUSER,FBXTCODE,"newPassWD123"),200);
        //开放系统用户账户并找回密码
        Assert.assertEquals(PublicInterFace.validationUserAccount(USERPHONE,KFXTCODE,2),200);
        Assert.assertEquals(PublicInterFace.updateUserPassword(KFUSER,KFXTCODE,"newPassWD123"),200);
        //登录验证找回密码是否成功
        Assert.assertEquals(PublicInterFace.login(FBUSER,"newPassWD123",FBXTCODE).getCode(),200);
        Assert.assertEquals(PublicInterFace.login(KFUSER,"newPassWD123",KFXTCODE).getCode(),200);
    }

    /**
     * 分别修改封闭开放两个用户的密码
     * 然后登录验证是否修改成功
     */
    @Test(priority = 8)
    public void updatePassword() {
        //封闭系统用户修改密码
        UserLoginInfo userLoginInfo = PublicInterFace.login(FBUSER,"newPassWD123",FBXTCODE);
        Assert.assertEquals(userLoginInfo.getCode(),200);
        Assert.assertEquals(PublicInterFace.updateLoginUserPassword(userLoginInfo,FBUSER,FBXTCODE,USERPASSWORD),200);
        //开放系统用户修改密码
        userLoginInfo = PublicInterFace.login(KFUSER,"newPassWD123",KFXTCODE);
        Assert.assertEquals(userLoginInfo.getCode(),200);
        Assert.assertEquals(PublicInterFace.updateLoginUserPassword(userLoginInfo,KFUSER,KFXTCODE,USERPASSWORD),200);

        //验证修改密码是否成功
        Assert.assertEquals(PublicInterFace.login(FBUSER,USERPASSWORD,FBXTCODE).getCode(),200);
        Assert.assertEquals(PublicInterFace.login(KFUSER,USERPASSWORD,KFXTCODE).getCode(),200);
    }

    /**
     * 封闭系统用户登录开放平台
     * 然后刷新该用户，系统令牌校验
     * 用户退出
     */
    @Test(priority = 9)
    public void loginRefreshTokenCheckLogout() {
        //封闭系统用户登录开放系统
        UserLoginInfo userLoginInfo = PublicInterFace.login(KFUSER,USERPASSWORD,FBXTCODE,KFXTCODE);
        Assert.assertEquals(userLoginInfo.getCode(),200);
        //封闭系统用户刷新开放系统
        userLoginInfo = SealSystem.FBRefresh(FBXTCODE,userLoginInfo.getCookieStore());
//        OpenSystem.refreshtUser(KFXTCODE,userLoginInfo.getCookieStore());
        Assert.assertEquals(userLoginInfo.getCode(),200);
        //系统令牌校验
        Assert.assertEquals(PublicInterFace.getTokenCheck(userLoginInfo.getToken(),FBXTCODE),200);
        //封闭系统用户退出登录的开放系统平台
        Assert.assertEquals(SealSystem.FBLogoutUser(FBXTCODE,userLoginInfo.getToken(),userLoginInfo.getCookieStore()),200);
//        Assert.assertEquals(OpenSystem.logoutUser(KFXTCODE,userLoginInfo.getToken(),userLoginInfo.getCookieStore()),200);

        userLoginInfo = PublicInterFace.login(FBUSER,USERPASSWORD,FBXTCODE);
        Assert.assertEquals(userLoginInfo.getCode(),200);
        Assert.assertEquals(PublicInterFace.getTokenCheck(userLoginInfo.getToken(),FBXTCODE),200);

        userLoginInfo = PublicInterFace.login(KFUSER,USERPASSWORD,KFXTCODE);
        Assert.assertEquals(userLoginInfo.getCode(),200);
        Assert.assertEquals(PublicInterFace.getTokenCheck(userLoginInfo.getToken(),KFXTCODE),200);
    }

    /**
     * 删除单个会员
     */
    @Test(priority = 10)
    public void deleteUser() {
        Assert.assertEquals(UCenterInterFace.doDeleteUser(FBUSER,FBXTCODE),200);
    }

    /**
     * 批量添加会员
     * 然后批量删除会员
     */
    @Test(priority = 11)
    public void batchUserDelete() {
        Assert.assertEquals(UCenterInterFace.batchAddUser("Soroketest1,Soroketest2,Soroketest3,Soroketest4,Soroketest5",USERPASSWORD,FBXTCODE),200);
        Assert.assertEquals(UCenterInterFace.BatchUserDelete("Soroketest1,Soroketest2,Soroketest3,Soroketest4,Soroketest5",FBXTCODE),200);
    }

    /**
     * 获取用户信息
     */
    @Test(priority = 12)
    public void getUserList() {
        Assert.assertEquals(UCenterInterFace.getUserList("KFAuto30143820,KFAuto30115029,KFAuto30094705,KFAuto30094237,KFAuto30094150,KFAuto30094047",KFXTCODE),200);
    }

    /**
     * 注册封闭系统用户
     * 重置用户密码
     * 验证是否重置成功
     * 删除用户
     */
    @Test(priority = 13)
    public void resetUserPassword() {
        Assert.assertEquals(UCenterInterFace.addUser(FBUSER,USERPASSWORD,FBXTCODE),200);
        Assert.assertEquals(UCenterInterFace.resetUserPassword(FBUSER,FBXTCODE,"song123"),200);
        Assert.assertEquals(PublicInterFace.login(FBUSER,"song123",FBXTCODE).getCode(),200);
        Assert.assertEquals(UCenterInterFace.doDeleteUser(FBUSER,FBXTCODE),200);
    }

    /**
     * 获取会员详情
     */
    @Test(priority = 14)
    public void getUserDetail() {
        Assert.assertEquals(UCenterInterFace.getUserDetail(FBUSER,FBXTCODE),200);
    }
}
