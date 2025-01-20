package com.github.scipioam.scipiofx.materialfx.dialog;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.enums.ButtonType;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.AbstractMap;
import java.util.Map;

/**
 * {@link MFXDialog}专用的按钮对象
 *
 * @author Alan Scipio
 * @since 2022/6/7
 */
@Data
@Accessors(chain = true)
public class MFXDialogButton {

    /** 按钮文本 */
    private String text;

    /** 按钮的动作回调 */
    private EventHandler<MouseEvent> action;

    /** 按钮往右排序，值越小越靠近右边 */
    private int sortToRight;

    public static MFXDialogButton create() {
        return new MFXDialogButton();
    }

    Map.Entry<Node, EventHandler<MouseEvent>> get() {
        MFXButton btn = new MFXButton(text);
        btn.setButtonType(ButtonType.FLAT);
        return new AbstractMap.SimpleEntry<>(btn, action);
    }

}
