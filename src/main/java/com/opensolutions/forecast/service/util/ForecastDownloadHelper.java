package com.opensolutions.forecast.service.util;

import java.io.FileOutputStream;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;

import com.opensolutions.forecast.domain.DaysOfMonth;
import com.opensolutions.forecast.domain.Employee;

/**
 * Forecast Download Helper
 */
public class ForecastDownloadHelper {

    /*private static final EnumSet<DayOfWeek> WEEKEND = EnumSet.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);

    public static void main(final String[] args) {
        final Map<Employee, Map<LocalDate, List<DaysOfMonth>>> employeeHours = new HashMap<>();
        Employee emp = new Employee();
        emp.setId(1L);
        emp.setAssociateId(224801L);
        emp.setName("First Name, Last Name 1");
        emp.setDomain("Project A");
        Map<LocalDate, List<DaysOfMonth>> empHours = new HashMap<>();
        empHours.put(LocalDate.now(), addHours(LocalDate.now()));
        empHours.put(LocalDate.now().plusMonths(1), addHours(LocalDate.now().plusMonths(1)));
        empHours.put(LocalDate.now().plusMonths(2), addHours(LocalDate.now().plusMonths(2)));
        empHours.put(LocalDate.now().plusMonths(3), addHours(LocalDate.now().plusMonths(3)));
        employeeHours.put(emp, empHours);
        emp = new Employee();
        emp.setId(2L);
        emp.setAssociateId(224488L);
        emp.setName("First Name, Last Name 2");
        emp.setDomain("Project B");
        empHours = new HashMap<>();
        empHours.put(LocalDate.now().plusMonths(1), addHours(LocalDate.now().plusMonths(1)));
        employeeHours.put(emp, empHours);
        new EmployeeHoursHelper().writeHoursInWorkbook(employeeHours);
    }

    private static List<DaysOfMonth> addHours(final LocalDate date) {
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
    }*/

    /**
     * Write hours in workbook.
     *
     * @param employeeHours the employee hours
     * @return the HSSF workbook
     */
    public HSSFWorkbook writeHoursInWorkbook(final Map<Employee, Map<LocalDate, List<DaysOfMonth>>> employeeHours) {
        final HSSFWorkbook workbook = new HSSFWorkbook();
        final HSSFCellStyle headerStyle = workbook.createCellStyle();
        final HSSFFont headerFont = workbook.createFont();
        headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        headerStyle.setFont(headerFont);
        headerStyle.setAlignment(CellStyle.ALIGN_CENTER);

        final HSSFCellStyle employeeStyle = workbook.createCellStyle();
        final HSSFFont employeeFont = workbook.createFont();
        employeeFont.setColor(HSSFColor.BLUE.index);
        employeeFont.setFontName(HSSFFont.FONT_ARIAL);
        employeeStyle.setFont(employeeFont);
        employeeStyle.setAlignment(CellStyle.ALIGN_RIGHT);

        final HSSFCellStyle forecastStyle = workbook.createCellStyle();
        // forecastStyle.setFillForegroundColor(HSSFColor.LIME.index);
        // forecastStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        final HSSFFont forecastFont = workbook.createFont();
        forecastFont.setColor(HSSFColor.RED.index);
        forecastStyle.setFont(forecastFont);
        forecastStyle.setAlignment(CellStyle.ALIGN_CENTER);

        final HSSFCellStyle holidayStyle = workbook.createCellStyle();
        holidayStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        holidayStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        createSheetWithForecastA(employeeHours, workbook, headerStyle, employeeStyle, forecastStyle, holidayStyle);
        createSheetWithForecastB(employeeHours, workbook, headerStyle, employeeStyle, forecastStyle, holidayStyle);
        autoSizeColumns(workbook);

        /*try { final FileOutputStream outputStream = new FileOutputStream("W:/test.xls");
        workbook.write(outputStream); } catch (final Exception e) { System.out.println(e); }*/

        return workbook;
    }

