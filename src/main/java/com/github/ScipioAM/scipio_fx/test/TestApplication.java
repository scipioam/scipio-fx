package com.github.ScipioAM.scipio_fx.test;

import com.github.ScipioAM.scipio_fx.app.config.AppConfigBean;
import com.github.ScipioAM.scipio_fx.app.config.ApplicationConfig;
import com.github.ScipioAM.scipio_fx.app.config.ConfigLoadListener;
import com.github.ScipioAM.scipio_fx.app.JFXApplication;
import com.github.ScipioAM.scipio_fx.app.LaunchListener;
import com.github.ScipioAM.scipio_fx.view.FXMLView;
import org.yaml.snakeyaml.Yaml;

public class TestApplication extends JFXApplication implements LaunchListener, ConfigLoadListener {

    private static long startTime;

    public static void main(String[] args) {
        try {
            startTime = System.currentTimeMillis();
            launchApp(TestApplication.class, args, false);
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    @Override
    public String configPrefix() {
        return "[example]fx-app";
    }

    @Override
    public ConfigLoadListener configLoadListener() {
        return this;
    }

    @Override
    public LaunchListener bindLaunchListener() {
        return this;
    }

    //==============================================================================================================================


    @Override
    public void afterLoad(Yaml yaml, AppConfigBean bean, ApplicationConfig config) {
        System.out.println("read config from: " + config.configFileName());
    }

    @Override
    public void afterShowMainView(JFXApplication app, FXMLView mainView) {
        System.out.println("启动耗时(ms): " + (System.currentTimeMillis() - startTime));
    }

}