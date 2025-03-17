package scipiofx.test.controllers;

import com.github.scipioam.scipiofx.controlsfx.CFXDialogHelper;
import com.github.scipioam.scipiofx.framework.AppContext;
import com.github.scipioam.scipiofx.framework.BaseController;
import com.github.scipioam.scipiofx.framework.fxml.ViewArgs;
import com.github.scipioam.scipiofx.materialfx.dialog.MFXDialogHelper;
import com.github.scipioam.scipiofx.materialfx.table.MFXTableBuilder;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import scipiofx.test.bean.Person;

/**
 * @author Alan Scipio
 * created on 2025-01-23
 */
public class TableMfxController extends BaseController {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private MFXTableView<Person> tableView;
    @FXML
    private MFXButton btnUpdateRow;

    private final ObservableList<Person> tableList = FXCollections.observableArrayList();

    @Override
    public void onLoadInit(AppContext context, Parent rootNode, ViewArgs initArgs) {
        // 初始化表格
        try {
//            Person.buildTestData(tableList, 10);
            long startTime = System.currentTimeMillis();
            MFXTableBuilder<Person> tableBuilder = new MFXTableBuilder<>(tableView);
            tableBuilder.initDataSource(tableList)
                    .dataType(Person.class)
                    .build();
            System.out.println("Build MFXTableView cost: " + (System.currentTimeMillis() - startTime) + "ms");
        } catch (Exception e) {
            e.printStackTrace();
            CFXDialogHelper.showExceptionDialog(e);
        }
    }

    /**
     * 查询按钮
     */
    @FXML
    private void clickSearch() {

    }

    /**
     * 重置按钮
     */
    @FXML
    private void clickReset() {
        System.out.println(tableView.getItems());
        tableList.clear();
    }

    /**
     * 新增行按钮
     */
    @FXML
    private void clickAddRow() {
        tableList.add(new Person());
    }

    /**
     * 删除行按钮
     */
    @FXML
    private void clickDeleteRow() {
        Person selectedData = tableView.getSelectionModel().getSelectedValue();
        if (selectedData != null) {
            tableView.getItems().remove(selectedData);
        } else {
            MFXDialogHelper.showWarning(rootPane, "警告", "请先选择要删除的行！");
        }
    }

    /**
     * 更新行按钮
     */
    @FXML
    private void clickUpdateRow() {
    }
}
