package com.github.scipioam.scipiofx.framework.config;

import com.github.scipioam.scipiofx.framework.JFXApplication;
import com.github.scipioam.scipiofx.framework.exception.FrameworkException;
import lombok.Getter;
import lombok.Setter;

import java.net.URL;

/**
 * @author Alan Scipio
 * @since 2024/4/29
 */
@Getter
@Setter
public abstract class AbstractProperties {

    protected Class<? extends JFXApplication> appClass;

    protected URL loadUrl(URL urlProperty, String path) {
        Class<?> clazz = appClass;
        if (appClass == null) {
            clazz = getClass();
            System.out.println("Use default appClass to load url resource stream: [" + clazz.getName() + "]");
        }

        if (urlProperty != null) {
            return urlProperty;
        } else if (path != null && !path.isEmpty()) {
            return clazz.getResource(path);
        } else {
            return null;
        }
    }

    protected boolean loadBoolean(String value) {
        return "true".equalsIgnoreCase(value) || "on".equalsIgnoreCase(value) || "yes".equals(value);
    }

    @SuppressWarnings("unchecked")
    protected <T> T loadInstance(T instanceProperty, String className) {
        if (instanceProperty != null) {
            return instanceProperty;
        }
        if (className == null || className.isEmpty()) {
            return null;
        }
        try {
            return (T) Class.forName(className).getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new FrameworkException("Got exception while create instance: " + e, e);
        }
    }

}
