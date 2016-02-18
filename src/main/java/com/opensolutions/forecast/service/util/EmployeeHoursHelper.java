package com.opensolutions.forecast.service.util;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

/**
 * Created by Jegannathan on 2/18/2016.
 */
public class EmployeeHoursHelper {

    public void getWorkingDaysForMonths(){
        Year year = Year.now();
        Month month = YearMonth.now().getMonth();
        printWorkingDaysInMonth(year, month);
        printWorkingDaysInMonth(year, month.plus(1));
        printWorkingDaysInMonth(year, month.plus(2));
    }

    private static void printWorkingDaysInMonth(Year year, Month month){
        LocalDate firstWorkingDate = getFirstWorkingDayInMonth(year.getValue(), month.getValue());
        LocalDate lastWorkingDate = getLastWorkingDayInMonth(year.getValue(), month.getValue());
        LocalDate startDate = firstWorkingDate;
        int weekCounter = 0;
        long weeksBetweenDates = ChronoUnit.WEEKS.between(firstWorkingDate, lastWorkingDate.plusDays(1));
        while(weekCounter <= weeksBetweenDates) {
            LocalDate endDate = getWeekEndDate(startDate, startDate.getDayOfWeek().getValue(), lastWorkingDate.getDayOfMonth());
            weekCounter++;
            System.out.printf("Week " + weekCounter + " : " + startDate.getDayOfMonth() + " " + month + " - " + endDate.getDayOfMonth() + " " + month + "%n");
            startDate = endDate.plusDays(3);
        }
    }
    private static LocalDate getWeekEndDate(LocalDate date, int dayValue, int lastDate){
        LocalDate endDate = date;
        if(date.getDayOfMonth() > lastDate){
            return endDate;
        } else {
            if (lastDate - date.getDayOfMonth()<4){
                endDate = date.plusDays(lastDate-date.getDayOfMonth());
            } else {
                if(dayValue == 1){
                    endDate = date.plusDays(4);
                } else if(dayValue == 2){
                    endDate = date.plusDays(3);
                } else if(dayValue == 3){
                    endDate = date.plusDays(2);
                } else if(dayValue == 4){
                    endDate = date.plusDays(1);
                }
            }
        }
        return endDate;
    }

    private static LocalDate getFirstWorkingDayInMonth(int year, int month){
        LocalDate startDate = LocalDate.of(year, month, 1).with(TemporalAdjusters.firstDayOfMonth());
        int dayValue = startDate.getDayOfWeek().getValue();
        if(dayValue == 6){
            startDate = startDate.plusDays(2);
        }else if(dayValue == 7){
            startDate = startDate.plusDays(1);
        }
        return startDate;
    }

    private static LocalDate getLastWorkingDayInMonth(int year, int month){
        LocalDate endDate = LocalDate.of(year, month, 1).with(TemporalAdjusters.lastDayOfMonth());
        int dayValue = endDate.getDayOfWeek().getValue();
        if(dayValue == 6){
            endDate = endDate.minusDays(1);
        }else if(dayValue == 7){
            endDate = endDate.minusDays(2);
        }
        return endDate;
    }

}
