package com.github.scipioam.scipiofx.framework.config;

import com.github.scipioam.scipiofx.framework.AppInitThread;
import com.github.scipioam.scipiofx.framework.JFXApplication;
import com.github.scipioam.scipiofx.framework.LaunchListener;
import com.github.scipioam.scipiofx.framework.exception.ConfigLoadException;
import com.github.scipioam.scipiofx.utils.StringUtils;
import lombok.Getter;
import lombok.Setter;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.net.URL;

/**
 * @author Alan Scipio
 * @since 2024/4/26
 */
@Setter
@Getter
public class AppConfigLoader {

    private JFXApplication appInstance;

    public AppConfigLoader() {
    }

    public AppConfigLoader(JFXApplication appInstance) {
        this.appInstance = appInstance;
    }

    public ConfigRootProperties loadConfig(Class<? extends JFXApplication> appClass, Class<? extends ConfigRootProperties> propertiesClass) {
        if (appInstance == null) {
            try {
                appInstance = appClass.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new ConfigLoadException("Got exception while create appInstance: " + e, e);
            }
        }

        Yaml yaml = new Yaml();
        ConfigRootProperties configRootProperties;
        AppProperties appProperties;
        String configFileName = configFileName();
        try {
            InputStream in = appClass.getResourceAsStream(configFileName);
            configRootProperties = yaml.loadAs(in, propertiesClass);
            if (configRootProperties == null) {
                throw new ConfigLoadException("Load config failed, loadConfig object is null");
            }
            appProperties = configRootProperties.getApp();
            if (appProperties == null) {
                throw new ConfigLoadException("Load config failed, app object is null");
            }
            appProperties.setAppClass(appClass);
        } catch (Exception e) {
            throw new ConfigLoadException("Got exception while Read config file [" + configFileName + "]: " + e, e);
        }

        //优先级处理:标题
        String title = appInstance.title();
        if (StringUtils.isNotBlank(title)) {
            appProperties.setTitle(title);
        }
        //优先级处理:主画面的路径
        URL mainViewUrl = appInstance.mainViewUrl();
        if (mainViewUrl != null) {
            appProperties.getView().setMainViewUrl(mainViewUrl);
        }
        //优先级处理:程序图标的路径
        URL iconUrl = appInstance.iconUrl();
        if (iconUrl != null) {
            appProperties.setIconUrl(iconUrl);
        }
        //优先级处理:启动画面的图片路径
        URL splashImgUrl = appInstance.splashImgUrl();
        if (splashImgUrl != null) {
            appProperties.setSplashImgUrl(splashImgUrl);
        }
        //优先级处理:启动监听器
        LaunchListener launchListener = appInstance.launchListener();
        if (launchListener != null) {
            appProperties.setLaunchListenerInstance(launchListener);
        }
        //优先级处理:初始化子线程
        AppInitThread initThread = appInstance.initThread();
        if (initThread != null) {
            appProperties.setInitThreadInstance(initThread);
        }

        return configRootProperties;
    }

    /**
     * 获取要读取的配置文件名
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

}
