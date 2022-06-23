package com.github.ScipioAM.scipio_fx.app.config;

import com.github.ScipioAM.scipio_fx.utils.StringUtils;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;

/**
 * @since 2022/6/23
 */
public abstract class BaseConfigBean {

    /**
     * 获取资源文件的url
     *
     * @param path     配置文件中设定的路径
     * @param appClass 实际应用的class（通常是JFXApplication或其子类），以方便突破jar去拿资源文件
     * @param errTips  错误提示
     * @param notNull  必须需要，否则抛异常
     * @throws FileNotFoundException 文件不存在，或者没有在module-info.java中将资源文件开放给本调用者
     */
    public URL resolveUrl(String path, Class<?> appClass, String errTips, boolean notNull) throws FileNotFoundException {
        if (StringUtils.isNull(path)) {
            if (notNull) {
                throw new IllegalArgumentException("Launch application failed, [" + errTips + "] did not set !");
            } else {
                return null;
            }
        }
        URL url = appClass.getResource(path);
        if (url == null) {
            throw new FileNotFoundException("resource file [" + path + "] not found, or not opens to " + this.getClass().getPackageName() + " in JPMS");
        }
        return url;
    }

    /**
     * 根据类全名构建实例对象
     *
     * @param expectType 预期类型
     * @param className  类全名
     * @return 实例对象
     */
    public Object buildInstance(Class<?> expectType, String className)
            throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Object obj = null;
        if (StringUtils.isNotNull(className)) {
            Class<?> actualType = Class.forName(className);
            if (!expectType.isAssignableFrom(actualType)) {
                throw new ClassCastException("can not build instance, expect type(super class):[" + expectType + "], actual type:[" + actualType + "]");
            }
            obj = actualType.getDeclaredConstructor().newInstance();
        }
        return obj;
    }

}
