package com.github.scipioam.scipiofx.view.table.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

/**
 * 时间日期格式化，优先级低于{@link TableColumnBind#cellImpl()}
 *
 * @since 2022/6/15
 */
@Target({FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TableColumnTimeFormat {

    String pattern();

}
