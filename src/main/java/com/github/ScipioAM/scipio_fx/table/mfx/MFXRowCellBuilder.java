package com.github.ScipioAM.scipio_fx.table.mfx;

import com.github.ScipioAM.scipio_fx.table.annotations.TableColumnBind;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;

import java.lang.reflect.Field;

/**
 * MFXTableView的单元格绑定构建器
 *
 * @since 2022/6/10
 */
@FunctionalInterface
public interface MFXRowCellBuilder<T> {

    /**
     * 绑定字段与单元格的映射关系
     *
     * @param instance    每行数据的实例对象
     * @param dataType    数据类型
     * @param columnField 当前的字段（只可能是加了@TableColumnBind注解的字段）
     * @param bindInfo    字段设定的信息
     * @return MFXTableView的单元格绑定结果
     */
    MFXTableRowCell<T, ?> build(T instance, Class<T> dataType, Field columnField, TableColumnBind bindInfo);

}
