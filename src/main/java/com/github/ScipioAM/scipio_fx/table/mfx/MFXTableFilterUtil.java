package com.github.ScipioAM.scipio_fx.table.mfx;

import com.github.ScipioAM.scipio_fx.utils.ReflectUtil;
import io.github.palexdev.materialfx.filter.*;
import io.github.palexdev.materialfx.filter.base.AbstractFilter;
import javafx.beans.property.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @since 2022/6/10
 */
class MFXTableFilterUtil {

    private MFXTableFilterUtil() {
    }

    static <T> AbstractFilter<T, ?> getDefaultFilter(String filterName, Field columnField, Class<T> type) {
        if (filterName == null || "".equals(filterName)) {
            filterName = columnField.getName();
        }
        Class<?> fieldType = columnField.getType();
        AbstractFilter<T, ?> filter;
        if (fieldType == String.class || fieldType == StringProperty.class) {
            filter = new StringFilter<>(filterName, t -> {
                try {
                    Method method = type.getMethod(ReflectUtil.getterMethodName(columnField.getName()));
                    return (String) method.invoke(t);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        } else if (fieldType == Integer.class || fieldType == IntegerProperty.class) {
            filter = new IntegerFilter<>(filterName, t -> {
                try {
                    Method method = type.getMethod(ReflectUtil.getterMethodName(columnField.getName()));
                    return (Integer) method.invoke(t);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        } else if (fieldType == Double.class || fieldType == DoubleProperty.class) {
            filter = new DoubleFilter<>(filterName, t -> {
                try {
                    Method method = type.getMethod(ReflectUtil.getterMethodName(columnField.getName()));
                    return (Double) method.invoke(t);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        } else if (fieldType == Long.class || fieldType == LongProperty.class) {
            filter = new LongFilter<>(filterName, t -> {
                try {
                    Method method = type.getMethod(ReflectUtil.getterMethodName(columnField.getName()));
                    return (Long) method.invoke(t);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        } else if (fieldType == Boolean.class || fieldType == BooleanProperty.class) {
            filter = new BooleanFilter<>(filterName, t -> {
                try {
                    Method method = type.getMethod(ReflectUtil.getterMethodName(columnField.getName()));
                    return (Boolean) method.invoke(t);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        } else if (fieldType == Float.class || fieldType == FloatProperty.class) {
            filter = new FloatFilter<>(filterName, t -> {
                try {
                    Method method = type.getMethod(ReflectUtil.getterMethodName(columnField.getName()));
                    return (Float) method.invoke(t);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        } else {
            throw new UnsupportedOperationException("unsupported fieldType for MFXTableColumn.AbstractFilter: " + fieldType.getName());
        }
        return filter;
    }

}
