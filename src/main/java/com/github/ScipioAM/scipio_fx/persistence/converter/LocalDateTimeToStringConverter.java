package com.github.ScipioAM.scipio_fx.persistence.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Java的{@link LocalDateTime}，与DB的字符串值互相转换
 * @since 2022/7/27
 */
@Converter
public class LocalDateTimeToStringConverter implements AttributeConverter<LocalDateTime, String> {

    public static String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIME_PATTERN);

    /**
     * 自定义日期时间格式
     */
    public static void setTimePattern(String timePattern) {
        TIME_PATTERN = timePattern;
        formatter = DateTimeFormatter.ofPattern(TIME_PATTERN);
    }

    @Override
    public String convertToDatabaseColumn(LocalDateTime attribute) {
        if (attribute == null) {
            return null;
        } else {
            return attribute.format(formatter);
        }
    }

    @Override
    public LocalDateTime convertToEntityAttribute(String dbData) {
        return dbData == null ? null : LocalDateTime.parse(dbData, formatter);
    }

}
