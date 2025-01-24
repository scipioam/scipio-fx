package scipiofx.test.controllers;

import com.github.scipioam.scipiofx.framework.AppContext;
import com.github.scipioam.scipiofx.framework.BaseController;
import com.github.scipioam.scipiofx.framework.Language;
import com.github.scipioam.scipiofx.framework.fxml.ViewArgs;
import com.github.scipioam.scipiofx.materialfx.MFXComboBoxSupport;
import com.github.scipioam.scipiofx.materialfx.dialog.MFXDialogHelper;
import com.github.scipioam.scipiofx.view.ComboBoxSupport;
import com.github.scipioam.scipiofx.view.dialog.AlertHelper;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.SegmentedButton;
import scipiofx.test.JumpView;

import java.util.Optional;

/**
 * @author Alan Scipio
 * created on 2025-01-22
 */
public class InputsController extends BaseController {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private ComboBox<Language> cb0;
    @FXML
    private MFXComboBox<JumpView> cb1;
    @FXML
    private SegmentedButton btnGroup0;
    @FXML
    private SegmentedButton btnGroup1;

    @Override
    public void onLoadInit(AppContext context, Parent rootNode, ViewArgs initArgs) {
        // 初始化Combobox
        ComboBoxSupport.setup(cb0, Language::name, Language.values());
//        MFXComboBoxSupport.setup(cb1, JumpView::title, JumpView.values());

        // 初始化按钮
        ToggleGroup toggleGroup0 = new ToggleGroup();
        for (ToggleButton btn : btnGroup0.getButtons()) {
            btn.setToggleGroup(toggleGroup0);
        }

        ToggleGroup toggleGroup1 = new ToggleGroup();
        for (ToggleButton btn : btnGroup1.getButtons()) {
            btn.setToggleGroup(toggleGroup1);
        }
    }

    @FXML
    private void showOriginalError() {
        AlertHelper.showError("Error", "Test Error", "This is an error message");
    }

    @FXML
    private void showOriginalInfo() {
        AlertHelper.showInfo("Info", "Test Info", "This is an info message");
    }

    @FXML
    private void showOriginalWarning() {
        AlertHelper.showWarning("Warning", "Test Warning", "This is a warning message");
    }

    @FXML
    private void showOriginalConfirmation() {
        Optional<ButtonType> result = AlertHelper.showConfirm("Confirmation", "Test Confirmation", "This is a confirmation message");
        if (result.isPresent() && result.get() == ButtonType.OK) {
            AlertHelper.showInfo("Info", "Confirmed", "You confirmed");
        } else {
            AlertHelper.showInfo("Info", "Not Confirmed", "You did not confirm");
        }
    }

    @FXML
    private void showMfxError() {
        MFXDialogHelper.showError(rootPane, "Test Error", "This is a error message");
    }

    @FXML
    private void showMfxInfo() {
        MFXDialogHelper.showInfo(rootPane, "Test Info", "This is an info message");
    }

    @FXML
    private void showMfxWarning() {
        MFXDialogHelper.showWarning(rootPane, "Test Warning", "This is a warning message");
    }

    @FXML
    private void showMfxFilter() {
        MFXDialogHelper.showInfo(rootPane, "Info", "This function is still under development");
    }

}
