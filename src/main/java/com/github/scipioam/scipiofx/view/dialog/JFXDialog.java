package com.github.scipioam.scipiofx.view.dialog;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * {@link Dialog}的易用封装
 * @author Alan Scipio
 * @since 2022/2/23
 */
@Data
@Accessors(chain = true)
public class JFXDialog implements IDialog {

    private final Dialog<ButtonType> dialogObj = new Dialog<>();
    @Getter(AccessLevel.NONE)
    private final Map<ButtonType, DialogBtnListener> btnMap = new LinkedHashMap<>();

    private String title;
    private String headerText;
    private String contentText;
    private EventHandler<DialogEvent> onCloseHandler;
    private EventHandler<DialogEvent> onShownHandler;

    public static JFXDialog buildDefault(String contentText) {
        return buildDefault(null, null, contentText);
    }

    public static JFXDialog buildDefault(String headerText, String contentText) {
        return buildDefault(null, headerText, contentText);
    }

    public static JFXDialog buildDefault(String title, String headerText, String contentText) {
        return new JFXDialog()
                .setTitle(title)
                .setHeaderText(headerText)
                .setContentText(contentText)
                .addOkButton()
                .addCancelButton()
                .build();
    }

    public JFXDialog build() {
        dialogObj.setTitle(title);
        dialogObj.setHeaderText(headerText);
        dialogObj.setContentText(contentText);
        dialogObj.setOnCloseRequest(onCloseHandler);
        dialogObj.setOnShown(onShownHandler);
        if(!btnMap.isEmpty()) {
            DialogPane dialogPane = dialogObj.getDialogPane();
            ObservableList<ButtonType> buttonTypes = dialogPane.getButtonTypes();
            buttonTypes.addAll(btnMap.keySet());
            for(Map.Entry<ButtonType, DialogBtnListener> entry : btnMap.entrySet()) {
                if(entry.getValue() != null) {
                    Button btn = (Button) dialogPane.lookupButton(entry.getKey());
                    btn.setOnMouseClicked((mouseEvent) -> entry.getValue().onClicked(mouseEvent, this));
                }
            }
        }
        return this;
    }

    @Override
    public void show() {
        dialogObj.show();
    }

    public Optional<ButtonType> showAndWait() {
        return dialogObj.showAndWait();
    }

    @Override
    public void close() {
        dialogObj.close();
    }

    public JFXDialog setPrefSize(double prefWidth, double prefHeight) {
        dialogObj.getDialogPane().setPrefSize(prefWidth, prefHeight);
        return this;
    }

    public JFXDialog setPrefWidth(double prefWidth) {
        dialogObj.getDialogPane().setPrefWidth(prefWidth);
        return this;
    }

    public JFXDialog setPrefHeight(double prefHeight) {
        dialogObj.getDialogPane().setPrefHeight(prefHeight);
        return this;
    }

    public JFXDialog setResizable(boolean resizable) {
        dialogObj.setResizable(resizable);
        return this;
    }

    public JFXDialog setPosition(double x, double y) {
        dialogObj.setX(x);
        dialogObj.setY(y);
        return this;
    }

    public JFXDialog setImage(ImageView imageView) {
        dialogObj.setGraphic(imageView);
        return this;
    }

    public DialogBtnListener getButtonListener(ButtonType type) {
        return btnMap.get(type);
    }

    public boolean containsButton(ButtonType type) {
        return btnMap.containsKey(type);
    }

    public JFXDialog addButton(ButtonType type, DialogBtnListener listener) {
        btnMap.put(type, listener);
        return this;
    }

    public JFXDialog addButton(ButtonType type) {
        btnMap.put(type, DialogBtnListener.CLOSE_DIALOG);
        return this;
    }

    public JFXDialog addOkButton(DialogBtnListener listener) {
        return addButton(ButtonType.OK, listener);
    }

    public JFXDialog addOkButton() {
        return addButton(ButtonType.OK);
    }

    public JFXDialog addCancelButton(DialogBtnListener listener) {
        return addButton(ButtonType.CANCEL, listener);
    }

    public JFXDialog addCancelButton() {
        return addButton(ButtonType.CANCEL);
    }

    public JFXDialog addApplyButton(DialogBtnListener listener) {
        return addButton(ButtonType.APPLY, listener);
    }

    public JFXDialog addApplyButton() {
        return addButton(ButtonType.APPLY);
    }

    public JFXDialog addCloseButton(DialogBtnListener listener) {
        return addButton(ButtonType.CLOSE, listener);
    }

    public JFXDialog addCloseButton() {
        return addButton(ButtonType.CLOSE);
    }

    public JFXDialog addFinishButton(DialogBtnListener listener) {
        return addButton(ButtonType.FINISH, listener);
    }

    public JFXDialog addFinishButton() {
        return addButton(ButtonType.FINISH);
    }
}
