package com.github.ScipioAM.scipio_fx.view;

import com.github.ScipioAM.scipio_fx.utils.StringUtils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextArea;

import java.util.List;

/**
 * {@link Console}的默认文本变化监听器（前提是指明console有输入）
 *
 * @since 2022/6/22
 */
class ConsoleChangeListener implements ChangeListener<String> {

    private final List<String> lines;
    private final TextArea textArea;

    public ConsoleChangeListener(List<String> lines, TextArea textArea) {
        this.lines = lines;
        this.textArea = textArea;
    }

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (StringUtils.isNull(newValue)) {
            return;
        }
        if (newValue.equals("\n")) {
            String[] arr = textArea.getText().split("\n");
            if (arr.length <= 1) {
                lines.add("");
            } else {
                lines.add(arr[arr.length - 1]);
            }
            textArea.setText("[" + (lines.size() + 1) + "]");
        }
    }

}
