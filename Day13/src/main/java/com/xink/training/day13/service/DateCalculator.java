package com.xink.training.day13.service;

import java.time.Month;
import java.time.Year;

public class DateCalculator {
    
    public int getDaysInMonth(Month month, int year) {
        return month.length(Year.isLeap(year));
    }
    
    public boolean isLeapYear(int year) {
        return Year.isLeap(year);
    }
}

