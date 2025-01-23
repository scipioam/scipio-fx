package com.github.scipioam.scipiofx.framework.config;

import com.github.scipioam.scipiofx.framework.exception.FrameworkException;
import com.github.scipioam.scipiofx.materialfx.MaterialFXInitializer;
import com.github.scipioam.scipiofx.utils.StringUtils;

import java.io.Serial;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Alan Scipio
 * created on 2025-01-22
 */
@SuppressWarnings({"LombokGetterMayBeUsed", "LombokSetterMayBeUsed"})
public class MaterialFxProperties implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private boolean enable = false;

    private boolean useMaterialFxThemeOnly = false;

    private boolean includeLegacy = true;

    private String initializer;
    private transient Class<?> initializerClass;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isUseMaterialFxThemeOnly() {
        return useMaterialFxThemeOnly;
    }

    public void setUseMaterialFxThemeOnly(boolean useMaterialFxThemeOnly) {
        this.useMaterialFxThemeOnly = useMaterialFxThemeOnly;
    }

    public boolean isIncludeLegacy() {
        return includeLegacy;
    }

    public void setIncludeLegacy(boolean includeLegacy) {
        this.includeLegacy = includeLegacy;
    }

    public String getInitializer() {
        return initializer;
    }

    public void setInitializer(String initializer) {
        this.initializer = initializer;
        if (StringUtils.isBlank(initializer)) {
            initializer = "com.github.scipioam.scipiofx.materialfx.DefaultMaterialFXInitializer";
        }
        try {
            initializerClass = Class.forName(initializer);
            if (!MaterialFXInitializer.class.isAssignableFrom(initializerClass)) {
                throw new FrameworkException("MaterialFx initializer class must implement MaterialFXInitializer: " + initializer);
            }
        } catch (ClassNotFoundException e) {
            throw new FrameworkException("MaterialFx initializer class not found: " + initializer);
        }
    }

    public Class<?> getInitializerClass() {
        return initializerClass;
    }

    public void setInitializerClass(Class<? extends MaterialFXInitializer> initializerClass) {
        this.initializerClass = initializerClass;
        if (initializerClass != null) {
            initializer = initializerClass.getName();
        }
    }

    public void init() {
        // 实例化初始化器
        Object initializerInstance;
        try {
            initializerInstance = initializerClass.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException e) {
            throw new FrameworkException("MaterialFx initializer class must have a no-args constructor: " + initializer);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new FrameworkException("Error instantiating MaterialFx initializer class: " + initializer, e);
        }

        // 调用初始化方法
        try {
            Method initMethod = initializerClass.getMethod("init", boolean.class, boolean.class);
            initMethod.invoke(initializerInstance, useMaterialFxThemeOnly, includeLegacy);
        } catch (NoSuchMethodException e) {
            throw new FrameworkException("MaterialFx initializer class must have a method 'init(boolean)': " + initializer);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new FrameworkException("Error invoking MaterialFx initializer method: " + initializer, e);
        }
    }
}
