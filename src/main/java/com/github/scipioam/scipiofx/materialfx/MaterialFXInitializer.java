package com.github.scipioam.scipiofx.materialfx;

import io.github.palexdev.materialfx.theming.UserAgentBuilder;

/**
 * @author Alan Scipio
 * created on 2023/12/22
 */
public interface MaterialFXInitializer {

    /**
     * 初始化MaterialFX
     *
     * @param builder        style主题构建器
     * @param materialFXOnly 是否只使用MaterialFX的主题
     */
    void init(UserAgentBuilder builder, boolean materialFXOnly);

}
