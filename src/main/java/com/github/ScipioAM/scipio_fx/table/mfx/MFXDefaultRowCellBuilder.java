package com.github.ScipioAM.scipio_fx.table.mfx;

import com.github.ScipioAM.scipio_fx.table.annotations.TableColumnBind;
import com.github.ScipioAM.scipio_fx.table.annotations.TableColumnTimeFormat;
import com.github.ScipioAM.scipio_fx.utils.ReflectUtil;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @since 2022/6/15
 */
public class MFXDefaultRowCellBuilder<T> implements MFXRowCellBuilder<T> {

    @Override
    public MFXTableRowCell<T, ?> build(T instance, Class<T> dataType, Field field, TableColumnBind bindInfo) {
        return new MFXTableRowCell<>(t -> {
            try {
                Method method = dataType.getMethod(ReflectUtil.getterMethodName(field.getName()));
                Object data = method.invoke(t);
                Class<?> fieldType = field.getType();
                if (bindInfo.blankIfNull() && fieldType == String.class && data == null) {
                    return "";
                }
                if (fieldType == LocalDate.class || fieldType == LocalDateTime.class || fieldType == LocalTime.class || fieldType == Date.class) {
                    TableColumnTimeFormat formatInfo = field.getAnnotation(TableColumnTimeFormat.class);
                    if (formatInfo == null) {
                        return data;
                    }
                    //时间日期格式化
                    if (fieldType == LocalDate.class) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatInfo.pattern());
                        return formatter.format((LocalDate) data);
                    } else if (fieldType == LocalDateTime.class) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatInfo.pattern());
                        return formatter.format((LocalDateTime) data);
                    } else if (fieldType == LocalTime.class) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatInfo.pattern());
                        return formatter.format((LocalTime) data);
                    } else {
                        DateFormat formatter = new SimpleDateFormat(formatInfo.pattern());
                        return formatter.format((Date) data);
                    }
                } else {
                    return data;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

}
