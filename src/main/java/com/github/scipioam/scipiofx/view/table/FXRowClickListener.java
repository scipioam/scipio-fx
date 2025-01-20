package com.github.scipioam.scipiofx.view.table;

import javafx.scene.input.MouseEvent;

/**
 * TableView的行点击事件监听器
 *
 * @author Alan Scipio
 * created on 2024/1/17
 */
public interface FXRowClickListener<T> {

    void onClick(MouseEvent event, T rowData);

}
