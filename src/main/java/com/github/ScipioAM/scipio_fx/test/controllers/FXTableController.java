package com.github.ScipioAM.scipio_fx.test.controllers;

import com.github.ScipioAM.scipio_fx.controller.BaseController;
import com.github.ScipioAM.scipio_fx.table.FXTableBuilder;
import com.github.ScipioAM.scipio_fx.table.cell.LocalDateTableCell;
import com.github.ScipioAM.scipio_fx.test.bean.Person;
import com.github.ScipioAM.scipio_fx.test.util.RandomUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.cell.PropertyValueFactory;
import org.controlsfx.control.tableview2.TableColumn2;
import org.controlsfx.control.tableview2.TableView2;

import java.time.LocalDate;

/**
 * 原生及ControlsFX的tableView实验
 *
 * @since 2022/6/13
 */
public class FXTableController extends BaseController {

    @FXML
    private TableView2<Person> tableView;

    private final ObservableList<Person> tableData = FXCollections.observableArrayList();

    @Override
    public void onLoadInit(Parent rootNode, Object... initArgs) {
//        setupTableView();
        setup_builder();
    }

    @Override
    public void onInitThreadFinished() {
        System.out.println("FXTableController: onInitThreadFinished()");
    }

    private void setup_builder() {
        try {
            FXTableBuilder<Person> builder = FXTableBuilder.builder(tableView);
            builder.setDataType(Person.class)
                    .initDataSource(tableData)
                    .setReadSuperClassFields(true);
            builder.build();

            Person.buildTestData(tableData);
            System.out.println(tableData.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void setupTableView() {
        TableColumn2<Person, Integer> col_id = new TableColumn2<>("ID");
        TableColumn2<Person, String> col_name = new TableColumn2<>("NAME");
        TableColumn2<Person, String> col_nickName = new TableColumn2<>("NICKNAME");
        TableColumn2<Person, Integer> col_age = new TableColumn2<>("AGE");
        TableColumn2<Person, String> col_gender = new TableColumn2<>("GENDER");
        TableColumn2<Person, String> col_email = new TableColumn2<>("EMAIL");
        TableColumn2<Person, LocalDate> col_birthday = new TableColumn2<>("BIRTHDAY");

        PropertyValueFactory pvf = new PropertyValueFactory<Person, Integer>("id");
        col_id.setCellValueFactory(pvf);
        col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_nickName.setCellValueFactory(new PropertyValueFactory<>("nickName"));
        col_age.setCellValueFactory(new PropertyValueFactory<>("age"));
        col_gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        col_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        col_birthday.setCellValueFactory(new PropertyValueFactory<>("birthday"));

        //原始例子：日期格式化
//        col_birthday.setCellFactory(tableColumn -> new TableCell<>() {
//            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
//            @Override
//            protected void updateItem(LocalDate item, boolean empty) {
//                super.updateItem(item, empty);
//                if(empty || item == null) {
//                    setText("");
//                } else {
//                    setText(formatter.format(item));
//                }
//            }
//        });
        col_birthday.setCellFactory(param -> new LocalDateTableCell<>("yyyy_MM_dd"));

        tableView.getColumns().addAll(col_id, col_name, col_nickName, col_age, col_gender, col_email, col_birthday);
        tableView.setItems(tableData);

        Person.buildTestData(tableData);
    }

    @FXML
    private void onclick_btn1() {
        int id = RandomUtil.getIntWithRange(20000, 30000);
        String name = RandomUtil.getString(5);
        tableData.add(Person.build(id, name));
        System.out.println("Added, tableData.size: " + tableData.size());
    }

}
