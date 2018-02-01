package com.interfacetest.ucenter.houtai;

import com.interfacetest.core.Http;
import com.interfacetest.core.Request;
import com.interfacetest.util.JsonHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by song on 2017/11/29.
 */
public class UCenterInterFace {

    /**
     * 重置用户密码
     * @param userAccount 用户名
     * @param sysCode 系统编码
     * @param rePassWord 重置为哪个密码
     */
    public static int resetUserPassword(String userAccount,String sysCode,String rePassWord) {
        Map<Object,Object> params = new HashMap<>();
        params.put("userAccount",userAccount);
        params.put("sysCode",sysCode);
        params.put("resPassWord",rePassWord);

        Request request = new Http()
                .setUrl("ucds/ucenter/resetUserPassword.do").setParam(params).post();
        return Integer.valueOf(JsonHelper.getValue(request.getBody(),"code").toString());
    }

    /**
     * 获取会员列表
     * @param userAccounts 帐号字拼接符串 “001，002,003…”
     * @param sysCode 单位编码
     * @return 请求状态码
     */
    public static int getUserList(String userAccounts,String sysCode) {
        Map<Object,Object> params = new HashMap<>();
        params.put("userAccounts",userAccounts);
        params.put("sysCode",sysCode);

        Request request = new Http()
                .setUrl("ucds/ucenter/getUserList.do").setParam(params).post();
        return Integer.valueOf(JsonHelper.getValue(request.getBody(),"code").toString());
    }

    /**
     * 获取会员详情
     * @param userAccount
     * @param userPhone
     * @param userEmail
     * @param userQq
     * @param sysCode
     * @return
     */
    public static int getUserDetail(String userAccount,String userPhone,String userEmail,String userQq,String sysCode) {
        Map<Object,Object> params = new HashMap<>();
        params.put("userAccount",userAccount);
        params.put("userPhone",userPhone);
        params.put("userEmail",userEmail);
        params.put("userQq",userQq);
        params.put("sysCode",sysCode);

        Request request = new Http()
                .setUrl("ucds/ucenter/getUserDetail.do").setParam(params).post();
        return Integer.valueOf(JsonHelper.getValue(request.getBody(),"code").toString());
    }
    /**
     * 获取会员详情
     * @param userAccount
     * @param sysCode
     * @return
     */
    public static int getUserDetail(String userAccount,String sysCode) {
        Map<Object,Object> params = new HashMap<>();
        params.put("userAccount",userAccount);
        params.put("sysCode",sysCode);

        Request request = new Http()
                .setUrl("ucds/ucenter/getUserDetail.do").setParam(params).post();
        return Integer.valueOf(JsonHelper.getValue(request.getBody(),"code").toString());
    }

    /**
     * 会员添加
     * @param userAccount
     * @param userPassword
     * @param sysCode
     * @return
     */
    public static int addUser(String userAccount,String userPassword,String sysCode) {
        Map<Object,Object> params = new HashMap<>();
        params.put("userAccount",userAccount);
        params.put("userPassword",userPassword);
        params.put("sysCode",sysCode);

        Request request = new Http()
                .setUrl("ucds/ucenter/addUser.do").setParam(params).post();
        return Integer.valueOf(JsonHelper.getValue(request.getBody(),"code").toString());
    }


    /**
     * 会员批量添加
     * @param userAccounts
     * @param userPassword
     * @param sysCode
     * @return
     */
    public static int batchAddUser(String userAccounts,String userPassword,String sysCode) {
        Map<Object,Object> params = new HashMap<>();
        params.put("userAccounts",userAccounts);
        params.put("userPassword",userPassword);
        params.put("sysCode",sysCode);
        Request request = new Http()
                .setUrl("ucds/ucenter/batchAddUser.do").setParam(params).post();
        return Integer.valueOf(JsonHelper.getValue(request.getBody(),"code").toString());
    }

    /**
     * 删除会员接口
     * @param userAccount
     * @param sysCode
     * @return
     */
    public static int doDeleteUser(String userAccount,String sysCode) {
        Map<Object,Object> params = new HashMap<>();
        params.put("userAccount",userAccount);
        params.put("sysCode",sysCode);
        Request request = new Http()
                .setUrl("ucds/ucenter/doDeleteUser.do").setParam(params).post();
        return Integer.valueOf(JsonHelper.getValue(request.getBody(),"code").toString());
    }

    /**
     * 批量删除会员接口
     * @param userAccounts
     * @param sysCode
     * @return
     */
    public static int BatchUserDelete(String userAccounts,String sysCode) {
        Map<Object,Object> params = new HashMap<>();
        params.put("userAccounts",userAccounts);
        params.put("sysCode",sysCode);
        Request request = new Http()
                .setUrl("ucds/ucenter/doBatchUserDelete.do").setParam(params).post();
        return Integer.valueOf(JsonHelper.getValue(request.getBody(),"code").toString());
    }
}
