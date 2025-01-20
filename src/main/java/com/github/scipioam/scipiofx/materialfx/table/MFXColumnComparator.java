package com.github.scipioam.scipiofx.materialfx.table;

import javafx.beans.property.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.Date;

/**
 * 列排序器
 *
 * @author Alan Scipio
 * @since 2022/6/11
 */
public class MFXColumnComparator<T> implements Comparator<T> {

    protected final Field field;

    public MFXColumnComparator(Field field) {
        this.field = field;
    }

    @Override
    public int compare(T o1, T o2) {
        try {
            Class<?> type = o1.getClass();
            Object o1Val = getFieldValue(type, field, o1);
            Object o2Val = getFieldValue(type, field, o2);
            Class<?> fieldType = field.getType();
            if (fieldType == String.class || fieldType == StringProperty.class) {
                String trueO1Val = (String) o1Val;
                String trueO2Val = (String) o2Val;
                return trueO1Val.compareTo(trueO2Val);
            } else if (fieldType == Integer.class || fieldType == IntegerProperty.class) {
                Integer trueO1Val = (Integer) o1Val;
                Integer trueO2Val = (Integer) o2Val;
                return trueO1Val.compareTo(trueO2Val);
            } else if (fieldType == Long.class || fieldType == LongProperty.class) {
                Long trueO1Val = (Long) o1Val;
                Long trueO2Val = (Long) o2Val;
                return trueO1Val.compareTo(trueO2Val);
            } else if (fieldType == Double.class || fieldType == DoubleProperty.class) {
                Double trueO1Val = (Double) o1Val;
                Double trueO2Val = (Double) o2Val;
                return trueO1Val.compareTo(trueO2Val);
            } else if (fieldType == Float.class || fieldType == FloatProperty.class) {
                Float trueO1Val = (Float) o1Val;
                Float trueO2Val = (Float) o2Val;
                return trueO1Val.compareTo(trueO2Val);
            } else if (fieldType == Byte.class) {
                Byte trueO1Val = (Byte) o1Val;
                Byte trueO2Val = (Byte) o2Val;
                return trueO1Val.compareTo(trueO2Val);
            } else if (fieldType == Boolean.class || fieldType == BooleanProperty.class) {
                Boolean trueO1Val = (Boolean) o1Val;
                Boolean trueO2Val = (Boolean) o2Val;
                return trueO1Val.compareTo(trueO2Val);
            } else if (fieldType == Character.class) {
                Character trueO1Val = (Character) o1Val;
                Character trueO2Val = (Character) o2Val;
                return trueO1Val.compareTo(trueO2Val);
            } else if (fieldType == BigDecimal.class) {
                BigDecimal trueO1Val = (BigDecimal) o1Val;
                BigDecimal trueO2Val = (BigDecimal) o2Val;
                return trueO1Val.compareTo(trueO2Val);
            } else if (fieldType == Short.class) {
                Short trueO1Val = (Short) o1Val;
                Short trueO2Val = (Short) o2Val;
                return trueO1Val.compareTo(trueO2Val);
            } else if (fieldType == BigInteger.class) {
                BigInteger trueO1Val = (BigInteger) o1Val;
                BigInteger trueO2Val = (BigInteger) o2Val;
                return trueO1Val.compareTo(trueO2Val);
            } else if (fieldType == Date.class) {
                Date trueO1Val = (Date) o1Val;
                Date trueO2Val = (Date) o2Val;
                return trueO1Val.compareTo(trueO2Val);
            } else if (fieldType == LocalDate.class) {
                LocalDate trueO1Val = (LocalDate) o1Val;
                LocalDate trueO2Val = (LocalDate) o2Val;
                return trueO1Val.compareTo(trueO2Val);
            } else if (fieldType == LocalDateTime.class) {
                LocalDateTime trueO1Val = (LocalDateTime) o1Val;
                LocalDateTime trueO2Val = (LocalDateTime) o2Val;
                return trueO1Val.compareTo(trueO2Val);
            } else if (fieldType == LocalTime.class) {
                LocalTime trueO1Val = (LocalTime) o1Val;
                LocalTime trueO2Val = (LocalTime) o2Val;
                return trueO1Val.compareTo(trueO2Val);
            } else {
                throw new UnsupportedOperationException("unsupported fieldType for ColumnComparator.compare: " + fieldType.getName());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object getFieldValue(Class<?> type, Field field, Object instance) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Method method = type.getMethod(getMethodName(field.getName()));
        return method.invoke(instance);
    }

    /**
     * 拼凑get方法的名称
     *
     * @param fieldName 字段名
     * @return get方法名
     */
    private String getMethodName(String fieldName) {
        char[] arr = fieldName.toCharArray();
        StringBuilder methodName = new StringBuilder()
                .append("get")
                .append(Character.toUpperCase(arr[0]));
        for (int i = 1; i < arr.length; i++) {
            methodName.append(arr[i]);
        }
        return methodName.toString();
    }

}