    private void createSheetWithForecastA(Map<Employee, Map<LocalDate, List<DaysOfMonth>>> employeeHours, HSSFWorkbook workbook, HSSFCellStyle headerStyle, HSSFCellStyle employeeStyle, HSSFCellStyle forecastStyle, HSSFCellStyle holidayStyle) {
        final HSSFSheet sheet= workbook.createSheet("Forecast A");
        int rowCount = 0;
        int columnCount = 0;
        Row row = sheet.createRow(rowCount++);
        Cell headerCell = row.createCell(columnCount++);
        headerCell.setCellValue("ID");
        headerCell.setCellStyle(headerStyle);
        headerCell = row.createCell(columnCount++);
        headerCell.setCellValue("AssociateID");
        headerCell.setCellStyle(headerStyle);
        headerCell = row.createCell(columnCount++);
        headerCell.setCellValue("LeaveDate");
        headerCell.setCellStyle(headerStyle);
        headerCell = row.createCell(columnCount++);
        headerCell.setCellValue("Days");
        headerCell.setCellStyle(headerStyle);
        headerCell = row.createCell(columnCount++);
        headerCell.setCellValue("AssociateName");
        headerCell.setCellStyle(headerStyle);
        headerCell = row.createCell(columnCount++);
        headerCell.setCellValue("ProjectName");
        headerCell.setCellStyle(headerStyle);
        headerCell = row.createCell(columnCount++);
        headerCell.setCellValue("DomainName");
        headerCell.setCellStyle(headerStyle);

        for (final Map.Entry<Employee, Map<LocalDate, List<DaysOfMonth>>> entry : employeeHours.entrySet()) {
            for (final Map.Entry<LocalDate, List<DaysOfMonth>> empHours : entry.getValue().entrySet()) {
                final LocalDate date = empHours.getKey();
                //if (date.getMonthValue() > LocalDate.now().getMonthValue()) {
                    final List<DaysOfMonth> daysOfMonth = empHours.getValue();
                    for (final DaysOfMonth dayOfMonth : daysOfMonth) {
                        columnCount = 0;
                        if (dayOfMonth.isSelected()) {
                            row = sheet.createRow(rowCount++);
                            Cell cell = row.createCell(columnCount++);
                            cell.setCellValue(entry.getKey().getId());
                            cell = row.createCell(columnCount++);
                            cell.setCellValue(entry.getKey().getAssociateId());
                            cell.setCellStyle(employeeStyle);
                            cell = row.createCell(columnCount++);
                            cell.setCellValue(date.withDayOfMonth(dayOfMonth.getDay()).toString());
                            cell.setCellStyle(forecastStyle);
                            cell = row.createCell(columnCount++);
                            cell.setCellValue(1);
                            cell = row.createCell(columnCount++);
                            cell.setCellValue(entry.getKey().getName());
                            cell.setCellStyle(employeeStyle);
                            cell = row.createCell(columnCount++);
                            cell.setCellValue(entry.getKey().getDomain());
                            cell = row.createCell(columnCount++);
                            cell.setCellValue("");
                        }
                    }
                //}
            }
        }
    }

    private void createSheetWithForecastB(Map<Employee, Map<LocalDate, List<DaysOfMonth>>> employeeHours, HSSFWorkbook workbook, HSSFCellStyle headerStyle, HSSFCellStyle employeeStyle, HSSFCellStyle forecastStyle, HSSFCellStyle holidayStyle) {
        final HSSFSheet sheet = workbook.createSheet("Forecast B");

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
                final LocalDate date = empHours.getKey();
                if (date.getMonthValue() > LocalDate.now().getMonthValue()) {
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
        }
    }

    private void createDatesOfMonth(final Row row, final LocalDate date, int columnCount,
                                    final HSSFCellStyle headerStyle)
    {
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
        for (int i = 1; i <= date.plusMonths(3).with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth(); i++) {
            final Cell headerCell = row.createCell(columnCount++);
            headerCell.setCellValue(date.plusMonths(3).getMonth().toString().substring(0, 3) + i);
            headerCell.setCellStyle(headerStyle);
        }
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
