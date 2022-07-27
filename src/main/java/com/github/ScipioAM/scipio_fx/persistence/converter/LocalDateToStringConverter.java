package com.github.ScipioAM.scipio_fx.persistence.converter;

import com.github.ScipioAM.scipio_fx.exception.ConvertException;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Java的{@link LocalDate}，与DB的字符串值互相转换
 * @since 2022/7/27
 */
@Converter
public class LocalDateToStringConverter implements AttributeConverter<LocalDate, String> {

    public static String TIME_PATTERN = "yyyy-MM-dd";

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIME_PATTERN);

    /**
     * 自定义日期时间格式
     */
    public static void setTimePattern(String timePattern) {
        TIME_PATTERN = timePattern;
        formatter = DateTimeFormatter.ofPattern(TIME_PATTERN);
    }

    @Override
    public String convertToDatabaseColumn(LocalDate attribute) {
        if (attribute == null) {
            return null;
        } else {
            return attribute.format(formatter);
        }
    }

    @Override
    public LocalDate convertToEntityAttribute(String dbData) {
        return dbData == null ? null : LocalDate.parse(dbData, formatter);
    }

}
