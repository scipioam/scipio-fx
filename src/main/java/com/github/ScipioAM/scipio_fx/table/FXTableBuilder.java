package com.github.ScipioAM.scipio_fx.table;

import com.github.ScipioAM.scipio_fx.table.annotations.TableColumnBind;
import com.github.ScipioAM.scipio_fx.table.annotations.TableColumnComparator;
import com.github.ScipioAM.scipio_fx.table.annotations.TableColumnTimeFormat;
import com.github.ScipioAM.scipio_fx.table.cell.*;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.controlsfx.control.tableview2.TableView2;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;

/**
 * {@link TableView}的构建器
 *
 * @since 2022/6/13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class FXTableBuilder<T> extends AbstractTableBuilder<T> {

    @Setter(AccessLevel.NONE)
    private TableView<T> tableView;
    private boolean isTableView2 = false; //是否为controlsFX包里的威力加强版（TableView2）
    private FXTableColumnBuildListener<T> columnBuildListener;
    private FXTableColumnBuilder<T> columnBuilder;

    //===============================================================================================================================================

    public FXTableBuilder(boolean isTableView2) {
        this.isTableView2 = isTableView2;
        this.tableView = isTableView2 ? new TableView2<>() : new TableView<>();
    }

    public FXTableBuilder(TableView<T> tableView) {
        if (tableView == null) {
            this.tableView = new TableView<>();
        } else {
            isTableView2 = (tableView instanceof TableView2);
            this.tableView = tableView;
        }
    }

    public static <T> FXTableBuilder<T> builder(boolean isTableView2) {
        return new FXTableBuilder<>(isTableView2);
    }

    public static <T> FXTableBuilder<T> builder(TableView<T> tableView) {
        return new FXTableBuilder<>(tableView);
    }

    //===============================================================================================================================================

    /**
     * 每列的构建
     *
     * @param field    该列对应的字段
     * @param bindInfo 该字段上设定的基础信息
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    protected void fieldColumnBuild(Field field, TableColumnBind bindInfo) throws Exception {
        if (columnBuilder == null) {
            columnBuilder = new FXDefaultColumnBuilder<>();
        }
        TableColumn<T, ?> column = columnBuilder.build(field, bindInfo, isTableView2);
        //行构建监听器，可自定义构建逻辑，以取代后续所有默认构建逻辑
        if (columnBuildListener != null) {
            if (!columnBuildListener.onBuildColumn(tableView, column, dataType, field, bindInfo, isTableView2)) {
                return;
            }
        }
        //自定义列排序
        if (bindInfo.compared()) {
            TableColumnComparator comparatorInfo = field.getAnnotation(TableColumnComparator.class);
            if (comparatorInfo != null) {
                Class<? extends Comparator<?>> comparatorType = comparatorInfo.value();
                Comparator comparator;
                if (comparatorInfo.bindField()) {
                    comparator = comparatorType.getDeclaredConstructor(Field.class).newInstance(field);
                } else {
                    comparator = comparatorType.getDeclaredConstructor().newInstance();
                }
                column.setComparator(comparator);
            }
        }

        tableView.getColumns().add(column);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void buildCellFactory(TableColumn<T, ?> column, Field field, TableColumnBind bindInfo) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        TableColumnTimeFormat formatInfo = field.getAnnotation(TableColumnTimeFormat.class);
        Class<? extends TableCell<?,?>> cellImpl = bindInfo.cellImpl();
        if (formatInfo != null && cellImpl == EmptyTableCell.class) {
            Class<?> fieldType = field.getType();
            String pattern = formatInfo.pattern();
            TableCell tc;
            if (fieldType == LocalDate.class) {
                tc = new LocalDateTableCell<T>(pattern);
            } else if (fieldType == LocalDateTime.class) {
                tc = new LocalDateTimeTableCell<T>(pattern);
            } else if (fieldType == Date.class) {
                tc = new DateTableCell<T>(pattern);
            } else if (fieldType == LocalTime.class) {
                tc = new LocalTimeTableCell<>(pattern);
            } else {
                throw new UnsupportedOperationException("unsupported fieldType for @TableColumnTimeFormat: " + fieldType.getName());
            }
            column.setCellFactory(param -> tc);
        } else if (cellImpl != EmptyTableCell.class) {
            //自定义实现
            TableCell tc = cellImpl.getDeclaredConstructor().newInstance();
            column.setCellFactory(param -> tc);
        }
    }

    /**
     * 其他构建
     */
    @Override
    protected void otherBuild() {
        tableView.setItems(dataSource);
    }

    //===============================================================================================================================================

    public FXTableBuilder<T> initDataSource(boolean initEmptyData) {
        return (FXTableBuilder<T>) super.initDataSource(initEmptyData);
    }

    @Override
    public FXTableBuilder<T> initDataSource(Collection<T> initData) {
        return (FXTableBuilder<T>) super.initDataSource(initData);
    }

    @Override
    public FXTableBuilder<T> setDataType(Class<T> dataType) {
        return (FXTableBuilder<T>) super.setDataType(dataType);
    }
}
