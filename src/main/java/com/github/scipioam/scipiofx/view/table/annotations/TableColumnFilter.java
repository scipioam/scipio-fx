package com.github.scipioam.scipiofx.view.table.annotations;

import io.github.palexdev.materialfx.filter.base.AbstractFilter;

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
public @interface TableColumnFilter {

    Class<? extends AbstractFilter<?,?>> value();

}
