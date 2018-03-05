package net.faxuan.interfaceframework.util;

import java.util.Calendar;

/**
 * Created by song on 2017/11/27.
 * 获取当前时间
 */
public class GetDate {
    /**
     * 获取电脑时间
     * @return 返回具体天时分秒格式例如
     */
    public static String getDate() {
        Calendar calendar = Calendar.getInstance();//可以对每个时间域单独修改
        String dateR,hour,minute,second;
        if (calendar.get(Calendar.DATE) < 10) {
            dateR = "0" + String.valueOf(calendar.get(Calendar.DATE));
        } else {
            dateR = String.valueOf(calendar.get(Calendar.DATE));
        }
        if (calendar.get(Calendar.HOUR_OF_DAY) < 10) {
            hour = "0" + String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        } else {
            hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        }
        if (calendar.get(Calendar.MINUTE) < 10) {
            minute = "0" + String.valueOf(calendar.get(Calendar.MINUTE));
        } else {
            minute = String.valueOf(calendar.get(Calendar.MINUTE));
        }
        if (calendar.get(Calendar.SECOND)<10) {
            second = "0" + String.valueOf(calendar.get(Calendar.SECOND));
        } else {
            second = String.valueOf(calendar.get(Calendar.SECOND));
        }
        String date = dateR + hour + minute + second;
        return date;
    }

}
