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

    private final Console console;
    private final Console.InputListener inputListener;

    public ConsoleChangeListener(Console console, Console.InputListener inputListener) {
        this.console = console;
        this.inputListener = inputListener;
    }

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (StringUtils.isNull(newValue)) {
            return;
        }

        TextArea textArea = console.getTextArea();
        List<String> lines = console.getLines();

        //换行时的处理
        if (newValue.charAt(newValue.length() - 1) == '\n') {
            //拿到新输入的行
            String[] arr = textArea.getText().split("\n");
            String lastLine = arr[arr.length - 1];
            //处理掉行前缀
            String prefix = console.getPrefix(lines.size() + 1);
            String trueLine = lastLine.replace(prefix,"");
            //记录下来
            lines.add(trueLine);
            //回调
            if (inputListener != null) {
                inputListener.onInput(textArea, trueLine);
            }
            //确定显示
            textArea.setText(newValue + console.getPrefix(lines.size() + 1));
        }
    }

}
