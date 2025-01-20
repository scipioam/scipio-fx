package com.github.scipioam.scipiofx.view.table.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Comparator;

import static java.lang.annotation.ElementType.FIELD;

/**
 * @since 2022/6/13
 */
@Target({FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TableColumnComparator {

    Class<? extends Comparator<?>> value();

    /**
     * 是否需要在构造时尝试传入每列对应的{@link  java.lang.reflect.Field}对象
     * @return true：需要
     */
    boolean bindField() default false;

}
