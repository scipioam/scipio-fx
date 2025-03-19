package com.github.scipioam.scipiofx.framework;

import com.github.scipioam.scipiofx.framework.config.AppConfigLoader;
import com.github.scipioam.scipiofx.framework.config.AppProperties;
import com.github.scipioam.scipiofx.framework.config.ConfigRootProperties;
import com.github.scipioam.scipiofx.framework.exception.ConfigLoadException;
import com.github.scipioam.scipiofx.framework.fxml.FXMLView;
import com.github.scipioam.scipiofx.framework.fxml.FXMLViewLoader;
import com.github.scipioam.scipiofx.view.SplashScreen;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Alan Scipio
 * @since 2024/4/26
 */
@SuppressWarnings("LombokGetterMayBeUsed")
public abstract class JFXApplication extends Application {

    private static JFXApplication instance;

    private final LogHelper log;

    protected final AppContext context;

    protected AppProperties appProperties;

    protected Stage mainStage;

    protected FXMLView mainView;

    protected SplashScreen splashScreen;

    protected double xOffset;
    protected double yOffset;

    public JFXApplication() {
        instance = this;
        log = new LogHelper(null);
        // 加载配置
        long startTime = System.currentTimeMillis();
        AppConfigLoader configLoader = new AppConfigLoader(this);
        ConfigRootProperties configRootProperties;
        try {
            configRootProperties = configLoader.loadConfig(this.getClass(), this.configRootPropertiesType());
            appProperties = configRootProperties.getApp();
            log.info("Load config file [{}] success, cost {}ms", configLoader.configFileName(), (System.currentTimeMillis() - startTime));
        } catch (Exception e) {
            log.error("Load config file [{}] error", configLoader.configFileName(), e);
            LaunchListener launchListener = appProperties == null ? this.launchListener() : appProperties.getLaunchListenerInstance();
            if (launchListener != null) {
                launchListener.onLaunchError(this, e);
            }
            throw e;
        }

        // 初始化上下文
        try {
            // 创建上下文
            context = buildContext();
            AppContext.registerInstance(context);
            // 设置上下文属性
            context.setApplication(this);
            context.setApplicationClass(this.getClass());
            context.setConfigRootProperties(configRootProperties);
            context.setAppProperties(appProperties);
            context.setCustomProperties(configRootProperties.getCustomProperties());
            context.setLaunchListener(appProperties.getLaunchListenerInstance());
            context.setInitThread(appProperties.getInitThreadInstance());
            // 创建线程池
            context.setThreadPool(buildThreadPool());
        } catch (Exception e) {
            log.error("Init context error", e);
            LaunchListener launchListener = appProperties == null ? this.launchListener() : appProperties.getLaunchListenerInstance();
            if (launchListener != null) {
                launchListener.onLaunchError(this, e);
            }
            throw e;
        }
    }

    @Override
    public void start(Stage mainStage) throws Exception {
        setUserAgentStylesheet(STYLESHEET_MODENA);
        this.mainStage = mainStage;
        context.setMainStage(mainStage);
        try {
            splashScreen = this.splashScreen();
            if (splashScreen != null) {
                // 显示启动画面
                splashScreen.buildAndShowStage();
            }

            //启动初始化线程
            AppInitThread initThread = context.getInitThread();
            if (initThread != null) {
                initThread.setApplication(this);
                initThread.setContext(context);
                initThread.setLaunchListener(context.getLaunchListener());
                initThread.setSplashScreen(splashScreen);
                context.submitTask(initThread);
            }

            if (splashScreen == null) {
                // 直接显示mainView
                Scene mainScene = buildMainScene();
                initMainStage(mainScene);
                showMainView();
            } else if (initThread == null) {
                // 没有初始化子线程，但有SplashScreen，则启动默认的空初始化线程（单纯等n秒）
                EmptyAppInitThread emptyAppInitThread = new EmptyAppInitThread();
                emptyAppInitThread.setApplication(this);
                emptyAppInitThread.setContext(context);
                emptyAppInitThread.setLaunchListener(context.getLaunchListener());
                emptyAppInitThread.setSplashScreen(splashScreen);
                context.submitTask(emptyAppInitThread);
            }
        } catch (Exception e) {
            log.error("start main stage error", e);
            if (context.getLaunchListener() != null) {
                context.getLaunchListener().onLaunchError(this, e);
            }
            throw e;
        }
    }

