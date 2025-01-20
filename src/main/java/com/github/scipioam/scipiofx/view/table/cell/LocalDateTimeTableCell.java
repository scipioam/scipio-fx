package com.github.scipioam.scipiofx.view.table.cell;

import javafx.scene.control.TableCell;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @since 2022/6/15
 */
public class LocalDateTimeTableCell<T> extends TableCell<T, LocalDateTime> {

    private final DateTimeFormatter formatter;

    public LocalDateTimeTableCell(String pattern) {
        formatter = DateTimeFormatter.ofPattern(pattern);
    }

    @Override
    protected void updateItem(LocalDateTime item, boolean empty) {
        super.updateItem(item, empty);
        if(empty || item == null) {
            setText("");
        } else {
            setText(formatter.format(item));
        }
    }

}
