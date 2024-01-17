package com.github.ScipioAM.scipio_fx.table;

import javafx.scene.control.TableRow;
import javafx.scene.input.MouseEvent;

/**
 * TableView的行点击事件监听器
 *
 * @author Alan Scipio
 * created on 2024/1/17
 */
@Deprecated
public interface FXRowClickListener<T> {

    void onClick(MouseEvent event, TableRow<T> row);

}
