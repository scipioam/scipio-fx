package com.github.ScipioAM.scipio_fx.app.config;

import org.yaml.snakeyaml.Yaml;

/**
 * 配置加载时的监听回调
 *
 * @since 2022/6/22
 */
public interface ConfigLoadListener {

    /**
     * 加载前的回调，此方法可整体替换原本的加载逻辑
     *
     * @param yaml   读取器
     * @param config 实例对象
     * @return true:继续原本的加载逻辑
     */
    default boolean onLoad(Yaml yaml, ApplicationConfig config) {
        return true;
    }

    /**
     * 加载后的回调
     *
     * @param yaml    读取器
     * @param wrapper 外包装
     * @param config  实例对象
     */
    default void afterLoad(Yaml yaml, ApplicationConfigWrapper wrapper, ApplicationConfig config) {
    }

}
