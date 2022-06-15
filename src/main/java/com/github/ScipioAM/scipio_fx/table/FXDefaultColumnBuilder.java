package com.github.ScipioAM.scipio_fx.table;

import com.github.ScipioAM.scipio_fx.table.annotations.TableColumnBind;
import com.github.ScipioAM.scipio_fx.table.annotations.TableColumnTimeFormat;
import com.github.ScipioAM.scipio_fx.table.cell.*;
import javafx.beans.property.*;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import org.controlsfx.control.tableview2.TableColumn2;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * @since 2022/6/14
 */
public class FXDefaultColumnBuilder<T> implements FXTableColumnBuilder<T> {

    @SuppressWarnings("unchecked")
    @Override
    public TableColumn<T, ?> build(Field field, TableColumnBind bindInfo, boolean isTableView2) throws Exception {
        Class<?> fieldType = field.getType();
        Class<? extends TableCell<?, ?>> cellImpl = bindInfo.cellImpl();
        if (fieldType == String.class || fieldType == StringProperty.class) {
            TableColumn<T, String> column = isTableView2 ? new TableColumn2<>(bindInfo.title()) : new TableColumn<>(bindInfo.title());
            //列数据绑定
            column.setCellValueFactory(new PropertyValueFactory<>(field.getName()));
            //单元格的自定义实现
            if (cellImpl != EmptyTableCell.class) {
                TableCell<T, String> tc = (TableCell<T, String>) cellImpl.getDeclaredConstructor().newInstance();
                column.setCellFactory(param -> tc);
            }
            return column;
        }
//        else if (fieldType == Character.class) {
//            TableColumn<T, Character> column = isTableView2 ? new TableColumn2<>(bindInfo.title()) : new TableColumn<>(bindInfo.title());
//            column.setCellValueFactory(new PropertyValueFactory<>(field.getName()));
//            return column;
//        }
        else if (fieldType == Integer.class || fieldType == IntegerProperty.class) {
            TableColumn<T, Integer> column = isTableView2 ? new TableColumn2<>(bindInfo.title()) : new TableColumn<>(bindInfo.title());
            //列数据绑定
            column.setCellValueFactory(new PropertyValueFactory<>(field.getName()));
            //单元格的自定义实现
            if (cellImpl != EmptyTableCell.class) {
                TableCell<T, Integer> tc = (TableCell<T, Integer>) cellImpl.getDeclaredConstructor().newInstance();
                column.setCellFactory(param -> tc);
            }
            return column;
        } else if (fieldType == Long.class || fieldType == LongProperty.class) {
            TableColumn<T, Long> column = isTableView2 ? new TableColumn2<>(bindInfo.title()) : new TableColumn<>(bindInfo.title());
            //列数据绑定
            column.setCellValueFactory(new PropertyValueFactory<>(field.getName()));
            //单元格的自定义实现
            if (cellImpl != EmptyTableCell.class) {
                TableCell<T, Long> tc = (TableCell<T, Long>) cellImpl.getDeclaredConstructor().newInstance();
                column.setCellFactory(param -> tc);
            }
            return column;
        } else if (fieldType == Double.class || fieldType == DoubleProperty.class) {
            TableColumn<T, Double> column = isTableView2 ? new TableColumn2<>(bindInfo.title()) : new TableColumn<>(bindInfo.title());
            //列数据绑定
            column.setCellValueFactory(new PropertyValueFactory<>(field.getName()));
            //单元格的自定义实现
            if (cellImpl != EmptyTableCell.class) {
                TableCell<T, Double> tc = (TableCell<T, Double>) cellImpl.getDeclaredConstructor().newInstance();
                column.setCellFactory(param -> tc);
            }
            return column;
        } else if (fieldType == Float.class || fieldType == FloatProperty.class) {
            TableColumn<T, Float> column = isTableView2 ? new TableColumn2<>(bindInfo.title()) : new TableColumn<>(bindInfo.title());
            //列数据绑定
            column.setCellValueFactory(new PropertyValueFactory<>(field.getName()));
            //单元格的自定义实现
            if (cellImpl != EmptyTableCell.class) {
                TableCell<T, Float> tc = (TableCell<T, Float>) cellImpl.getDeclaredConstructor().newInstance();
                column.setCellFactory(param -> tc);
            }
            return column;
        }
//        else if (fieldType == Short.class) {
//            TableColumn<T, Short> column = isTableView2 ? new TableColumn2<>(bindInfo.title()) : new TableColumn<>(bindInfo.title());
//            column.setCellValueFactory(new PropertyValueFactory<>(field.getName()));
//            return column;
//        }
        else if (fieldType == Boolean.class || fieldType == BooleanProperty.class) {
            TableColumn<T, Boolean> column = isTableView2 ? new TableColumn2<>(bindInfo.title()) : new TableColumn<>(bindInfo.title());
            //列数据绑定
            column.setCellValueFactory(new PropertyValueFactory<>(field.getName()));
            //单元格的自定义实现
            if (cellImpl != EmptyTableCell.class) {
                TableCell<T, Boolean> tc = (TableCell<T, Boolean>) cellImpl.getDeclaredConstructor().newInstance();
                column.setCellFactory(param -> tc);
            }
            return column;
        } else if (fieldType == BigDecimal.class) {
            TableColumn<T, BigDecimal> column = isTableView2 ? new TableColumn2<>(bindInfo.title()) : new TableColumn<>(bindInfo.title());
            //列数据绑定
            column.setCellValueFactory(new PropertyValueFactory<>(field.getName()));
            //单元格的自定义实现
            if (cellImpl != EmptyTableCell.class) {
                TableCell<T, BigDecimal> tc = (TableCell<T, BigDecimal>) cellImpl.getDeclaredConstructor().newInstance();
                column.setCellFactory(param -> tc);
            }
            return column;
        } else if (fieldType == BigInteger.class) {
            TableColumn<T, BigInteger> column = isTableView2 ? new TableColumn2<>(bindInfo.title()) : new TableColumn<>(bindInfo.title());
            //列数据绑定
            column.setCellValueFactory(new PropertyValueFactory<>(field.getName()));
            //单元格的自定义实现
            if (cellImpl != EmptyTableCell.class) {
                TableCell<T, BigInteger> tc = (TableCell<T, BigInteger>) cellImpl.getDeclaredConstructor().newInstance();
                column.setCellFactory(param -> tc);
            }
            return column;
        } else if (fieldType == Date.class) {
            TableColumn<T, Date> column = isTableView2 ? new TableColumn2<>(bindInfo.title()) : new TableColumn<>(bindInfo.title());
            //列数据绑定
            column.setCellValueFactory(new PropertyValueFactory<>(field.getName()));
            //时间日期格式化
            TableColumnTimeFormat formatInfo = field.getAnnotation(TableColumnTimeFormat.class);
            if (formatInfo != null && bindInfo.cellImpl() == EmptyTableCell.class) {
                column.setCellFactory(param -> new DateTableCell<>(formatInfo.pattern()));
            }
            //单元格的自定义实现
            else if (cellImpl != EmptyTableCell.class) {
                TableCell<T, Date> tc = (TableCell<T, Date>) cellImpl.getDeclaredConstructor().newInstance();
                column.setCellFactory(param -> tc);
            }
            return column;
        } else if (fieldType == LocalDate.class) {
            TableColumn<T, LocalDate> column = isTableView2 ? new TableColumn2<>(bindInfo.title()) : new TableColumn<>(bindInfo.title());
            //列数据绑定
            column.setCellValueFactory(new PropertyValueFactory<>(field.getName()));
            //时间日期格式化
            TableColumnTimeFormat formatInfo = field.getAnnotation(TableColumnTimeFormat.class);
            if (formatInfo != null && bindInfo.cellImpl() == EmptyTableCell.class) {
                column.setCellFactory(param -> new LocalDateTableCell<>(formatInfo.pattern()));
            }
            //单元格的自定义实现
            else if (cellImpl != EmptyTableCell.class) {
                TableCell<T, LocalDate> tc = (TableCell<T, LocalDate>) cellImpl.getDeclaredConstructor().newInstance();
                column.setCellFactory(param -> tc);
            }
            return column;
        } else if (fieldType == LocalDateTime.class) {
            TableColumn<T, LocalDateTime> column = isTableView2 ? new TableColumn2<>(bindInfo.title()) : new TableColumn<>(bindInfo.title());
            //列数据绑定
            column.setCellValueFactory(new PropertyValueFactory<>(field.getName()));
            //时间日期格式化
            TableColumnTimeFormat formatInfo = field.getAnnotation(TableColumnTimeFormat.class);
            if (formatInfo != null && bindInfo.cellImpl() == EmptyTableCell.class) {
                column.setCellFactory(param -> new LocalDateTimeTableCell<>(formatInfo.pattern()));
            }
            //单元格的自定义实现
            else if (cellImpl != EmptyTableCell.class) {
                TableCell<T, LocalDateTime> tc = (TableCell<T, LocalDateTime>) cellImpl.getDeclaredConstructor().newInstance();
                column.setCellFactory(param -> tc);
            }
            return column;
        } else if (fieldType == LocalTime.class) {
            TableColumn<T, LocalTime> column = isTableView2 ? new TableColumn2<>(bindInfo.title()) : new TableColumn<>(bindInfo.title());
            //列数据绑定
            column.setCellValueFactory(new PropertyValueFactory<>(field.getName()));
            //时间日期格式化
            TableColumnTimeFormat formatInfo = field.getAnnotation(TableColumnTimeFormat.class);
            if (formatInfo != null && bindInfo.cellImpl() == EmptyTableCell.class) {
                column.setCellFactory(param -> new LocalTimeTableCell<>(formatInfo.pattern()));
            }
            //单元格的自定义实现
            else if (cellImpl != EmptyTableCell.class) {
                TableCell<T, LocalTime> tc = (TableCell<T, LocalTime>) cellImpl.getDeclaredConstructor().newInstance();
                column.setCellFactory(param -> tc);
            }
            return column;
        } else {
            throw new UnsupportedOperationException("unsupported fieldType for FXRowCellBuilder.buildFactory: " + fieldType.getName());
        }
    }

}
