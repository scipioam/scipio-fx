package com.github.scipioam.scipiofx.view.dialog;

import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * 原生dialog工具类
 *
 * @author Alan Scipio
 * @since 2022/2/23
 */
public class DialogHelper {

    public static Optional<ButtonType> showDialog(String title, String headerText, String content) {
        return JFXDialog.buildDefault(title, headerText, content).showAndWait();
    }

    public static Optional<ButtonType> showDialog(String headerText, String content) {
        return JFXDialog.buildDefault(headerText, content).showAndWait();
    }

    public static Optional<ButtonType> showDialog(String content) {
        return JFXDialog.buildDefault(content).showAndWait();
    }

}
