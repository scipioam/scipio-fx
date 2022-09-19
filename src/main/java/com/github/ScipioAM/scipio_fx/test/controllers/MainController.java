package com.github.ScipioAM.scipio_fx.test.controllers;

import com.github.ScipioAM.scipio_fx.constant.AppViewId;
import com.github.ScipioAM.scipio_fx.constant.Language;
import com.github.ScipioAM.scipio_fx.controller.BaseMainController;
import com.github.ScipioAM.scipio_fx.dialog.mfx.MFXDialogHelper;
import com.github.ScipioAM.scipio_fx.test.TestApplication;
import com.github.ScipioAM.scipio_fx.test.original.TestNotification;
import com.github.ScipioAM.scipio_fx.test.threads.ConsoleTask;
import com.github.ScipioAM.scipio_fx.view.Console;
import com.github.ScipioAM.scipio_fx.view.FXMLView;
import com.github.ScipioAM.scipio_fx.view.ViewLoadOptions;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;

public class MainController extends BaseMainController {

    private final FXMLView mfxTableTestView = new FXMLView();
    private final FXMLView fxTableTestView = new FXMLView();
    private final FXMLView fxInputsView = new FXMLView();

    @FXML
    private StackPane rootPane;
    @FXML
    private TextArea textArea;

    private Console console;
    private ConsoleTask consoleTask = null;

    @Override
    public void onLoadInit(Parent rootNode, Object... initArgs) {
        console = new Console(textArea, true);
    }

    @FXML
    private void onTest1BtnClick() {
        console.clear();
//        console.output(RandomUtil.getString(5));
    }

    @FXML
    private void onTest2BtnClick() {
//        TestMFXDialog.showDialog(rootPane, "content text");
        MFXDialogHelper.showInfo("Header", "This is a content", Language.CN);
    }

    @FXML
    private void onTest3BtnClick() {
        TestNotification.showMFX(parentStage);
    }

    @FXML
    private void onTest32BtnClick() {
        TestNotification.showControlsFX();
    }

    @FXML
    private void onTest4BtnClick() {
        ViewLoadOptions options = ViewLoadOptions.build()
                .setTitle("Test4 MFX表格")
                .setFxmlUrl(this.getClass(), "/test-views/table-mfx.fxml")
                .defaultStageOptions(rootPane);
        FXMLView.loadAndShow(mfxTableTestView, options);
    }

    @FXML
    private void onTest5BtnClick() {
        ViewLoadOptions options = ViewLoadOptions.build()
                .setTitle("Test5 FX表格")
                .setFxmlUrl(this.getClass(), "/test-views/table-fx.fxml")
                .defaultStageOptions(rootPane);
        FXMLView.loadAndShow(fxTableTestView, options);
    }

    @FXML
    private void onTest6BtnClick() {
        ViewLoadOptions options = ViewLoadOptions.build()
                .setTitle("Test6 输入组件")
                .setFxmlUrl(this.getClass(), "/test-views/inputs-fx.fxml")
                .defaultStageOptions(rootPane);
        FXMLView.loadAndShow(fxInputsView, options);
    }

    @FXML
    private void onTest7BtnClick() {
        consoleTask = new ConsoleTask(console);
//        consoleTask.setOnSucceeded(event -> console.output("OnSucceeded")); //会报错: A bound value cannot be set
        consoleTask.setSucceedListener(ctx -> ctx.updateUiMessage("执行成功"));
        consoleTask.setCanceledListener(ctx -> System.out.println("ConsoleTask has been canceled"));
        TestApplication.context.submitTask(consoleTask);
    }

    @FXML
    private void onTest72BtnClick() {
        try {
            if (consoleTask != null) {
                String taskResult = consoleTask.getTaskResult();
                System.out.println("获取consoleTask的执行结果(FutureTask)：[" + taskResult + "]");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onTest73BtnClick() {
        if (consoleTask != null) {
            boolean cancelResult = consoleTask.cancel();
            System.out.println("已取消consoleTask，取消结果：" + cancelResult);
        }
    }

    @Override
    public void loadChildView(AppViewId viewInfo, Object initArgs) {
        System.out.println("loadChildViews() called.");
    }
}