package com.github.ScipioAM.scipio_fx.test.controllers;

import com.github.ScipioAM.scipio_fx.controller.BaseController;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.SegmentedButton;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

/**
 * @since 2022/6/16
 */
public class FXInputsController extends BaseController {

    private int lines = 1;
    @FXML
    private TextArea inputBox;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private TextField tf1;

    @Override
    public void onLoadInit(Parent rootNode, Object... initArgs) {
        setupSegmentedButton();

        ValidationSupport validationSupport = new ValidationSupport();
        validationSupport.registerValidator(tf1, (Validator<String>) (control, s) -> ValidationResult.fromErrorIf(control, "tf1 can not be blank", (s == null || s.equals(""))));
    }

    private void setupSegmentedButton() {
        ToggleButton b1 = new ToggleButton("Btn1");
        b1.setOnMouseClicked(btn -> {
            input("Btn1 clicked!");
        });
        ToggleButton b2 = new ToggleButton("Btn2");
        b2.setOnMouseClicked(btn -> {
            input("Btn2 clicked!");
        });
        ToggleButton b3 = new ToggleButton("Btn3");
        b3.setOnMouseClicked(btn -> {
            input("Btn3 clicked!");
        });

        SegmentedButton sBtn = new SegmentedButton(b1, b2, b3);
        sBtn.setLayoutX(353.0);
        sBtn.setLayoutY(147.0);
        rootPane.getChildren().add(sBtn);
    }

    //===================================================================================================================

    private void input(String text) {
        inputBox.setText(inputBox.getText() + "[" + (lines++) + "]" + text + "\n");
    }

    private void clearInput() {
        inputBox.setText("");
        lines = 1;
    }

    @FXML
    private void onclick_clear() {
        clearInput();
    }

    @FXML
    private void onclick_btn1() {
        input(tf1.getText());
    }

}
