package com.github.ScipioAM.scipio_fx.combobox.mfx;

import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import javafx.util.StringConverter;

import java.util.Collection;
import java.util.function.Function;

/**
 * @since 2022/6/24
 */
public class MFXComboBoxSupport {

    public static <T> MFXComboBox<T> setup(MFXComboBox<T> comboBox, Collection<T> data, Function<T, String> displayFunc) {
        if(comboBox == null) {
            comboBox = new MFXComboBox<>();
        }
        comboBox.getItems().addAll(data);
        StringConverter<T> converter = FunctionalStringConverter.to(item -> item == null ? "" : displayFunc.apply(item));
        comboBox.setConverter(converter);
        return comboBox;
    }

}
