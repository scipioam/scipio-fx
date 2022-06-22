package com.github.ScipioAM.scipio_fx.view;

import com.github.ScipioAM.scipio_fx.utils.StringUtils;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.TextArea;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 一个小的辅助工具，
 * 用于包装一个{@link TextArea}来方便输出
 *
 * @since 2022/6/21
 */
@Data
public class Console {

    private final TextArea textArea;

    private final List<String> lines;

    private final String linePrefix;

    private InputListener inputListener;

    //=========================================== ↓↓↓↓↓↓ 构造方法 ↓↓↓↓↓↓ ===========================================

    /**
     * 构造方法，无输入
     */
    public Console(TextArea textArea) {
        this(textArea, false, null);
    }

    /**
     * 构造方法
     *
     * @param input 是否有输入。true:有
     */
    public Console(TextArea textArea, boolean input, String linePrefix) {
        if (textArea == null) {
            throw new IllegalArgumentException("textArea can not be null");
        }
        this.textArea = textArea;
        this.linePrefix = linePrefix;
        lines = new ArrayList<>();
        if (input) {
            this.textArea.setText(getPrefix(1));
            this.textArea.textProperty().addListener(new ConsoleChangeListener(this, inputListener));
        }
    }

    public Console(TextArea textArea, boolean input) {
        this(textArea, input, null);
    }

    /**
     * 构造方法
     *
     * @param textChangeListener 自定义文本变化监听器
     */
    public Console(TextArea textArea, ChangeListener<String> textChangeListener, String linePrefix) {
        if (textArea == null) {
            throw new IllegalArgumentException("textArea can not be null");
        }
        this.textArea = textArea;
        this.linePrefix = linePrefix;
        lines = new ArrayList<>();
        if (textChangeListener != null) {
            this.textArea.textProperty().addListener(textChangeListener);
        }
    }

    public Console(TextArea textArea, ChangeListener<String> textChangeListener) {
        this(textArea, textChangeListener, null);
    }

    //=========================================== ↓↓↓↓↓↓ API ↓↓↓↓↓↓ ===========================================

    /**
     * 输出
     *
     * @param text 要输出的内容（1行内容）
     */
    public Console output(String text) {
        StringBuilder s = new StringBuilder(textArea.getText());
        if (lines.size() > 0) {
            s.append('\n');
        }
        s.append(getPrefix(lines.size() + 1)).append(text);
        textArea.setText(s.toString());
        lines.add(text);
        return this;
    }

    /**
     * 清空内容
     */
    public void clear() {
        textArea.setText(getPrefix(1));
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

    //=========================================== ↓↓↓↓↓↓ 内部调用 ↓↓↓↓↓↓ ===========================================

    /**
     * 获取最终的行前缀
     *
     * @param lineNo 当前行号
     */
    String getPrefix(int lineNo) {
        if (StringUtils.isNull(linePrefix)) {
            return "[" + lineNo + "]";
        } else {
            return linePrefix;
        }
    }

    /**
     * 是否为默认的行前缀
     *
     * @return true:是
     */
    boolean isDefaultPrefix() {
        return StringUtils.isNull(linePrefix);
    }

    public interface InputListener {

        /**
         * 输入时的回调
         *
         * @param inputLine 输入的内容
         */
        void onInput(TextArea textArea, String inputLine);

    }

}
