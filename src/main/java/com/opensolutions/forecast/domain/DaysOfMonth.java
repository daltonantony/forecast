package com.opensolutions.forecast.domain;

/**
 * Days Of Month
 */
public class DaysOfMonth {

    private int day;
    private boolean selected;
    private boolean holiday;

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isHoliday() {
        return holiday;
    }

    public void setHoliday(boolean holiday) {
        this.holiday = holiday;
    }

}
