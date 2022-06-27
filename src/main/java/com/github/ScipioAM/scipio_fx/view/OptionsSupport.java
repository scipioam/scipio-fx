package com.github.ScipioAM.scipio_fx.view;

import javafx.scene.layout.Region;

/**
 * @since 2022/6/27
 */
public abstract class OptionsSupport {

    protected static void setOptions(Region view, Options options) {
        if (options.getPreWidth() != null) {
            view.setPrefWidth(options.getPreWidth());
        }
        if (options.getPreHeight() != null) {
            view.setPrefHeight(options.getPreHeight());
        }
        if (options.getStyleClass() != null && options.getStyleClass().size() > 0) {
            view.getStyleClass().setAll(options.getStyleClass());
        }
    }

}
