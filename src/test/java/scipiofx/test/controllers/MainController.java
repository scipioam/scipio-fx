package scipiofx.test.controllers;

import com.github.scipioam.scipiofx.framework.AppContext;
import com.github.scipioam.scipiofx.framework.BaseMainController;
import com.github.scipioam.scipiofx.framework.fxml.ViewArgs;
import com.github.scipioam.scipiofx.view.Console;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import scipiofx.test.JumpView;
import scipiofx.test.TestAppContext;
import scipiofx.test.threads.ConsoleCallable;
import scipiofx.test.threads.ConsoleTask;

import java.util.concurrent.Future;

public class MainController extends BaseMainController {

    @FXML
    private StackPane rootPane;
    @FXML
    private TextArea textArea;

    private Console console;

    private Future<String> taskResult;

    @Override
    public void onMainControllerInit(AppContext context, Parent rootNode, ViewArgs initArgs) {
        console = new Console(textArea, true);
    }

    @FXML
    private void onClearClick() {
        console.clear();
        taskResult = null;
    }

    @FXML
    private void onTask1Click() {
        ConsoleTask task = new ConsoleTask(console);
        task.setOnSucceeded(event -> {
            console.output("Task1 done, result: " + task.getValue());
        });
        TestAppContext.getInstance().submitTask(task);
    }

    @FXML
    private void onTask2Click() {
        ConsoleCallable callable = new ConsoleCallable(20);
        taskResult = TestAppContext.getInstance().submitFutureTask(callable);
        console.output("Task2 started.");
    }

    @FXML
    private void getTask2ResultClick() {
        if (taskResult == null) {
            console.output("Task2 has not been started yet.");
            return;
        }
        try {
            if (taskResult.isDone()) {
                console.output("Task2 result: " + taskResult.get());
            } else {
                console.output("Task2 is not done yet.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void getTask2StateClick() {
        if (taskResult == null) {
            console.output("Task2 has not been started yet.");
            return;
        }
        console.output("Task2 state: " + taskResult.state());
    }

    @FXML
    private void jumpInputsClick() {
        showExternalChildView(JumpView.INPUTS_DIALOG, rootPane, StageStyle.UTILITY, Modality.APPLICATION_MODAL);
    }

    @FXML
    private void jumpTableCfxClick() {
        showExternalChildView(JumpView.TABLE_CFX, rootPane, StageStyle.UTILITY, Modality.APPLICATION_MODAL);
    }

    @FXML
    private void jumpTableMfxClick() {
        showExternalChildView(JumpView.TABLE_MFX, rootPane, StageStyle.UTILITY, Modality.APPLICATION_MODAL);
    }

}