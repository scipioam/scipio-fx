package com.github.ScipioAM.scipio_fx.utils;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 序列化的{@link Function}
 *
 * @param <R> 返回值
 * @since 2022/7/8
 */
@FunctionalInterface
public interface SeFunction<R> extends Serializable, Supplier<R> {

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
