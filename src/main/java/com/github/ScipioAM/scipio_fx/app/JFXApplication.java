package com.github.ScipioAM.scipio_fx.app;

import com.github.ScipioAM.scipio_fx.controller.BaseController;
import com.github.ScipioAM.scipio_fx.view.FXMLView;
import com.github.ScipioAM.scipio_fx.view.FXMLViewLoader;
import com.github.ScipioAM.scipio_fx.utils.StringUtils;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 应用程序
 *
 * @author Alan Scipio
 * @since 2022/2/22
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class JFXApplication extends Application implements ApplicationInterface {

    /**
     * 启动界面
     */
    @Setter(AccessLevel.NONE) @Getter(AccessLevel.NONE)
    protected static URL mainViewUrl = null;

    /**
     * app配置
     */
    protected final ApplicationConfig config = new ApplicationConfig();

    /**
     * 主界面的stage
     */
    @Setter(AccessLevel.NONE)
    protected Stage primaryStage;

    /**
     * 主界面对线
     */
    @Setter(AccessLevel.NONE)
    protected FXMLView mainView;

    /**
     * 启动界面
     */
    @Setter(AccessLevel.NONE)
    protected static SplashScreen splashScreen = null;

    /**
     * 线程池
     */
    protected final ExecutorService threadPool = Executors.newCachedThreadPool();

    //=========================================== ↓↓↓↓↓↓ 启动入口API ↓↓↓↓↓↓ ===========================================

    /**
     * 程序启动
     *
     * @param appClass     继承本类的子类
     * @param args         main方法的入参
     * @param splashScreen 自定义启动画面
     */
    public static void launchApp(final Class<? extends JFXApplication> appClass, String[] args, SplashScreen splashScreen) {
        JFXApplication.splashScreen = splashScreen;
        launch(appClass, args);
    }

    /**
     * 程序启动
     *
     * @param appClass      继承本类的子类
     * @param args          main方法的入参
     * @param splashVisible 是否显示启动画面(构建默认的启动画面)
     */
    public static void launchApp(final Class<? extends JFXApplication> appClass, String[] args, boolean splashVisible) {
        JFXApplication.splashScreen = splashVisible ? new SplashScreen() : null;
        launch(appClass, args);
    }

    /**
     * 程序启动(没有启动画面)
     *
     * @param appClass 继承本类的子类
     * @param args     main方法的入参
     */
    public static void launchApp(final Class<? extends JFXApplication> appClass, String[] args) {
        launch(appClass, args);
    }

    public static void launchApp(String[] args, SplashScreen splashScreen, URL mainViewUrl) {
        JFXApplication.mainViewUrl = mainViewUrl;
        launchApp(JFXApplication.class, args, splashScreen);
    }

    public static void launchApp(String[] args, boolean splashVisible, URL mainViewUrl) {
        JFXApplication.mainViewUrl = mainViewUrl;
        launchApp(JFXApplication.class, args, splashVisible);
    }

    public static void launchApp(String[] args, URL mainViewUrl) {
        JFXApplication.mainViewUrl = mainViewUrl;
        launchApp(JFXApplication.class, args);
    }

    //=========================================== ↓↓↓↓↓↓ 程序启动 ↓↓↓↓↓↓ ===========================================

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            this.primaryStage = primaryStage;
            //加载配置
            loadConfig();
            //初始化主界面
            initPrimaryStage(primaryStage);
            //画面显示
            if (splashScreen != null && splashScreen.isVisible()) {
                //显示启动画面
                Stage splashStage = new Stage(StageStyle.TRANSPARENT);
                String iconPath = config.getIconPath();
                if (StringUtils.isNotNull(iconPath)) {
                    splashStage.getIcons().addAll(new Image(Objects.requireNonNull(getClass().getResource(iconPath)).toExternalForm()));
                }
                Scene splashScene = new Scene(splashScreen.buildViews(), Color.TRANSPARENT);
                splashStage.setScene(splashScene);
                splashStage.setResizable(false);
                splashScreen.setStage(splashStage);
                splashStage.show();
            } else {
                //不显示启动画面，直接显示主画面
                mainView = buildMainView();
                if (config.getLaunchListener() != null) {
                    config.getLaunchListener().onFinishInit(this, mainView);
                }
                showMainView();
                //显示主界面后的回调
                if (config.getLaunchListener() != null) {
                    config.getLaunchListener().afterShowMainView(this, mainView);
                }
            }
            //启动初始化的子线程
            AppInitThread initThread = bindInitThread();
            if (initThread != null) {
                initThread.setApplication(this).setLaunchListener(config.getLaunchListener());
                threadPool.submit(initThread);
            }
        } catch (Exception e) {
            if (config.getLaunchListener() != null) {
                config.getLaunchListener().onLaunchError(this, e);
            } else {
                throw e;
            }
        }
    }//end of start()

    //=========================================== ↓↓↓↓↓↓ 接口默认实现 ↓↓↓↓↓↓ ===========================================

    @Override
    public String iconPath() {
        return config.getIconPath();
    }

    @Override
    public String title() {
        return config.getTitle();
    }

    @Override
    public URL mainViewUrl() {
        return mainViewUrl;
    }

    @Override
    public FXMLView buildMainView() throws IOException {
        URL mainViewUrl = mainViewUrl();
        if (mainViewUrl == null) {
            throw new IOException("Launch application failed, [mainViewUrl] did not set !");
        }
        return FXMLViewLoader.build().load(mainViewUrl, null);
    }

    //=========================================== ↓↓↓↓↓↓ 工具方法 ↓↓↓↓↓↓ ===========================================

    /**
     * 初始化主界面
     */
    protected void initPrimaryStage(Stage primaryStage) {
        //设置图标
        String iconPath = config.getIconPath();
        if (StringUtils.isNotNull(iconPath)) {
            InputStream in = getClass().getResourceAsStream(iconPath);
            if (in == null) {
                throw new IllegalStateException("get resource as stream from: [" + iconPath + "] failed");
            }
            primaryStage.getIcons().add(new Image(in));
        }
        //设置标题
        String title = config.getTitle();
        if (StringUtils.isNotNull(title)) {
            primaryStage.setTitle(title);
        }
    }

    /**
     * 显示主界面
     */
    protected void showMainView() {
        primaryStage.setScene(new Scene(mainView.getView()));
        if (splashScreen != null && splashScreen.isVisible()) {
            Stage splashStage = splashScreen.getStage();
            splashStage.close();
            splashStage.setScene(null);
            splashScreen.setStage(null);
        }
        BaseController mainController = mainView.getController();
        mainController.setThisStage(primaryStage);
        primaryStage.show();
    }

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
    protected void loadConfig()
            throws ClassNotFoundException, ClassCastException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        ResourceBundle bundle;
        try {
            bundle = ResourceBundle.getBundle(getConfigPrefix());
        } catch (MissingResourceException e) {
            System.out.println("No config file found: " + getConfigPrefix() + ".properties");
            return;
        }
        //加载配置文件
        //读取配置项
        config.setTitle(getStrFromConfig(bundle, "app.title", title()));
        config.setIconPath(getStrFromConfig(bundle, "app.icon-path", iconPath()));
        //构建启动监听器
        LaunchListener bindLL = bindLaunchListener();
        if (bindLL == null) {
            Object llObj = getInstanceFromConfig(LaunchListener.class, "app.launch-listener", bundle);
            if (llObj != null) {
                config.setLaunchListener((LaunchListener) llObj);
            }
        } else {
            config.setLaunchListener(bindLL);
        }
        //初始化子线程
        AppInitThread bindIT = bindInitThread();
        if (bindIT == null) {
            Object itObj = getInstanceFromConfig(AppInitThread.class, "app.init-thread", bundle);
            if (itObj != null) {
                config.setInitThread((AppInitThread) itObj);
            }
        } else {
            config.setInitThread(bindIT);
        }
    }

    /**
     * 从配置文件中构建实例对象
     *
     * @param expectType 预期类型(父类)
     * @param key        配置key
     */
    private Object getInstanceFromConfig(Class<?> expectType, String key, ResourceBundle bundle)
            throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Object obj = null;
        String className = getStrFromConfig(bundle, key, null);
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
     * @param priorityValue 优先值，它为null时才采纳配置文件里的参数
     */
    private String getStrFromConfig(ResourceBundle bundle, String key, String priorityValue) {
        String value = null;
        try {
            value = StringUtils.isNotNull(priorityValue) ? priorityValue : bundle.getString(key);
        } catch (MissingResourceException e) {
            System.err.println("[" + key + "] not found in config file: " + getConfigPrefix() + ".properties");
        }
        return value;
    }

}
