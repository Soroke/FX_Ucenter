package com;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by song on 2018/3/27.
 * 删除数据
 */
public class DelData {
    public static int[] del(String ip,String port,String databaseName,String userName,String passWord,String systemID) {
        int[] i = new int[2];
        i[0] = 0;
        i[1] = 0;
        Mysql sql = new Mysql();
        String url = "jdbc:mysql://" + ip + ":" + port + "/" + databaseName + "?useSSL=true&characterEncoding=UTF-8";
        sql.setUrl(url);
        sql.setUserName(userName);
        sql.setPassWord(passWord);
        boolean b = sql.connSQL();
        if (!b) {
            i[0] = 1;
            return i;
        }


        try {
            int caseID = 0;
            ResultSet rs1 = sql.selectSQL("SELECT id FROM cases WHERE `sys_id`='" + systemID + "';");
            while (rs1.next()) {
                caseID = rs1.getInt("id");
            }
           rs1 = sql.selectSQL("SELECT COUNT(*) count FROM datas WHERE `case_id`='" + caseID + "';");
            while (rs1.next()) {
                i[1] = rs1.getInt("count");
            }
            if (i[1] == 0) {
                i[0] = 2;
            } else {
                sql.deleteSQL("DELETE FROM datas WHERE `case_id`='" + caseID + "';");
            }

        } catch (SQLException e) {
            i[0] = 3;
            e.printStackTrace();
        } finally {
            sql.deconnSQL();
        }
        return i;
    }
}
