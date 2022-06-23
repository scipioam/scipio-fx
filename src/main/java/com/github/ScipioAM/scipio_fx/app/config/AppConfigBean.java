package com.github.ScipioAM.scipio_fx.app.config;

import com.github.ScipioAM.scipio_fx.app.AppInitThread;
import com.github.ScipioAM.scipio_fx.app.LaunchListener;
import com.github.ScipioAM.scipio_fx.utils.StringUtils;
import javafx.stage.StageStyle;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.IOException;
import java.net.URL;

/**
 * @since 2022/6/23
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AppConfigBean extends BaseConfigBean {

    private String version;

    private String title;

    private String iconPath;
    private transient URL iconUrl;

    private String splashImgPath = ApplicationConfig.DEFAULT_SPLASH_IMG_PATH;
    private transient URL splashImgUrl;

    private String launchListener;
    private transient LaunchListener launchListenerObj;

    private String initThread;
    private transient AppInitThread initThreadObj;

    private MainViewBean mainView = new MainViewBean();

    public URL getMainViewUrl() {
        return mainView == null ? null : mainView.getMainViewUrl();
    }

    public void setMainViewUrl(URL url) {
        mainView.setMainViewUrl(url);
    }

    public URL getMainViewUrl(Class<?> appClass) throws IOException {
        return mainView.getMainViewUrl(appClass);
    }

    public URL getIconUrl(Class<?> appClass) throws IOException {
        if (iconUrl == null) {
            iconUrl = resolveUrl(iconPath, appClass, "iconPath", false);
        }
        return iconUrl;
    }

    public URL getSplashImgUrl(Class<?> appClass) throws IOException {
        if (splashImgUrl == null) {
            splashImgUrl = resolveUrl(splashImgPath, appClass, "splashImgUrl", false);
        }
        return splashImgUrl;
    }

    public String getMainViewPath() {
        return mainView.getPath();
    }

    public void setMainViewPath(String path) {
        mainView.setPath(path);
    }

    public boolean isMainViewDraggable() {
        return mainView.isDraggableBool();
    }

    public void setMainViewDraggable(boolean mainViewDraggable) {
        mainView.setDraggableBool(mainViewDraggable);
    }

    public void setLaunchListener(String launchListener) throws Exception {
        this.launchListener = launchListener;
        if (StringUtils.isNotNull(launchListener)) {
            launchListenerObj = (LaunchListener) buildInstance(LaunchListener.class, launchListener);
        } else {
            launchListenerObj = null;
        }
    }

    public void setLaunchListenerObj(LaunchListener launchListenerObj) {
        this.launchListenerObj = launchListenerObj;
        if (launchListenerObj != null) {
            launchListener = launchListenerObj.getClass().getName();
        } else {
            launchListener = null;
        }
    }

    public void setInitThread(String initThread) throws Exception {
        this.initThread = initThread;
        if (StringUtils.isNotNull(initThread)) {
            initThreadObj = (AppInitThread) buildInstance(AppInitThread.class, initThread);
        } else {
            initThreadObj = null;
        }
    }

    public void setInitThreadObj(AppInitThread initThreadObj) {
        this.initThreadObj = initThreadObj;
        if (initThreadObj != null) {
            initThread = initThreadObj.getClass().getName();
        } else {
            initThread = null;
        }
    }

    public String getStageStyle() {
        return mainView.getStageStyle();
    }

    public StageStyle getStageStyleEnum() {
        return mainView.getStageStyleEnum();
    }

    public void setStageStyle(String stageStyle) {
        mainView.setStageStyle(stageStyle);
    }

    public void setStageStyleEnum(StageStyle stageStyleEnum) {
        mainView.setStageStyleEnum(stageStyleEnum);
    }

}