    /**
     * 构建主画面内容
     */
    protected Scene buildMainScene() throws Exception {
        FXMLViewLoader fxmlViewLoader = new FXMLViewLoader();
        URL mainViewUrl = appProperties.getView().getMainViewUrl();
        if (mainViewUrl == null) {
            throw new ConfigLoadException("Main view URL is null");
        }
        mainView = fxmlViewLoader.load(mainViewUrl, null);

        context.setMainView(mainView);
        BaseController mainController = mainView.getController();
        context.setMainController(mainController);

        Scene scene = new Scene(mainView.getSelf());
        if (context.getLaunchListener() != null) {
            context.getLaunchListener().beforeLoadMainScene(mainStage, scene);
        }

        // 初始化MaterialFx
        try {
            if (appProperties.isMaterialFxEnabled()) {
                appProperties.getMaterialFx().init();
            }
        } catch (Exception e) {
            log.info("MaterialFx init failed: {}", e);
        }

        return scene;
    }

    /**
     * 初始化主画面
     */
    protected void initMainStage(Scene mainScene) {
        long startTime = System.currentTimeMillis();
        // 设置主画面
        mainStage.setScene(mainScene);
        mainView.setStage(mainStage);
        mainView.getController().setMyWindow(mainStage);
        // 设置标题
        String title = appProperties.getTitle();
        if (title != null) {
            mainStage.setTitle(title);
        }
        // 设置图标
        URL iconUrl = appProperties.getIconUrl();
        if (iconUrl != null) {
            mainStage.getIcons().add(new Image(iconUrl.toExternalForm()));
        }
        // 主画面式样
        StageStyle stageStyle = appProperties.getView().getStageStyleEnum();
        if (stageStyle != null) {
            mainStage.initStyle(stageStyle);
        }
        // 让主窗体可以被随意拖拽
        if (appProperties.getView().isDraggableFlag()) {
            Parent rootNode = mainView.getSelf();
            rootNode.setOnMousePressed(event -> {
                xOffset = mainStage.getX() - event.getScreenX();
                yOffset = mainStage.getY() - event.getScreenY();
            });
            rootNode.setOnMouseDragged(event -> {
                mainStage.setX(event.getScreenX() + xOffset);
                mainStage.setY(event.getScreenY() + yOffset);
            });
        }
        // 设置主窗体是否可以改变大小
        if (!appProperties.getView().isResizableFlag()) {
            mainStage.setResizable(false);
        }
        log.info("Initialize main stage success, cost {}ms", (System.currentTimeMillis() - startTime));
    }

    /**
     * 显示主画面
     */
    public void showMainView() {
        if (context.getLaunchListener() != null) {
            context.getLaunchListener().beforeShowMainView(this, mainView, mainStage.getScene());
        }
        // 如果有启动画面，则关闭启动画面
        if (splashScreen != null) {
            splashScreen.closeStage();
            log.info("Splash screen closed, show duration: {}ms", splashScreen.getShowDuration());
        }
        mainStage.show();
        if (context.getLaunchListener() != null) {
            context.getLaunchListener().afterShowMainView(this, mainView);
        }
    }

    @Override
    public void stop() throws Exception {
        if (mainView.getController() != null) {
            mainView.getController().onStop();
        }
    }

    /**
     * 设置配置文件对应的类型
     */
    public Class<? extends ConfigRootProperties> configRootPropertiesType() {
        return ConfigRootProperties.class;
    }

    /**
     * 创建上下文
     */
    public AppContext buildContext() {
        return new AppContext();
    }

    /**
     * 创建线程池
     */
    protected ExecutorService buildThreadPool() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    /**
     * 配置文件名
     */
    public String configPrefix() {
        return "app-config";
    }

    /**
     * 程序标题（优先级高于配置文件）
     */
    public String title() {
        return null;
    }

    /**
     * 主画面的URL（优先级高于配置文件）
     */
    public URL mainViewUrl() {
        return null;
    }

    /**
     * 主画面图标的URL（优先级高于配置文件）
     */
    public URL iconUrl() {
        return null;
    }

    /**
     * 启动画面的URL（优先级高于配置文件）
     */
    public URL splashImgUrl() {
        return null;
    }

    /**
     * 启动监听器（优先级高于配置文件）
     */
    public LaunchListener launchListener() {
        return null;
    }

    /**
     * 初始化线程（优先级高于配置文件）
     */
    public AppInitThread initThread() {
        return null;
    }

    /**
     * 启动画面实例化（优先级高于配置文件）
     */
    public SplashScreen splashScreen() {
        URL splashImgUrl = appProperties.getSplashImgUrl();
        if (splashImgUrl != null) {
            return SplashScreen.create(splashImgUrl, true, appProperties.isSplashProgressBarFlag());
        } else {
            return null;
        }
    }

    public static JFXApplication getInstance() {
        return instance;
    }

    public AppProperties getAppProperties() {
        return appProperties;
    }

    public Stage getMainStage() {
        return mainStage;
    }

    public FXMLView getMainView() {
        return mainView;
    }

    public static AppContext getContext() {
        return instance.context;
    }
}
