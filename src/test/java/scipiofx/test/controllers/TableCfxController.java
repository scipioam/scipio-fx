package scipiofx.test.controllers;

import com.github.scipioam.scipiofx.controlsfx.CFXDialogHelper;
import com.github.scipioam.scipiofx.controlsfx.table.FXTableBuilder;
import com.github.scipioam.scipiofx.framework.AppContext;
import com.github.scipioam.scipiofx.framework.BaseController;
import com.github.scipioam.scipiofx.framework.fxml.ViewArgs;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import org.controlsfx.control.tableview2.TableView2;
import scipiofx.test.bean.Person;

/**
 * ControlsFX的{@link TableView2}相关测试
 *
 * @author Alan Scipio
 * created on 2025-01-23
 */
public class TableCfxController extends BaseController {

    @FXML
    private TableView2<Person> tableView;
    @FXML
    private Button btnUpdateRow;

    private final ObservableList<Person> tableList = FXCollections.observableArrayList();

    @Override
    public void onLoadInit(AppContext context, Parent rootNode, ViewArgs initArgs) {
        // 初始化表格
        try {
            long startTime = System.currentTimeMillis();
            FXTableBuilder<Person> tableBuilder = new FXTableBuilder<>(tableView);
            tableBuilder.initDataSource(tableList)
                    .dataType(Person.class)
                    .build();
            System.out.println("Build ControlsFX TableView2 cost: " + (System.currentTimeMillis() - startTime) + "ms");
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
        tableList.clear();
        Person.buildTestData(tableList, 10);
        tableView.refresh();
    }

    /**
     * 重置按钮
     */
    @FXML
    private void clickReset() {
        tableList.clear();
        tableView.refresh();
    }

    /**
     * 新增行按钮
     */
    @FXML
    private void clickAddRow() {
    }

    /**
     * 删除行按钮
     */
    @FXML
    private void clickDeleteRow() {
    }

    /**
     * 更新行按钮
     */
    @FXML
    private void clickUpdateRow() {
    }
}
