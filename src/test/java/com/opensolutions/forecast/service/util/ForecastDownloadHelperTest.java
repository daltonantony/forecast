package com.opensolutions.forecast.service.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Test;

import com.opensolutions.forecast.domain.DaysOfMonth;
import com.opensolutions.forecast.domain.EmployeeForecast;

public class ForecastDownloadHelperTest {

    private static final EnumSet<DayOfWeek> WEEKEND = EnumSet.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);

    @Test
    public void getWorkingDatesPerWeekForMonths() throws Exception {
        final HSSFWorkbook workbook =
                new ForecastDownloadHelper().writeHoursInWorkbook(createEmployeeForecastMockData());
        assertNotNull(workbook);
        assertEquals(3, workbook.getSheetAt(1).getPhysicalNumberOfRows());
        /*
         * try { final FileOutputStream outputStream = new FileOutputStream("C:/forecast/test.xls");
         * workbook.write(outputStream); } catch (final Exception e) { System.out.println(e); }
         */
    }

    private List<EmployeeForecast> createEmployeeForecastMockData() {
        final List<EmployeeForecast> employeesForecast = new ArrayList<>();
        EmployeeForecast emp = new EmployeeForecast();
        emp.setId(1L);
        emp.setAssociateId(224801L);
        emp.setName("First Name, Last Name 1");
        emp.setDomain("Domain A");
        emp.setProject("Project A");
        Map<LocalDate, List<DaysOfMonth>> empHours = new HashMap<>();
        empHours.put(LocalDate.now(), addHoursWithLeave(LocalDate.now()));
        empHours.put(LocalDate.now().plusMonths(1), addHoursWithLeave(LocalDate.now().plusMonths(1)));
        empHours.put(LocalDate.now().plusMonths(2), addHoursWithLeave(LocalDate.now().plusMonths(2)));
        empHours.put(LocalDate.now().plusMonths(3), addHoursWithLeave(LocalDate.now().plusMonths(3)));
        emp.setEmployeeHours(empHours);
        employeesForecast.add(emp);
        emp = new EmployeeForecast();
        emp.setId(2L);
        emp.setAssociateId(224488L);
        emp.setName("First Name, Last Name 2");
        emp.setDomain("Domain B");
        emp.setProject("Project B");
        empHours = new HashMap<>();
        empHours.put(LocalDate.now(), addHours(LocalDate.now()));
        empHours.put(LocalDate.now().plusMonths(1), addHours(LocalDate.now().plusMonths(1)));
        empHours.put(LocalDate.now().plusMonths(2), addHoursWithLeave(LocalDate.now().plusMonths(2)));
        empHours.put(LocalDate.now().plusMonths(3), addHours(LocalDate.now().plusMonths(3)));
        emp.setEmployeeHours(empHours);
        employeesForecast.add(emp);
        return employeesForecast;
    }

    private static List<DaysOfMonth> addHoursWithLeave(final LocalDate date) {
        final List<DaysOfMonth> dom = new ArrayList<>();
        for (int i = 1; i <= date.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth(); i++) {
            final DaysOfMonth daysOfMonth = new DaysOfMonth();
            daysOfMonth.setDay(i);
            daysOfMonth.setHoliday(WEEKEND.contains(date.withDayOfMonth(i).getDayOfWeek()));
            if (i % 3 == 0) {
                daysOfMonth.setSelected(Boolean.TRUE);
            }
            dom.add(daysOfMonth);
        }
        return dom;
    }

    private static List<DaysOfMonth> addHours(final LocalDate date) {
        final List<DaysOfMonth> dom = new ArrayList<>();
        for (int i = 1; i <= date.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth(); i++) {
            final DaysOfMonth daysOfMonth = new DaysOfMonth();
            daysOfMonth.setDay(i);
            daysOfMonth.setHoliday(WEEKEND.contains(date.withDayOfMonth(i).getDayOfWeek()));
            dom.add(daysOfMonth);
        }
        return dom;
    }
}
