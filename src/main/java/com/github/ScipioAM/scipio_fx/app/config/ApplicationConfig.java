package com.github.ScipioAM.scipio_fx.app.config;

import com.github.ScipioAM.scipio_fx.app.AppInitThread;
import com.github.ScipioAM.scipio_fx.app.JFXApplication;
import com.github.ScipioAM.scipio_fx.app.LaunchListener;
import com.github.ScipioAM.scipio_fx.app.mfx.MaterialFXInitializer;
import com.github.ScipioAM.scipio_fx.exception.ConfigLoadException;
import com.github.ScipioAM.scipio_fx.utils.StringUtils;
import javafx.stage.StageStyle;
import org.yaml.snakeyaml.Yaml;

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
public class ApplicationConfig extends BaseConfigBean {

    public final static String DEFAULT_SPLASH_IMG_PATH = "/img/splash.gif";

    private transient Class<? extends RootConfig> rootConfigClass;

    private transient Class<? extends JFXApplication> appClass;
    private transient JFXApplication appInstance;

    /**
     * 配置加载时的监听回调
     */
    private transient ConfigLoadListener loadListener;

    //==============================================================================================================================

    private String version;

    private String title;

    private String iconPath;
    private transient URL iconUrl;

    private String splashImgPath = DEFAULT_SPLASH_IMG_PATH;
    private transient URL splashImgUrl;

    private String launchListener;
    private transient LaunchListener llObj;

    private String initThread;
    private transient AppInitThread itObj;

    private MainViewBean mainView = new MainViewBean();

    private Map<String, Object> custom;

    private boolean useMaterialFX = false;
    private boolean isUseMaterialFXThemeOnly = false;
    private String materialFXInitializer;
    private transient MaterialFXInitializer materialFXInitializerObj;

    //==============================================================================================================================

    public static ApplicationConfig build(Class<? extends RootConfig> wrapperClass) {
        ApplicationConfig instance = new ApplicationConfig();
        instance.setRootConfigClass(wrapperClass);
        return instance;
    }

