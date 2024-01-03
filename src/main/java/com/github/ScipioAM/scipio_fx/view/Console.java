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

    /**
     * 每行的内容
     */
    private final List<String> lineList;

    /**
     * 自定义行前缀，为null时默认为行号(1-based)
     */
    private final String linePrefix;

    /**
     * 每行输入时的回调
     */
    private InputListener inputListener;

    /**
     * 是否可输入，true代表是
     */
    private boolean input = false;

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
        lineList = new ArrayList<>();
        this.input = input;
        if (input) {
            this.textArea.setText(getPrefix(0));
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
        lineList = new ArrayList<>();
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
        if (!lineList.isEmpty()) {
            //第n行的情况下（n > 1）
            s.append('\n').append(getPrefix(lineList.size())).append(text);
        } else if (input) {
            //可输入的情况下，第1行已初始化了前缀
            s.append(text);
        } else {
            //不可输入的情况下，第1行什么都没有
            s.append(getPrefix(0)).append(text);
        }
        textArea.setText(s.toString());
        lineList.add(text);
        return this;
    }

    /**
     * 清空内容
     */
    public void clear() {
        textArea.setText(getPrefix(0));
        lineList.clear();
    }

    /**
     * 获取行总数
     */
    public int getLineCount() {
        return lineList.size();
    }

    /**
     * 获取指定行的内容
     *
     * @param lineIndex 行号(0-based)
     */
    public String getLine(int lineIndex) {
        return lineList.get(lineIndex);
    }

    //=========================================== ↓↓↓↓↓↓ 内部调用 ↓↓↓↓↓↓ ===========================================

    /**
     * 获取最终的行前缀
     *
     * @param lineIndex 当前行号(0-based)
     */
    String getPrefix(int lineIndex) {
        if (StringUtils.isNull(linePrefix)) {
            return "[" + (lineIndex + 1) + "] ";
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
