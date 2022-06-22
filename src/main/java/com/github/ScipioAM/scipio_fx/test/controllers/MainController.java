package com.github.ScipioAM.scipio_fx.test.controllers;

import com.github.ScipioAM.scipio_fx.constant.Language;
import com.github.ScipioAM.scipio_fx.controller.BaseController;
import com.github.ScipioAM.scipio_fx.dialog.DialogHelper;
import com.github.ScipioAM.scipio_fx.dialog.mfx.MFXDialogHelper;
import com.github.ScipioAM.scipio_fx.test.original.TestMFXNotification;
import com.github.ScipioAM.scipio_fx.view.FXMLView;
import com.github.ScipioAM.scipio_fx.view.ViewLoadOptions;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class MainController extends BaseController {

    private final FXMLView mfxTableTestView = new FXMLView();
    private final FXMLView fxTableTestView = new FXMLView();
    private final FXMLView fxInputsView = new FXMLView();

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
        TestMFXNotification.show(parentStage);
    }

    @FXML
    protected void onTest4BtnClick() {
        ViewLoadOptions options = ViewLoadOptions.build()
                .setTitle("Test4 MFX表格")
                .setFxmlUrl(this.getClass(), "/views/table-mfx.fxml")
                .defaultStageOptions(rootPane);
        FXMLView.loadAndShow(mfxTableTestView, options);
    }

    @FXML
    protected void onTest5BtnClick() {
        ViewLoadOptions options = ViewLoadOptions.build()
                .setTitle("Test5 FX表格")
                .setFxmlUrl(this.getClass(), "/views/table-fx.fxml")
                .defaultStageOptions(rootPane);
        FXMLView.loadAndShow(fxTableTestView, options);
    }

    @FXML
    protected void onTest6BtnClick() {
        ViewLoadOptions options = ViewLoadOptions.build()
                .setTitle("Test6 输入组件")
                .setFxmlUrl(this.getClass(), "/views/inputs-fx.fxml")
                .defaultStageOptions(rootPane);
        FXMLView.loadAndShow(fxInputsView, options);
    }
}