    public static ApplicationConfig build(Class<? extends RootConfig> wrapperClass, Class<? extends JFXApplication> appClass) {
        ApplicationConfig instance = build(wrapperClass);
        instance.setAppClass(appClass);
        return instance;
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
    public RootConfig loadConfig(Class<? extends JFXApplication> appClass) throws Exception {
        if (appInstance == null) {
            appInstance = appClass.getDeclaredConstructor().newInstance();
        }

        Yaml yaml = new Yaml();
        RootConfig rootConfig;
        //加载前的回调，此方法可整体替换原本的加载逻辑
        if (loadListener != null) {
            if (!loadListener.onLoad(yaml, this)) {
                if (rootConfigClass != null) {
                    try {
                        rootConfig = rootConfigClass.getDeclaredConstructor().newInstance();
                        rootConfig.setApp(this);
                        return rootConfig;
                    } catch (Exception e) {
                        return null;
                    }
                } else {
                    rootConfig = new RootConfig();
                    rootConfig.setApp(this);
                    return rootConfig;
                }
            }
        }

        //加载配置文件
        ApplicationConfig loadedAppBean;
        try {
            if (rootConfigClass == null) {
                rootConfigClass = RootConfig.class;
            }
            InputStream in = appClass.getResourceAsStream(configFileName());
            rootConfig = yaml.loadAs(in, rootConfigClass);
            loadedAppBean = rootConfig.getApp();
            rootConfig.setApp(this);
            if (loadedAppBean == null) {
                throw new ConfigLoadException("load config failed, loadConfig object is null");
            }
        } catch (Exception e) {
            throw new ConfigLoadException("Got exception while Read config file [" + configFileName() + "], " + e, e);
        }

        setVersion(loadedAppBean.getVersion());
        setCustom(loadedAppBean.getCustom());
        setMainView(loadedAppBean.getMainView());

        //优先级处理:标题
        if (StringUtils.isNotNull(appInstance.title())) {
            setTitle(appInstance.title());
        } else {
            setTitle(loadedAppBean.getTitle());
        }
        //优先级处理:主画面的路径
        URL mainViewUrl = appInstance.mainViewUrl();
        if (mainViewUrl != null) {
            setMainViewUrl(mainViewUrl);
        }
        //优先级处理:程序图标的路径
        URL iconUrl = appInstance.iconUrl();
        if (iconUrl != null) {
            setIconUrl(iconUrl);
        } else {
            setIconPath(loadedAppBean.getIconPath());
        }
        //优先级处理:启动画面的图片路径
        URL splashImgUrl = appInstance.splashImgUrl();
        if (splashImgUrl != null) {
            setSplashImgUrl(splashImgUrl);
        } else {
            setSplashImgPath(loadedAppBean.getSplashImgPath());
        }
        //优先级处理:启动监听器
        LaunchListener bindLL = appInstance.bindLaunchListener();
        if (bindLL != null) {
            setLaunchListenerObj(bindLL);
        } else {
            setLaunchListener(loadedAppBean.getLaunchListener());
        }
        //优先级处理:初始化子线程
        AppInitThread bindIT = appInstance.bindInitThread();
        if (bindIT != null) {
            setInitThreadObj(bindIT);
        } else {
            setInitThread(loadedAppBean.getInitThread());
        }

        //加载后的回调
        if (loadListener != null) {
            loadListener.afterLoad(yaml, rootConfig, this);
        }

        return rootConfig;
    }

    public RootConfig loadConfig() throws Exception {
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

    //==============================================================================================================================

    public String getLaunchListener() {
        return launchListener;
    }

    public void setLaunchListener(String launchListener) {
        this.launchListener = launchListener;
    }

    public LaunchListener getLaunchListenerObj() {
        if (llObj == null && StringUtils.isNotNull(launchListener)) {
            try {
                llObj = (LaunchListener) buildInstance(LaunchListener.class, launchListener);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        if (llObj != null) {
            return llObj;
        } else if (appInstance != null) {
            return appInstance.bindLaunchListener();
        } else {
            return null;
        }
    }

    public String getInitThread() {
        return initThread;
    }

    public void setInitThread(String initThread) {
        this.initThread = initThread;
    }

    public void setLaunchListenerObj(LaunchListener launchListenerObj) {
        this.llObj = launchListenerObj;
        if (launchListenerObj != null) {
            launchListener = launchListenerObj.getClass().getName();
        } else {
            launchListener = null;
        }
    }

    public AppInitThread getInitThreadObj() {
        if (itObj == null && StringUtils.isNotNull(initThread)) {
            try {
                itObj = (AppInitThread) buildInstance(AppInitThread.class, initThread);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        if (itObj != null) {
            return itObj;
        } else if (appInstance != null) {
            return appInstance.bindInitThread();
        } else {
            return null;
        }
    }

    public void setInitThreadObj(AppInitThread initThreadObj) {
        this.itObj = initThreadObj;
        if (initThreadObj != null) {
            initThread = initThreadObj.getClass().getName();
        } else {
            initThread = null;
        }
    }

    public String getMainViewPath() {
        return mainView.getPath();
    }

    public StageStyle getMainStageStyle() {
        return mainView.getStageStyleEnum();
    }

    public void setMainStageStyle(StageStyle mainStageStyle) {
        mainView.setStageStyleEnum(mainStageStyle);
    }

    public boolean isMainViewDraggable() {
        return mainView.isDraggableBool();
    }

    public void setMainViewDraggable(boolean mainViewDraggable) {
        mainView.setDraggableBool(mainViewDraggable);
    }

    public boolean isMainViewResizable() {
        return mainView.isResizableBool();
    }

    public void setMainViewResizable(boolean mainViewResizable) {
        mainView.setResizableBool(mainViewResizable);
    }

    public URL getMainViewUrl() {
        return mainView.getMainViewUrl(appClass);
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

    //==============================================================================================================================

    public Class<? extends JFXApplication> getAppClass() {
        return appClass;
    }

    public void setAppClass(Class<? extends JFXApplication> appClass) {
        this.appClass = appClass;
    }

    public JFXApplication getAppInstance() {
        return appInstance;
    }

    public void setAppInstance(JFXApplication appInstance) {
        this.appInstance = appInstance;
    }

    public ConfigLoadListener getLoadListener() {
        return loadListener;
    }

    public void setLoadListener(ConfigLoadListener loadListener) {
        this.loadListener = loadListener;
    }

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

    public Class<? extends RootConfig> getRootConfigClass() {
        return rootConfigClass;
    }

    public void setRootConfigClass(Class<? extends RootConfig> rootConfigClass) {
        this.rootConfigClass = rootConfigClass;
    }

    public boolean isUseMaterialFX() {
        return useMaterialFX;
    }

    public void setUseMaterialFX(boolean useMaterialFX) {
        this.useMaterialFX = useMaterialFX;
    }

    public boolean isUseMaterialFXThemeOnly() {
        return isUseMaterialFXThemeOnly;
    }

    public void setUseMaterialFXThemeOnly(boolean useMaterialFXThemeOnly) {
        isUseMaterialFXThemeOnly = useMaterialFXThemeOnly;
    }

    public String getMaterialFXInitializer() {
        return materialFXInitializer;
    }

    public void setMaterialFXInitializer(String materialFXInitializer) {
        this.materialFXInitializer = materialFXInitializer;
    }

    public MaterialFXInitializer getMaterialFXInitializerObj() {
        if (materialFXInitializerObj == null && StringUtils.isNotNull(materialFXInitializer)) {
            try {
                materialFXInitializerObj = (MaterialFXInitializer) buildInstance(MaterialFXInitializer.class, materialFXInitializer);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else if (materialFXInitializerObj == null && StringUtils.isNotNull(materialFXInitializer)) {

        }
        return materialFXInitializerObj;
    }

    public void setMaterialFXInitializerObj(MaterialFXInitializer materialFXInitializerObj) {
        this.materialFXInitializerObj = materialFXInitializerObj;
    }
}
