package com.github.scipioam.scipiofx.view.dialog;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.StageStyle;

import java.util.Optional;

/**
 * @author Alan Scipio
 * @since 2022/2/23
 */
public class AlertHelper {

    /**
     * 显示对话框
     *
     * @param title       窗体标题
     * @param headerText  对话框标题
     * @param contentText 对话框内容
     * @param type        对话框类型
     * @return 返回用户点击按钮后的回调
     */
    public static Optional<ButtonType> showAlert(String title, String headerText, String contentText, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.initStyle(StageStyle.UTILITY);
        if (title != null && !title.isEmpty())
            alert.setTitle(title);
        if (headerText != null && !headerText.isEmpty())
            alert.setHeaderText(headerText);
        if (contentText != null && !contentText.isEmpty())
            alert.setContentText(contentText);
        return alert.showAndWait();
    }

    public static Optional<ButtonType> showError(String title, String headerText, String contentText) {
        return showAlert(title, headerText, contentText, Alert.AlertType.ERROR);
    }

    public static Optional<ButtonType> showWarning(String title, String headerText, String contentText) {
        return showAlert(title, headerText, contentText, Alert.AlertType.WARNING);
    }

    public static Optional<ButtonType> showInfo(String title, String headerText, String contentText) {
        return showAlert(title, headerText, contentText, Alert.AlertType.INFORMATION);
    }

    public static Optional<ButtonType> showConfirm(String title, String headerText, String contentText) {
        return showAlert(title, headerText, contentText, Alert.AlertType.CONFIRMATION);
    }

}
