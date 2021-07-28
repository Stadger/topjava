package ru.javawebinar.topjava.util;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

final class StringToLocalDate implements Converter<String, LocalDate> {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public LocalDate convert(String text) {
        return StringUtils.hasLength(text) ? LocalDate.parse(text, TIME_FORMATTER) : null;
    }
}
