package com.github.ScipioAM.scipio_fx.table.cell;

import javafx.scene.control.TableCell;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @since 2022/6/15
 */
public class LocalTimeTableCell<T> extends TableCell<T, LocalTime> {

    private final DateTimeFormatter formatter;

    public LocalTimeTableCell(String pattern) {
        formatter = DateTimeFormatter.ofPattern(pattern);
    }

    @Override
    protected void updateItem(LocalTime item, boolean empty) {
        super.updateItem(item, empty);
        if(empty || item == null) {
            setText("");
        } else {
            setText(formatter.format(item));
        }
    }

}
