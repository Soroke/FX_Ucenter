package net.faxuan.root.sale;

import net.faxuan.interfaceframework.core.Response;
import net.faxuan.interfaceframework.util.JsonHelper;

import static net.faxuan.interfaceframework.core.Http.post;

/**
 * Created by song on 2018/3/28.
 */
public class SaleLogin {
    public static SaleUserInfo signInAdmin(String userInfo) {
        String[] user = userInfo.split(";");
        Response response = post("http://salegl.t.faxuan.net/saless/service/adminService!doAdminLogin.do","adminAccount=" + user[0] + ";adminPassword=" + user[1] + ";login=0;key=5").body("code","200");
        SaleUserInfo saleUserInfo = new SaleUserInfo();
        saleUserInfo.setAccount(JsonHelper.getValue(response.getBody(),"data.adminAccount").toString());
        saleUserInfo.setEmail(JsonHelper.getValue(response.getBody(),"data.adminEmail").toString());
        saleUserInfo.setName(JsonHelper.getValue(response.getBody(),"data.adminName").toString());
        saleUserInfo.setRoleId(Integer.valueOf(JsonHelper.getValue(response.getBody(),"data.roleId").toString()));
        saleUserInfo.setRoleName(JsonHelper.getValue(response.getBody(),"data.roleName").toString());
        saleUserInfo.setSid(JsonHelper.getValue(response.getBody(),"data.sid").toString());
        saleUserInfo.setStatus(Integer.valueOf(JsonHelper.getValue(response.getBody(),"data.status").toString()));

        return saleUserInfo;
    }
}
