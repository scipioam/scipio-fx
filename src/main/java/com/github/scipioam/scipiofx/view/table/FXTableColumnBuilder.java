package com.github.scipioam.scipiofx.view.table;

import com.github.scipioam.scipiofx.view.table.annotations.TableColumnBind;
import javafx.scene.control.TableColumn;

import java.lang.reflect.Field;

/**
 * JavaFX的TableView的column对象构建器
 *
 * @since 2022/6/14
 */
@FunctionalInterface
public interface FXTableColumnBuilder<T> {

    /**
     * 构建column对象
     *
     * @param field        对应的字段
     * @param bindInfo     设定的信息
     * @param isTableView2 是否为ControlsFX包里的威力增强版
     * @return column对象
     */
    TableColumn<T, ?> build(Field field, TableColumnBind bindInfo, boolean isTableView2) throws Exception;

}
