package com.github.scipioam.scipiofx.view.table.cell;

import javafx.scene.control.TableCell;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @since 2022/6/15
 */
public class DateTableCell<T> extends TableCell<T, Date> {

    private final DateFormat formatter;

    public DateTableCell(String pattern) {
        formatter = new SimpleDateFormat(pattern);
    }

    @Override
    protected void updateItem(Date item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText("");
        } else {
            setText(formatter.format(item));
        }
    }

}
