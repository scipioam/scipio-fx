package com.github.ScipioAM.scipio_fx.app.config;

import com.github.ScipioAM.scipio_fx.app.AppInitThread;
import com.github.ScipioAM.scipio_fx.app.LaunchListener;
import com.github.ScipioAM.scipio_fx.utils.StringUtils;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.Map;

/**
 * @since 2022/6/23
 */
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

    private Map<String, Object> custom;

    public URL getMainViewUrl() {
        return mainView == null ? null : mainView.getMainViewUrl();
    }

    public void setMainViewUrl(URL url) {
        mainView.setMainViewUrl(url);
    }

    public URL getMainViewUrl(Class<?> appClass) {
        return mainView.getMainViewUrl(appClass);
    }

    public URL getIconUrl(Class<?> appClass) {
        if (iconUrl == null) {
            iconUrl = resolveUrl(iconPath, appClass, "iconPath", false);
        }
        return iconUrl;
    }

    public URL getSplashImgUrl(Class<?> appClass) {
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

    public LaunchListener getLaunchListener() throws Exception {
        if (launchListenerObj == null && StringUtils.isNotNull(launchListener)) {
            launchListenerObj = (LaunchListener) buildInstance(LaunchListener.class, launchListener);
        }
        return launchListenerObj;
    }

    public void setLaunchListenerObj(LaunchListener launchListenerObj) {
        this.launchListenerObj = launchListenerObj;
        if (launchListenerObj != null) {
            launchListener = launchListenerObj.getClass().getName();
        } else {
            launchListener = null;
        }
    }

    public AppInitThread getInitThreadObj() throws Exception {
        if(initThreadObj == null && StringUtils.isNotNull(initThread)) {
            initThreadObj = (AppInitThread) buildInstance(AppInitThread.class, initThread);
        }
        return initThreadObj;
    }

    public AppInitThread getInitThreadObjDirectly() {
        return initThreadObj;
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

    //=======================================================================================================================================

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public URL getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(URL iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getSplashImgPath() {
        return splashImgPath;
    }

    public void setSplashImgPath(String splashImgPath) {
        this.splashImgPath = splashImgPath;
    }

    public URL getSplashImgUrl() {
        return splashImgUrl;
    }

    public void setSplashImgUrl(URL splashImgUrl) {
        this.splashImgUrl = splashImgUrl;
    }

    public void setLaunchListener(String launchListener) {
        this.launchListener = launchListener;
    }

    public LaunchListener getLaunchListenerObj() {
        return launchListenerObj;
    }

    public void setInitThread(String initThread) {
        this.initThread = initThread;
    }

    public String getInitThread() {
        return initThread;
    }

    public MainViewBean getMainView() {
        return mainView;
    }

    public void setMainView(MainViewBean mainView) {
        this.mainView = mainView;
    }

    public Map<String, Object> getCustom() {
        return custom;
    }

    public void setCustom(Map<String, Object> custom) {
        this.custom = custom;
    }
}
