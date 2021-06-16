package ru.javawebinar.topjava.util;

import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static boolean isBetweenHalfOpen(LocalTime lt, LocalTime startTime, LocalTime endTime) {
        return lt.compareTo(startTime) >= 0 && lt.compareTo(endTime) < 0;
    }

    public static boolean isBetweenClosed(LocalDate lt, LocalDate startDate, LocalDate endDate) {
        return lt.compareTo(startDate) >= 0 && lt.compareTo(endDate) <= 0;
    }

    public static <T extends Comparable<? super T>> boolean isBetween(T tested, T startPeriod, T endPeriod) {
        return tested.compareTo(startPeriod) >= 0 && tested.compareTo(endPeriod) <= 0;
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }

    public static LocalDate dataParse(String s) {
        return StringUtils.hasLength(s) ? LocalDate.parse(s) : null;
    }

    public static LocalTime timeParse(String s) {
        return StringUtils.hasLength(s) ? LocalTime.parse(s) : null;
    }
}

