package com.github.scipioam.scipiofx.view.table.cell;

import javafx.scene.control.TableCell;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @since 2022/6/15
 */
public class LocalDateTableCell<T> extends TableCell<T, LocalDate> {

    private final DateTimeFormatter formatter;

    public LocalDateTableCell(String datePattern) {
        formatter = DateTimeFormatter.ofPattern(datePattern);
    }

    @Override
    protected void updateItem(LocalDate item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText("");
        } else {
            setText(formatter.format(item));
        }
    }

}
