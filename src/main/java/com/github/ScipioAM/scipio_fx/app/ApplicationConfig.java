package com.github.ScipioAM.scipio_fx.app;

import com.github.ScipioAM.scipio_fx.utils.StringUtils;
import javafx.stage.StageStyle;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

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

    /**
     * 标题
     */
    private String title;

    /**
     * 图标的路径
     */
    private String iconPath;
    private URL iconUrl;

    /**
     * 主界面的路径
     */
    private String mainViewPath;
    private URL mainViewUrl;

    /**
     * 启动加载图片的路径
     */
    private String splashImgPath;
    private URL splashImgUrl;

    /**
     * 启动监听器
     */
    private LaunchListener launchListener;

    /**
     * 初始化子线程
     */
    private AppInitThread initThread;

    /**
     * 主界面式样
     */
    private StageStyle mainStageStyle;

    /**
     * 主界面是否可拖拽
     */
    private boolean mainViewDraggable = false;

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
        ResourceBundle bundle;
        try {
            bundle = ResourceBundle.getBundle(appInstance.configPrefix());
        } catch (MissingResourceException e) {
            throw new FileNotFoundException("can not found configuration file: " + appInstance.configPrefix() + ".properties");
        }
        //加载配置文件
        //读取配置项
        this.setTitle(getStrFromConfig(bundle, "app.title", appInstance.title(), appInstance.configPrefix()));
        setIconPath(getStrFromConfig(bundle, "app.icon-path", null, appInstance.configPrefix()));
        setMainViewPath(getStrFromConfig(bundle, "app.main-view.path", null, appInstance.configPrefix()));
        String splashImgPath = getStrFromConfig(bundle, "app.splash-img.path", null, appInstance.configPrefix());
        if (splashImgPath == null) {
            splashImgPath = DEFAULT_SPLASH_IMG_PATH;
        }
        setSplashImgPath(splashImgPath);
        resolveStageStyle(getStrFromConfig(bundle, "app.main-view.stage-style", null, appInstance.configPrefix()));
        resolveDraggable(getStrFromConfig(bundle, "app.main-view.draggable", null, appInstance.configPrefix()));
        //app里重写的优先级最高，覆盖配置文件里设定的
        setMainViewUrl(appInstance.mainViewUrl());
        setIconUrl(appInstance.iconUrl());
        setSplashImgUrl(appInstance.splashImgUrl());
        //构建启动监听器
        LaunchListener bindLL = appInstance.bindLaunchListener();
        if (bindLL == null) {
            Object llObj = getInstanceFromConfig(LaunchListener.class, "app.launch-listener", bundle, appInstance.configPrefix());
            if (llObj != null) {
                setLaunchListener((LaunchListener) llObj);
            }
        } else {
            setLaunchListener(bindLL);
        }
        //初始化子线程
        AppInitThread bindIT = appInstance.bindInitThread();
        if (bindIT == null) {
            Object itObj = getInstanceFromConfig(AppInitThread.class, "app.init-thread", bundle, appInstance.configPrefix());
            if (itObj != null) {
                setInitThread((AppInitThread) itObj);
            }
        } else {
            setInitThread(bindIT);
        }
        return this;
    }

    public ApplicationConfig loadConfig() throws Exception {
        if (appClass != null) {
            return loadConfig(appClass);
        } else if (appInstance != null) {
            return loadConfig(appInstance.getClass());
        } else {
            throw new IllegalStateException("");
        }
    }

    //==============================================================================================================================

    public URL getMainViewUrl() throws IOException {
        if (mainViewUrl == null) {
            mainViewUrl = resolveUrl(mainViewPath, "mainViewPath", true);
        }
        return mainViewUrl;
    }

    public URL getIconUrl() throws IOException {
        if (iconUrl == null) {
            iconUrl = resolveUrl(iconPath, "iconPath", false);
        }
        return iconUrl;
    }

    public URL getSplashImgUrl() throws IOException {
        if (splashImgUrl == null) {
            splashImgUrl = resolveUrl(splashImgPath, "splashImgUrl", false);
        }
        return splashImgUrl;
    }

    //==============================================================================================================================

    /**
     * 获取资源文件的url
     *
     * @param path    配置文件中设定的路径
     * @param errTips 错误提示
     * @param notNull 必须需要，否则抛异常
     * @throws FileNotFoundException 文件不存在，或者没有在module-info.java中将资源文件开放给本调用者
     */
    private URL resolveUrl(String path, String errTips, boolean notNull) throws FileNotFoundException {
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
     * 从配置文件中构建实例对象
     *
     * @param expectType 预期类型(父类)
     * @param key        配置key
     */
    private Object getInstanceFromConfig(Class<?> expectType, String key, ResourceBundle bundle, String configPrefix)
            throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Object obj = null;
        String className = getStrFromConfig(bundle, key, null, configPrefix);
        if (StringUtils.isNotNull(className)) {
            Class<?> actualType = Class.forName(className);
            if (actualType != expectType) {
                throw new ClassCastException("Expect type:[" + expectType + "], actual type:[" + actualType + "]");
            }
            obj = actualType.getDeclaredConstructor().newInstance();
        }
        return obj;
    }

    /**
     * 从配置文件中读取参数
     *
     * @param priorityValue 优先值，它为空白时才采纳配置文件里的参数
     */
    private String getStrFromConfig(ResourceBundle bundle, String key, String priorityValue, String configPrefix) {
        String value = null;
        try {
            value = StringUtils.isNotNull(priorityValue) ? priorityValue : bundle.getString(key);
        } catch (MissingResourceException e) {
            System.err.println("[" + key + "] not found in config file: " + configPrefix + ".properties");
        }
        return value;
    }

    private void resolveStageStyle(String configValue) {
        if (StringUtils.isNull(configValue)) {
            return;
        }
        String enumName = configValue.toUpperCase();
        try {
            setMainStageStyle(StageStyle.valueOf(enumName));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resolveDraggable(String configValue) {
        if (StringUtils.isNull(configValue)) {
            return;
        }
        String draggableStr = configValue.toLowerCase();
        if (draggableStr.equals("true") || draggableStr.equals("on") || draggableStr.equals("1")) {
            setMainViewDraggable(true);
        }
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

    public String getTitle() {
        return title;
    }

    public ApplicationConfig setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getIconPath() {
        return iconPath;
    }

    public ApplicationConfig setIconPath(String iconPath) {
        this.iconPath = iconPath;
        return this;
    }

    private ApplicationConfig setIconUrl(URL iconUrl) {
        this.iconUrl = iconUrl;
        return this;
    }

    public String getMainViewPath() {
        return mainViewPath;
    }

    public ApplicationConfig setMainViewPath(String mainViewPath) {
        this.mainViewPath = mainViewPath;
        return this;
    }

    private ApplicationConfig setMainViewUrl(URL mainViewUrl) {
        this.mainViewUrl = mainViewUrl;
        return this;
    }

    public String getSplashImgPath() {
        return splashImgPath;
    }

    public ApplicationConfig setSplashImgPath(String splashImgPath) {
        this.splashImgPath = splashImgPath;
        return this;
    }

    private ApplicationConfig setSplashImgUrl(URL splashImgUrl) {
        this.splashImgUrl = splashImgUrl;
        return this;
    }

    public LaunchListener getLaunchListener() {
        return launchListener;
    }

    public ApplicationConfig setLaunchListener(LaunchListener launchListener) {
        this.launchListener = launchListener;
        return this;
    }

    public AppInitThread getInitThread() {
        return initThread;
    }

    public ApplicationConfig setInitThread(AppInitThread initThread) {
        this.initThread = initThread;
        return this;
    }

    public StageStyle getMainStageStyle() {
        return mainStageStyle;
    }

    public ApplicationConfig setMainStageStyle(StageStyle mainStageStyle) {
        this.mainStageStyle = mainStageStyle;
        return this;
    }

    public boolean isMainViewDraggable() {
        return mainViewDraggable;
    }

    public ApplicationConfig setMainViewDraggable(boolean mainViewDraggable) {
        this.mainViewDraggable = mainViewDraggable;
        return this;
    }
}
