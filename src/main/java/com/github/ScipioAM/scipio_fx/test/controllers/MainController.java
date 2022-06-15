package com.github.ScipioAM.scipio_fx.test.controllers;

import com.github.ScipioAM.scipio_fx.constant.Language;
import com.github.ScipioAM.scipio_fx.controller.BaseController;
import com.github.ScipioAM.scipio_fx.dialog.DialogHelper;
import com.github.ScipioAM.scipio_fx.dialog.mfx.MFXDialogHelper;
import com.github.ScipioAM.scipio_fx.test.original.TestMFXNotification;
import com.github.ScipioAM.scipio_fx.view.FXMLView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class MainController extends BaseController {

    private final FXMLView mfxTableTestView = new FXMLView();
    private final FXMLView fxTableTestView = new FXMLView();

    @FXML
    private StackPane rootPane;
    @FXML
    private Label welcomeText;

    @FXML
    protected void onTest1BtnClick() {
//        welcomeText.setText("Welcome to JavaFX Application!");
        DialogHelper.showDialog("asd");
    }

    @FXML
    protected void onTest2BtnClick() {
//        TestMFXDialog.showDialog(rootPane, "content text");
        MFXDialogHelper.showInfo("Header", "This is a content", Language.CN);
    }

    @FXML
    protected void onTest3BtnClick() {
        TestMFXNotification.show(thisStage);
    }

    @FXML
    protected void onTest4BtnClick() {
        try {
            FXMLView.loadAndShow(mfxTableTestView, "/views/mfx-table.fxml", "Test4 MFX表格", rootPane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onTest5BtnClick() {
        try {
            FXMLView.loadAndShow(fxTableTestView, "/views/fx-table.fxml", "Test5 FX表格", rootPane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}