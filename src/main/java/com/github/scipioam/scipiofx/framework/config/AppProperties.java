package com.github.scipioam.scipiofx.framework.config;

import com.github.scipioam.scipiofx.framework.AppInitThread;
import com.github.scipioam.scipiofx.framework.JFXApplication;
import com.github.scipioam.scipiofx.framework.LaunchListener;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.net.URL;

/**
 * @author Alan Scipio
 * @since 2024/4/26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AppProperties extends AbstractProperties implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String title;

    private String iconPath;
    private transient URL iconUrl;

    private String splashImgPath;
    private transient URL splashImgUrl;

    private String splashProgressBar;

    private String launchListener;
    private transient LaunchListener launchListenerInstance;

    private String initThread;
    private transient AppInitThread initThreadInstance;

    private ViewProperties view = new ViewProperties();

    public URL getIconUrl() {
        return loadUrl(iconUrl, iconPath);
    }

    public URL getSplashImgUrl() {
        return loadUrl(splashImgUrl, splashImgPath);
    }

    public LaunchListener getLaunchListenerInstance() {
        launchListenerInstance = loadInstance(launchListenerInstance, launchListener);
        return launchListenerInstance;
    }

    public AppInitThread getInitThreadInstance() {
        initThreadInstance = loadInstance(initThreadInstance, initThread);
        return initThreadInstance;
    }

    public boolean isSplashProgressBarFlag() {
        return loadBoolean(splashProgressBar);
    }

    public void setAppClass(Class<? extends JFXApplication> appClass) {
        super.appClass = appClass;
        if (view != null) {
            view.setAppClass(appClass);
        }
    }

    //===================================================================================================

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private final AppProperties prop = new AppProperties();

        public Builder appClass(Class<? extends JFXApplication> appClass) {
            prop.setAppClass(appClass);
            return this;
        }

        public Builder title(String title) {
            prop.setTitle(title);
            return this;
        }

        public Builder iconPath(String iconPath) {
            prop.setIconPath(iconPath);
            return this;
        }

        public Builder splashImgPath(String splashImgPath) {
            prop.setSplashImgPath(splashImgPath);
            return this;
        }

        public Builder launchListener(String launchListener) {
            prop.setLaunchListener(launchListener);
            return this;
        }

        public Builder initThread(String initThread) {
            prop.setInitThread(initThread);
            return this;
        }

        public Builder view(ViewProperties view) {
            prop.setView(view);
            return this;
        }

        public AppProperties build() {
            return prop;
        }
    }

}
