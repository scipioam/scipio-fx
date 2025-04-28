package com.github.scipioam.scipiofx.framework.config;

import com.github.scipioam.scipiofx.framework.AppInitThread;
import com.github.scipioam.scipiofx.framework.EmptyAppInitThread;
import com.github.scipioam.scipiofx.framework.JFXApplication;
import com.github.scipioam.scipiofx.framework.LaunchListener;
import com.github.scipioam.scipiofx.framework.exception.ConfigLoadException;
import com.github.scipioam.scipiofx.mybatis.ext.DBAppInitThread;
import com.github.scipioam.scipiofx.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;

/**
 * @author Alan Scipio
 * @since 2024/4/26
 */
@SuppressWarnings({"LombokGetterMayBeUsed", "LombokSetterMayBeUsed"})
public class AppConfigLoader {

    private static final Logger log = LoggerFactory.getLogger(AppConfigLoader.class);

    private JFXApplication appInstance;

    private File externalConfigFile;

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
            InputStream in = getConfigInputStream(configFileName, appClass);
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
        } else if (StringUtils.isNotBlank(appProperties.getInitThread())) {
            if (appProperties.getInitThread().equalsIgnoreCase("EmptyAppInitThread")
                    || appProperties.getInitThread().equalsIgnoreCase("empty")) {
                appProperties.setInitThreadInstance(new EmptyAppInitThread());
            } else if (appProperties.getInitThread().equalsIgnoreCase("DBAppInitThread")
                    || appProperties.getInitThread().equalsIgnoreCase("db")) {
                appProperties.setInitThreadInstance(new DBAppInitThread());
            }
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

    public InputStream getConfigInputStream(String configFileName, Class<? extends JFXApplication> appClass) throws FileNotFoundException {
        InputStream in;
        String innerConfigFileName = configFileName;
        if (configFileName.startsWith("/")) {
            configFileName = "." + configFileName;
        }

        //搜寻jar同级目录下的配置文件
        File configFile = new File(configFileName);
        if (configFile.exists()) {
            externalConfigFile = configFile;
            in = new FileInputStream(configFile);
            log.info("Load config file from default path: [{}]", configFile.getAbsolutePath());
        } else {
            //内置默认配置文件
            externalConfigFile = null;
            in = appClass.getResourceAsStream(innerConfigFileName);
            log.info("Load config file from jar inner file: [{}]", innerConfigFileName);
        }
        return in;
    }

    public JFXApplication getAppInstance() {
        return appInstance;
    }

    public void setAppInstance(JFXApplication appInstance) {
        this.appInstance = appInstance;
    }

    public File getExternalConfigFile() {
        return externalConfigFile;
    }
}
