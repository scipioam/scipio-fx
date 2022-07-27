package com.github.ScipioAM.scipio_fx.persistence.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Java的布尔值，与DB的字符串值互相转换
 * @since 2022/7/27
 */
@Converter
public class BooleanToStringConverter implements AttributeConverter<Boolean, String> {

    public static String TRUE = "Y";
    public static String FALSE = "N";

    /**
     * 自定义true、false对应的字符串
     */
    public static void setTrueFalseString(String trueStr, String falseStr) {
        TRUE = trueStr;
        FALSE = falseStr;
    }

    @Override
    public String convertToDatabaseColumn(Boolean attribute) {
        if (attribute == null) {
            return null;
        } else {
            return attribute ? TRUE : FALSE;
        }
    }

    @Override
    public Boolean convertToEntityAttribute(String dbData) {
        return dbData != null && dbData.equals(TRUE);
    }



}
