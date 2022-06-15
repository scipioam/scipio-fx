package com.github.ScipioAM.scipio_fx.test.original;

import io.github.palexdev.materialfx.controls.MFXSimpleNotification;
import io.github.palexdev.materialfx.enums.NotificationPos;
import io.github.palexdev.materialfx.enums.NotificationState;
import io.github.palexdev.materialfx.notifications.MFXNotificationSystem;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * @since 2022/6/7
 */
public class TestMFXNotification {

    public static void show(Stage stage) {
        //初始化消息发布系统
        MFXNotificationSystem.instance()
                .initOwner(stage);

        //构建消息对象
        MFXSimpleNotification notification = MFXSimpleNotification.Builder.build()
                .setContent(new Label("asdadasfaeqfwegw"))
                .setState(NotificationState.UNREAD)
                .get();
        //发布消息
        MFXNotificationSystem.instance()
                .setPosition(NotificationPos.TOP_LEFT)
                .publish(notification);
    }

}
