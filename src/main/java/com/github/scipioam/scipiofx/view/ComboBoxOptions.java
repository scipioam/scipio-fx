package com.github.scipioam.scipiofx.view;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.function.Function;

/**
 * @since 2022/6/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ComboBoxOptions<T> extends Options {

    private Function<T, String> displayFunc;

    private Collection<T> data;

    @SafeVarargs
    public static <T> ComboBoxOptions<T> build(T... data) {
        return new ComboBoxOptions<T>().setData(data);
    }

    public static <T> ComboBoxOptions<T> build(Collection<T> data) {
        return new ComboBoxOptions<T>().setData(data);
    }

    public ComboBoxOptions<T> setData(Collection<T> data) {
        this.data = data;
        return this;
    }

    @SafeVarargs
    public final ComboBoxOptions<T> setData(T... data) {
        this.data = Arrays.asList(data);
        return this;
    }

    public ComboBoxOptions<T> setPreWidth(double preWidth) {
        super.preWidth = preWidth;
        return this;
    }

    public ComboBoxOptions<T> setPreHeight(double preHeight) {
        super.preHeight = preHeight;
        return this;
    }

    public ComboBoxOptions<T> setStyleClass(Set<String> styleClass) {
        super.styleClass = styleClass;
        return this;
    }

    public ComboBoxOptions<T> setStyleClass(String... styleClass) {
        super.setStyleClass(styleClass);
        return this;
    }

    public ComboBoxOptions<T> setStyleClass(Collection<String> styleClass) {
        super.setStyleClass(styleClass);
        return this;
    }

}
