package com.example.asif.idiot;

public class Reminder_Time {

    int s_hour;
    int s_min;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String id;
    int s_year,s_month,s_date;
    int s_repeat,s_Value;
    String s_message,s_type;

    public Reminder_Time() {
    }

    public Reminder_Time(int s_hour, int s_min, int s_year, int s_month, int s_date, int s_repeat, int s_Value, String s_message, String s_type) {
        this.s_hour = s_hour;
        this.s_min = s_min;
        this.s_year = s_year;
        this.s_month = s_month;
        this.s_date = s_date;
        this.s_repeat = s_repeat;
        this.s_Value = s_Value;
        this.s_message = s_message;
        this.s_type = s_type;
    }

    public int getS_hour() {
        return s_hour;
    }

    public void setS_hour(int s_hour) {
        this.s_hour = s_hour;
    }

    public int getS_min() {
        return s_min;
    }

    public void setS_min(int s_min) {
        this.s_min = s_min;
    }

    public int getS_year() {
        return s_year;
    }

    public void setS_year(int s_year) {
        this.s_year = s_year;
    }

    public int getS_month() {
        return s_month;
    }

    public void setS_month(int s_month) {
        this.s_month = s_month;
    }

    public int getS_date() {
        return s_date;
    }

    public void setS_date(int s_date) {
        this.s_date = s_date;
    }

    public int getS_repeat() {
        return s_repeat;
    }

    public void setS_repeat(int s_repeat) {
        this.s_repeat = s_repeat;
    }

    public int getS_Value() {
        return s_Value;
    }

    public void setS_Value(int s_Value) {
        this.s_Value = s_Value;
    }

    public String getS_message() {
        return s_message;
    }

    public void setS_message(String s_message) {
        this.s_message = s_message;
    }

    public String getS_type() {
        return s_type;
    }

    public void setS_type(String s_type) {
        this.s_type = s_type;
    }
}
