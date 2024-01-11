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
     * @return null:继续原本的加载逻辑，非null:停止原本的加载逻辑，使用返回的实例对象
     */
    RootConfig onLoad(Yaml yaml, ApplicationConfig config);

}
