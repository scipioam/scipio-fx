package scipiofx.test.controllers;

import com.github.scipioam.scipiofx.framework.AppContext;
import com.github.scipioam.scipiofx.framework.BaseMainController;
import com.github.scipioam.scipiofx.framework.fxml.ViewArgs;
import com.github.scipioam.scipiofx.view.Console;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;

public class MainController extends BaseMainController {

    @FXML
    private StackPane rootPane;
    @FXML
    private TextArea textArea;

    private Console console;

    @Override
    public void onMainControllerInit(AppContext context, Parent rootNode, ViewArgs initArgs) {
        console = new Console(textArea, true);
    }

    @FXML
    private void onClearClick() {
        console.clear();
    }

}