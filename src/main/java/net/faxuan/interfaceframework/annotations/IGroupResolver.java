package net.faxuan.interfaceframework.annotations;

import java.lang.reflect.Method;

/**
 * Created by song on 2018/3/6.
 */
public interface IGroupResolver {
    /**
     * 根据测试方法解析数据分组名
     *
     * @param method 测试方法
     * @return 数据分组名
     */
    String resolve(Method method);
}
