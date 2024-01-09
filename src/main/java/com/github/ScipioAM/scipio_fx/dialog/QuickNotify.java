package com.github.ScipioAM.scipio_fx.dialog;

import com.github.ScipioAM.scipio_fx.utils.StringUtils;
import javafx.geometry.Pos;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.action.Action;

/**
 * 快速便捷的提示消息API
 *
 * @see Notifications
 * @since 2022/9/19
 */
public class QuickNotify {

    public static final Pos DEFAULT_POS = Pos.BOTTOM_RIGHT;

    /**
     * 构建基础对象
     *
     * @param title    标题
     * @param content  内容
     * @param position 出现位置
     * @param millis   持续时间（毫秒）（默认5秒）
     */
    public static Notifications build(String title, String content, Pos position, Double millis) {
        Notifications notifications = Notifications.create();
        if (StringUtils.isNotNull(title)) {
            notifications.title(title);
        }
        if (StringUtils.isNotNull(content)) {
            notifications.text(content);
        }
        if (position != null) {
            notifications.position(position);
        }
        if (millis != null && millis > 0) {
            notifications.hideAfter(Duration.millis(millis));
        }
        return notifications;
    }

    public void show(String title, String content, Pos position) {
        build(title, content, position, null).show();
    }

    public void show(String title, String content) {
        show(title, content, DEFAULT_POS);
    }

    public static void showError(String title, String content, Pos position) {
        build(title, content, position, null).showError();
    }

    public static void showError(String title, String content) {
        showError(title, content, DEFAULT_POS);
    }

    public static void showWarning(String title, String content, Pos position) {
        build(title, content, position, null).showWarning();
    }

    public static void showWarning(String title, String content) {
        showWarning(title, content, DEFAULT_POS);
    }

    public static void showInfo(String title, String content, Pos position) {
        build(title, content, position, null).showInformation();
    }

    public static void showInfo(String title, String content) {
        showInfo(title, content, DEFAULT_POS);
    }

    public static void showConfirm(String title, String content, Pos position, Action... buttons) {
        build(title, content, position, null)
                .action(buttons)
                .showConfirm();
    }

    public static void showConfirm(String title, String content, Action... buttons) {
        showConfirm(title, content, DEFAULT_POS, buttons);
    }

}
