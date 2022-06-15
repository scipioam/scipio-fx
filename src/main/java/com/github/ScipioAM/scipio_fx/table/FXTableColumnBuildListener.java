package com.github.ScipioAM.scipio_fx.table;

import com.github.ScipioAM.scipio_fx.table.annotations.TableColumnBind;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.lang.reflect.Field;

/**
 * 构建TableView列时的监听器
 *
 * @since 2022/6/13
 */
@FunctionalInterface
public interface FXTableColumnBuildListener<T> {

    /**
     * 构建TableView列时的监听器回调
     *
     * @param tableView    表格对象
     * @param column       列对象
     * @param dataType     数据类型
     * @param columnField  这列对应的数据字段
     * @param bindInfo     字段绑定信息
     * @param isTableView2 是否为ControlsFX包的威力加强版
     * @return 是否继续。true：继续默认构建逻辑，false：不继续，跳到下一个字段的循环
     */
    boolean onBuildColumn(TableView<T> tableView, TableColumn<T, ?> column, Class<T> dataType, Field columnField, TableColumnBind bindInfo, boolean isTableView2);

}
