package com.github.ScipioAM.scipio_fx.test.controllers;

import com.github.ScipioAM.scipio_fx.controller.BaseController;
import com.github.ScipioAM.scipio_fx.table.mfx.MFXTableBuilder;
import com.github.ScipioAM.scipio_fx.test.bean.Person;
import com.github.ScipioAM.scipio_fx.test.util.RandomUtil;
import io.github.palexdev.materialfx.controls.MFXPaginatedTableView;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.IntegerFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

import java.util.Comparator;

/**
 * @since 2022/6/7
 */
public class MFXTableController extends BaseController {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private MFXPaginatedTableView<Person> tableView;

    private ObservableList<Person> tableData;

    @Override
    public void onLoadInit(Parent rootNode, Object... initArgs) {
//        setupTableView();
        setupTV_builder();
    }

    private void setupTV_builder() {
        try {
            tableData = MFXTableBuilder
                    .builder(tableView)
                    .initDataSource(true)
                    .setDataType(Person.class)
                    .setRowsPerPage(10)
                    .setReadSuperClassFields(true)
                    .build();
            Person.buildTestData(tableData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void setupTableView() {
        MFXTableColumn<Person> col_id = new MFXTableColumn<>("ID", true, Comparator.comparing(Person::getId));
        MFXTableColumn<Person> col_name = new MFXTableColumn<>("NAME", true, Comparator.comparing(Person::getName));
        MFXTableColumn<Person> col_nickName = new MFXTableColumn<>("NICKNAME", true, Comparator.comparing(Person::getNickName));
        MFXTableColumn<Person> col_age = new MFXTableColumn<>("AGE", true, Comparator.comparing(Person::getAge));
        MFXTableColumn<Person> col_gender = new MFXTableColumn<>("GENDER", true, Comparator.comparing(Person::getGender));
        MFXTableColumn<Person> col_email = new MFXTableColumn<>("EMAIL", true, Comparator.comparing(Person::getEmail));
//        MFXTableColumn<Person> col_remark = new MFXTableColumn<>("REMARK", true, Comparator.comparing(Person::getRemark));

        col_id.setRowCellFactory(person -> new MFXTableRowCell<>(Person::getId));
        col_name.setRowCellFactory(person -> new MFXTableRowCell<>(Person::getName));
        col_nickName.setRowCellFactory(person -> new MFXTableRowCell<>(Person::getNickName));
        col_age.setRowCellFactory(person -> new MFXTableRowCell<>(Person::getAge));
        col_gender.setRowCellFactory(person -> new MFXTableRowCell<>(Person::getGender));
        col_email.setRowCellFactory(person -> new MFXTableRowCell<>(Person::getEmail));
//        col_remark.setRowCellFactory(person -> new MFXTableRowCell<>(Person::getRemark));
//        ageColumn.setRowCellFactory(person -> new MFXTableRowCell<>(Person::getAge) {{
//            setAlignment(Pos.CENTER_RIGHT);
//        }});
//        col_age.setAlignment(Pos.CENTER_RIGHT);

        tableView.getTableColumns().addAll(col_id, col_name, col_nickName, col_age, col_gender, col_email);
        tableView.getFilters().addAll(
                new StringFilter<>("NAME", Person::getName),
                new IntegerFilter<>("ID", Person::getId),
                new IntegerFilter<>("AGE", Person::getAge)
        );

        tableData = FXCollections.observableArrayList();

//        Person.buildTestData();
        tableView.setItems(tableData);
//        Person.buildTestData();
//        tableData.add(Person.build(654321, "asd")); //如果一开始tableData是空的话，这里的add是没有用的
//        tableView.update(); //帮不上上一行的忙
    }

    @FXML
    private void onclick_btn1() {
        int id = RandomUtil.getIntWithRange(20000, 30000);
        tableData.add(Person.build(id, "newPerson"));
    }

}
