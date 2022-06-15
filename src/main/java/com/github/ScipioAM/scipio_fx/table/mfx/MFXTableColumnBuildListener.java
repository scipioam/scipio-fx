package com.github.ScipioAM.scipio_fx.table.mfx;

import com.github.ScipioAM.scipio_fx.table.annotations.TableColumnBind;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;

import java.lang.reflect.Field;

/**
 * 构建MFXTableView列时的监听器
 *
 * @since 2022/6/10
 */
@FunctionalInterface
public interface MFXTableColumnBuildListener<T> {

    /**
     * 构建MFXTableView列时的监听器回调
     *
     * @param tableView   表格对象
     * @param column      列对象
     * @param dataType    数据类型
     * @param columnField 这列对应的数据字段
     * @param bindInfo    字段绑定信息
     * @return 是否继续。true：继续默认构建逻辑，false：不继续，跳到下一个字段的循环
     */
    boolean onBuildColumn(MFXTableView<T> tableView, MFXTableColumn<T> column, Class<T> dataType, Field columnField, TableColumnBind bindInfo);

}
