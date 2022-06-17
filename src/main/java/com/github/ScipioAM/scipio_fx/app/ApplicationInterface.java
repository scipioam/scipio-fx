package com.github.ScipioAM.scipio_fx.app;

import com.github.ScipioAM.scipio_fx.view.FXMLView;

import java.io.IOException;
import java.net.URL;

/**
 * @author Alan Scipio
 * @since 2022/2/22
 */
public interface ApplicationInterface {

    //=========================================== ↓↓↓↓↓↓ 配置 ↓↓↓↓↓↓ ===========================================

    default String getConfigPrefix() {
        return "app";
    }

    String iconPath();

    String title();

    URL mainViewUrl();

    //=========================================== ↓↓↓↓↓↓ 绑定 ↓↓↓↓↓↓ ===========================================

    default AppInitThread bindInitThread() {
        return null;
    }

    default LaunchListener bindLaunchListener() {
        return null;
    }

    //=========================================== ↓↓↓↓↓↓ 工具API ↓↓↓↓↓↓ ===========================================

    FXMLView buildMainView() throws IOException;

}
