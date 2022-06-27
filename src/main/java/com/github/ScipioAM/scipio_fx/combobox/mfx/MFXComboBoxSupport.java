package com.github.ScipioAM.scipio_fx.combobox.mfx;

import com.github.ScipioAM.scipio_fx.combobox.ComboBoxOptions;
import com.github.ScipioAM.scipio_fx.view.OptionsSupport;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import javafx.util.StringConverter;

import java.util.Collection;
import java.util.function.Function;

/**
 * @since 2022/6/24
 */
public class MFXComboBoxSupport extends OptionsSupport {

    public static <T> MFXComboBox<T> setup(MFXComboBox<T> comboBox, ComboBoxOptions<T> options) {
        if (comboBox == null) {
            comboBox = new MFXComboBox<>();
        }
        setOptions(comboBox, options);
        if (options.getData() != null) {
            comboBox.getItems().addAll(options.getData());
        }
        Function<T, String> displayFunc = options.getDisplayFunc();
        if (displayFunc != null) {
            StringConverter<T> converter = FunctionalStringConverter.to(item -> item == null ? "" : displayFunc.apply(item));
            comboBox.setConverter(converter);
        }
        return comboBox;
    }

    public static <T> MFXComboBox<T> setup(MFXComboBox<T> comboBox, Function<T, String> displayFunc, Collection<T> data) {
        ComboBoxOptions<T> options = ComboBoxOptions.build(data)
                .setDisplayFunc(displayFunc);
        return setup(comboBox, options);
    }

    @SafeVarargs
    public static <T> MFXComboBox<T> setup(MFXComboBox<T> comboBox, Function<T, String> displayFunc, T... data) {
        ComboBoxOptions<T> options = ComboBoxOptions.build(data)
                .setDisplayFunc(displayFunc);
        return setup(comboBox, options);
    }

}
