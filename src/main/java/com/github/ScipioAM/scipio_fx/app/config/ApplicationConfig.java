package com.github.ScipioAM.scipio_fx.app.config;

import com.github.ScipioAM.scipio_fx.app.AppInitThread;
import com.github.ScipioAM.scipio_fx.app.JFXApplication;
import com.github.ScipioAM.scipio_fx.app.LaunchListener;
import com.github.ScipioAM.scipio_fx.exception.ConfigLoadException;
import com.github.ScipioAM.scipio_fx.utils.StringUtils;
import javafx.stage.StageStyle;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Map;

/**
 * app配置
 *
 * @author Alan Scipio
 * @since 2022/2/22
 */
public class ApplicationConfig {

    public final static String DEFAULT_SPLASH_IMG_PATH = "/img/splash.gif";

    private Class<? extends JFXApplication> appClass;
    private JFXApplication appInstance;

    private AppConfigBean configBean;

    /**
     * 配置加载时的监听回调
     */
    private ConfigLoadListener loadListener;

    //==============================================================================================================================

    public static ApplicationConfig build() {
        return new ApplicationConfig();
    }

    public static ApplicationConfig build(Class<? extends JFXApplication> appClass) {
        return build().setAppClass(appClass);
    }

    //==============================================================================================================================

    /**
     * 加载配置
     *
     * @throws ClassNotFoundException    找不到配置的启动监听器
     * @throws ClassCastException        配置的启动监听器类没有实现{@link LaunchListener}
     * @throws NoSuchMethodException     启动监听器没有空参构造方法
     * @throws InvocationTargetException 反射构建启动监听器的实例失败
     * @throws InstantiationException    反射构建启动监听器的实例失败
     * @throws IllegalAccessException    反射构建启动监听器的实例失败
     */
    public ApplicationConfig loadConfig(Class<? extends JFXApplication> appClass) throws Exception {
        if (appInstance == null) {
            appInstance = appClass.getDeclaredConstructor().newInstance();
        }

        Yaml yaml = new Yaml(new Constructor(AppConfigBeanWrapper.class));
        //加载前的回调，此方法可整体替换原本的加载逻辑
        if (loadListener != null) {
            if (!loadListener.onLoad(yaml, this)) {
                return this;
            }
        }

        //加载配置文件
        try {
            InputStream in = appClass.getResourceAsStream(configFileName());
            AppConfigBeanWrapper wrapper = yaml.load(in);
            configBean = wrapper.getApp();
            if (configBean == null) {
                throw new ConfigLoadException("load config failed, configBean is null");
            }
        } catch (Exception e) {
            throw new ConfigLoadException("Got exception while Read config file [" + configFileName() + "], " + e, e);
        }

        //优先级处理:标题
        if (StringUtils.isNotNull(appInstance.title())) {
            configBean.setTitle(appInstance.title());
        }
        //优先级处理:主画面的路径
        URL mainViewUrl = appInstance.mainViewUrl();
        if (mainViewUrl != null) {
            configBean.setMainViewUrl(mainViewUrl);
        }
        //优先级处理:程序图标的路径
        URL iconUrl = appInstance.iconUrl();
        if (iconUrl != null) {
            configBean.setIconUrl(iconUrl);
        }
        //优先级处理:启动画面的图片路径
        URL splashImgUrl = appInstance.splashImgUrl();
        if (splashImgUrl != null) {
            configBean.setSplashImgUrl(splashImgUrl);
        }
        //优先级处理:启动监听器
        LaunchListener bindLL = appInstance.bindLaunchListener();
        if (bindLL != null) {
            configBean.setLaunchListenerObj(bindLL);
        }
        //优先级处理:初始化子线程
        AppInitThread bindIT = appInstance.bindInitThread();
        if (bindIT != null) {
            configBean.setInitThreadObj(bindIT);
        }

        //加载后的回调
        if (loadListener != null) {
            loadListener.afterLoad(yaml, configBean, this);
        }
        return this;
    }

    public ApplicationConfig loadConfig() throws Exception {
        if (appClass != null) {
            return loadConfig(appClass);
        } else if (appInstance != null) {
            return loadConfig(appInstance.getClass());
        } else {
            throw new IllegalStateException("appClass is not been set");
        }
    }

