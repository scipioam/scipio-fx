package com.github.ScipioAM.scipio_fx.utils;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;

/**
 * @since 2022/7/13
 */
public interface SeFunctional {

    default SerializedLambda getSerializedLambda() throws Exception {
        Method write = this.getClass().getDeclaredMethod("writeReplace");
        write.setAccessible(true);
        return (SerializedLambda) write.invoke(this);
    }

    default String getImplClass() {
        try {
            return getSerializedLambda().getImplClass();
        } catch (Exception e) {
            return null;
        }
    }

    default String getImplMethodName() {
        try {
            return getSerializedLambda().getImplMethodName();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 如果实现是一个getter或setter方法，尝试根据方法名获取其属性名
     *
     * @return 属性名
     */
    default String getPropertyByMethod() {
        return ReflectUtil.beanMethodToProperty(getImplMethodName());
    }

}
