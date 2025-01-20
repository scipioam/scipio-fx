package com.github.scipioam.scipiofx.view.table;

import com.github.scipioam.scipiofx.view.table.annotations.TableColumnBind;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;

/**
 * @since 2022/6/30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableColumnBindEntry {

    private Field field;

    private TableColumnBind bindInfo;

    private int sort;

}
