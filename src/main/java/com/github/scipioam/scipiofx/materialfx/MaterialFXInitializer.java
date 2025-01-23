package com.github.scipioam.scipiofx.materialfx;

/**
 * @author Alan Scipio
 * created on 2023/12/22
 */
public interface MaterialFXInitializer {

    /**
     * 初始化MaterialFX
     *
     * @param materialFXOnly 是否只使用MaterialFX的主题
     * @param includeLegacy  是否使用旧的MaterialFX主题
     */
    void init(boolean materialFXOnly, boolean includeLegacy);

}
