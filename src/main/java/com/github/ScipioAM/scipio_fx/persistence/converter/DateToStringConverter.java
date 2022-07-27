package com.github.ScipioAM.scipio_fx.persistence.converter;

import com.github.ScipioAM.scipio_fx.exception.ConvertException;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Java的{@link Date}，与DB的字符串值互相转换
 * @since 2022/7/27
 */
@Converter
public class DateToStringConverter implements AttributeConverter<Date, String> {

    public static String TIME_PATTERN = "yyyy-MM-dd";

    private static SimpleDateFormat formatter = new SimpleDateFormat(TIME_PATTERN);

    /**
     * 自定义日期时间格式
     */
    public static void setTimePattern(String timePattern) {
        TIME_PATTERN = timePattern;
        formatter = new SimpleDateFormat(TIME_PATTERN);
    }

    @Override
    public String convertToDatabaseColumn(Date attribute) {
        if (attribute == null) {
            return null;
        } else {
            return formatter.format(attribute);
        }
    }

    @Override
    public Date convertToEntityAttribute(String dbData) {
        try {
            return dbData == null ? null : formatter.parse(dbData);
        } catch (ParseException e) {
            throw new ConvertException(e);
        }
    }

}
