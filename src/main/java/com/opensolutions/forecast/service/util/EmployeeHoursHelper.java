package com.opensolutions.forecast.service.util;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Employee Hours Helper
 */
public class EmployeeHoursHelper {

    public static Map<String, List<String>> getWorkingDatesPerWeekForMonths() {
        final Map<String, List<String>> monthWeekMap = new LinkedHashMap<>();
        Year year = Year.now();
        Month month = YearMonth.now().getMonth();
        getWorkingDaysInMonth(year, month.plus(1), monthWeekMap);
        getWorkingDaysInMonth(year, month.plus(2), monthWeekMap);
        getWorkingDaysInMonth(year, month.plus(3), monthWeekMap);
        return monthWeekMap;
    }

    private static void getWorkingDaysInMonth(Year year, Month month, Map<String, List<String>> workingDaysPerWeek) {
        LocalDate firstWorkingDate = getFirstWorkingDayInMonth(year.getValue(), month.getValue());
        LocalDate lastWorkingDate = getLastWorkingDayInMonth(year.getValue(), month.getValue());
        LocalDate weekStartDate = firstWorkingDate;
        int weekCounter = 0;
        long weeksInMonth = ChronoUnit.WEEKS.between(firstWorkingDate, lastWorkingDate.plusDays(1));
        final List<String> dates = new ArrayList<>();
        while (weekCounter <= weeksInMonth) {
            LocalDate weekEndDate = getWeekEndDate(weekStartDate, weekStartDate.getDayOfWeek().getValue(), lastWorkingDate.getDayOfMonth());
            weekCounter++;
            dates.add("Week" + weekCounter + " - " + weekStartDate.getDayOfMonth() + " to " + weekEndDate.getDayOfMonth());
            weekStartDate = weekEndDate.plusDays(3);
        }
        workingDaysPerWeek.put(month.toString(), dates);
    }

    private static LocalDate getWeekEndDate(LocalDate weekStartDate, int weekStartDayValue, int lastWorkingDate) {
        LocalDate weekEndDate = weekStartDate;
        if (weekStartDate.getDayOfMonth() > lastWorkingDate) {
            return weekEndDate;
        } else {
            if (lastWorkingDate - weekStartDate.getDayOfMonth() < 4) {
                weekEndDate = weekStartDate.plusDays(lastWorkingDate - weekStartDate.getDayOfMonth());
            } else {
                if (weekStartDayValue == 1) {
                    weekEndDate = weekStartDate.plusDays(4);
                } else if (weekStartDayValue == 2) {
                    weekEndDate = weekStartDate.plusDays(3);
                } else if (weekStartDayValue == 3) {
                    weekEndDate = weekStartDate.plusDays(2);
                } else if (weekStartDayValue == 4) {
                    weekEndDate = weekStartDate.plusDays(1);
                }
            }
        }
        return weekEndDate;
    }

    private static LocalDate getFirstWorkingDayInMonth(int year, int month) {
        LocalDate startDate = LocalDate.of(year, month, 1).with(TemporalAdjusters.firstDayOfMonth());
        int startDayValue = startDate.getDayOfWeek().getValue();
        if (startDayValue == 6) {
            startDate = startDate.plusDays(2);
        } else if (startDayValue == 7) {
            startDate = startDate.plusDays(1);
        }
        return startDate;
    }

    private static LocalDate getLastWorkingDayInMonth(int year, int month) {
        LocalDate endDate = LocalDate.of(year, month, 1).with(TemporalAdjusters.lastDayOfMonth());
        int endDayValue = endDate.getDayOfWeek().getValue();
        if (endDayValue == 6) {
            endDate = endDate.minusDays(1);
        } else if (endDayValue == 7) {
            endDate = endDate.minusDays(2);
        }
        return endDate;
    }

}
