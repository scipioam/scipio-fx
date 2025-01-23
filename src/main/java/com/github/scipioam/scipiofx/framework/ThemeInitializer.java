package com.github.scipioam.scipiofx.framework;

import javafx.application.Application;

/**
 * @author Alan Scipio
 * created on 2025-01-23
 */
public class ThemeInitializer {

    /**
     * Initialize the theme
     */
    public void init(JFXApplication application) {
        Application.setUserAgentStylesheet(Application.STYLESHEET_MODENA);
    }

}
