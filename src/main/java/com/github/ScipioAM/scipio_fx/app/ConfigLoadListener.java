package com.github.ScipioAM.scipio_fx.app;

import java.util.ResourceBundle;

/**
 * 配置加载时的监听回调
 *
 * @since 2022/6/22
 */
public interface ConfigLoadListener {

    /**
     * 加载前的回调，此方法可整体替换原本的加载逻辑
     *
     * @param bundle 配置文件
     * @param config 实例对象
     * @return true:继续原本的加载逻辑
     */
    default boolean onLoad(ResourceBundle bundle, ApplicationConfig config) {
        return true;
    }

    /**
     * 加载后的回调
     *
     * @param bundle 配置文件
     * @param config 实例对象
     */
    default void afterLoad(ResourceBundle bundle, ApplicationConfig config) {
    }

}
