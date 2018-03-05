package net.faxuan.interfaceframework.util;

/**
 * Created by song on 2018/3/2.
 * 获取当前执行文件、类、方法、行数名称工具
 */
public class CurrentLineInfo {
    private static int originStackIndex = 2;

    public static String getFileName() {
        return Thread.currentThread().getStackTrace()[originStackIndex].getFileName();
    }

    public static String getClassName() {
        return Thread.currentThread().getStackTrace()[originStackIndex].getClassName();
    }

    public static String getMethodName() {
        return Thread.currentThread().getStackTrace()[originStackIndex].getMethodName();
    }

    public static int getLineNumber() {
        return Thread.currentThread().getStackTrace()[originStackIndex].getLineNumber();
    }
}