package com.github.ScipioAM.scipio_fx.view;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.TextArea;

import java.util.ArrayList;
import java.util.List;

/**
 * 一个小的辅助工具，
 * 用于包装一个{@link TextArea}来方便输出
 *
 * @since 2022/6/21
 */
public class Console {

    private final TextArea textArea;

    private final List<String> lines;

    /**
     * 构造方法，无输入
     */
    public Console(TextArea textArea) {
        this(textArea, false);
    }

    /**
     * 构造方法
     *
     * @param input 是否有输入。true:有
     */
    public Console(TextArea textArea, boolean input) {
        if (textArea == null) {
            throw new IllegalArgumentException("textArea can not be null");
        }
        this.textArea = textArea;
        lines = new ArrayList<>();
        if (input) {
            this.textArea.textProperty().addListener(new ConsoleChangeListener(lines, textArea));
        }
    }

    /**
     * 构造方法
     *
     * @param textChangeListener 自定义文本变化监听器
     */
    public Console(TextArea textArea, ChangeListener<String> textChangeListener) {
        if (textArea == null) {
            throw new IllegalArgumentException("textArea can not be null");
        }
        this.textArea = textArea;
        lines = new ArrayList<>();
        if (textChangeListener != null) {
            this.textArea.textProperty().addListener(textChangeListener);
        }
    }

    /**
     * 输出
     *
     * @param text 要输出的内容（1行内容）
     */
    public Console output(String text) {
        lines.add(text);
        textArea.setText("[" + (lines.size() + 1) + "]" + textArea.getText() + text + '\n');
        return this;
    }

    /**
     * 清空内容
     */
    public void clear() {
        textArea.setText("");
        lines.clear();
    }

    /**
     * 获取行总数
     */
    public int getLineCount() {
        return lines.size();
    }

    /**
     * 获取指定行的内容
     *
     * @param lineNo 行号(1-based)
     */
    public String getLineContent(int lineNo) {
        return lines.get((lineNo + 1));
    }

}
