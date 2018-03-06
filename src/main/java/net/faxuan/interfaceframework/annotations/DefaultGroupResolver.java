package net.faxuan.interfaceframework.annotations;

import java.lang.reflect.Method;

/**
 * Created by song on 2018/3/6.
 */
public class DefaultGroupResolver implements IGroupResolver{

    @Override
    public String resolve(Method method) {
        String vRet = getGroupFromAnno(method);
        if (vRet == null) {
            vRet = method.getDeclaringClass().getSimpleName() + "_" + method.getName();
        }
        System.err.print(vRet);
        return vRet;

    }

    private String getGroupFromAnno(Method method) {
        Path vAnno = method.getAnnotation(Path.class);
        if (vAnno != null) {
            return vAnno.value();
        }
        return null;
    }
}
