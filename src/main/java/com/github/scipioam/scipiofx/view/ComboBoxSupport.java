package com.github.scipioam.scipiofx.view;

import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

import java.util.Collection;
import java.util.function.Function;

/**
 * @since 2022/6/24
 */
public class ComboBoxSupport extends OptionsSupport {

    public static <T> ComboBox<T> setup(ComboBox<T> comboBox, ComboBoxOptions<T> options) {
        if (comboBox == null) {
            comboBox = new ComboBox<>();
        }
        setOptions(comboBox, options);
        if (options.getData() != null) {
            comboBox.getItems().addAll(options.getData());
        }
        Function<T, String> displayFunc = options.getDisplayFunc();
        if (displayFunc != null) {
            StringConverter<T> converter = new StringConverter<>() {
                @Override
                public String toString(T object) {
                    return object == null ? "" : displayFunc.apply(object);
                }

                @Override
                public T fromString(String string) {
                    return null;
                }
            };
            comboBox.setConverter(converter);
        }
        return comboBox;
    }

    public static <T> ComboBox<T> setup(ComboBox<T> comboBox, Function<T, String> displayFunc, Collection<T> data) {
        ComboBoxOptions<T> options = ComboBoxOptions.build(data)
                .setDisplayFunc(displayFunc);
        return setup(comboBox, options);
    }

    @SafeVarargs
    public static <T> ComboBox<T> setup(ComboBox<T> comboBox, Function<T, String> displayFunc, T... data) {
        ComboBoxOptions<T> options = ComboBoxOptions.build(data)
                .setDisplayFunc(displayFunc);
        return setup(comboBox, options);
    }

}
