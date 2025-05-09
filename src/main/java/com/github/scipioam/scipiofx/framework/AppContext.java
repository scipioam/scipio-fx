package com.github.scipioam.scipiofx.framework;

import com.github.scipioam.scipiofx.framework.config.AppProperties;
import com.github.scipioam.scipiofx.framework.config.ConfigRootProperties;
import com.github.scipioam.scipiofx.framework.config.ViewProperties;
import com.github.scipioam.scipiofx.framework.fxml.FXMLView;
import javafx.stage.Stage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * 程序上下文
 *
 * @author Alan Scipio
 * @since 2024/4/26
 */
@Setter(AccessLevel.PACKAGE)
@Getter
public class AppContext {

    private static final Map<Class<? extends AppContext>, AppContext> instances = new ConcurrentHashMap<>();

    public static <T extends AppContext> T getInstance(Class<T> clazz) {
        return clazz.cast(instances.get(clazz));
    }

    public static AppContext getInstance() {
        return instances.values().iterator().next();
    }

    static void registerInstance(AppContext context) {
        instances.put(context.getClass(), context);
    }

    // APP Objects

    protected JFXApplication application;

    protected Class<? extends JFXApplication> applicationClass;

    protected LaunchListener launchListener;

    protected AppInitThread initThread;

    protected ExecutorService threadPool;

    // Properties

    protected ConfigRootProperties configRootProperties;

    protected AppProperties appProperties;

    protected Map<String, Object> customProperties;

    public ViewProperties getViewProperties() {
        return appProperties.getView();
    }

    private File externalConfigFile; // 只有在使用了jar包外的配置文件时，此字段才有值

    // UI Objects

    protected Stage mainStage;

    protected FXMLView mainView;

    protected BaseController mainController;

    /**
     * 发起子线程任务
     */
    public void submitTask(Runnable task) {
        if (threadPool != null) {
            threadPool.submit(task);
        } else {
            new Thread(task).start();
        }
    }

    /**
     * 发起子线程任务，并返回Future对象
     */
    public <T> Future<T> submitFutureTask(Callable<T> task) {
        if (threadPool != null) {
            return threadPool.submit(task);
        } else {
            throw new IllegalStateException("No thread pool available");
        }
    }

    /**
     * 初始化线程是否已经结束
     *
     * @return true：已结束
     */
    public boolean isInitThreadFinished() {
        return initThread == null || initThread.isFinished();
    }

}
