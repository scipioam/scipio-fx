package com.github.ScipioAM.scipio_fx.app;

import com.github.ScipioAM.scipio_fx.app.config.RootConfig;
import com.github.ScipioAM.scipio_fx.app.config.ConfigLoadListener;
import com.github.ScipioAM.scipio_fx.view.FXMLView;

import java.io.IOException;
import java.net.URL;

/**
 * @author Alan Scipio
 * @since 2022/2/22
 */
public interface ApplicationInterface {

    //=========================================== ↓↓↓↓↓↓ 配置 ↓↓↓↓↓↓ ===========================================

    default String configPrefix() {
        return "fx-app";
    }

    default Class<? extends RootConfig> getRootConfigType() {
        return RootConfig.class;
    }

    String title();

    URL iconUrl();

    URL mainViewUrl();

    URL splashImgUrl();

    //=========================================== ↓↓↓↓↓↓ 绑定 ↓↓↓↓↓↓ ===========================================

    default ConfigLoadListener configLoadListener() {
        return null;
    }

    default AppInitThread bindInitThread() {
        return null;
    }

    default LaunchListener bindLaunchListener() {
        return null;
    }

    //=========================================== ↓↓↓↓↓↓ 工具API ↓↓↓↓↓↓ ===========================================

    FXMLView buildMainView() throws IOException;

}
