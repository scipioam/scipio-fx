package com.github.scipioam.scipiofx.view.dialog;

import com.github.scipioam.scipiofx.framework.AppContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.Optional;

/**
 * @author Alan Scipio
 * @since 2022/2/23
 */
@SuppressWarnings("UnusedReturnValue")
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
        if (title != null && !title.isEmpty()) {
            alert.setTitle(title);
        }
        if (headerText != null && !headerText.isEmpty()) {
            alert.setHeaderText(headerText);
        }
        if (contentText != null && !contentText.isEmpty()) {
            alert.setContentText(contentText);
        }

        // 目前MaterialFX会导致图标无法显示，故重新设置
        if (AppContext.getInstance().getAppProperties().isMaterialFxEnabled() && !AppContext.getInstance().getAppProperties().getMaterialFx().isUseMaterialFxThemeOnly()) {
            switch (type) {
                case ERROR:
                    setIcon(alert, "/img/dialog-error.png");
                    break;
                case WARNING:
                    setIcon(alert, "/img/dialog-warning.png");
                    break;
                case INFORMATION:
                    setIcon(alert, "/img/dialog-information.png");
                    break;
                case CONFIRMATION:
                    setIcon(alert, "/img/dialog-confirm.png");
                    break;
            }
        }

        return alert.showAndWait();
    }

    private static void setIcon(Alert alert, String path) {
        URL imgUrl = AlertHelper.class.getResource(path);
        if (imgUrl != null) {
            alert.setGraphic(new ImageView(imgUrl.toString()));
        }
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