    //==============================================================================================================================

    /**
     * 获取读取的配置文件名
     */
    public String configFileName() {
        String configPrefix = appInstance.configPrefix();
        StringBuilder s = new StringBuilder();
        if (configPrefix.charAt(0) != '/') {
            s.append("/");
        }
        s.append(configPrefix).append(".yaml");
        return s.toString();
    }

    public URL getMainViewUrl() {
        return configBean == null ? null : configBean.getMainViewUrl(appClass);
    }

    public URL getIconUrl() {
        return configBean == null ? null : configBean.getIconUrl(appClass);
    }

    public URL getSplashImgUrl() {
        return configBean == null ? null : configBean.getSplashImgUrl(appClass);
    }

    //==============================================================================================================================

    public Class<? extends JFXApplication> getAppClass() {
        return appClass;
    }

    public ApplicationConfig setAppClass(Class<? extends JFXApplication> appClass) {
        this.appClass = appClass;
        return this;
    }

    public JFXApplication getAppInstance() {
        return appInstance;
    }

    public ApplicationConfig setAppInstance(JFXApplication appInstance) {
        this.appInstance = appInstance;
        return this;
    }

    public ConfigLoadListener getLoadListener() {
        return loadListener;
    }

    public ApplicationConfig setLoadListener(ConfigLoadListener loadListener) {
        this.loadListener = loadListener;
        return this;
    }

    public String getTitle() {
        return configBean.getTitle();
    }

    public ApplicationConfig setTitle(String title) {
        configBean.setTitle(title);
        return this;
    }

    public String getIconPath() {
        return configBean.getIconPath();
    }

    public ApplicationConfig setIconPath(String iconPath) {
        configBean.setIconPath(iconPath);
        return this;
    }

    private ApplicationConfig setIconUrl(URL iconUrl) {
        configBean.setIconUrl(iconUrl);
        return this;
    }

    public String getMainViewPath() {
        return configBean.getMainViewPath();
    }

    public ApplicationConfig setMainViewPath(String mainViewPath) {
        configBean.setMainViewPath(mainViewPath);
        return this;
    }

    private ApplicationConfig setMainViewUrl(URL mainViewUrl) {
        configBean.setMainViewUrl(mainViewUrl);
        return this;
    }

    public String getSplashImgPath() {
        return configBean.getSplashImgPath();
    }

    public ApplicationConfig setSplashImgPath(String splashImgPath) {
        configBean.setSplashImgPath(splashImgPath);
        return this;
    }

    private ApplicationConfig setSplashImgUrl(URL splashImgUrl) {
        configBean.setSplashImgUrl(splashImgUrl);
        return this;
    }

    public LaunchListener getLaunchListener() {
        return configBean.getLaunchListenerObj();
    }

    public ApplicationConfig setLaunchListener(LaunchListener launchListener) {
        configBean.setLaunchListenerObj(launchListener);
        return this;
    }

    public AppInitThread getInitThread() {
        return configBean.getInitThreadObj();
    }

    public ApplicationConfig setInitThread(AppInitThread initThread) {
        configBean.setInitThreadObj(initThread);
        return this;
    }

    public StageStyle getMainStageStyle() {
        return configBean.getStageStyleEnum();
    }

    public ApplicationConfig setMainStageStyle(StageStyle mainStageStyle) {
        configBean.setStageStyleEnum(mainStageStyle);
        return this;
    }

    public boolean isMainViewDraggable() {
        return configBean.isMainViewDraggable();
    }

    public ApplicationConfig setMainViewDraggable(boolean mainViewDraggable) {
        configBean.setMainViewDraggable(mainViewDraggable);
        return this;
    }

    public String getVersion() {
        return configBean.getVersion();
    }

    public ApplicationConfig setVersion(String version) {
        configBean.setVersion(version);
        return this;
    }

    public Map<String, Object> getCustom() {
        return configBean.getCustom();
    }

    public ApplicationConfig setCustom(Map<String, Object> custom) {
        configBean.setCustom(custom);
        return this;
    }

}
