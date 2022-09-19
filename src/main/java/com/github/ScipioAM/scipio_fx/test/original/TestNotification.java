package com.github.ScipioAM.scipio_fx.test.original;

import io.github.palexdev.materialfx.controls.MFXSimpleNotification;
import io.github.palexdev.materialfx.enums.NotificationPos;
import io.github.palexdev.materialfx.enums.NotificationState;
import io.github.palexdev.materialfx.notifications.MFXNotificationSystem;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.action.Action;

/**
 * @since 2022/6/7
 */
public class TestNotification {

    public static void showMFX(Stage stage) {
        //初始化消息发布系统
        MFXNotificationSystem.instance()
                .initOwner(stage);

        //构建消息对象
        MFXSimpleNotification notification = MFXSimpleNotification.Builder.build()
                .setContent(new Label("This is a test notification"))
                .setState(NotificationState.UNREAD)
                .get();
        //发布消息
        MFXNotificationSystem.instance()
                .setPosition(NotificationPos.TOP_LEFT)
                .publish(notification);
    }

    public static void showControlsFX() {
        Notifications.create()
                .title("Title")
                .text("This is a test notification")
                .action(new Action("yes?", event -> {
                    System.out.println(event);
                    System.out.println("notification confirm button clicked");
                }))
                .onAction(event -> {
                    System.out.println(event);
                    System.out.println(event.getSource());
                })
                .position(Pos.TOP_RIGHT)
                .showConfirm();
    }

}
