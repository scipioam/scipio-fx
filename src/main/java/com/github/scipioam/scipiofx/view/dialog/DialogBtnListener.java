package com.github.scipioam.scipiofx.view.dialog;

import javafx.scene.input.MouseEvent;

/**
 * Dialog按钮的监听回调
 *
 * @author Alan Scipio
 * @since 2022/2/23
 */
@FunctionalInterface
public interface DialogBtnListener {

    /**
     * 按钮点击事件
     */
    void onClicked(MouseEvent mouseEvent, IDialog myself);

    /**
     * 默认实现：关闭dialog
     */
    DialogBtnListener CLOSE_DIALOG = (mouseEvent, myself) -> myself.close();

}
