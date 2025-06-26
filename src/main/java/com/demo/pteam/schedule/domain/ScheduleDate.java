package com.demo.pteam.schedule.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class ScheduleDate {
    private static final int MIN_YEAR = 1900;
    private static final int MAX_YEAR = LocalDate.now().getYear() + 10;

    private final int year;
    private final int month;

    private ScheduleDate(int year, int month) {
        if (year < MIN_YEAR || year > MAX_YEAR) {
            throw new IllegalArgumentException(
                    "Year must be between " + MIN_YEAR + " and " + MAX_YEAR + ". Invalid year: " + year);
        }
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Invalid month: " + month);
        }
        this.year = year;
        this.month = month;
    }

    public static ScheduleDate of(Integer year, Integer month) {
        LocalDate localDate = LocalDate.now();
        int safeYear = Objects.isNull(year) ? localDate.getYear() : year;
        int safeMonth = Objects.isNull(month) ? localDate.getMonthValue() : month;
        return new ScheduleDate(safeYear, safeMonth);
    }

    public LocalDateTime getStartOfMonth() {
        return LocalDate.of(year, month, 1).atStartOfDay();
    }

    public LocalDateTime getEndOfMonth() {
        return getStartOfMonth().plusMonths(1);
    }
}
