package com.opensolutions.forecast.service.util;

import java.io.FileOutputStream;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.opensolutions.forecast.domain.DaysOfMonth;
import com.opensolutions.forecast.domain.Employee;

/**
 * Employee Hours Helper
 */
public class EmployeeHoursHelper {

    private static final EnumSet<DayOfWeek> WEEKEND = EnumSet.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);

    /*public static void main(final String[] args) {
        final Map<Employee, Map<LocalDate, List<DaysOfMonth>>> employeeHours = new HashMap<>();
        final List<DaysOfMonth> dom = new ArrayList<>();
        addHours(dom, LocalDate.now());
        addHours(dom, LocalDate.now().plusMonths(1));
        addHours(dom, LocalDate.now().plusMonths(2));
        final Map<LocalDate, List<DaysOfMonth>> empHours = new HashMap<>();
        empHours.put(LocalDate.now(), dom);
        final Employee emp = new Employee();
        emp.setAssociateId(224801L);
        emp.setName("Jegannathan, Ramesh Kala");
        employeeHours.put(emp, empHours);
        new EmployeeHoursHelper().writeHoursInWorkbook(employeeHours);
    }

    private static void addHours(final List<DaysOfMonth> dom, final LocalDate date) {
        for (int i = 1; i <= date.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth(); i++) {
            final DaysOfMonth daysOfMonth = new DaysOfMonth();
            daysOfMonth.setDay(i);
            daysOfMonth.setHoliday(WEEKEND.contains(date.withDayOfMonth(i).getDayOfWeek()));
            if (i % 8 == 0) {
                daysOfMonth.setSelected(Boolean.TRUE);
            }
            dom.add(daysOfMonth);
        }
    }*/

    private void createDatesOfMonth(final Row row, final LocalDate date, int columnCount,
                                    final HSSFCellStyle headerStyle)
    {
        for (int i = 1; i <= date.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth(); i++) {
            final Cell headerCell = row.createCell(columnCount++);
            headerCell.setCellValue(date.getMonth().toString().substring(0, 3) + i);
            headerCell.setCellStyle(headerStyle);
        }
        for (int i = 1; i <= date.plusMonths(1).with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth(); i++) {
            final Cell headerCell = row.createCell(columnCount++);
            headerCell.setCellValue(date.plusMonths(1).getMonth().toString().substring(0, 3) + i);
            headerCell.setCellStyle(headerStyle);
        }
        for (int i = 1; i <= date.plusMonths(2).with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth(); i++) {
            final Cell headerCell = row.createCell(columnCount++);
            headerCell.setCellValue(date.plusMonths(2).getMonth().toString().substring(0, 3) + i);
            headerCell.setCellStyle(headerStyle);
        }
    }

    public HSSFWorkbook writeHoursInWorkbook(final Map<Employee, Map<LocalDate, List<DaysOfMonth>>> employeeHours) {
        final HSSFWorkbook workbook = new HSSFWorkbook();
        final HSSFSheet sheet = workbook.createSheet("Forecast");

        final HSSFCellStyle headerStyle = workbook.createCellStyle();
        final HSSFFont headerFont = workbook.createFont();
        headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        headerStyle.setFont(headerFont);

        final HSSFCellStyle employeeStyle = workbook.createCellStyle();
        final HSSFFont employeeFont = workbook.createFont();
        employeeFont.setColor(HSSFColor.BLUE.index);
        employeeFont.setFontName(HSSFFont.FONT_ARIAL);
        employeeStyle.setFont(employeeFont);

        final HSSFCellStyle forecastStyle = workbook.createCellStyle();
        forecastStyle.setFillForegroundColor(HSSFColor.LIME.index);
        forecastStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        final HSSFFont forecastFont = workbook.createFont();
        forecastFont.setColor(HSSFColor.RED.index);
        forecastStyle.setFont(forecastFont);

        final HSSFCellStyle holidayStyle = workbook.createCellStyle();
        holidayStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        holidayStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        int rowCount = 0;
        int columnCount = 0;
        Row row = sheet.createRow(rowCount++);
        Cell headerCell = row.createCell(columnCount++);
        headerCell.setCellValue("EmployeeName");
        headerCell.setCellStyle(headerStyle);
        headerCell = row.createCell(columnCount++);
        headerCell.setCellValue("AssociateId");
        headerCell.setCellStyle(headerStyle);
        createDatesOfMonth(row, LocalDate.now(), columnCount, headerStyle);

        for (final Map.Entry<Employee, Map<LocalDate, List<DaysOfMonth>>> entry : employeeHours.entrySet()) {
            row = sheet.createRow(rowCount++);
            columnCount = 0;
            Cell employeeCell = row.createCell(columnCount++);
            employeeCell.setCellValue(entry.getKey().getName());
            employeeCell.setCellStyle(employeeStyle);
            employeeCell = row.createCell(columnCount++);
            employeeCell.setCellStyle(employeeStyle);
            employeeCell.setCellValue(entry.getKey().getAssociateId());
            for (final Map.Entry<LocalDate, List<DaysOfMonth>> empHours : entry.getValue().entrySet()) {
                final List<DaysOfMonth> daysOfMonth = empHours.getValue();
                for (final DaysOfMonth dayOfMonth : daysOfMonth) {
                    final Cell cell = row.createCell(columnCount++);
                    if (dayOfMonth.isSelected()) {
                        cell.setCellValue(0);
                        cell.setCellStyle(forecastStyle);
                    } else if (dayOfMonth.isHoliday()) {
                        cell.setCellStyle(holidayStyle);
                    } else {
                        cell.setCellValue(8);
                    }
                }
            }
        }
        autoSizeColumns(workbook);
        /*try {
            final FileOutputStream outputStream = new FileOutputStream("C:/forecast/Sample.xls");
            workbook.write(outputStream);
        } catch (final Exception e) {
            System.out.println(e);
        }*/
        return workbook;
    }

    private void autoSizeColumns(final HSSFWorkbook workbook) {
        final int numberOfSheets = workbook.getNumberOfSheets();
        for (int i = 0; i < numberOfSheets; i++) {
            final HSSFSheet sheet = workbook.getSheetAt(i);
            if (sheet.getPhysicalNumberOfRows() > 0) {
                final Row row = sheet.getRow(0);
                final Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {
                    final Cell cell = cellIterator.next();
                    final int columnIndex = cell.getColumnIndex();
                    sheet.autoSizeColumn(columnIndex);
                }
            }
        }
    }

    public static Map<String, List<String>> getWorkingDatesPerWeekForMonths() {
        final Map<String, List<String>> monthWeekMap = new LinkedHashMap<>();
        final Year year = Year.now();
        final Month month = YearMonth.now().getMonth();
        getWorkingDaysInMonth(year, month.plus(1), monthWeekMap);
        getWorkingDaysInMonth(year, month.plus(2), monthWeekMap);
        getWorkingDaysInMonth(year, month.plus(3), monthWeekMap);
        return monthWeekMap;
    }

    private static void getWorkingDaysInMonth(final Year year, final Month month,
                                              final Map<String, List<String>> workingDaysPerWeek)
    {
        final LocalDate firstWorkingDate = getFirstWorkingDayInMonth(year.getValue(), month.getValue());
        final LocalDate lastWorkingDate = getLastWorkingDayInMonth(year.getValue(), month.getValue());
        LocalDate weekStartDate = firstWorkingDate;
        int weekCounter = 0;
        final long weeksInMonth = ChronoUnit.WEEKS.between(firstWorkingDate, lastWorkingDate.plusDays(1));
        final List<String> dates = new ArrayList<>();
        while (weekCounter <= weeksInMonth) {
            final LocalDate weekEndDate = getWeekEndDate(weekStartDate, weekStartDate.getDayOfWeek().getValue(),
                lastWorkingDate.getDayOfMonth());
            weekCounter++;
            dates.add(
                "Week" + weekCounter + " - " + weekStartDate.getDayOfMonth() + " to " + weekEndDate.getDayOfMonth());
            weekStartDate = weekEndDate.plusDays(3);
        }
        workingDaysPerWeek.put(month.toString(), dates);
    }

    private static LocalDate getWeekEndDate(final LocalDate weekStartDate, final int weekStartDayValue,
                                            final int lastWorkingDate)
    {
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

    private static LocalDate getFirstWorkingDayInMonth(final int year, final int month) {
        LocalDate startDate = LocalDate.of(year, month, 1).with(TemporalAdjusters.firstDayOfMonth());
        final DayOfWeek startDay = startDate.getDayOfWeek();
        if (DayOfWeek.SATURDAY == startDay || DayOfWeek.SUNDAY == startDay) {
            startDate = startDate.with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));
        }
        return startDate;
    }

    private static LocalDate getLastWorkingDayInMonth(final int year, final int month) {
        LocalDate endDate = LocalDate.of(year, month, 1).with(TemporalAdjusters.lastDayOfMonth());
        final DayOfWeek endDay = endDate.getDayOfWeek();
        if (DayOfWeek.SATURDAY == endDay || DayOfWeek.SUNDAY == endDay) {
            endDate = endDate.with(TemporalAdjusters.lastInMonth(DayOfWeek.FRIDAY));
        }
        return endDate;
    }

}
