package com.github.ScipioAM.scipio_fx.table.annotations;

import com.github.ScipioAM.scipio_fx.table.cell.EmptyTableCell;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

/**
 * @since 2022/6/10
 */
@Target({FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TableColumnBind {

    /**
     * 列标题
     */
    String title();

    /**
     * 列是否可被拖动为新的宽度
     *
     * @return true：可以
     */
    boolean resizable() default true;

    /**
     * 是否可被筛选
     *
     * @return true：可被筛选
     */
    boolean filtered() default false;

    /**
     * 过滤器里字段的标题
     *
     * @return 默认就是java的字段名
     */
    String filterName() default "";

    /**
     * 列内容的位置
     *
     * @return 默认左对齐
     */
    Pos alignment() default Pos.CENTER_LEFT;

    /**
     * 是否开启列排序
     *
     * @return true：开启
     */
    boolean compared() default false;

    /**
     * 单元格的自定义实现，默认为没有自定义实现
     */
    Class<? extends TableCell<?, ?>> cellImpl() default EmptyTableCell.class;

    /**
     * 列顺序
     *
     * @return 数字越大越靠右
     */
    int order() default 100;

    /**
     * 如果字符串值为null，显示为空白（而不是null字样）
     */
    boolean blankIfNull() default true;

    /**
     * 百分比列宽（0-1）
     * @return 超出范围就不设定（小于等于0或大于1）
     */
    double widthPercent() default 0.0;

}
