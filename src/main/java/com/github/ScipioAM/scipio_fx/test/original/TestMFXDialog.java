package com.github.ScipioAM.scipio_fx.test.original;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.dialogs.MFXDialogs;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.AbstractMap;

/**
 * MaterialFX的dialog的原型测试
 *
 * @since 2022/6/7
 */
public class TestMFXDialog {

    public static void showDialog(Pane ownerNode, String contentText) {
        MFXGenericDialog dialogContent = MFXDialogs.error()
                .setContentText(contentText)
                .setHeaderText("header text")
                .get();
//        MFXGenericDialog dialogContent = MFXGenericDialogBuilder.build()
//                .setContentText(contentText)
//                .setHeaderText("header 222")
//                .get();

        MFXStageDialog dialog = new MFXStageDialog();

        AbstractMap.SimpleEntry<Node, EventHandler<MouseEvent>> entry1 = new AbstractMap.SimpleEntry<>(
                new MFXButton("OK"), (mouseEvent) -> {
            System.out.println("OK btn clicked");
            dialog.close();
            dialog.dispose();
        }
        );
        AbstractMap.SimpleEntry<Node, EventHandler<MouseEvent>> entry2 = new AbstractMap.SimpleEntry<>(
                new MFXButton("btn2"), (mouseEvent) -> System.out.println("btn2 clicked")
        );
        dialogContent.addActions(entry1, entry2);
        dialogContent.setShowClose(true);
        dialogContent.setShowMinimize(true); //开关“最小化按钮”，默认true
        dialogContent.setShowAlwaysOnTop(true);
        //右上角关闭按钮的action（没有默认实现，需要自己实现）
        dialogContent.setOnClose(event -> {
            System.out.println("OnClose clicked");
            dialog.close();
            dialog.dispose();
        });
        dialogContent.setOnMinimize(event -> {
            System.out.println("OnMinimize clicked");
            dialog.setIconified(true);
        });
        dialogContent.setOnAlwaysOnTop(event -> {
            System.out.println("OnAlwaysOnTop clicked");
            dialog.requestFocus();
            dialog.toFront();
        });

        dialog.setOwnerNode(ownerNode);
        dialog.setScrimOwner(true); //scrim就是打开dialog时，把父pane给黑色虚化，此方法是开关此特效的（但需要setOwnerNode才能生效）
        dialog.setDraggable(true);
        dialog.setOverlayClose(false);
        dialog.setContent(dialogContent);
        dialog.showDialog();
    }

}
