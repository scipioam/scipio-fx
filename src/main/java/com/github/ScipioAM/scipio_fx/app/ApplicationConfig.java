package com.github.ScipioAM.scipio_fx.app;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Alan Scipio
 * @since 2022/2/22
 */
@Data
@Accessors(chain = true)
public class ApplicationConfig {

    /** 图标 */
    private String iconPath;

    /** 标题 */
    private String title;

    /* 主界面的url */
//    private URL mainViewUrl;

    /** 启动监听器 */
    private LaunchListener launchListener;

    /** 初始化子线程 */
    private AppInitThread initThread;

    public static ApplicationConfig create() {
        return new ApplicationConfig();
    }

    public static ApplicationConfig create(String title, String iconPath) {
        return new ApplicationConfig()
                .setTitle(title)
                .setIconPath(iconPath);
    }

    public static ApplicationConfig create(String title) {
        return create(title, null);
    }

}
