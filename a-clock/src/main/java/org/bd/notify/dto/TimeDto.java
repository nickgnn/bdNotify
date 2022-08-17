package org.bd.notify.dto;

public class TimeDto {
    private Integer year;
    private String month;
    private String dayOfMonth;
    private String dayOfWeek;
    private String time;

    public TimeDto() {
    }

    public TimeDto(Integer year, String month, String dayOfMonth, String dayOfWeek, String time) {
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
        this.dayOfWeek = dayOfWeek;
        this.time = time;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(String dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "TimeDto{" +
                "year='" + year + '\'' +
                ", month='" + month + '\'' +
                ", dayOfMonth='" + dayOfMonth + '\'' +
                ", dayOfWeek='" + dayOfWeek + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}